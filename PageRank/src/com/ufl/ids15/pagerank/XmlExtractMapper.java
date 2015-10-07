package com.ufl.ids15.pagerank;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;


public class XmlExtractMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
    static String re = "\\[\\[(.*?)(\\|(.*?))*\\]\\]";
    static Pattern pattern = Pattern.compile(re);
    private Text title = new Text();
    private Text mark = new Text("#");
    private Text link = new Text();
    @Override
    public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
	XMLInputFactory factory = XMLInputFactory.newInstance();
	String tagContent = "";
	try {
	    XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(value.toString()));
	    while(reader.hasNext()){
		int event = reader.next();

		switch(event){		   
		case XMLStreamConstants.CHARACTERS:
		    tagContent = reader.getText().trim();
		    break;

		case XMLStreamConstants.END_ELEMENT:
		    switch(reader.getLocalName()){			
		    case "title":
			title.set(tagContent.replace(' ', '_'));
			output.collect(title, mark); 
			break;
		    case "text":
			Matcher m = pattern.matcher(tagContent);			   
			while(m.find()) {
			    link.set(m.group(1).replace(' ', '_'));
			    output.collect(link, title); 
			}
			break;
		    }
		    break;
		}
	    }
	} catch (XMLStreamException e) {
	    System.out.println("[ERROR] Parse XML failed!");
	}
    }
}

