package com.ufl.ids15.pagerank;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

public class RankSort {

    public static void orderRank(String input, String output) throws Exception {
	JobConf conf = new JobConf(RankSort.class);
	conf.setJobName("ranksort");
	conf.setInputFormat(TextInputFormat.class);

	conf.setOutputFormat(TextOutputFormat.class);
	conf.setOutputKeyComparatorClass(DescDoubleComparator.class);
	conf.setOutputKeyClass(DoubleWritable.class);
	conf.setOutputValueClass(Text.class);

	conf.setMapperClass(RankSortMapper.class);
	conf.setReducerClass(RankSortReducer.class);

	FileInputFormat.setInputPaths(conf, new Path(input));
	FileOutputFormat.setOutputPath(conf, new Path(output));

	JobClient.runJob(conf);
    }

    public static void main(String[] args) throws Exception {
	orderRank("/cise/homes/rui/Desktop/PageRank.out",
		"/cise/homes/rui/Desktop/output");
    }

}
