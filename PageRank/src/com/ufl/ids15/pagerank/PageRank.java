package com.ufl.ids15.pagerank;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

public class PageRank {
    public static void calPageRank(String input, String output) throws Exception
    {
	JobConf conf = new JobConf(PageRank.class);
	conf.setJobName("pagerank"); 
	conf.setInputFormat(TextInputFormat.class);
        
        conf.setOutputFormat(TextOutputFormat.class);
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(Text.class);     
        
        conf.setMapperClass(PageRankMapper.class);
        conf.setReducerClass(PageRankReducer.class);
        
        FileInputFormat.setInputPaths(conf, new Path(input));
        FileOutputFormat.setOutputPath(conf, new Path(output));    
        
        JobClient.runJob(conf);
    } 
   
    public static void main(String[] args) throws Exception {
	calPageRank("E:\\JavaProg\\PageRank\\input.txt", "E:\\JavaProg\\PageRank\\output");
    }
    
}
