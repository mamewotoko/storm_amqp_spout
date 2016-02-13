Storm spout using amqp as data source
=====================================

Install libraries
------------------
```
sh bin/setup.sh
```

Buiid
------
```
ant
```

Run: local mode
---------------
1. start qpid broker
```
cd qpid-broker/6.0.0
sh bin/qpid-server
```
2. open new terminal and start local storm topology
```
sh bin/run.sh
```
3. generate text message to spout
```
sh bin/generate.sh
```

TODO
----
* use maven
* pass connection setting as property file

----
Takashi Masuyama < mamewotoko@gmail.com >
http://mamewo.ddo.jp/
