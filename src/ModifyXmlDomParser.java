import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ModifyXmlDomParser {
	private static final String FILENAME = "C:\\Users\\Sasmita Laptop\\eclipse-workspac4\\XhtmlProject\\filename.txt";
	static LinkedList<String> list = new LinkedList<>();

	public static void main(String[] args) throws IOException {
		mappingFile();
		replaceValue();
		replaceNgModel();
		mappingFileOutput();
	}

	public static void replaceValue() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try (InputStream is = new FileInputStream(FILENAME)) {

			DocumentBuilder db = dbf.newDocumentBuilder();
			
			ArrayList<String> mapping = new ArrayList<String>();
			mapping.add("p-inputMask");
			// mapping.put("value", "[(ngModel)]");
			mapping.add("input");
			mapping.add("p-multiSelect");
			mapping.add("p-radioButton");
			mapping.add("label");
			Document doc = db.parse(is);
			for (String str : mapping) {
				// NodeList listOfStaff = doc.getElementsByTagName("p-inputMask");
				NodeList listOfStaff = doc.getElementsByTagName(str);
				if (str != "p-button") {
					for (int i = 0; i < listOfStaff.getLength(); i++) { // get first staff Node
						Node staff = listOfStaff.item(i);
						if (staff.getNodeType() == Node.ELEMENT_NODE) {
							String id = staff.getAttributes().getNamedItem("value").getTextContent();
							System.out.println(id + "id");
							list.add(id);
							if (id.indexOf(".") > 0) {
								String y = id.substring(id.indexOf(".") + 1, id.indexOf("}"));
								System.out.println(y + "Y");
								staff.getAttributes().getNamedItem("value").setTextContent(y);
								String value = staff.getAttributes().getNamedItem("value").getTextContent();
								System.out.println(value);
							}

						}
					}
				}
				if (str.equals("p-multiSelect") || str.equals("p-radioButton")) {
					for (int i = 0; i < listOfStaff.getLength(); i++) {
						// get first staff
						Node staff = listOfStaff.item(i);
						if (staff.getNodeType() == Node.ELEMENT_NODE) {
							String id = staff.getAttributes().getNamedItem("id").getTextContent();
							System.out.println(id + "id");
							// if ("option".equals(id.trim()) || "line".equals(id.trim())) {
							NodeList childNodes = staff.getChildNodes();
							for (int j = 0; j < childNodes.getLength(); j++) {
								Node item = childNodes.item(j);
								if (item.getNodeType() == Node.ELEMENT_NODE) {
									System.out.println(item.getNodeName() + "node name");
									if ("f:selectItem".equalsIgnoreCase(item.getNodeName())) {
										// remove xml element `name`
										staff.removeChild(item);
									}
								}
							}
							// }
						}
					}
				}

				/*
				 * if (str.equals("label")) { for (int i = 0; i < listOfStaff.getLength(); i++)
				 * { // get first staff Node staff = listOfStaff.item(i); if
				 * (staff.getNodeType() == Node.ELEMENT_NODE) { if
				 * (staff.getAttributes().getNamedItem("for") != null) { String id =
				 * staff.getAttributes().getNamedItem("for").getTextContent();
				 * System.out.println(id + "for"); } } } }
				 */
			}

			// Save the modified document
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("output.html"));
			transformer.transform(source, result);
			System.out.println("HTML file modified successfully.");

		} catch (

		FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void mappingFile() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(
				"C:\\Users\\Sasmita Laptop\\eclipse-workspac4\\XhtmlProject\\src\\resources\\index.xhtml"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			LinkedHashMap<String, String> mapping = new LinkedHashMap<String, String>();
			mapping.put("p:fieldset", "p-fieldset");
			mapping.put("p:inputMask", "p-inputMask");
			// mapping.put("value", "[(ngModel)]");
			mapping.put("p:inputText", "input type=\"text\"");
			mapping.put("p:selectOneMenu", "p-multiSelect");
			mapping.put("p:selectOneRadio", "p-radioButton");
			mapping.put("p:commandButton", "p-button");
			mapping.put("p:panelGrid", "p-panel");
			mapping.put("h:outputText", "label");
			mapping.put("p:dialog", "<p-button label=\"Show\"><p-dialog");

			while (line != null) {

				for (Entry<String, String> entry : mapping.entrySet()) {

					if (line.contains(entry.getKey())) {
						line = line.replace(entry.getKey(), entry.getValue());
					}

				}
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			System.out.println(everything);
			try (Writer writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("filename.txt"), "utf-8"))) {
				writer.write(everything);
			}
		} finally {
			br.close();
		}
	}

	public static void replaceNgModel() throws IOException {
		BufferedReader br = new BufferedReader(
				new FileReader("C:\\Users\\Sasmita Laptop\\eclipse-workspac4\\XhtmlProject\\output.html"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			LinkedHashMap<String, String> mapping = new LinkedHashMap<String, String>();
			mapping.put("value", "[(ngModel)]");

			while (line != null) {

				for (Entry<String, String> entry : mapping.entrySet()) {

					if (line.contains(entry.getKey()) && !line.contains("p-button") && !line.contains("label")) {
						line = line.replace(entry.getKey(), entry.getValue());
					} else if (line.contains("value") && line.contains("p-button")) {
						line = line.replace("value", "label");
					} else if (line.contains("label")) {
						line = line.replace("value", "for");
					}

				}
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			System.out.println(everything);
			try (Writer writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("filename1.txt"), "utf-8"))) {
				writer.write(everything);
			}
		} finally {
			br.close();
		}
	}

	public static void mappingFileOutput() throws IOException {
		BufferedReader br = new BufferedReader(
				new FileReader("C:\\Users\\Sasmita Laptop\\eclipse-workspac4\\XhtmlProject\\filename1.txt"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				if (line.contains("action")) {
					System.out.println(line + "line");
					// System.out.println(
					// line.substring(line.indexOf("action"),line.indexOf("}")+2));
					String s = line.substring(line.indexOf("action"), line.indexOf("}") + 2);
					System.out.println(s + "s");
					line = line.replace(s, "");
				} else if (line.contains("label")) {
					String s = line.substring(line.indexOf("/>"));
					line = line.replace(s, ">$</label>");
				}
				
				if(line.contains("$")) {
					String forVal = line.substring((line.indexOf("\"")+1),line.indexOf("\"",(line.indexOf("\""))+1));
					line = line.replace("$", forVal);
				}
				
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			System.out.println(everything + "everything");
			
			try (Writer writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("filename.txt"), "utf-8"))) {
				writer.write(everything);
			}
		} finally {
			br.close();
		}
	}

}
