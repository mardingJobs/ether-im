package cn.ether.im.message.single.mapper;

import cn.ether.im.message.single.model.entity.ImChatMessageInbox;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jack
 * @description 针对表【im_chat_message_inbox(收件箱)】的数据库操作Mapper
 * @createDate 2024-09-22 15:44:47
 * @Entity cn.ether.im.message.model.entity.ImChatMessageInbox
 */
@Mapper
public interface ImChatMessageInboxMapper extends BaseMapper<ImChatMessageInbox> {

}




