package InformationRetrieval2.IR2;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import java.io.FileWriter;
import java.io.PrintWriter;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.UnicodeWhitespaceAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;

public class Search {

	public static void SearchFiles(ArrayList<String> queryList, String indexPath, String analyzerType,
			String similarityType) {
		try {
			System.out.println("1");
			FileWriter fileWriter = new FileWriter("Results");
			PrintWriter printWriter = new PrintWriter(fileWriter);
			System.out.println("2");
			IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
			IndexSearcher searcher = new IndexSearcher(reader);
			System.out.println("3");
			searcher.setSimilarity(new BM25Similarity());
			System.out.println("4");
			/*similarityType = similarityType.toLowerCase();
			if (similarityType.equals("tfidf")) {
				searcher.setSimilarity(new ClassicSimilarity());
			}*/
			System.out.println("5");
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
				} else if (analyzerType.equals("keyword")) {
					analyzer = new KeywordAnalyzer();
					System.out.println("Using Keyword Analyzer:");
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
			System.out.println("6");
			QueryParser parser = new QueryParser("CONTENT", analyzer);
			Iterator<String> iterator = queryList.iterator();
			System.out.println("7");
			int j = 0;
			while (iterator.hasNext()) {
				String queryStatement = iterator.next().toString();
				Query query = parser.parse(queryStatement);

				TopDocs results = searcher.search(query, 1000);
				ScoreDoc[] hits = results.scoreDocs;
				j++;
				System.out.println("@@@@@@: " + hits.length);
				
				for (int i = 0; i < hits.length; i++) {
					int actualId = hits[i].doc + 1;
					System.out.println("Converting into TrecEval readable Format: " + j + " 0 " + actualId + " " + i
							+ " " + hits[i].score);
					printWriter.print(j + " 0 " + actualId + " " + i + " " + hits[i].score + " 0 \n");
				}

			}
			printWriter.close();
			reader.close();
			System.out.println("Done !!!");
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

}
