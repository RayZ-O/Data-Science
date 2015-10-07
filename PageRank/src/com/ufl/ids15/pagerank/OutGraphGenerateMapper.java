package com.ufl.ids15.pagerank;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class OutGraphGenerateMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
    private Text title = new Text();
    private Text link = new Text();
    @Override
    public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) 
	    throws IOException {
	String line = value.toString();
	try {
	    StringTokenizer tokenizer = new StringTokenizer(line);
	    title.set(tokenizer.nextToken());
	    while (tokenizer.hasMoreTokens()) {
		link.set(tokenizer.nextToken());
		output.collect(new Text(title), link);
	    }
	} catch(NoSuchElementException e) {
	    // nothing to do
	}
    }
}
