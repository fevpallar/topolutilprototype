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
    public static void runTopology() throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("sourceTuplesID", new RandSpout());
        builder.setBolt("filteringBoltID", new FilterBolt()).shuffleGrouping("sourceTuplesID");
        builder.setBolt("aggregBoltID", new AggregatorBolt()
                        .withTimestampField("timestamp")
                        .withLag(BaseWindowedBolt.Duration.seconds(1))
                        .withWindow(BaseWindowedBolt.Duration.seconds(2)))
                .shuffleGrouping("filteringBoltID");
        builder.setBolt("printingAggregID", new OutputBolt())
                .shuffleGrouping("aggregBoltID");

        Config config = new Config();
        config.setDebug(false);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("CustomTopog", config, builder.createTopology());
    }

    public static void main(String[] args) throws Exception {
        runTopology();
    }
}
