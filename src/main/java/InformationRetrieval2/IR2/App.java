package InformationRetrieval2.IR2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws SAXException, ParserConfigurationException, IOException {
		String format = "[-func index/query/processData] [-index Location to store/read Index File]"
				+ " [-doc Location of the Cran document to be indexed] [-similarity tfidf]"
				+ " [-analyzer standard/english/simple/stop/uniwhite/white/classic]";

		System.out.println("Please input the following details:" + format);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String commandLine = br.readLine();

		String[] command = commandLine.split("\\s+");

		String function = null;
		String indexPath = null;
		String docsPath = null;
		String analyzer = null;
		String similarity = null;

		for (int i = 0; i < command.length; i++) {
			if ("-func".equals(command[i])) {
				function = command[i + 1];
				i++;
			} else if ("-index".equals(command[i])) {
				indexPath = command[i + 1];
				i++;
			} else if ("-doc".equals(command[i])) {
				docsPath = command[i + 1];
				i++;
			} else if ("-analyzer".equals(command[i])) {
				analyzer = command[i + 1];
				i++;
			} else if ("-similarity".equals(command[i])) {
				similarity = command[i + 1];
				i++;
			}
		}

		if (!(function == null || indexPath == null || docsPath == null)) {
			if (function.equals("index")) {
				Indexer.IndexFiles(docsPath, indexPath, analyzer);
			} else if (function.equals("query")) {
				QueryDoc.SearchFiles(indexPath, docsPath, analyzer, similarity);
			} else if (function.equals("processData")) {
				//DataPreProcessor.ProcessData();
				System.out.println("Feature not available yet !!! Please try again in a few days");
			}
		} else {
			System.out.println("Invalid Arguments: Exiting");
			System.exit(0);
		}
	}

}
