package com.ufl.ids15.pagerank;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.mahout.text.wikipedia.XmlInputFormat;

public class PageRank {
    private static String numberOfPages;
    private static String initialized = "false";
    private static String pathName;
    private static String tmpPathName;
    private static String resultPathName;
    private static String outlinkResName;
    private static String nResName;
    private static String iter1ResName;
    private static String iter8ResName;
    private static String job1TmpName;
    private static String job2TmpName;
    private static String job3TmpName;
    private static String job4TmpName;
    private static String job5TmpName;

    public static void initialFileName (String outBucketName) {
	if (!outBucketName.endsWith("/")) {
	    outBucketName += "/";
	}
	pathName = outBucketName;
	tmpPathName = pathName + "tmp/";
	resultPathName = pathName + "results/";
	outlinkResName = resultPathName + "PageRank.outlink.out";
	nResName = resultPathName + "PageRank.n.out";
	iter1ResName = resultPathName + "PageRank.iter1.out";
	iter8ResName = resultPathName + "PageRank.iter8.out";
	job1TmpName = tmpPathName + "job1/";
	job2TmpName = tmpPathName + "job2/";
	job3TmpName = tmpPathName + "job3/";
	job4TmpName = tmpPathName + "job4/";
	job5TmpName = tmpPathName + "job5/";
    }

    // Job 1: extract outlink from XML file and remove red link
    public static void parseXml(String input, String output) throws Exception {
	JobConf conf = new JobConf(PageRank.class);
	conf.setJobName("xmlextract");
	conf.set(XmlInputFormat.START_TAG_KEY, "<page>");
	conf.set(XmlInputFormat.END_TAG_KEY, "</page>");

	conf.setInputFormat(XmlInputFormat.class);
	conf.setOutputFormat(TextOutputFormat.class);

	conf.setMapperClass(XmlExtractMapper.class);
	conf.setOutputKeyClass(Text.class);
	conf.setOutputValueClass(Text.class);
	conf.setReducerClass(XmlExtractReducer.class);

	FileInputFormat.setInputPaths(conf, new Path(input));
	FileOutputFormat.setOutputPath(conf, new Path(output));

	JobClient.runJob(conf);
    }

    // Job 2: Generate adjacency outlink graph
    public static void getAdjacencyGraph(String input, String output) throws Exception {
	JobConf conf = new JobConf(PageRank.class);
	conf.setJobName("outgraphgenerate");

	conf.setInputFormat(TextInputFormat.class);
	conf.setOutputValueClass(Text.class);

	conf.setMapperClass(OutGraphGenerateMapper.class);
	conf.setOutputFormat(TextOutputFormat.class);
	conf.setOutputKeyClass(Text.class);
	conf.setReducerClass(OutGraphGenerateReducer.class);

	FileInputFormat.setInputPaths(conf, new Path(input));
	FileOutputFormat.setOutputPath(conf, new Path(output));

	JobClient.runJob(conf);
    }

    // Job3: Count number of pages
    public static void calTotalPages(String input, String output) throws Exception {
	JobConf conf = new JobConf(PageRank.class);
	conf.setJobName("pagecount");

	conf.setInputFormat(TextInputFormat.class);
	conf.setOutputFormat(TextOutputFormat.class);

	conf.setMapperClass(PageCountMapper.class);
	conf.setOutputKeyClass(Text.class);
	conf.setOutputValueClass(IntWritable.class);
	conf.setReducerClass(PageCountReducer.class);

	FileInputFormat.setInputPaths(conf, new Path(input));
	FileOutputFormat.setOutputPath(conf, new Path(output));

	JobClient.runJob(conf);
    }

    // Job4: Calculate PageRank
    public static void calPageRank(String input, String output) throws Exception {
	JobConf conf = new JobConf(PageRank.class);
	conf.setJobName("pagerank");

	conf.set("NumberOfPages", numberOfPages);
	conf.set("Initialized", initialized);

	conf.setInputFormat(TextInputFormat.class);
	conf.setOutputFormat(TextOutputFormat.class);

	conf.setMapperClass(PageRankMapper.class);
	conf.setOutputKeyClass(Text.class);
	conf.setOutputValueClass(PageRankGenericWritable.class);
	conf.setReducerClass(PageRankReducer.class);

	FileInputFormat.setInputPaths(conf, new Path(input));
	FileOutputFormat.setOutputPath(conf, new Path(output));

	JobClient.runJob(conf);
    }

    // Job 5: Sort by PageRank and write out Pages >= 5/N
    public static void orderRank(String input, String output) throws Exception {
	JobConf conf = new JobConf(PageRank.class);
	conf.setJobName("ranksort");
	conf.set("NumberOfPages", numberOfPages);

	conf.setInputFormat(TextInputFormat.class);
	conf.setOutputFormat(TextOutputFormat.class);

	conf.setMapperClass(RankSortMapper.class);
	conf.setOutputKeyComparatorClass(DescDoubleComparator.class);
	conf.setOutputKeyClass(DoubleWritable.class);
	conf.setOutputValueClass(Text.class);
	conf.setReducerClass(RankSortReducer.class);
	conf.setNumReduceTasks(1);

	FileInputFormat.setInputPaths(conf, new Path(input));
	FileOutputFormat.setOutputPath(conf, new Path(output));

	JobClient.runJob(conf);
    }



    public static void main(String[] args) throws Exception {
	// initialization
	String inputXMLPath = args[0]; //s3://uf-dsr-courses-ids/data/enwiki-latest-pages-articles.xml
	PageRank.initialFileName(args[1]); //s3://rui-zhang-emr/
	Configuration conf = new Configuration() ;
	FileSystem fs = FileSystem.get(new URI(tmpPathName), conf);
	//extract wiki and remove redlinks
	PageRank.parseXml(inputXMLPath, job1TmpName);
	// wiki adjacency graph generation
	PageRank.getAdjacencyGraph(job1TmpName, job2TmpName);
	FileUtil.copyMerge(fs, new Path(job2TmpName), fs, new Path(outlinkResName), false, conf, "");
	// total number of pages
	PageRank.calTotalPages(outlinkResName, job3TmpName);
	FileUtil.copyMerge(fs, new Path(job3TmpName), fs, new Path(nResName), false, conf, "");
	BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(new Path(nResName))));
	String line = br.readLine();
	numberOfPages = line.substring(line.indexOf('=') + 1);
	// iterative MapReduce
	PageRank.calPageRank(outlinkResName, job4TmpName + "iter0");
	initialized = "true";
	for (int i = 0; i < 8; i++) {
	    PageRank.calPageRank(job4TmpName + "iter" + i, job4TmpName + "iter" + (i + 1));
	}
	// Rank page in the descending order of PageRank
	PageRank.orderRank(job4TmpName + "iter1", job5TmpName + "iter1");
	PageRank.orderRank(job4TmpName + "iter8", job5TmpName + "iter8");
	FileUtil.copyMerge(fs, new Path(job5TmpName + "iter1"), fs, new Path(iter1ResName), false, conf, "");
	FileUtil.copyMerge(fs, new Path(job5TmpName + "iter8"), fs, new Path(iter8ResName), false, conf, "");
    }
}


