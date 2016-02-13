package com.mamewo.storm.app;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TestTopology {
    public static class PrinterBolt
	extends BaseRichBolt
    {
	OutputCollector _collector;
		
	@Override
	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
	    _collector = collector;
	}

	@Override
	public void execute(Tuple tuple) {
	    LOG.info("receieved: " + tuple.getString(0));
	    _collector.emit(tuple, new Values(tuple.getString(0)));
	    _collector.ack(tuple);
	}
		
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	    declarer.declare(new Fields("word"));
	}
	static final private Logger LOG = LoggerFactory.getLogger(PrinterBolt.class);
    }

    public static void main(String args[])
	throws Exception
    {
	TopologyBuilder builder = new TopologyBuilder();
	builder.setSpout("word", new AMQSpout(), 3);
	builder.setBolt("out", new PrinterBolt(), 3).shuffleGrouping("word");

	Config conf = new Config();
	conf.setDebug(true);

	if (args != null && args.length > 0) {
	    conf.setNumWorkers(3);
			
	    StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
	}
	else {
	    LocalCluster cluster = new LocalCluster();
	    cluster.submitTopology("test", conf, builder.createTopology());
	    // Utils.sleep(10000);
	    // cluster.killTopology("test");
	    // cluster.shutdown();
	}
    }
}
