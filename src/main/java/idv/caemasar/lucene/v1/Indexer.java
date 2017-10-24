package idv.caemasar.lucene.v1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * 
 * 创建时间: 2017年10月23日 上午11:50:36
 * 
 * 描述: Indexer.java
 *
 * 用于索引的原始数据，这样我们就可以使用Lucene库，使其可搜索。
 * 
 * @author Caemasar
 * @version 1.0
 */
public class Indexer {
	private static Logger logger = LogManager.getLogger(Indexer.class);

	private IndexWriter writer;

	public Indexer(String indexDirectoryPath) throws IOException {

		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

		// this directory will contain the indexes
		Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
		// create the indexer
		writer = new IndexWriter(indexDirectory, config);
	}

	public void close() throws CorruptIndexException, IOException {
		writer.close();
	}

	private Document getDocument(File file) throws IOException {
		Document document = new Document();

		FileReader fr = new FileReader(file);
		// 以下这种方式存进去的field是整的，查找不到
		// StringBuffer sb = new StringBuffer();
		// char[] buf = new char[1024];
		// int len = 0;
		//
		// while ((len = fr.read(buf)) != -1) {
		// sb.append(new String(buf, 0, len));
		// }
		// logger.debug("\n" + sb.toString());
		// 以上这种方式存进去的field是整的，查找不到

		// index file contents
		TextField contentReaderField = new TextField(LuceneConstants.CONTENTS, fr);

		// 以下这种方式把内容放进去也是查不到的
		BufferedReader br = new BufferedReader(fr);
		String s = null;
		String sresult = "";

		while ((s = br.readLine()) != null) {
			sresult = sresult + "\n" + s;
		}
		logger.debug(sresult);
		TextField contentField = new TextField(LuceneConstants.TXT, sresult, Field.Store.YES);
		// 以上这种方式把内容放进去也是查不到的

		// index file name
		TextField fileNameField = new TextField(LuceneConstants.FILE_NAME, file.getName(), Field.Store.YES);

		// index file path
		TextField filePathField = new TextField(LuceneConstants.FILE_PATH, file.getCanonicalPath(), Field.Store.YES);
		document.add(contentReaderField);
		document.add(fileNameField);
		document.add(filePathField);
		document.add(contentField);
		logger.debug(document.get(LuceneConstants.TXT));
		// fr.close();

		return document;
	}

	private Document getDocument(File file, int i) throws IOException {
		Document document = getDocument(file);
		TextField numField = new TextField("num", String.valueOf(i), Field.Store.YES);
		document.add(numField);
		return document;
	}

	@SuppressWarnings("unused")
	private void indexFile(File file) throws IOException {
		logger.debug("Indexing " + file.getCanonicalPath());
		Document document = getDocument(file);
		writer.addDocument(document);
	}

	private void indexFile(File file, int i) throws IOException {
		logger.debug("Indexing " + file.getCanonicalPath());
		Document document = getDocument(file, i);
		writer.addDocument(document);
	}

	public int createIndex(String dataDirPath, FileFilter filter) throws IOException {
		// get all files in the data directory
		File[] files = new File(dataDirPath).listFiles();
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isDirectory() && !files[i].isHidden() && files[i].exists() && files[i].canRead()
					&& filter.accept(files[i])) {
				indexFile(files[i], (i % 2));
			}
		}
		return writer.numDocs();
	}

}
