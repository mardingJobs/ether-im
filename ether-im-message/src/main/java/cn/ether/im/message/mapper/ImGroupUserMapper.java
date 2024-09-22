package cn.ether.im.message.mapper;

import cn.ether.im.message.model.entity.ImGroupUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jack
 * @description 针对表【im_group_user(群和用户关系)】的数据库操作Mapper
 * @createDate 2024-09-22 14:48:20
 * @Entity cn.ether.im.message.model.entity.ImGroupUser
 */
@Mapper
public interface ImGroupUserMapper extends BaseMapper<ImGroupUser> {

}




