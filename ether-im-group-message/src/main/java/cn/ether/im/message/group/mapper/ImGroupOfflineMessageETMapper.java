package cn.ether.im.message.group.mapper;

import cn.ether.im.message.group.model.entity.ImGroupMessageExt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author jack
 * @description 针对表【im_group_offline_message(存放未接收的消息)】的数据库操作Mapper
 * @createDate 2024-10-07 15:35:45
 * @Entity cn.ether.im.message.group.model.entity.ImGroupOfflineMessageET
 */
@Mapper
public interface ImGroupOfflineMessageETMapper extends BaseMapper<ImGroupMessageExt> {


    void addMessageReceiveId(@Param("messageId") Long messageId,
                             @Param("userId") String userId);

}




