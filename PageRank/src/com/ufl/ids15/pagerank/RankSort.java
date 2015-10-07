package com.ufl.ids15.pagerank;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

public class RankSort {   

    public static class DecreasingComparator extends WritableComparator {
	protected DecreasingComparator() {
	    super(Text.class, true);
	}
	@SuppressWarnings("rawtypes")
	public int compare(WritableComparable a, WritableComparable b) {
	    return -super.compare(a, b);
	}
	public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
	    return -super.compare(b1, s1, l1, b2, s2, l2);
	}
    }

    public static void orderRank(String input, String output) throws Exception
    {
	JobConf conf = new JobConf(RankSort.class);
	conf.setJobName("ranksort"); 
	conf.setInputFormat(TextInputFormat.class);

	conf.setOutputFormat(TextOutputFormat.class);
	conf.setOutputKeyComparatorClass(DecreasingComparator.class);
	conf.setOutputKeyClass(DoubleWritable.class);
	conf.setOutputValueClass(Text.class);     

	conf.setMapperClass(RankSortMapper.class);
	conf.setReducerClass(RankSortReducer.class);

	FileInputFormat.setInputPaths(conf, new Path(input));
	FileOutputFormat.setOutputPath(conf, new Path(output));    

	JobClient.runJob(conf);
    } 
    public static void main(String[] args) throws Exception {
	orderRank("E:\\JavaProg\\PageRank\\input.txt", "E:\\JavaProg\\PageRank\\output");
    }

}
