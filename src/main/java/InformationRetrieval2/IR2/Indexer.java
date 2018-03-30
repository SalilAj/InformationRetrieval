package InformationRetrieval2.IR2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.UnicodeWhitespaceAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.xml.sax.SAXException;

//import com.groupIR.IR2.parser.xmlParser;

public class Indexer {

	public static void IndexFiles(String docsPath, String indexPath, String analyzerType) {

		final Path docDir = Paths.get(docsPath);

		if (!Files.isReadable(docDir)) {
			System.out.println("Document directory '" + docDir.toAbsolutePath()
					+ "' does not exist or is not readable, please check the path");
			System.exit(1);
		}

		try {

			Directory dir = FSDirectory.open(Paths.get(indexPath));

			Analyzer analyzer = new StandardAnalyzer();

			if (analyzerType != null) {
				analyzerType = analyzerType.toLowerCase();
				if (analyzerType.equals("standard")) {
					analyzer = new StandardAnalyzer();
					System.out.println("Using Standard Analyzer:");
				} else if (analyzerType.equals("english")) {
					analyzer = new EnglishAnalyzer();
					System.out.println("Using English Analyzer:");
				} else if (analyzerType.equals("simple")) {
					analyzer = new SimpleAnalyzer();
					System.out.println("Using Simple Analyzer:");
				} else if (analyzerType.equals("stop")) {
					analyzer = new StopAnalyzer();
					System.out.println("Using Stop Analyzer:");
				} else if (analyzerType.equals("uniwhite")) {
					analyzer = new UnicodeWhitespaceAnalyzer();
					System.out.println("Using Unicode Whitespace Analyzer:");
				} else if (analyzerType.equals("white")) {
					analyzer = new WhitespaceAnalyzer();
					System.out.println("Using Whitespace Analyzer:");
				} else if (analyzerType.equals("classic")) {
					analyzer = new ClassicAnalyzer();
					System.out.println("Using Classic Analyzer:");
				}
			}

			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setSimilarity(new BM25Similarity());

			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);

			// Optional: for better indexing performance, if you
			// are indexing many documents, increase the RAM
			// buffer. But if you do this, increase the max heap
			// size to the JVM (eg add -Xmx512m or -Xmx1g):
			//
			// iwc.setRAMBufferSizeMB(256.0);

			IndexWriter writer = new IndexWriter(dir, iwc);
			indexDocument(writer, docDir);

			// NOTE: if you want to maximize search performance,
			// you can optionally call forceMerge here. This can be
			// a terribly costly operation, so generally it's only
			// worth it when your index is relatively static (ie
			// you're done adding documents to it):
			//
			// writer.forceMerge(1);
			writer.close();

		} catch (Exception e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		}
	}

	static void indexDocument(IndexWriter writer, Path file) {
		try {
			File folder = new File(file.toString() + "/");
			File[] listOfFiles = folder.listFiles();
			int k = 1;
			List<Document> docs = new ArrayList();
			for (File fileName : listOfFiles) {
				Document doc = new Document();
				Scanner s = new Scanner(new File(file.toString() + "/" + fileName.getName()));
				String s1 = s.useDelimiter("\\Z").next();
				doc.add(new StringField("id", fileName.getName(), Field.Store.YES));
				doc.add(new TextField("CONTENT", s1, Field.Store.YES));
				docs.add(doc);
				k++;
			}
			writer.addDocuments(docs);
			System.out.println("indexing done with  " + k);

		} catch (

		Exception e) {
			System.out.println("Error:" + e.getMessage());
		}
	}

}
