package cn.ether.im.message.mapper;

import cn.ether.im.message.model.entity.ImPersonalMessageEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jack
 * @description 针对表【im_personal_message(单聊消息表)】的数据库操作Mapper
 * @createDate 2024-09-17 14:30:22
 * @Entity cn.ether.im.message.domain.ImPersonalMessage
 */
@Mapper
public interface ImPersonalMessageMapper extends BaseMapper<ImPersonalMessageEntity> {

}




