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
 * �๦��: �Ӳ�ͬ������Դ���ռ�����
 *
 * @author Qiang Wang
 * email: wangqiang.1988@yahoo.com.cn
 *
 * 2012-5-25 ����9:48:21
 *
 */
public class NounExpCollector {
	private static final List<String> POS_ADJS = posAdjs();
	private static final List<String> NEG_ADJS = negAdjs();
	//������Ƚ���ʱ����Լ��������
	private static Pattern resultPattern = Pattern.compile("�ҵ���ؽ��Լ?[0-9,]+��");
	private static Pattern numPattern = Pattern.compile("[0-9,]+");
	//�̳߳ص��߳���Ŀ
	private static final int NTHREDS = 6;
	
	
	public static void main(String[] args) {
		Set<String> nouns = getSougouNouns();
		storeNounExp(nouns);
	}
	
	/**
	 * 
	 * @param nouns ��Ҫ�ж������Ե�����
	 * �ж����ʵ������ԣ���������洢�������ļ��ƶ����ļ���
	 */
	private static void storeNounExp(Set<String> nouns) {
		ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
		for (String noun : nouns) {
			Runnable task = new NounExpTask(noun);
			executor.execute(task);
		}
		//�ر��̳߳�
		executor.shutdown();
		while (executor.isTerminated() == false) {}
		System.out.println("all nouns are calculated!");
	}
	
	/**
	 *  
	 * @param noun ��Ҫ�ж�������
	 * @return �����ʵ�������
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
	 * @param query �����ض����ʵĲ�ѯ
	 * @return �ò�ѯ�ڰٶ��Ϸ��صĽ����
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
				//�õ��������������
				if (rm.find()) {
					//�õ������Ŀ
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
	 * @param query ��ѯ�Ĺؼ���
	 * @return http�����URL
	 */
	private static String getBaiduURL(String query) {
		StringBuilder sb = new StringBuilder();
		//sb.append("http://www.baidu.com/s?wd=");
		sb.append("http://www.baidu.com/s?q1=&q2=");
		try {
		    //�ٶȸ߼���ѯ����GBK����
			sb.append(URLEncoder.encode(query, "GBK"));
			sb.append("&q3=&q4=&rn=10&lm=0&ct=0&ft=&q5=&q6=&tn=baiduadv");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @param str ���ж��ŵ������ַ���
	 * @return ���ַ������������
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
	 * @param noun Ҫ��ѯ������
	 * @param adjs ��������ݴ�
	 * @return �����趨��ģʽ���ɵĲ�ѯ
	 */
	private static List<String> getQueries(String noun, 
			List<String> adjs) {
		List<String> queries = new LinkedList<String>();
		for (String adj : adjs) {
			queries.add(noun + "�е�" + adj);
			queries.add(noun + "�е��" + adj);
			queries.add(noun + adj + "��ô��");
			queries.add("��" + noun + adj);
		}
		return queries;
	}
	
	//���ѹ��ֵ��л�ȡ����
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
				//����Ĵ�����Ϣ��������
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
	
	//positive-like���ݴ��б�
	private static List<String> posAdjs() {
		List<String> posAdjs = new LinkedList<String>();
		posAdjs.add("��");
		posAdjs.add("��");
		posAdjs.add("��");
		posAdjs.add("��");
		posAdjs.add("��");
		posAdjs.add("��");
		return posAdjs;
	}

	//negative-like���ݴ��б�
	private static List<String> negAdjs() {
		List<String> negAdjs = new LinkedList<String>();
		negAdjs.add("С");
		negAdjs.add("��");
		negAdjs.add("��");
		negAdjs.add("��");
		negAdjs.add("ǳ");
		negAdjs.add("��");
		return negAdjs;
	}
}
