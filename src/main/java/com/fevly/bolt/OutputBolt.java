package com.fevly.bolt;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutputBolt extends BaseBasicBolt {

    private static final Logger log = LoggerFactory
            .getLogger(OutputBolt.class);

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        String aggregatedMesageField = tuple.getStringByField("aggregatedMesageField");
        log.info("Result: " + aggregatedMesageField);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
