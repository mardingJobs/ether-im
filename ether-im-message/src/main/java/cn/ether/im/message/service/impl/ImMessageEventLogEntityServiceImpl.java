package cn.ether.im.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.ether.im.message.entity.ImMessageEventLogEntity;
import cn.ether.im.message.service.ImMessageEventLogEntityService;
import cn.ether.im.message.mapper.ImMessageEventLogEntityMapper;
import org.springframework.stereotype.Service;

/**
* @author jack
* @description 针对表【im_message_event_log(消息事件日志表)】的数据库操作Service实现
* @createDate 2024-09-17 20:58:16
*/
@Service
public class ImMessageEventLogEntityServiceImpl extends ServiceImpl<ImMessageEventLogEntityMapper, ImMessageEventLogEntity>
    implements ImMessageEventLogEntityService{

}




