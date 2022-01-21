package com.bird.topology.cartransform;

import com.bird.entity.dto.CarDTO;
import com.bird.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.Resource;

/**
 * @Author 李璞
 * @Date 2022/1/17 17:04
 * @Description
 */
@Slf4j
public class Bolt extends BaseBasicBolt {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        //获取tuple的数据
        String value = tuple.getStringByField("value");
        CarDTO carDTO = JsonUtils.jsonToEntity(value, CarDTO.class);
        //提取Brand与Speed进行相关计算
        String brand = carDTO.getBrand();
        String speed = carDTO.getSpeed();
        String uuid = carDTO.getId();
        //速度超过160贴罚单
        carDTO.setIsPunish(Integer.parseInt(speed) > 160);
        //将受到惩罚的汽车数据推送到Kafka
        //采用异步调用的方式
        kafkaTemplate.send("topic-punish", uuid, speed).addCallback((success) -> {
            //发送成功
            log.info("UUID:{}的{}汽车超速违规,速度为{}",uuid,brand,speed);
        }, (failure) -> {
            //获取失败信息
            String message = failure.getMessage();
            log.error("消息发送失败,失败的原因为:{}",message);
        });
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
