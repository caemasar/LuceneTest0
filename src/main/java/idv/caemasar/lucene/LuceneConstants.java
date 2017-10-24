package idv.caemasar.lucene;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * 创建时间: 2017年10月23日 上午11:18:27
 * 
 * 描述: LuceneConstants.java
 *
 * 这个类是用来提供跨示例应用程序中使用的各种常量。
 * 
 * @author Caemasar
 * @version 1.0
 */
public class LuceneConstants {
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(LuceneConstants.class);
	public static final String CONTENTS_READER = "contents_reader";
	public static final String TXT = "txt";
	public static final String CONTENTS = "contents";
	public static final String FILE_NAME = "filename";
	public static final String FILE_PATH = "filepath";
	public static final int MAX_SEARCH = 10;
}
