package com.ufl.ids15.pagerank;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

public class OutGraphGenerate {
    public static void getAdjacencyGraph(String input, String output)
	    throws Exception {
	JobConf conf = new JobConf(OutGraphGenerate.class);
	conf.setJobName("outgraphgenerate");
	conf.setInputFormat(TextInputFormat.class);

	conf.setOutputFormat(TextOutputFormat.class);
	conf.setOutputKeyClass(Text.class);
	conf.setOutputValueClass(Text.class);

	conf.setMapperClass(OutGraphGenerateMapper.class);
	conf.setReducerClass(OutGraphGenerateReducer.class);

	FileInputFormat.setInputPaths(conf, new Path(input));
	FileOutputFormat.setOutputPath(conf, new Path(output));

	JobClient.runJob(conf);
    }

    public static void main(String[] args) throws Exception {
	getAdjacencyGraph("/cise/homes/rui/Desktop/PageRank.outlink",
		"/cise/homes/rui/Desktop/output");
    }
}
