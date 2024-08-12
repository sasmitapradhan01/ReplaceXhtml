import java.io.File;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class ReplaceXhtml {
	public static void main(String[] args) throws Exception {
		Document document = readXMLDocumentFromFile("C:\\Users\\Sasmita Laptop\\eclipse-workspac4\\XhtmlProject\\src\\resources\\index.xhtml");
		//Verify XML Content

	    //Here comes the root node
	    Element root = document.getDocumentElement();
	    System.out.println(root.getNodeName());
	    
	    //Get all employees
	    NodeList nList = document.getElementsByTagName("p:inputMask");
	    System.out.println("============================");
		}
	
	public static Document readXMLDocumentFromFile(String fileNameWithPath) throws Exception {

	    //Get Document Builder
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();

	    //Build Document
	    Document document = builder.parse(new File(fileNameWithPath));

	    //Normalize the XML Structure; It's just too important !!
	    document.getDocumentElement().normalize();

	    return document;
	}

}


