package com.bird.topology.trident.filter;

import org.apache.storm.trident.operation.BaseFilter;
import org.apache.storm.trident.operation.TridentOperationContext;
import org.apache.storm.trident.tuple.TridentTuple;

import java.util.Map;

/**
 * @Author 李璞
 * @Date 2022/2/15 10:07
 * @Description
 */
public class CustomerFilter extends BaseFilter {

    private int partitionIndex;

    @Override
    public void prepare(Map conf, TridentOperationContext context) {
        this.partitionIndex = context.getPartitionIndex();
    }

    @Override
    public boolean isKeep(TridentTuple tuple) {
        System.err.println("I am partition [" + partitionIndex + "] and I have kept a tweet"+tuple.get(0));
        return true;
    }
}
