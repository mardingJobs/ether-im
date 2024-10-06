package cn.ether.im.message.single.service;

import cn.ether.im.common.model.message.ImSingleMessage;
import cn.ether.im.message.single.model.dto.MessageSendReq;
import cn.ether.im.message.single.model.entity.ImSingleMessageET;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author jack
 * @description 针对表【im_single_message(单聊信息表)】的数据库操作Service
 * @createDate 2024-10-05 19:55:33
 */
public interface ImSingleMessageService extends IService<ImSingleMessageET> {

    /**
     * 将发送请求转为单聊消息模型
     *
     * @param sendReq
     * @return
     */
    ImSingleMessage convertSendReqToCoreModel(MessageSendReq sendReq);

    /**
     * 将单聊消息模型转为实体
     *
     * @param singleMessage
     * @return
     */
    ImSingleMessageET convertCoreModelToEntity(ImSingleMessage singleMessage);

    /**
     * 持久化模型
     *
     * @param singleMessage
     */
    boolean persistCoreModel(ImSingleMessage singleMessage);

    /**
     * 发送单聊消息
     *
     * @param singleMessage
     * @return
     * @throws Exception
     */
    void sendSingleMessage(ImSingleMessage singleMessage) throws Exception;

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
