package idv.caemasar.lucene.v1;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class LuceneTester {

	private static Logger logger = LogManager.getLogger(LuceneTester.class);

	private static String indexDir = "Lucene\\Index";
	private static String dataDir = "Lucene\\Data";
	private Indexer indexer;
	private Searcher searcher;

	public static void main(String[] args) {
		LuceneTester tester;
		try {
			tester = new LuceneTester();
			File fileIndex = new File(indexDir);
			if (deleteDir(fileIndex)) {
				fileIndex.mkdir();
			} else {
				fileIndex.mkdir();
			}
			tester.createIndex();
			tester.search("fuck_lucene");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件目录下的所有文件
	 * 
	 * @param file
	 *            要删除的文件目录
	 * @return 如果成功，返回true.
	 */
	public static boolean deleteDir(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteDir(files[i]);
			}
		}
		file.delete();
		logger.debug("目录："+file.getName()+"已删除");
		return true;
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
		TopDocs hits = searcher.searchTxt(searchQuery);
		long endTime = System.currentTimeMillis();

		logger.debug(hits.totalHits + " documents found. Time :" + (endTime - startTime));
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			logger.debug("\n------------------------S------------------------");
			logger.debug("\ntxt: " + doc.get(LuceneConstants.TXT));
			logger.debug("\nFile: " + doc.get(LuceneConstants.FILE_PATH));
			logger.debug("\n------------------------E------------------------");
		}
		logger.debug("\n=================================================");
		startTime = System.currentTimeMillis();
		hits = searcher.searchNum("0");
		endTime = System.currentTimeMillis();

		logger.debug(hits.totalHits + " documents found. Time :" + (endTime - startTime));
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			logger.debug("\n------------------------S------------------------");
			logger.debug("\nnum: " + doc.get("num"));
			logger.debug("\nFile: " + doc.get(LuceneConstants.FILE_PATH));
			logger.debug("\n------------------------E------------------------");
		}
		searcher.close();
	}
}
