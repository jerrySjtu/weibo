package org.ltlab.sentiment.weibo.dic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ltlab.sentiment.weibo.util.NounExp;
import org.ltlab.sentiment.weibo.util.NounExpType;
import org.ltlab.sentiment.weibo.util.PropertyReader;


/**
 * 
 * 类功能: 从不同的数据源中收集名词
 *
 * @author Qiang Wang
 * email: wangqiang.1988@yahoo.com.cn
 *
 * 2012-5-25 下午9:48:21
 *
 */
public class NounExpCollector {
	private static final List<String> POS_ADJS = posAdjs();
	private static final List<String> NEG_ADJS = negAdjs();
	//当结果比较少时，“约”不存在
	private static Pattern resultPattern = Pattern.compile("找到相关结果约?[0-9,]+个");
	private static Pattern numPattern = Pattern.compile("[0-9,]+");
	//线程池的线程数目
	private static final int NTHREDS = 6;
	
	
	public static void main(String[] args) {
		Set<String> nouns = getSougouNouns();
		storeNounExp(nouns);
	}
	
	/**
	 * 
	 * @param nouns 需要判定倾向性的名词
	 * 判定名词的倾向性，并将结果存储到配置文件制定的文件中
	 */
	private static void storeNounExp(Set<String> nouns) {
		ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
		for (String noun : nouns) {
			Runnable task = new NounExpTask(noun);
			executor.execute(task);
		}
		//关闭线程池
		executor.shutdown();
		while (executor.isTerminated() == false) {}
		System.out.println("all nouns are calculated!");
	}
	
	/**
	 *  
	 * @param noun 需要判定的名词
	 * @return 该名词的倾向性
	 */
	public static NounExp getNounExp(String noun) {
		List<String> posQueries = getQueries(noun, POS_ADJS);
		List<String> negQueries = getQueries(noun, NEG_ADJS);
		long posHit = 0;
		long negHit = 0;
		
		for (String query : posQueries) {
			posHit += queryHit(query);
		}
		for (String query : negQueries) {
			negHit += queryHit(query);
		}
		
		NounExpType expectation = NounExpType.NEU_EXP;
		if (posHit > negHit * 2)
		    expectation = NounExpType.NEG_EXP;
		else if (posHit * 2 < negHit)
		    expectation = NounExpType.POS_EXP;
	    NounExp result = new NounExp(noun, posHit, negHit, expectation);
        System.out.println(result);
        return result;
	}
	
	/**
	 * 
	 * @param query 包含特定名词的查询
	 * @return 该查询在百度上返回的结果数
	 * @throws  
	 */
	private static int queryHit(String query) {
		int num = 0;
		try {
			URL baidu = new URL(getBaiduURL(query));
			URLConnection conn = baidu.openConnection();
			System.out.println(baidu);
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);
			String line;
			while ((line = br.readLine()) != null) {
				Matcher rm = resultPattern.matcher(line);
				//得到包含结果数的行
				if (rm.find()) {
					//得到结果数目
					Matcher nm = numPattern.matcher(rm.group());
					if (nm.find()) 
						num = parseInt(nm.group());
					break;
				}
			}
			//close resources
			br.close();
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return num;
	}
	
	/**
	 *  
	 * @param query 查询的关键词
	 * @return http请求的URL
	 */
	private static String getBaiduURL(String query) {
		StringBuilder sb = new StringBuilder();
		//sb.append("http://www.baidu.com/s?wd=");
		sb.append("http://www.baidu.com/s?q1=&q2=");
		try {
		    //百度高级查询采用GBK编码
			sb.append(URLEncoder.encode(query, "GBK"));
			sb.append("&q3=&q4=&rn=10&lm=0&ct=0&ft=&q5=&q6=&tn=baiduadv");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @param str 带有逗号的数字字符串
	 * @return 该字符串代表的数字
	 */
	private static int parseInt(String numStr) {
		String[] strs = numStr.split(",");
		StringBuilder sb = new StringBuilder();
		for (String str : strs) 
			sb.append(str);
		return Integer.parseInt(sb.toString());
	}
	
	/**
	 * 
	 * @param noun 要查询的名词
	 * @param adjs 搭配的形容词
	 * @return 按照设定的模式生成的查询
	 */
	private static List<String> getQueries(String noun, 
			List<String> adjs) {
		List<String> queries = new LinkedList<String>();
		for (String adj : adjs) {
			queries.add(noun + "有点" + adj);
			queries.add(noun + "有点儿" + adj);
			queries.add(noun + adj + "怎么办");
			queries.add("嫌" + noun + adj);
		}
		return queries;
	}
	
	//从搜狗字典中获取名词
	private static Set<String> getSougouNouns() {
		PropertyReader props = new PropertyReader();
		String dicPath = props.getProp("sougouFreqDic");
		Set<String> nounSet = new HashSet<String>();
		try {
			FileReader fr = new FileReader(dicPath);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				String[] strs = line.split("\t");
				//词语的词性信息是完整的
				if (strs.length == 3) {
					String[] attrs = strs[2].split(",");
					boolean flag = false;
					for (String str : attrs) {
						if (str.equals("N")) {
							flag = true;
							break;
						}
					}
					if (flag == true)
						nounSet.add(strs[0]);
				}
			}//end while
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return nounSet;
	}
	
	//positive-like形容词列表
	private static List<String> posAdjs() {
		List<String> posAdjs = new LinkedList<String>();
		posAdjs.add("大");
		posAdjs.add("多");
		posAdjs.add("高");
		posAdjs.add("深");
		posAdjs.add("重");
		posAdjs.add("厚");
		return posAdjs;
	}

	//negative-like形容词列表
	private static List<String> negAdjs() {
		List<String> negAdjs = new LinkedList<String>();
		negAdjs.add("小");
		negAdjs.add("少");
		negAdjs.add("低");
		negAdjs.add("薄");
		negAdjs.add("浅");
		negAdjs.add("轻");
		return negAdjs;
	}
}
