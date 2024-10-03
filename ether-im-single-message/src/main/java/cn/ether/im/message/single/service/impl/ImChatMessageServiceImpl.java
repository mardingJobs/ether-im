package cn.ether.im.message.single.service.impl;

import cn.ether.im.message.single.mapper.ImChatMessageMapper;
import cn.ether.im.message.single.model.entity.ImChatMessageEntity;
import cn.ether.im.message.single.service.ImChatMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jack
 * @description 针对表【im_personal_message(单聊消息表)】的数据库操作Service实现
 * @createDate 2024-09-17 14:30:22
 */
@Service
public class ImChatMessageServiceImpl extends ServiceImpl<ImChatMessageMapper, ImChatMessageEntity>
        implements ImChatMessageService {

}




