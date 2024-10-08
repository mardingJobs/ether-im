package cn.ether.im.message.group.mapper;

import cn.ether.im.message.group.model.entity.ImGroupMessageET;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jack
 * @description 针对表【im_group_message(群聊消息表，存放所有群消息)】的数据库操作Mapper
 * @createDate 2024-10-07 15:35:45
 * @Entity cn.ether.im.message.group.model.entity.ImGroupMessageET
 */
@Mapper
public interface ImGroupMessageETMapper extends BaseMapper<ImGroupMessageET> {

}




