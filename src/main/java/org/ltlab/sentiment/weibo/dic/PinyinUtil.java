package org.ltlab.sentiment.weibo.dic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ltlab.sentiment.weibo.util.PropertyReader;

/**
 * 
 * 功能描述: 得到每个汉字的读音  <p>
 * 
 *
 * @author : qiang.wang <p>
 *
 * @version 2.2 2012-5-28
 *
 * @since weibo 2.2
 */
public class PinyinUtil
{
    private static Map<String, List<String>> pinyinToCharMap;
    private static Map<String, List<String>> charToPinyinMap;
    
    public static void main(String[] args) {
        Map<String, List<String>> map = getPinyinDic();
        System.out.println(map.size());
    }
    
    private static Map<String, List<String>> getPinyinToCharDic() {
        Map<String, List<String>> pinyinMap = 
            new HashMap<String, List<String>>();
        PropertyReader props = new PropertyReader();
        String pathname = props.getProp("pinyinDic");
        try
        {
            FileReader fr = new FileReader(pathname);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(":")) {
                    String[] strs = line.split(":");
                    //拼音
                    String pinyin = strs[0].substring(0, strs[0].length()-1);
                    //汉字
                    for (char c : strs[1].toCharArray())
                    {
                        if (pinyinMap.containsKey(c)) {
                            List<String> list = pinyinMap.get(c);
                            list.add(pinyin);
                        }
                        else {
                            List<String> list = new LinkedList<String>();
                            list.add(pinyin);
                            pinyinMap.put(String.valueOf(c), list);
                        }
                    }
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pinyinMap;
    }
    
    //建立从汉字到拼音的hash表
    private static Map<String, List<String>> getcharToPinyinDic() {
        Map<String, List<String>> pinyinMap = 
            new HashMap<String, List<String>>();
        PropertyReader props = new PropertyReader();
        String pathname = props.getProp("pinyinDic");
        try
        {
            FileReader fr = new FileReader(pathname);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(":")) {
                    String[] strs = line.split(":");
                    //拼音
                    String pinyin = strs[0].substring(0, strs[0].length()-1);
                    //汉字
                    for (char c : strs[1].toCharArray())
                    {
                        if (pinyinMap.containsKey(c)) {
                            List<String> list = pinyinMap.get(c);
                            list.add(pinyin);
                        }
                        else {
                            List<String> list = new LinkedList<String>();
                            list.add(pinyin);
                            pinyinMap.put(String.valueOf(c), list);
                        }
                    }
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pinyinMap;
    }

}
