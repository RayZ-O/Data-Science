package com.ufl.ids15.pagerank;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class PageRankReducer extends MapReduceBase implements
	Reducer<Text, PageRankGenericWritable, Text, PageRankGenericWritable> {

    static final double d = 0.85;
    private Text adjacency = null;
    private DoubleWritable rank = null;

    private static Long N;
    @Override
    public void configure(JobConf job) {
	N = Long.parseLong(job.get("NumberOfPages"));
    }

    @Override
    public void reduce(Text key, Iterator<PageRankGenericWritable> values,
	    OutputCollector<Text, PageRankGenericWritable> output,
	    Reporter reporter) throws IOException {

	double newRank = 0.0;
	while (values.hasNext()) {
	    Writable val = values.next().get();
	    if (val instanceof DoubleWritable) {
		rank = (DoubleWritable) val;
		newRank += rank.get();
	    } else {
		adjacency = (Text) val;
	    }
	}
	newRank = (1 - d) / N + d * newRank; // TODO get N from last job
	PageRankGenericWritable outputValue = new PageRankGenericWritable(
		new Text(String.valueOf(newRank) + adjacency));
	output.collect(key, outputValue);
    }
}
