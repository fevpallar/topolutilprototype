package com.fevly.topology;

import com.fevly.bolt.AggregatorBolt;
import com.fevly.spout.RandSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseWindowedBolt;
import com.fevly.bolt.FilterBolt;
import com.fevly.bolt.OutputBolt;


public class TopolExec {

    private static final String spoutID = "sourceTuplesID";
    private static final String filterBoltID = "filteringBoltID";
    private static final String aggBoltID = "aggregBoltID";
    private static final String ouputBoldID = "printingAggregID";
    
    public static void runTopology() throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout(spoutID, new RandSpout());
        builder.setBolt(filterBoltID, new FilterBolt()).shuffleGrouping(spoutID);
        builder.setBolt(aggBoltID, new AggregatorBolt()
                        .withTimestampField("timestamp")
                        .withLag(BaseWindowedBolt.Duration.seconds(1))
                        .withWindow(BaseWindowedBolt.Duration.seconds(2)))
                .shuffleGrouping(filterBoltID);
        builder.setBolt(ouputBoldID, new OutputBolt())
                .shuffleGrouping(aggBoltID);

        Config config = new Config();
        config.setDebug(false);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("CustomTopog", config, builder.createTopology());
    }

    public static void main(String[] args) throws Exception {
        runTopology();
    }
}
