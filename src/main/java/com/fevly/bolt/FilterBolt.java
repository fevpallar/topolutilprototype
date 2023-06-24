package com.fevly.bolt;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*The first bolt that receives the tuples from the spout is the FilteringMessageBolt, which
 is responsible for filtering out all the messages that contain the letter ‘z’.*/
public class FilterBolt extends BaseBasicBolt {

    private static final Logger log = LoggerFactory
            .getLogger(FilterBolt.class);

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        log.info("First bolt in operation......");
        String randomMessage = tuple.getStringByField("randomMessage");
        if (!randomMessage.contains("z")) {
            basicOutputCollector.emit(tuple.getValues());
        } else {
            log.info("Message filter out: " + randomMessage);
        }
        log.info("End first bolt......");

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("filteredMessageField", "timestamp"));
    }
}
