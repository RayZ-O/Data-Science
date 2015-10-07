package com.ufl.ids15.pagerank;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class XmlExtractReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter)
	    throws IOException {
	boolean notRedLink = false;
	HashSet<Text> pages = new HashSet<>();
	while (values.hasNext()) {
	    Text page = values.next();
	    if (page.equals("#")) {
		notRedLink = true;
		continue;
	    }
	    pages.add(page);
	}
	if (notRedLink) {
	    for (Text p : pages) {
		output.collect(key, p);
	    }
	}
    }

}
