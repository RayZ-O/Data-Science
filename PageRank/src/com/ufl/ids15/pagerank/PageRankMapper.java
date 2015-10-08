package com.ufl.ids15.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class PageRankMapper extends MapReduceBase implements
Mapper<LongWritable, Text, Text, PageRankGenericWritable> {

    private Text adjacency = new Text();
    private Text adjNode = new Text();
    private DoubleWritable rankWritable = new DoubleWritable(0.0);

    private static int iterationCnt;
    private static long N;
    @Override
    public void configure(JobConf job) {
	N = Long.parseLong(job.get("NumberOfPages"));
	iterationCnt = Integer.parseInt(job.get("IterationCount"));
    }

    @Override
    public void map(LongWritable key, Text value,
	    OutputCollector<Text, PageRankGenericWritable> output,
	    Reporter reporter) throws IOException {
	String line = value.toString();
	String[] parts = null;
	if (iterationCnt < 1) {
	    // get the length of adjacency list
	    long adjListSize = line.split("\t").length - 1;
	    // split the title and adjacency list
	    parts = line.split("\t", 2);
	    String initialRank = Double.toString(1.0 / N) + '\t';
	    String adjSize = Long.toString(adjListSize) + '\t';
	    String adjList = adjListSize > 0 ? parts[1] : "";
	    adjacency.set(initialRank + adjSize + adjList);
	} else {
	    parts = line.split("\t", 4);
	    long adjListSize = Long.parseLong(parts[2]);
	    String adjList = adjListSize > 0 ? parts[3] : "";
	    adjacency.set(parts[2] + '\t' + adjList);
	    double rank = adjListSize > 0 ? Double.parseDouble(parts[1]) / adjListSize : 0;
	    for (int i = 2; i < parts.length; i++) {
		adjNode.set(parts[i]);
		rankWritable.set(rank);
		output.collect(adjNode, new PageRankGenericWritable(rankWritable));
	    }
	}
	String title = parts[0];
	output.collect(new Text(title), new PageRankGenericWritable(adjacency));
    }
}
