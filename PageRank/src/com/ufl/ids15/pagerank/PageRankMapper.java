package com.ufl.ids15.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class PageRankMapper extends MapReduceBase implements
	Mapper<LongWritable, Text, Text, PageRankGenericWritable> {

    private Text adjacency = new Text();
    private DoubleWritable rankWritable = new DoubleWritable(0.0);

    @Override
    public void map(LongWritable key, Text value,
	    OutputCollector<Text, PageRankGenericWritable> output,
	    Reporter reporter) throws IOException {
	String line = value.toString();
	String[] parts = line.split("\t");
	String title = parts[0];
	Double rank = Double.parseDouble(parts[1]);
	int size = parts.length - 2;
	if (size < 1) {
	    adjacency.set("");
	    output.collect(new Text(title), new PageRankGenericWritable(
		    adjacency));
	} else {
	    adjacency.set(line.substring(line.indexOf('\t',
		    line.indexOf('\t') + 1))); // get adjacency
	    output.collect(new Text(title), new PageRankGenericWritable(
		    adjacency));
	    for (int i = 2; i < parts.length; i++) {
		rankWritable.set(rank / size);
		output.collect(new Text(parts[i]), new PageRankGenericWritable(
			rankWritable));
	    }
	}
    }
}
