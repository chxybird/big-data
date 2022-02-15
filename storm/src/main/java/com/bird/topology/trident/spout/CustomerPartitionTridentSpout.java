package com.bird.topology.trident.spout;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.trident.spout.IPartitionedTridentSpout;
import org.apache.storm.tuple.Fields;

import java.util.Map;

/**
 * @Author 李璞
 * @Date 2022/2/14 14:18
 * @Description
 */
public class CustomerPartitionTridentSpout implements IPartitionedTridentSpout {

    @Override
    public Coordinator getCoordinator(Map conf, TopologyContext context) {
        return null;
    }

    @Override
    public Emitter getEmitter(Map conf, TopologyContext context) {
        return null;
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    @Override
    public Fields getOutputFields() {
        return null;
    }
}
