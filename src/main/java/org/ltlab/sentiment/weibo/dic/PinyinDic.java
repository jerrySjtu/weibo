package org.ltlab.sentiment.weibo.dic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ltlab.sentiment.weibo.util.PropertyReader;

/**
 * 
 * 功能描述: 得到每个汉字的读音
 * <p>
 * 
 * 
 * @author : qiang.wang
 *         <p>
 * 
 * @version 2.2 2012-5-28
 * 
 * @since weibo 2.2
 */
public class PinyinDic {
	private static Map<String, Set<Character>> pinyinToCharMap;
	private static Map<Character, Set<String>> charToPinyinMap;
	
	static {
		pinyinToCharMap = getPinyinToCharDic();
		charToPinyinMap = getCharToPinyinDic();
	}
	
	/**
	 * 
	 * @param c 汉字
	 * @return 拼音
	 */
	public static Set<String> getPinyin(char c) {
		return charToPinyinMap.get(c);
	}
	
	/**
	 * 
	 * @param pinyin 拼音
	 * @return 汉字
	 */
	public static Set<Character> getChar(String pinyin) {
		return pinyinToCharMap.get(pinyin);
	}

	// 建立从拼音到汉字的字典
	private static Map<String, Set<Character>> getPinyinToCharDic() {
		Map<String, Set<Character>> pinyinMap = new HashMap<String, Set<Character>>();
		PropertyReader props = new PropertyReader();
		String pathname = props.getProp("pinyinDic");
		try {
			FileReader fr = new FileReader(pathname);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains(":")) {
					String[] strs = line.split(" : ");
					// 拼音
					String pinyin = strs[0].substring(0, strs[0].length() - 1);
					// 汉字
					for (char c : strs[1].toCharArray()) {
						if (c != ' ') {
							if (pinyinMap.containsKey(pinyin)) {
								Set<Character> set = pinyinMap.get(pinyin);
								set.add(c);
							} else {
								Set<Character> set = new HashSet<Character>();
								set.add(c);
								pinyinMap.put(pinyin, set);
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pinyinMap;
	}

	// 建立从汉字到拼音的字典
	private static Map<Character, Set<String>> getCharToPinyinDic() {
		Map<Character, Set<String>> charMap = new HashMap<Character, Set<String>>();
		PropertyReader props = new PropertyReader();
		String pathname = props.getProp("pinyinDic");
		try {
			FileReader fr = new FileReader(pathname);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains(":")) {
					String[] strs = line.split(" : ");
					// 拼音
					String pinyin = strs[0].substring(0, strs[0].length() - 1);
					// 汉字
					for (char c : strs[1].toCharArray()) {
						if (c != ' ') {
							if (charMap.containsKey(c)) {
								Set<String> set = charMap.get(c);
								set.add(pinyin);
							} else {
								Set<String> set = new HashSet<String>();
								set.add(pinyin);
								charMap.put(c, set);
								if (c == '高')
									System.out.println(c + " : " + pinyin);
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return charMap;
	}

}
