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
    private Text adjacency = new Text();
    private DoubleWritable rank = null;

    private static long N;
    private static double factor;
    private static int iterationCnt;
    @Override
    public void configure(JobConf job) {
	N = Long.parseLong(job.get("NumberOfPages"));
	factor = (1 - d) / N;
	iterationCnt = Integer.parseInt(job.get("IterationCount"));
    }

    @Override
    public void reduce(Text key, Iterator<PageRankGenericWritable> values,
	    OutputCollector<Text, PageRankGenericWritable> output,
	    Reporter reporter) throws IOException {
//
//	while (values.hasNext()) {
//	    System.out.println("**************"+key.toString()+"**************"+values.next().get()+"**************");
//	}
	double newRank = 0.0;
	adjacency.set("");
	while (values.hasNext()) {
	    Writable val = values.next().get();
	    if (val instanceof DoubleWritable) {
		rank = (DoubleWritable) val;
		newRank += rank.get();
	    } else {
		adjacency.set((Text) val);
	    }
	}
	String rank = iterationCnt >= 1 ? String.valueOf(factor + d * newRank) : String.valueOf(1.0 / N);
	PageRankGenericWritable outputValue = new PageRankGenericWritable(new Text(rank + adjacency));
//	System.out.println("**************"+key+"**************");
	output.collect(key, outputValue);
    }
}
