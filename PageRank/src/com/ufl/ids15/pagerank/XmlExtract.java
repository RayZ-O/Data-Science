package com.ufl.ids15.pagerank;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.mahout.text.wikipedia.XmlInputFormat;

public class XmlExtract {
    public static void parseXml(String input, String output) throws Exception
    {
	JobConf conf = new JobConf(XmlExtract.class);
	conf.setJobName("xmlextract"); 
        conf.setInputFormat(XmlInputFormat.class);
        conf.set(XmlInputFormat.START_TAG_KEY, "<page>");
        conf.set(XmlInputFormat.END_TAG_KEY, "</page>");
        
        conf.setOutputFormat(TextOutputFormat.class);
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(Text.class);     
        
        conf.setMapperClass(XmlExtractMapper.class);
        conf.setReducerClass(XmlExtractReducer.class);
        
        FileInputFormat.setInputPaths(conf, new Path(input));
        FileOutputFormat.setOutputPath(conf, new Path(output));    
        
        JobClient.runJob(conf);
    } 
    
    public static void main(String[] args) throws Exception {
	parseXml("E:\\JavaProg\\PageRank\\input.txt", "E:\\JavaProg\\PageRank\\output");
    }
}
