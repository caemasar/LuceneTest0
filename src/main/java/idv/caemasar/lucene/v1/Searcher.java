package idv.caemasar.lucene.v1;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * 
 * 创建时间: 2017年10月23日 下午12:00:14
 * 
 * 描述: Searcher.java
 *
 * 这个类是用来搜索索引所创建的索引搜索请求的内容。
 * 
 * @author Caemasar
 * @version 1.0
 */
public class Searcher {
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(Searcher.class);
	private Directory indexDirectory;
	private DirectoryReader ireader;
	private IndexSearcher indexSearcher;
	private QueryParser queryParser;
	private Query query;

	public Searcher(String indexDirectoryPath) throws IOException {
		indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
		ireader = DirectoryReader.open(indexDirectory);
		indexSearcher = new IndexSearcher(ireader);
		queryParser = new QueryParser(Version.LUCENE_40, LuceneConstants.CONTENTS, analyzer);
	}

	public TopDocs search(String searchQuery) throws IOException, ParseException {
		query = queryParser.parse(searchQuery);
		return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
	}

	public TopDocs searchTxt(String searchQuery) throws IOException, ParseException {
		// query = queryParser.parse(searchQuery);
		Term t = new Term(LuceneConstants.TXT, searchQuery);
		Term t2 = new Term("num", "0");
		String[] queries = { "txt", "num" };
		String[] fields = { searchQuery, "0" };
		query = MultiFieldQueryParser.parse(Version.LUCENE_40, fields, queries,
				new StandardAnalyzer(Version.LUCENE_40));
		System.out.println(query);
		// HashSet<Term> set = new HashSet<>();
		// query = new TermQuery(t);
		return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
	}

	public TopDocs searchNum(String searchQuery) throws IOException, ParseException {
		// query = queryParser.parse(searchQuery);
		Term t = new Term("num", searchQuery);
		// HashSet<Term> set = new HashSet<>();
		query = new TermQuery(t);
		return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
	}

	public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException {
		return indexSearcher.doc(scoreDoc.doc);
	}

	public void close() throws IOException {
		ireader.close();
		indexDirectory.close();
	}
}
