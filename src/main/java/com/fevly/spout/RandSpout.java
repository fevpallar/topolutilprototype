package com.fevly.spout;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.Map;
import java.util.Random;

/*This class  is the source of the stream,
responsible to emit tuples to the topology.*/
public class RandSpout extends BaseRichSpout {
    /**
     * This output collector exposes the API for emitting tuples from an IRichSpout. The main
     * difference between this output collector and OutputCollector for IRichBolt is that
     * spouts can tag messages with ids so that they can be acked or failed later on.
     * This is the Spout portion of Storm’s API to guarantee that each message is fully processed at least once.
     */
    private SpoutOutputCollector outputCollector;

    @Override
    public void open(Map map, TopologyContext topologyContext,
                     SpoutOutputCollector spoutOutputCollector) {
        outputCollector = spoutOutputCollector;
    }

    protected String getSaltString() {
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * CHARS.length());
            salt.append(CHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    @Override
    public void nextTuple() {
        Utils.sleep(1000); // every one second
        //  Value class is A convenience class for making tuple values using new Values(“field1”, 2, 3) syntax.
        outputCollector.emit(new Values(getSaltString(),
                System.currentTimeMillis()));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("randomMessage", "timestamp"));
    }
}
