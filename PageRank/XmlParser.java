package xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlParser {

    public static void main(String[] args) throws XMLStreamException, FileNotFoundException {

	class Page{
	    Double pageRank;   // use later
	    String title;
	    List<String> links;
	}

	List<Page> pageList = new ArrayList<>();
	Page curPage = null;
	String tagContent = null;
	XMLInputFactory factory = XMLInputFactory.newInstance();
	XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream("/cise/homes/rui/Desktop/input.xml"));

	String re = "\\[\\[(.*?)(\\|(.*?))*\\]\\]";
	Pattern pattern = Pattern.compile(re);

	while(reader.hasNext()){
	    int event = reader.next();

	    switch(event){
	    case XMLStreamConstants.START_ELEMENT:
		if("page".equals(reader.getLocalName())){
		    curPage = new Page();
		}
		break;

	    case XMLStreamConstants.CHARACTERS:
		tagContent = reader.getText().trim();
		break;

	    case XMLStreamConstants.END_ELEMENT:
		switch(reader.getLocalName()){
		case "page":
		    pageList.add(curPage);
		    break;
		case "title":
		    curPage.title = tagContent.replace(' ', '_');
		    break;
		case "text":
		    Matcher m = pattern.matcher(tagContent);
		    curPage.links = new ArrayList<String>();
		    while(m.find()) {
			curPage.links.add(m.group(1).replace(' ', '_'));
		    }
		    break;
		}
		break;
	    }
	}

	//Print the employee list populated from XML
	for (Page p : pageList){
	    System.out.println(p.title);
	    for (String s : p.links) {
		System.out.println(s);
	    }
	}

    }
}


