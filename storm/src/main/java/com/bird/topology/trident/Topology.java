package com.bird.topology.trident;

import com.bird.entity.po.StudentPO;
import com.bird.utils.JsonUtils;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.BaseFilter;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.Consumer;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * @Author 李璞
 * @Date 2022/2/11 14:04
 * @Description Trident(三叉戟) 是Storm的一个高级抽象,提供了更加方便的Storm编程，隐藏了底层批量、事务、ACK等等细节。由抽象自动实现。
 */
public class Topology {
    public static void main(String[] args) throws Exception {
        /**
         * 使用StormTopology创建拓扑主要是基于Spout以及Bolt来进行建设的,使用Trident主要是建设TridentSpout、Operation、State。
         * TridentSpout类似于Spout,Operation类似于Bolt,State就是持久化。TridentTopology最终会被转化为StormTopology
         */

        //创建一个Spout 此Spout会循环无线的拉取数据 用于测试
        FixedBatchSpout spout = new FixedBatchSpout(new Fields("student"), 2,
                new Values(JsonUtils.entityToJson(new StudentPO("张三", 20))),
                new Values(JsonUtils.entityToJson(new StudentPO("李四", 25))),
                new Values(JsonUtils.entityToJson(new StudentPO("王五", 30))));
        spout.setCycle(true);
        //创建一个TridentTopology
        TridentTopology tridentTopology = new TridentTopology();
        tridentTopology.newStream("word-read", spout)
                .parallelismHint(2)
                //从word-read流中的student字段读取内容并作each操作输出为age字段 (提取age信息)
                .each(new Fields("student"),new BaseFunction() {
                    @Override
                    public void execute(TridentTuple tuple, TridentCollector collector) {
                        String json = tuple.getStringByField("student");
                        StudentPO student = JsonUtils.jsonToEntity(json, StudentPO.class);
                        Integer age = student.getAge();
                        collector.emit(new Values(age));
                    }
                }, new Fields("age"))
                //过滤
                .filter(new BaseFilter() {
                    @Override
                    public boolean isKeep(TridentTuple tuple) {
                        Integer age = tuple.getIntegerByField("age");
                        return age <= 26;
                    }
                })
                //peek遍历每个元组不会做任何操作,类似于打印功能
                .peek((Consumer) input -> {
                    Integer age = input.getIntegerByField("age");
                    System.out.println("---拿到数据有---" + age);
                });

        Config config = new Config();
        //配置worker的数量
        config.setNumWorkers(4);

        LocalCluster local = new LocalCluster();
        local.submitTopology("trident", config, tridentTopology.build());

    }
}
