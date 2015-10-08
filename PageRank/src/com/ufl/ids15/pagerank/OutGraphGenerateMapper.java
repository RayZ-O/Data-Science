package com.ufl.ids15.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class OutGraphGenerateMapper extends MapReduceBase implements
Mapper<LongWritable, Text, Text, Text> {

    private Text title = new Text();
    private Text link = new Text();

    @Override
    public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter)
	    throws IOException {
	String line = value.toString();
	String[] s = line.split("\t");
	title.set(s[0]);
	for (int i = 1; i < s.length; i++) {
	    link.set(s[i]);
	    output.collect(title, link);
	}
    }
}
