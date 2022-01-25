package com.bird.topology.cartransform;

import com.bird.StormApp;
import com.bird.entity.dto.CarDTO;
import com.bird.utils.JsonUtils;
import com.bird.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author 李璞
 * @Date 2022/1/17 17:04
 * @Description
 */
@Slf4j
public class Bolt extends BaseBasicBolt {

    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context) {
        super.prepare(topoConf, context);
        //调用Spring初始化方法,创建Spring容器
        StormApp.main(null);
    }


    /**
     * @Author 李璞
     * @Date 2022/1/24 15:36
     * @Description 每有一个元组发射过来就会被调用一次
     */
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        KafkaTemplate<String,String> kafkaTemplate =(KafkaTemplate<String, String>) SpringContextUtils.getBean("kafkaTemplate");
        //获取tuple的数据
        String value = tuple.getStringByField("value");
        CarDTO carDTO = JsonUtils.jsonToEntity(value, CarDTO.class);
        //提取Brand与Speed进行相关计算
        String brand = carDTO.getBrand();
        String speed = carDTO.getSpeed();
        String uuid = carDTO.getId();
        //速度超过160贴罚单
        carDTO.setIsPunish(Integer.parseInt(speed) > 80);
        //将受到惩罚的汽车数据推送到Kafka
        //采用同步调用的方式发送
        kafkaTemplate.send("topic-punish", uuid, speed);


    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
