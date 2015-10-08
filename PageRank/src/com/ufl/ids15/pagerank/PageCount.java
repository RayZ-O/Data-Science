package com.ufl.ids15.pagerank;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

public class PageCount {
    public static void calTotalPages(String input, String output)
	    throws Exception {
	JobConf conf = new JobConf(PageCount.class);
	conf.setJobName("pagecount");
	conf.setInputFormat(TextInputFormat.class);

	conf.setOutputFormat(TextOutputFormat.class);
	conf.setOutputKeyClass(Text.class);
	conf.setOutputValueClass(IntWritable.class);

	conf.setMapperClass(PageCountMapper.class);
	conf.setReducerClass(PageCountReducer.class);

	FileInputFormat.setInputPaths(conf, new Path(input));
	FileOutputFormat.setOutputPath(conf, new Path(output));

	JobClient.runJob(conf);

	Configuration fsconf = new Configuration();
	Path outPath = new Path("output");
	FileSystem fs = FileSystem.get(outPath.toUri(), fsconf);
	BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(outPath)));
	String line = br.readLine();
	conf.set("NumberOfPages", line.substring(line.indexOf('=') + 1));;
    }

    public static void main(String[] args) throws Exception {
	calTotalPages("/cise/homes/rui/Desktop/PageRank.outlink.out",
		"/cise/homes/rui/Desktop/output");
    }
}
