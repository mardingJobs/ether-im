package cn.ether.im.message.mapper;

import cn.ether.im.message.entity.ImMessageEventLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author jack
* @description 针对表【im_message_event_log(消息事件日志表)】的数据库操作Mapper
* @createDate 2024-09-17 20:58:16
* @Entity cn.ether.im.message.domain.ImMessageEventLogEntity
*/
@Mapper
public interface ImMessageEventLogEntityMapper extends BaseMapper<ImMessageEventLogEntity> {

}




