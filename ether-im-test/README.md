# 测试

## 创建模拟用户

### 客服:

1号客服：

    userId：kefu-01
    group: kefu
    web-token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJrZWZ1LTAxIiwiQ04iOiJ7XCJncm91cFwiOlwia2VmdVwiLFwidGVybWluYWxUeXBlXCI6XCJXRUJcIixcInVzZXJJZFwiOlwia2VmdS0wMVwifSIsImV4cCI6MTczNjczNjcxMn0.52iIZ2Mdq7XLs9P8xdsxJdKQ-KdFOi08Htj3ztqfS-s
    app-token：eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJrZWZ1LTAxIiwiQ04iOiJ7XCJncm91cFwiOlwia2VmdVwiLFwidGVybWluYWxUeXBlXCI6XCJBUFBcIixcInVzZXJJZFwiOlwia2VmdS0wMVwifSIsImV4cCI6MTczNjczNjcyOH0.24bcNj8xhcs_t51vd2RHlHZkHz59DJrFjcr2QlJStB8

2号客服：

    userId：kefu-02
    group: kefu
    web-token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJrZWZ1LTAyIiwiQ04iOiJ7XCJncm91cFwiOlwia2VmdVwiLFwidGVybWluYWxUeXBlXCI6XCJXRUJcIixcInVzZXJJZFwiOlwia2VmdS0wMlwifSIsImV4cCI6MTczNjczNjgwNn0.nCQZ3MNWxjA0AyoEnxW_Jvh6n7zoTTKfTVTZsv9C7Yc
    app-token：eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJrZWZ1LTAyIiwiQ04iOiJ7XCJncm91cFwiOlwia2VmdVwiLFwidGVybWluYWxUeXBlXCI6XCJBUFBcIixcInVzZXJJZFwiOlwia2VmdS0wMlwifSIsImV4cCI6MTczNjczNjc5Mn0.lRZob8NfYBf8Us_JtAtEd9CMBPY4zOanwxau16boX48

### 用户

1号用户：

    userId：customer-01
    group: customer
    web-token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjdXN0b21lci0wMSIsIkNOIjoie1wiZ3JvdXBcIjpcImN1c3RvbWVyXCIsXCJ0ZXJtaW5hbFR5cGVcIjpcIldFQlwiLFwidXNlcklkXCI6XCJjdXN0b21lci0wMVwifSIsImV4cCI6MTczNjczNjg2MH0.Yu1h41E1JKmX-XE3GnnET1aLyK1vSqbqmBK9uIImqMQ
    app-token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjdXN0b21lci0wMSIsIkNOIjoie1wiZ3JvdXBcIjpcImN1c3RvbWVyXCIsXCJ0ZXJtaW5hbFR5cGVcIjpcIkFQUFwiLFwidXNlcklkXCI6XCJjdXN0b21lci0wMVwifSIsImV4cCI6MTczNjczNjg4NH0.R4dgXhG9KqGRNHXLCP94qiFkg1-1yMRVX8HjPC8grrQ

## 单聊消息

1号用户web终端给1号客服发送消息：

```json
{
  "senderId": "customer-01",
  "senderGroup": "customer",
  "senderTerminal": "WEB",
  "receiverId": "kefu-01",
  "receiverGroup": "kefu",
  "content": "1号用户web终端给1号客服发送消息",
  "contentType": "TEXT",
  "sendTime": 1636260000001
}
```