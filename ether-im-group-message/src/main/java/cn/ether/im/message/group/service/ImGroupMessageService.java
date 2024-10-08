package cn.ether.im.message.group.service;

import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.model.message.ImGroupMessage;
import cn.ether.im.message.group.model.dto.MessageSendReq;
import cn.ether.im.message.group.model.entity.ImGroupMessageET;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author jack
 * @description 针对表【im_group_message(群聊消息表，存放所有群消息)】的数据库操作Service
 * @createDate 2024-10-07 15:35:45
 */
public interface ImGroupMessageService extends IService<ImGroupMessageET> {


    /**
     * 将发送请求转为单聊消息模型
     *
     * @param sendReq
     * @return
     */
    ImGroupMessage convertSendReqToCoreModel(MessageSendReq sendReq);

    /**
     * 将单聊消息模型转为实体
     *
     * @param groupMessage
     * @return
     */
    ImGroupMessageET convertCoreModelToEntity(ImGroupMessage groupMessage);

    /**
     * 持久化模型
     *
     * @param groupMessage
     */
    boolean persistCoreModel(ImGroupMessage groupMessage);


    /**
     * 将实体转为模型
     *
     * @param GroupMessageET
     * @return
     */
    ImGroupMessage convertEntityToCoreModel(ImGroupMessageET GroupMessageET);

    /**
     * 发送单聊消息
     *
     * @param groupMessage
     * @return
     * @throws Exception
     */
    void sendGroupMessage(ImGroupMessage groupMessage) throws Exception;


    /**
     * 重发收到的消息
     *
     * @param userId       接收用户ID
     * @param terminalType 接收终端类型
     */
    void resendUnReceivedMessage(String userId, ImTerminalType terminalType);

    /**
     * 发送消息已读通知
     *
     * @param messageId
     */
    void sendMessageReadNotice(String messageId);

    /**
     * 发送消息已撤回通知
     *
     * @param messageId
     */
    void sendMessageWithDrawnNotice(String messageId);

}
