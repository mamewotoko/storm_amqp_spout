#! /bin/sh

java -cp "lib/qpid-client/6.0.0/lib/*.jar:storm_amqp_spout.jar" com.mamewo.amq.producer.AMQProducer

