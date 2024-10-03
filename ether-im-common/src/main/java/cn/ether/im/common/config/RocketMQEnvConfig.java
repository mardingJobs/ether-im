//package cn.ether.im.common.config;
//
//import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.lang.NonNull;
//
///**
// * RocketMQ多环境隔离配置
// * 原理：对于每个配置的Bean在实例化前，拿到Bean的监听器注解把group或者topic改掉
// */
//@Configuration
//public class RocketMQEnvConfig implements BeanPostProcessor {
//
//    @Value("${spring.profiles.active:default}")
//    private String environmentName;
//
//    @Override
//    public Object postProcessBeforeInitialization(@NonNull Object bean,
//                                                  @NonNull String beanName) throws BeansException {
//        // DefaultRocketMQListenerContainer是监听器实现类
//        if (bean instanceof DefaultRocketMQListenerContainer) {
//            DefaultRocketMQListenerContainer container = (DefaultRocketMQListenerContainer) bean;
//            // 开启消息隔离情况下获取隔离配置，此处隔离topic，根据自己的需求隔离group或者tag
//            container.setTopic(String.join("-", container.getTopic(), environmentName));
//            container.setConsumerGroup(String.join("-", container.getConsumerGroup(), environmentName));
//            return container;
//        }
//        return bean;
//    }
//}