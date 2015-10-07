package com.ufl.ids15.pagerank;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class RankSortMapper extends MapReduceBase implements Mapper<LongWritable, Text, DoubleWritable, Text> {

    @Override
    public void map(LongWritable key, Text value, OutputCollector<DoubleWritable, Text> output, Reporter reporter)
	    throws IOException {
	String line = value.toString();
	try {
	    StringTokenizer tokenizer = new StringTokenizer(line);
	    String title = tokenizer.nextToken();
	    double rank = Double.parseDouble(tokenizer.nextToken());
	    output.collect(new DoubleWritable(rank), new Text(title)); 
	} catch(NoSuchElementException e) {
	    // nothing to do
	}
    }

}
