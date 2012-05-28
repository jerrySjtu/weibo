
package org.ltlab.sentiment.weibo.util;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ucai
 */
public class Weibo {
    public ArrayList<Sentence> sentences;
    public ArrayList<Hashtag> hashtags;
    public ArrayList<NumberedText> forwards;
    public ArrayList<NumberedText> comms;
    public int id = -1;

    public Weibo(){
        this.sentences = new ArrayList<Sentence>();
        this.hashtags = new ArrayList<Hashtag>();
        this.forwards = new ArrayList<NumberedText>();
        this.comms = new ArrayList<NumberedText>();
    }

    public static Weibo getInstance(Node weiboNode){
        Weibo w = new Weibo();
        try{
            if( weiboNode !=  null && weiboNode.getAttributes() != null){
                w.id = Integer.parseInt(weiboNode.getAttributes().getNamedItem("id").getTextContent());
            }else{
                throw new Exception("no id for Weibo");
            }
            NodeList nodes = weiboNode.getChildNodes();
            int number = nodes.getLength();        
            int currentOffset = 0;
            for(int i = 0; i < number; i++){
                Node childNode = nodes.item(i);
                String nodeName = childNode.getNodeName();
                if(nodeName.equals("sentence")){
                    Sentence s = Sentence.getInstance(childNode, currentOffset);
                    w.sentences.add(s);
                    currentOffset += s.text.length();
                }else if(nodeName.equals("hashtag")){
                    Hashtag h = Hashtag.getInstance(childNode);
                    w.hashtags.add(h);
                }else if(nodeName.equals("forward")){
                    NumberedText nt = NumberedText.getInstance(childNode);
                    w.forwards.add(nt);
                }else if(nodeName.equals("comm")){
                    NumberedText nt = NumberedText.getInstance(childNode);
                    w.comms.add(nt);
                }else if(!nodeName.equals("#text")){
                    throw new Exception("error initializing Weibo, no such element type");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }        
        return w;
    }

    public Element writeToElement(Document d){
        Element e = d.createElement("weibo");
        try{
            e.setAttribute("id", new Integer(this.id).toString());
            for(Sentence s : this.sentences){
                Element sentenceElement = s.writeToElement(d);
                e.appendChild(sentenceElement);
            }
            for(Hashtag h : this.hashtags){
                Element hElement = h.writeToElement(d);
                e.appendChild(hElement);
            }
            for(NumberedText f : this.forwards){
                Element fElement = f.writeToElement(d, "forward");
                e.appendChild(fElement);
            }
            for(NumberedText c : this.comms){
                Element cElement = c.writeToElement(d, "comm");
                e.appendChild(cElement);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return e;
    }
}
