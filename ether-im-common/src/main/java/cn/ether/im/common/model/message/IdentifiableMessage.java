package cn.ether.im.common.model.message;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/18 16:07
 * * @Description
 * * @Github https://github.com/mardingJobs
 **/
public interface IdentifiableMessage {

    /**
     * 获取唯一标识
     *
     * @return
     */
    String uniqueId();

}
