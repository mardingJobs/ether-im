package cn.ether.im.message.single.service.impl;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImChatMessageStatus;
import cn.ether.im.common.enums.ImMessageContentType;
import cn.ether.im.common.model.info.ImTopicInfo;
import cn.ether.im.common.model.message.ImSingleMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.mq.ImMqMessageSender;
import cn.ether.im.common.util.SnowflakeUtil;
import cn.ether.im.message.single.mapper.ImSingleMessageETMapper;
import cn.ether.im.message.single.mapper.ImSingleOfflineMessageMapper;
import cn.ether.im.message.single.model.dto.MessageSendReq;
import cn.ether.im.message.single.model.entity.ImSingleMessageET;
import cn.ether.im.message.single.model.session.SessionContext;
import cn.ether.im.message.single.service.ImSingleMessageService;
import cn.ether.im.sdk.client.EtherImClient;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author jack
 * @description 针对表【im_single_message(单聊信息表)】的数据库操作Service实现
 * @createDate 2024-10-05 19:55:33
 */
@Service
public class ImSingleMessageServiceImpl extends ServiceImpl<ImSingleMessageETMapper, ImSingleMessageET>
        implements ImSingleMessageService {

    @Resource
    private EtherImClient etherImClient;

    @Resource
    private SnowflakeUtil snowflakeUtil;

    @Resource
    private ImMqMessageSender messageSender;

    @Resource
    private ImSingleOfflineMessageMapper offlineMessageMapper;


    @Override
    public ImSingleMessage convertSendReqToCoreModel(MessageSendReq sendReq) {
        ImUserTerminal loggedUser = SessionContext.loggedUser();
        Long messageId = snowflakeUtil.nextId();
        ImSingleMessage singleMessage = new ImSingleMessage();
        singleMessage.setMessageId(messageId);
        singleMessage.setContent(sendReq.getContent());
        singleMessage.setContentType(ImMessageContentType.valueOf(sendReq.getContentType()));
        singleMessage.setSenderId(loggedUser.getUserId());
        singleMessage.setSenderTerminal(loggedUser.getTerminalType());
        singleMessage.setReceiverId(sendReq.getReceiverId());
        singleMessage.setSendTime(System.currentTimeMillis());
        return singleMessage;
    }

    @Override
    public ImSingleMessageET convertCoreModelToEntity(ImSingleMessage singleMessage) {
        ImSingleMessageET singleMessageET = new ImSingleMessageET();
        BeanUtil.copyProperties(singleMessage, singleMessageET);
        singleMessageET.setStatus(ImChatMessageStatus.INTI.name());
        singleMessageET.setCreateTime(new Date());
        return singleMessageET;
    }

    @Override
    @Transactional
    public boolean persistCoreModel(ImSingleMessage singleMessage) {
        ImSingleMessageET singleMessageET = convertCoreModelToEntity(singleMessage);
        this.save(singleMessageET);
        return true;
    }

    @Override
    public void sendSingleMessage(ImSingleMessage singleMessage) {
        ImTopicInfo topicInfo = new ImTopicInfo(singleMessage, ImConstants.IM_CHAT_TX_MESSAGE_TOPIC);
        messageSender.sendMessageInTransaction(topicInfo, null);
    }


    @Override
    public void sendMessageReadNotice(String messageId) {

    }

    @Override
    public void sendMessageWithDrawnNotice(String messageId) {

    }


}




