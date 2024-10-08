package cn.ether.im.message.group.transaction;

import cn.ether.im.common.model.message.ImGroupMessage;
import cn.ether.im.message.group.model.entity.ImGroupMessageET;
import cn.ether.im.message.group.service.ImGroupMessageService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/5 12:31
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Slf4j
@Component
@RocketMQTransactionListener(rocketMQTemplateBeanName = "rocketMQTemplate")
public class ImMessageTransactionListener implements RocketMQLocalTransactionListener {

    @Resource
    private ImGroupMessageService groupMessageService;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String messageString = new String((byte[]) msg.getPayload());
        try {
            ImGroupMessage groupMessage = JSON.parseObject(messageString, ImGroupMessage.class);
            groupMessageService.persistCoreModel(groupMessage);
        } catch (Exception e) {
            log.error("executeLocalTransaction|messageString:{}", messageString, e);
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        return RocketMQLocalTransactionState.COMMIT;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        String messageId = (String) msg.getHeaders().get("rocketmq_KEYS");
        ImGroupMessageET messageET = groupMessageService.getById(messageId);
        if (messageET != null) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
