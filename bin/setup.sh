#! /bin/sh
mkdir lib

wget http://ftp.jaist.ac.jp/pub/apache/storm/apache-storm-0.9.6/apache-storm-0.9.6.tar.gz -O lib/apache-storm-0.9.6.tar.gz
wget http://ftp.jaist.ac.jp/pub/apache/qpid/java/6.0.0/binaries/qpid-client-6.0.0-bin.tar.gz -O lib/qpid-client-6.0.0-bin.tar.gz
wget http://ftp.jaist.ac.jp/pub/apache/qpid/java/6.0.0/binaries/qpid-broker-6.0.0-bin.tar.gz 

tar xfz qpid-broker-6.0.0-bin.tar.gz

cd lib
tar xfz apache-storm-0.9.6.tar.gz
tar xfz qpid-client-6.0.0-bin.tar.gz

