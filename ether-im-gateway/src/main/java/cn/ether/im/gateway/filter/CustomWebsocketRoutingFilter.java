package cn.ether.im.gateway.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;

/**
 * 解决websocket关闭异常 问题
 *
 * @author wsz
 * @Desc websocket客户端主动断开连接, 网关服务报错1005
 * @date 2022/2/1 17:21
 */
@Component
public class CustomWebsocketRoutingFilter implements GlobalFilter, Ordered {

    public static final String SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
    private static final Log log = LogFactory.getLog(CustomWebsocketRoutingFilter.class);
    private final WebSocketClient webSocketClient;
    private final WebSocketService webSocketService;
    private final ObjectProvider<List<HttpHeadersFilter>> headersFiltersProvider;
    private volatile List<HttpHeadersFilter> headersFilters;

    public CustomWebsocketRoutingFilter(WebSocketClient webSocketClient, WebSocketService webSocketService, ObjectProvider<List<HttpHeadersFilter>> headersFiltersProvider) {
        this.webSocketClient = webSocketClient;
        this.webSocketService = webSocketService;
        this.headersFiltersProvider = headersFiltersProvider;
    }

    static String convertHttpToWs(String scheme) {
        scheme = scheme.toLowerCase();
        return "http".equals(scheme) ? "ws" : ("https".equals(scheme) ? "wss" : scheme);
    }

    static void changeSchemeIfIsWebSocketUpgrade(ServerWebExchange exchange) {
        URI requestUrl = (URI) exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        String scheme = requestUrl.getScheme().toLowerCase();
        String upgrade = exchange.getRequest().getHeaders().getUpgrade();
        if ("WebSocket".equalsIgnoreCase(upgrade) && ("http".equals(scheme) || "https".equals(scheme))) {
            String wsScheme = convertHttpToWs(scheme);
            boolean encoded = ServerWebExchangeUtils.containsEncodedParts(requestUrl);
            URI wsRequestUrl = UriComponentsBuilder.fromUri(requestUrl).scheme(wsScheme).build(encoded).toUri();
            exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, wsRequestUrl);
            if (log.isTraceEnabled()) {
                log.trace("changeSchemeTo:[" + wsRequestUrl + "]");
            }
        }

    }

    @Override
    public int getOrder() {
        return 2147483645;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        changeSchemeIfIsWebSocketUpgrade(exchange);
        URI requestUrl = (URI) exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        String scheme = requestUrl.getScheme();
        if (!ServerWebExchangeUtils.isAlreadyRouted(exchange) && ("ws".equals(scheme) || "wss".equals(scheme))) {
            ServerWebExchangeUtils.setAlreadyRouted(exchange);
            HttpHeaders headers = exchange.getRequest().getHeaders();
            HttpHeaders filtered = HttpHeadersFilter.filterRequest(this.getHeadersFilters(), exchange);
            List<String> protocols = this.getProtocols(headers);
            return this.webSocketService.handleRequest(exchange, new CustomWebsocketRoutingFilter.ProxyWebSocketHandler(requestUrl, this.webSocketClient, filtered, protocols));
        } else {
            return chain.filter(exchange);
        }
    }

    List<String> getProtocols(HttpHeaders headers) {
        List<String> protocols = headers.get("Sec-WebSocket-Protocol");
        if (protocols != null) {
            ArrayList<String> updatedProtocols = new ArrayList();

            for (int i = 0; i < ((List) protocols).size(); ++i) {
                String protocol = (String) ((List) protocols).get(i);
                updatedProtocols.addAll(Arrays.asList(StringUtils.tokenizeToStringArray(protocol, ",")));
            }

            protocols = updatedProtocols;
        }

        return (List) protocols;
    }

    List<HttpHeadersFilter> getHeadersFilters() {
        if (this.headersFilters == null) {
            this.headersFilters = (List) this.headersFiltersProvider.getIfAvailable(ArrayList::new);
            this.headersFilters.add((headers, exchange) -> {
                HttpHeaders filtered = new HttpHeaders();
                filtered.addAll(headers);
                filtered.remove("Host");
                boolean preserveHost = (Boolean) exchange.getAttributeOrDefault(ServerWebExchangeUtils.PRESERVE_HOST_HEADER_ATTRIBUTE, false);
                if (preserveHost) {
                    String host = exchange.getRequest().getHeaders().getFirst("Host");
                    filtered.add("Host", host);
                }

                return filtered;
            });
            this.headersFilters.add((headers, exchange) -> {
                HttpHeaders filtered = new HttpHeaders();
                Iterator var3 = headers.entrySet().iterator();

                while (var3.hasNext()) {
                    Map.Entry<String, List<String>> entry = (Map.Entry) var3.next();
                    if (!((String) entry.getKey()).toLowerCase().startsWith("sec-websocket")) {
                        filtered.addAll((String) entry.getKey(), (List) entry.getValue());
                    }
                }

                return filtered;
            });
        }

        return this.headersFilters;
    }

    private static class ProxyWebSocketHandler implements WebSocketHandler {
        private final WebSocketClient client;
        private final URI url;
        private final HttpHeaders headers;
        private final List<String> subProtocols;

        ProxyWebSocketHandler(URI url, WebSocketClient client, HttpHeaders headers, List<String> protocols) {
            this.client = client;
            this.url = url;
            this.headers = headers;
            if (protocols != null) {
                this.subProtocols = protocols;
            } else {
                this.subProtocols = Collections.emptyList();
            }

        }

        @Override
        public List<String> getSubProtocols() {
            return this.subProtocols;
        }

        @Override
        public Mono<Void> handle(WebSocketSession session) {
            return this.client.execute(this.url, this.headers, new WebSocketHandler() {

                private CloseStatus adaptCloseStatus(CloseStatus closeStatus) {
                    int code = closeStatus.getCode();
                    if (code > 2999 && code < 5000) {
                        return closeStatus;
                    }
                    switch (code) {
                        case 1000:
                            return closeStatus;
                        case 1001:
                            return closeStatus;
                        case 1002:
                            return closeStatus;
                        case 1003:
                            return closeStatus;
                        case 1004:
                            // Should not be used in a close frame
                            // RESERVED;
                            return CloseStatus.PROTOCOL_ERROR;
                        case 1005:
                            // Should not be used in a close frame
                            // return CloseStatus.NO_STATUS_CODE;
                            return CloseStatus.PROTOCOL_ERROR;
                        case 1006:
                            // Should not be used in a close frame
                            // return CloseStatus.NO_CLOSE_FRAME;
                            return CloseStatus.PROTOCOL_ERROR;
                        case 1007:
                            return closeStatus;
                        case 1008:
                            return closeStatus;
                        case 1009:
                            return closeStatus;
                        case 1010:
                            return closeStatus;
                        case 1011:
                            return closeStatus;
                        case 1012:
                            // Not in RFC6455
                            // return CloseStatus.SERVICE_RESTARTED;
                            return CloseStatus.PROTOCOL_ERROR;
                        case 1013:
                            // Not in RFC6455
                            // return CloseStatus.SERVICE_OVERLOAD;
                            return CloseStatus.PROTOCOL_ERROR;
                        case 1015:
                            // Should not be used in a close frame
                            // return CloseStatus.TLS_HANDSHAKE_FAILURE;
                            return CloseStatus.PROTOCOL_ERROR;
                        default:
                            return CloseStatus.PROTOCOL_ERROR;
                    }
                }

                @Override
                public Mono<Void> handle(WebSocketSession proxySession) {
                    Mono<Void> serverClose = proxySession.closeStatus().filter(__ -> session.isOpen())
                            .map(this::adaptCloseStatus)
                            .flatMap(session::close);
                    Mono<Void> proxyClose = session.closeStatus().filter(__ -> proxySession.isOpen())
                            .map(this::adaptCloseStatus)
                            .flatMap(proxySession::close);
                    // Use retain() for Reactor Netty
                    Mono<Void> proxySessionSend = proxySession
                            .send(session.receive().doOnNext(WebSocketMessage::retain));
                    Mono<Void> serverSessionSend = session
                            .send(proxySession.receive().doOnNext(WebSocketMessage::retain));
                    // Ensure closeStatus from one propagates to the other
                    Mono.when(serverClose, proxyClose).subscribe();
                    // Complete when both sessions are done
                    return Mono.zip(proxySessionSend, serverSessionSend).then();
                }

                @Override
                public List<String> getSubProtocols() {
                    return CustomWebsocketRoutingFilter.ProxyWebSocketHandler.this.subProtocols;
                }
            });
        }
    }
}

