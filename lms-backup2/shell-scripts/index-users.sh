#!/usr/bin/env bash
/usr/local/hadoop-2.7.2/bin/hadoop \
--config /usr/local/hadoop-2.7.2 jar /usr/local/search-mr/search-mr-1.2.0-job.jar org.apache.solr.hadoop.MapReduceIndexerTool \
-D 'mapred.child.java.opts=-Xmx500m' --log4j log4j.properties \
--morphline-file index-users.conf
--solr-home-dir src/test/resources/solr/minimr \
--output-dir hdfs://c2202.mycompany.com/user/$USER/test \
--shard-url http://solr001.mycompany.com:8983/solr/collection1 \
--go-live hdfs:///user/foo/indir
--dry-run
