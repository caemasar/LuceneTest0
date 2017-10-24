package idv.caemasar.lucene.v1;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class LuceneTester {

	private static Logger logger = LogManager.getLogger(LuceneTester.class);

	private String indexDir = "Lucene\\Index";
	private String dataDir = "Lucene\\Data";
	private Indexer indexer;
	private Searcher searcher;

	public static void main(String[] args) {
		LuceneTester tester;
		try {
			tester = new LuceneTester();
			tester.createIndex();
			tester.search("fuck_lucene");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void createIndex() throws IOException {
		indexer = new Indexer(indexDir);
		int numIndexed;
		long startTime = System.currentTimeMillis();
		numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
		long endTime = System.currentTimeMillis();
		indexer.close();
		logger.debug(numIndexed + " File indexed, time taken: " + (endTime - startTime) + " ms");
	}

	private void search(String searchQuery) throws IOException, ParseException {
		searcher = new Searcher(indexDir);
		long startTime = System.currentTimeMillis();
		TopDocs hits = searcher.search(searchQuery);
		long endTime = System.currentTimeMillis();

		logger.debug(hits.totalHits + " documents found. Time :" + (endTime - startTime));
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			logger.debug("\n------------------------S------------------------");
//			logger.debug("\ncontent: " + doc.get(LuceneConstants.CONTENT));
			logger.debug("\ncontents: " + doc.get(LuceneConstants.CONTENTS));
			logger.debug("\nFile: " + doc.get(LuceneConstants.FILE_PATH));
			logger.debug("\n------------------------E------------------------");
		}
		searcher.close();
	}
}
