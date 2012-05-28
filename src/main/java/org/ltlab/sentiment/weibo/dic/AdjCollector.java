

package org.ltlab.sentiment.weibo.dic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
    // 负面评价词
    private static final Set<String> NEG_COMM_WORDS = getNegCommWords();
    // 负面评价词拼音
    private static Set<String> NEG_COMM_PINYINS;
    // 负面情感词
    private static Set<String> NEG_SE_WORDS;
    // 负面情感词拼音
    private static Set<String> NEG_SE_PINYINS;
    // 正面评价词
    private static Set<String> POS_COMM_WORDS;
    // 正面评价词拼音
    private static Set<String> POS_COMM_PINYINS;
    // 正面情感词
    private static Set<String> POS_SE_WORDS;
    // 正面情感词拼音
    private static Set<String> POS_SE_PINYINS;
    
    public static Set<String> getNegCommWords() {
        //负面评价词4000左右
        Set<String> negCommWords = new HashSet<String>(4000); 
        PropertyReader props = new PropertyReader();
        String pathname = props.getProp("negCommWords");
        try
        {
            FileReader fr = new FileReader(pathname);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                negCommWords.add(line);
            }
            br.close();
            fr.close();
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
