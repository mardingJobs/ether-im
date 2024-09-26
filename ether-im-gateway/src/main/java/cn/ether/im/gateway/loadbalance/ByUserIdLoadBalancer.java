package cn.ether.im.gateway.loadbalance;

import cn.ether.im.gateway.constants.ImGatewayConstants;
import cn.ether.im.gateway.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 对用户ID计算Hash，然后取模选择具体的服务实例
 */
@Slf4j
public class ByUserIdLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    final String serviceId;
    ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    /**
     * @param serviceInstanceListSupplierProvider a provider of
     *                                            {@link ServiceInstanceListSupplier} that will be used to get available instances
     * @param serviceId                           id of the service for which to choose an instance
     */
    public ByUserIdLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.serviceId = serviceId;
    }


    @SuppressWarnings("rawtypes")
    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        HttpHeaders headers = (HttpHeaders) request.getContext();
        if (this.serviceInstanceListSupplierProvider != null) {
            ServiceInstanceListSupplier supplier = this.serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
            return ((Flux) supplier.get()).next().map(list -> getInstanceResponse((List<ServiceInstance>) list, headers));
        }

        return null;
    }

    /**
     * 按nacos权重
     *
     * @return
     */

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> serviceInstances, HttpHeaders headers) {
        if (serviceInstances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: {}", serviceId);
            }
            return new EmptyResponse();
        }
        //采用nacos所配置的权重进行负载均衡调用，随机权重算法
        //Instance instance = NacosBalancer.getHostByRandomWeight2(instances);
        // 根据token获取用户id，根据id取模，获取实例
        List<String> tokens = headers.get(ImGatewayConstants.TOKEN);
        if (tokens == null || tokens.isEmpty()) {
            return new EmptyResponse();
        }
        String userId = JwtUtils.getUserId(tokens.get(0));
        if (userId == null) {
            return new EmptyResponse();
        }
        int index = Math.abs(userId.hashCode()) % serviceInstances.size();
        ServiceInstance chosenInstance = serviceInstances.get(index);
        log.info("Chosen Push Server Instance: {}", chosenInstance);

        return new DefaultResponse(chosenInstance);
    }
}