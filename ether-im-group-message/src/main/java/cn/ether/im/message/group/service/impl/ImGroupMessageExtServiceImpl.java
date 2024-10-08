package cn.ether.im.message.group.service.impl;

import cn.ether.im.message.group.mapper.ImGroupOfflineMessageETMapper;
import cn.ether.im.message.group.model.entity.ImGroupMessageExt;
import cn.ether.im.message.group.service.ImGroupMessageExtService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jack
 * @description 针对表【im_group_offline_message(存放未接收的消息)】的数据库操作Service实现
 * @createDate 2024-10-07 15:35:45
 */
@Service
public class ImGroupMessageExtServiceImpl extends ServiceImpl<ImGroupOfflineMessageETMapper, ImGroupMessageExt>
        implements ImGroupMessageExtService {

    @Override
    public void refreshReceiveIds(Long messageId, String userId) {
        ImGroupMessageExt messageExt = this.getById(messageId);
        if (messageExt == null) return;
        String receivedUserIds = messageExt.getReceivedUserIds();
        if (StringUtils.isBlank(receivedUserIds)) {
            messageExt.setReceivedUserIds(receivedUserIds);
            this.updateById(messageExt);
        } else {
            List<String> list = Arrays.stream(receivedUserIds.split(",")).collect(Collectors.toList());
            if (!list.contains(userId)) {
                list.add(userId);
                messageExt.setReceivedUserIds(StringUtils.join(list, ","));
                this.updateById(messageExt);
            }
        }


    }
}




