package InformationRetrieval2.IR2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class QueryDoc {

	public static void SearchFiles(String indexPath, String docsPath, String analyzer, String similarity) {

		ArrayList<String> queryList = new ArrayList<String>();

		File file = new File(docsPath);
		StringBuilder sb = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}

			br.close();

			String xml = sb.toString();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xml)));
			Element rootElement = doc.getDocumentElement();
			System.out.println("Parent Node: " + rootElement.getNodeName());
			NodeList nodes = doc.getElementsByTagName("top");

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				Element ele = (Element) node;

				if (ele.getElementsByTagName("desc").item(0) != null) {
					int j = i + 1;
					System.out.println(j + ": --" + ele.getElementsByTagName("desc").item(0).getTextContent());
					System.out.println("------------------");
					queryList.add(ele.getElementsByTagName("desc").item(0).getTextContent());
				}
			}

			System.out.println("Total Number of Queries: " + queryList.size());
			Search.SearchFiles(queryList, indexPath, analyzer, similarity);
			
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
		}
	}

}
