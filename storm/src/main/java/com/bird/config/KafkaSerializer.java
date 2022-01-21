package com.bird.config;

import com.bird.utils.JsonUtils;
import org.apache.kafka.common.serialization.Serializer;

/**
 * @Author 李璞
 * @Date 2022/1/18 14:55
 * @Description Kafka自定义序列化器 可以不使用
 */
public class KafkaSerializer<T> implements Serializer<T> {
    /**
     * @Author 李璞
     * @Date 2022/1/18 14:57
     * @Description 自定义序列化器
     */
    @Override
    public byte[] serialize(String topic, T data) {
        //使用Jackson进行序列化
        return JsonUtils.entityToBytes(data);
    }


}
