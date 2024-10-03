package cn.ether.im.message.single.mapper;

import cn.ether.im.message.single.model.entity.ImConversationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jack
 * @description 针对表【im_conversation(会话表)】的数据库操作Mapper
 * @createDate 2024-09-26 03:27:15
 * @Entity cn.ether.im.message.model.entity.ImConversation
 */
@Mapper
public interface ImConversationMapper extends BaseMapper<ImConversationEntity> {

}




