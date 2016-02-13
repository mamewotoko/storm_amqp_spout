#! /bin/sh

STORM_HOME=lib/apache-storm-0.9.6

#local
${STORM_HOME}/bin/storm jar storm_amqp_spout.jar com.mamewo.storm.app.TestTopology


