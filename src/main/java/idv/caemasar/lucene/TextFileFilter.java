package idv.caemasar.lucene;

import java.io.File;
import java.io.FileFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * 创建时间: 2017年10月23日 上午11:18:43
 * 
 * 描述: TextFileFilter.java
 *
 * 此类用于为 .txt 文件过滤器
 * 
 * @author Caemasar
 * @version 1.0
 */
public class TextFileFilter implements FileFilter {
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(TextFileFilter.class);

	@Override
	public boolean accept(File pathname) {
		return pathname.getName().toLowerCase().endsWith(".txt");
	}
}
