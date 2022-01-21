package com.bird.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 李璞
 * @Date 2022/1/18 13:07
 * @Description
 */
@Configuration
public class KafkaConfig {

    /**
     * @Author 李璞
     * @Date 2022/1/18 13:08
     * @Description 主题:topic-storm 分区:1 副本:1
     */
    @Bean
    public NewTopic topicStorm() {
        return new NewTopic("topic-storm", 1, (short) 1);
    }

    /**
     * @Author 李璞
     * @Date 2022/1/18 17:01
     * @Description 主题:topic-punish 分区:1 副本:1
     */
    @Bean
    public NewTopic topicPunish() {
        return new NewTopic("topic-punish", 1, (short) 1);
    }


}
