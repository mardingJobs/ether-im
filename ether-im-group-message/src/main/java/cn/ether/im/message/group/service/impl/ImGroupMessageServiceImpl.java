package cn.ether.im.message.group.service.impl;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImMessageContentType;
import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.model.info.ImTopicInfo;
import cn.ether.im.common.model.message.ImGroupMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.mq.ImMessageMQSender;
import cn.ether.im.common.util.SnowflakeUtil;
import cn.ether.im.message.group.mapper.ImGroupMessageETMapper;
import cn.ether.im.message.group.model.dto.MessageSendReq;
import cn.ether.im.message.group.model.entity.ImGroupMessageET;
import cn.ether.im.message.group.model.entity.ImGroupMessageExt;
import cn.ether.im.message.group.model.entity.ImGroupUserET;
import cn.ether.im.message.group.model.session.SessionContext;
import cn.ether.im.message.group.service.ImGroupMessageExtService;
import cn.ether.im.message.group.service.ImGroupMessageService;
import cn.ether.im.message.group.service.ImGroupUserETService;
import cn.ether.im.sdk.agent.ImMessageAgent;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static cn.ether.im.common.enums.ImChatMessageStatus.INIT;

/**
 * @author jack
 * @description 针对表【im_group_message(群聊消息表，存放所有群消息)】的数据库操作Service实现
 * @createDate 2024-10-07 15:35:45
 */
@Slf4j
@Service
public class ImGroupMessageServiceImpl extends ServiceImpl<ImGroupMessageETMapper, ImGroupMessageET>
        implements ImGroupMessageService {

    @Resource
    private SnowflakeUtil snowflakeUtil;

    @Resource
    private ImMessageMQSender messageMQSender;

    @Resource
    private ImMessageAgent messageAgent;

    @Resource
    private ImGroupUserETService groupUserService;

    @Resource
    private ImGroupMessageExtService groupMessageExtService;


    @Override
    public ImGroupMessage convertSendReqToCoreModel(MessageSendReq sendReq) {
        ImUserTerminal loggedUser = SessionContext.loggedUser();
        Long messageId = snowflakeUtil.nextId();
        ImGroupMessage singleMessage = new ImGroupMessage();
        singleMessage.setMessageId(messageId);
        singleMessage.setContent(sendReq.getContent());
        singleMessage.setContentType(ImMessageContentType.valueOf(sendReq.getContentType()));
        singleMessage.setSenderId(loggedUser.getUserId());
        singleMessage.setSenderTerminal(loggedUser.getTerminalType());
        singleMessage.setGroupId(sendReq.getGroupId());
        singleMessage.setSendTime(System.currentTimeMillis());
        return singleMessage;
    }

    @Override
    public ImGroupMessageET convertCoreModelToEntity(ImGroupMessage singleMessage) {
        ImGroupMessageET groupMessageET = new ImGroupMessageET();
        BeanUtil.copyProperties(singleMessage, groupMessageET);
        groupMessageET.setStatus(INIT.name());
        groupMessageET.setCreateTime(new Date());
        return groupMessageET;
    }

    @Override
    @Transactional
    public boolean persistCoreModel(ImGroupMessage groupMessage) {
        ImGroupMessageET groupMessageET = convertCoreModelToEntity(groupMessage);
        this.save(groupMessageET);
        ImGroupMessageExt ext = new ImGroupMessageExt();
        ext.setMessageId(groupMessageET.getMessageId());
        ext.setGroupId(groupMessageET.getGroupId());
        ext.setReceivedUserIds(groupMessage.getSenderId());
        groupMessageExtService.save(ext);
        return true;
    }

    @Override
    public ImGroupMessage convertEntityToCoreModel(ImGroupMessageET singleMessageET) {
        ImGroupMessage groupMessage = new ImGroupMessage();
        BeanUtil.copyProperties(singleMessageET, groupMessage);
        return groupMessage;
    }

    @Override
    public void sendGroupMessage(ImGroupMessage groupMessage) {
        ImTopicInfo topicInfo = new ImTopicInfo(groupMessage, ImConstants.IM_GROUP_TX_MESSAGE_TOPIC);
        messageMQSender.sendMessageInTransaction(topicInfo, null);
    }

    @Override
    public void resendUnReceivedMessage(String userId, ImTerminalType terminalType) {
        List<ImGroupUserET> groupIds = groupUserService.lambdaQuery().eq(ImGroupUserET::getUserId, userId)
                .select(ImGroupUserET::getGroupId).list();
        if (CollectionUtil.isEmpty(groupIds)) {
            return;
        }

        List<ImGroupMessageExt> groupMessageExts = groupMessageExtService.lambdaQuery()
                .in(ImGroupMessageExt::getGroupId, groupIds)
                .list();

        if (CollectionUtil.isEmpty(groupMessageExts)) {
            return;
        }

        List<ImGroupMessageExt> unReceivedGroupMessages = groupMessageExts.stream().filter((groupMessageExt) -> {
            String receivedUserIds = groupMessageExt.getReceivedUserIds();
            if (StringUtils.isEmpty(receivedUserIds)) {
                return true;
            }
            String[] split = receivedUserIds.split(",");
            return !Arrays.asList(split).contains(userId);
        }).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(unReceivedGroupMessages)) {
            return;
        }

        List<Long> unReceiveMessageIds = unReceivedGroupMessages.stream().map(ImGroupMessageExt::getMessageId).collect(Collectors.toList());

        List<ImGroupMessageET> groupMessageETS = this.lambdaQuery().eq(ImGroupMessageET::getMessageId, unReceiveMessageIds).list();

        List<ImGroupMessage> groupMessages = groupMessageETS.stream().map(this::convertEntityToCoreModel)
                .peek(groupMessage -> {
                    groupMessage.setReceiverIds(Arrays.asList(userId));
                    groupMessage.setLimitTerminals(Arrays.asList(new ImUserTerminal(userId, terminalType)));
                }).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(groupMessages)) {
            return;
        }
        try {
            messageAgent.batchSendGroupMessage(groupMessages);
        } catch (Exception e) {
            log.error("重发消息失败,userId:{}", userId, e);
        }

    }


    @Override
    public void sendMessageReadNotice(String messageId) {

    }

    @Override
    public void sendMessageWithDrawnNotice(String messageId) {

    }

}




