package org.ltlab.sentiment.weibo.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



/**
 * 
 * 类功能: 读取配置文件中的配置
 *
 * @author Qiang Wang
 * email: wangqiang.1988@yahoo.com.cn
 *
 * 2012-5-25 下午9:53:49
 *
 */
public class PropertyReader {
	private  final Properties prop;
	
	public PropertyReader() {
		prop = new Properties();
		String pathname = "weibo.properties";
		InputStream in = getDefaultClassLoader().getResourceAsStream(pathname);
		//InputStream in = this.getClass().getResourceAsStream("/" + pathname);
		try {
			prop.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public  String getProp(String key) {
		return prop.getProperty(key);
	}
	
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = PropertyReader.class.getClassLoader();
		}
		return cl;
	}
}
