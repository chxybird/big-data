package com.bird.topology.trident;

import com.bird.entity.po.UserPO;
import com.bird.utils.JsonUtils;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.trident.operation.*;
import org.apache.storm.trident.operation.builtin.Count;
import org.apache.storm.trident.operation.builtin.Max;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.UUID;

/**
 * @Author 李璞
 * @Date 2022/2/14 16:39
 * @Description
 */
public class TridentTopology {
    public static void main(String[] args) throws Exception {
        //模拟一个数据源 以用户信息为主 Trident发送数据以batch为单位,设置每个batch中两个tuple
        FixedBatchSpout spout = new FixedBatchSpout(new Fields("info"), 2,
                new Values(JsonUtils.entityToJson(new UserPO(16036001L, "岑方伟", 24, "男", 114))),
                new Values(JsonUtils.entityToJson(new UserPO(16036002L, "方雅婷", 22, "女", 124))),
                new Values(JsonUtils.entityToJson(new UserPO(16036003L, "耿晴雨", 21, "女", 147))),
                new Values(JsonUtils.entityToJson(new UserPO(16036004L, "邱光润", 25, "男", 139))),
                new Values(JsonUtils.entityToJson(new UserPO(16036005L, "许士涛", 22, "男", 128))),
                new Values(JsonUtils.entityToJson(new UserPO(16036006L, "潘敏飞", 25, "男", 140))),
                new Values(JsonUtils.entityToJson(new UserPO(16036007L, "王红", 21, "女", 158))),
                new Values(JsonUtils.entityToJson(new UserPO(16036008L, "王丽", 22, "女", 120))),
                new Values(JsonUtils.entityToJson(new UserPO(16036009L, "赵天海", 25, "男", 111))),
                new Values(JsonUtils.entityToJson(new UserPO(160360010L, "余周同", 20, "男", 102)))
        );
        spout.setCycle(false);
        //创建一个TridentTopology
        org.apache.storm.trident.TridentTopology tridentTopology = new org.apache.storm.trident.TridentTopology();
        tridentTopology.newStream("trident-stream", spout)
                //设置Soput的并行度 设置为1
                .parallelismHint(1)
                //each 可以理解each是做一个逻辑处理,相当于一个Bolt 这里接入上游的元组,返回下游的元组
                .each(new Fields("info"), new BaseFunction() {
                    @Override
                    public void execute(TridentTuple tuple, TridentCollector collector) {
                        //这里不做复杂的业务逻辑,把获取的user信息绑定一个UUID字段
                        String uuid = UUID.randomUUID().toString();
                        String jsonUser = tuple.getStringByField("info");
                        collector.emit(new Values(uuid, jsonUser));
                    }
                }, new Fields("uuid", "user"))
                //filter实现过滤
                .filter(new BaseFilter() {
                    @Override
                    public boolean isKeep(TridentTuple tuple) {
                        //过滤掉年龄大于27岁的用户
                        String jsonUser = tuple.getStringByField("user");
                        UserPO userPO = JsonUtils.jsonToEntity(jsonUser, UserPO.class);
                        return userPO.getAge() <= 27;
                    }
                })
                //窥视 peek的作用就相当于是一些额外操作,操作不会对流产生任何影响,一般用来调试和打印相关信息
                .peek((Consumer) tuple -> {
                    String user = tuple.getStringByField("user");
                    String uuid = tuple.getStringByField("uuid");
                    System.out.println("UUID:" + uuid + "用户信息:" + user);
                })
                //投影 用于过滤某些字段 保留想要的字段
                .project(new Fields("user"))
                //用于转换 可以使用each来实现
                .map((MapFunction) input -> {
                    String jsonUser = input.getStringByField("user");
                    UserPO userPO = JsonUtils.jsonToEntity(jsonUser, UserPO.class);
                    return new Values(userPO.getId(), userPO.getName(), userPO.getAge(), userPO.getSex(), userPO.getScore());
                }, new Fields("id", "name", "age", "sex", "score"))
                .peek((Consumer) input -> {
                    System.out.println("姓名:"+input.getStringByField("name")+"分数:"+input.getIntegerByField("score"));
                });
                //minBy 与maxBy 用于计算某字段的最大以及最小字段的元组信息
//                .minBy("score")
//                .peek((Consumer) input -> System.out.println(input.getStringByField("name")));
                //聚合操作
//                .chainedAgg()
//                .partitionAggregate(new Count(), new Fields("count"))
////                .partitionAggregate(new Fields("score"), new Max("score"), new Fields("max"))
//                .chainEnd()
//                .peek((Consumer) input -> {
//                    Integer maxScore = input.getIntegerByField("score");
//                    Integer count = input.getIntegerByField("count");
////                    System.out.println("最大分数为" + maxScore);
//                    System.out.println("总人数为:" + count);
//                });


        Config config = new Config();
        //配置worker的数量
        config.setNumWorkers(4);
        config.setMaxTaskParallelism(10);

        LocalCluster local = new LocalCluster();
        local.submitTopology("trident", config, tridentTopology.build());
    }
}
