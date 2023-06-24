package com.fevly.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.windowing.TupleWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*second bolt, see TopologyExecutor
. To aggregate  message with "||" concatenator.sent last 2 seconds
Using BaseWindowBolt, that uses time windows
    to generate groups of elements for a given period of time, using the timestamps. In this case,
    since we are using the field of "timestamp", it is guaranteed that at least one element exists in each window
* */
public class AggregatorBolt extends BaseWindowedBolt {
    private OutputCollector outputCollector;
    private static final Logger log = LoggerFactory
            .getLogger(AggregatorBolt.class);

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.outputCollector = collector;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("aggregatedMesageField"));
    }

    @Override
    public void execute(TupleWindow tupleWindow) {
        log.info("second bolt in operation......");
        List<Tuple> tuples = tupleWindow.get();
        tuples.sort(Comparator.comparing(this::getTimestamp));

        String aggregatedMesageField = tuples.stream()
                .map(tuple -> tuple.getStringByField("filteredMessageField"))
                .collect(Collectors.joining(" ||"));

        Values values = new Values(aggregatedMesageField);
        outputCollector.emit(values);
        log.info("End second bolt......");
    }

    private Long getTimestamp(Tuple tuple) {
        return tuple.getLongByField("timestamp");
    }

}
