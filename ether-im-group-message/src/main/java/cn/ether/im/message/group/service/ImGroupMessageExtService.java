package cn.ether.im.message.group.service;

import cn.ether.im.message.group.model.entity.ImGroupMessageExt;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author jack
 * @description 针对表【im_group_offline_message(存放未接收的消息)】的数据库操作Service
 * @createDate 2024-10-07 15:35:45
 */
public interface ImGroupMessageExtService extends IService<ImGroupMessageExt> {


    void refreshReceiveIds(Long messageId, String userId);
}
