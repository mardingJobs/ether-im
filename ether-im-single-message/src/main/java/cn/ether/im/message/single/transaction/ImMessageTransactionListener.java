package cn.ether.im.message.single.transaction;

import cn.ether.im.common.enums.ImChatMessageStatus;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImSingleMessage;
import cn.ether.im.message.single.model.entity.ImSingleMessageET;
import cn.ether.im.message.single.service.ImSingleMessageETService;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

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
    private ImSingleMessageETService singleMessageEtService;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String messageString = new String((byte[]) msg.getPayload());
        try {
            ImChatMessage chatMessage = JSON.parseObject(messageString, ImSingleMessage.class);
            ImSingleMessageET singleMessageEt = new ImSingleMessageET();
            BeanUtil.copyProperties(chatMessage, singleMessageEt);
            singleMessageEt.setStatus(ImChatMessageStatus.SENT.name());
            singleMessageEt.setCreateTime(new Date());
            boolean saved = singleMessageEtService.save(singleMessageEt);
            if (!saved) {
                return RocketMQLocalTransactionState.ROLLBACK;
            }
        } catch (Exception e) {
            log.error("executeLocalTransaction|messageString:{}", messageString, e);
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        return RocketMQLocalTransactionState.COMMIT;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        String messageId = (String) msg.getHeaders().get("rocketmq_KEYS");
        ImSingleMessageET singleMessageEt = singleMessageEtService.getById(messageId);
        if (singleMessageEt != null) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
