

package org.ltlab.sentiment.weibo.dic;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ltlab.sentiment.weibo.util.PropertyReader;

/**
 * 功能描述: 收集起修饰作用的形容词和名词
 * <p>
 * 
 * @author : qiang.wang
 *         <p>
 * @version 2.2 2012-5-28
 * @since weibo 2.2
 */
public class AdjCollector
{
    // 不含空格的模式
    private static final Pattern NON_BLANK_P = Pattern.compile("[^ ]+");
    // 负面评价词
    private static final Set<String> NEG_COMM_WORDS = getNegCommWords();
    // 负面情感词
    private static Set<String> NEG_SE_WORDS = getNegSeWords();
    // 正面评价词
    private static Set<String> POS_COMM_WORDS = getPosCommWords();
    // 正面情感词
    private static Set<String> POS_SE_WORDS = getPosSeWords();
    
//    public static void main(String[] args) {
//    	System.out.println(isNegCommWord("卑微") ); 
//    	System.out.println(isNegSeWord("悲哀") ); 
//    	System.out.println(isPosCommWord("安静") ); 
//    	System.out.println(isPosSeWord("高兴") ); 
//    }
    
    /**
     * 
     * @param word 词组
     * @return 该词组是否为正面情感词
     */
    public static boolean isPosSeWord(String word) {
    	return POS_SE_WORDS.contains(word);
    }
    
    /**
     * 
     * @param word 词组
     * @return 该词组是否为正面评价词
     */
    public static boolean isPosCommWord(String word) {
    	return POS_COMM_WORDS.contains(word);
    }
    
    /**
     * 
     * @param word 词组
     * @return 该词组是否为负面的评价词
     */
    public static boolean isNegCommWord(String word) {
    	return NEG_COMM_WORDS.contains(word);
    }
    
    /**
     * 
     * @param word 词组
     * @return 该词组是否为负面情感词
     */
    public static boolean isNegSeWord(String word) {
    	return NEG_SE_WORDS.contains(word);
    }
    
    //从hownet词典中获取正面情感词语，共763个
    private static Set<String> getPosSeWords() {
    	 Set<String> posSeWords = new HashSet<String>(1000); 
         PropertyReader props = new PropertyReader();
         String pathname = props.getProp("posSeWords");
         try
         {
             FileInputStream is = new FileInputStream(pathname);
             InputStreamReader in = new InputStreamReader(is, "gbk");
             BufferedReader br = new BufferedReader(in);
             String line;
             while ((line = br.readLine()) != null) {
            	 Matcher matcher = NON_BLANK_P.matcher(line);
            	 if (matcher.find())
            		 posSeWords.add(matcher.group());
             }
             br.close();
             in.close();
             is.close();
         }
         catch (FileNotFoundException e)
         {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return posSeWords;
    }
    
    //从hownet字典中获取正面评价词，共3636个
    private static Set<String> getPosCommWords() {
    	 Set<String> posCommWords = new HashSet<String>(4000); 
         PropertyReader props = new PropertyReader();
         String pathname = props.getProp("posCommWords");
         try
         {
             FileInputStream is = new FileInputStream(pathname);
             InputStreamReader in = new InputStreamReader(is, "gbk");
             BufferedReader br = new BufferedReader(in);
             String line;
             while ((line = br.readLine()) != null) {
             	Matcher m = NON_BLANK_P.matcher(line);
             	if (m.find())
             		posCommWords.add(m.group());
             }
             br.close();
             in.close();
             is.close();
         }
         catch (FileNotFoundException e)
         {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return posCommWords;
    }
    
    
    //从hownet字典中获取负面情感词，共1259个
    private static Set<String> getNegSeWords() {
    	 Set<String> negSeWords = new HashSet<String>(1500); 
         PropertyReader props = new PropertyReader();
         String pathname = props.getProp("negSeWords");
         try
         {
             FileInputStream is = new FileInputStream(pathname);
             InputStreamReader in = new InputStreamReader(is, "gbk");
             BufferedReader br = new BufferedReader(in);
             String line;
             while ((line = br.readLine()) != null) {
            	 Matcher matcher = NON_BLANK_P.matcher(line);
            	 if (matcher.find())
            		 negSeWords.add(matcher.group());
             }
             br.close();
             in.close();
             is.close();
         }
         catch (FileNotFoundException e)
         {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return negSeWords;
    }
    
    //从hownet字典中获取负面评价词,负面评价词4000左右
    private static Set<String> getNegCommWords() {
        Set<String> negCommWords = new HashSet<String>(4000); 
        PropertyReader props = new PropertyReader();
        String pathname = props.getProp("negCommWords");
        try
        {
            FileInputStream is = new FileInputStream(pathname);
            InputStreamReader in = new InputStreamReader(is, "gbk");
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
            	Matcher m = NON_BLANK_P.matcher(line);
            	if (m.find())
            		negCommWords.add(m.group());
            }
            br.close();
            in.close();
            is.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return negCommWords;
    }
    
    

}
