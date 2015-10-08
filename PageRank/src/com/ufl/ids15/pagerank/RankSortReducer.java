package com.ufl.ids15.pagerank;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class RankSortReducer extends MapReduceBase implements
	Reducer<DoubleWritable, Text, DoubleWritable, Text> {

    @Override
    public void reduce(DoubleWritable key, Iterator<Text> values,
	    OutputCollector<DoubleWritable, Text> output, Reporter reportor)
	    throws IOException {
	while ((values.hasNext())) {
	    output.collect(key, values.next());
	}
    }

}
