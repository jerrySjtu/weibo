/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ltlab.sentiment.weibo.util;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author ucai
 */
public class Sentence {
    public int id = -1;
    public boolean opinionated = false;
    public String text = null;
    public int begin = -1;
    public int end = -1;//currently not in use
    public ArrayList<Target> targets = null;//starts from element 1, i.e. targets.get(0) is null
    public String polarity = null;

    public Sentence(int id, boolean opinionated, String text, int start, int end, ArrayList<Target> targets){
        this.id = id; this.opinionated = opinionated; this.text = text; this.begin = start; this.end = end; this.targets = targets;
    }
    public Sentence(){
    }

    public static Sentence getInstance(Node node, int offset){
        Sentence sentence = new Sentence();
        sentence.begin = offset;
        try{
            ArrayList<Target> targets = new ArrayList<Target>();
            sentence.text = node.getTextContent();
            sentence.targets = targets;

            NamedNodeMap map = node.getAttributes();
            int len = map.getLength();
            for(int i = 0; i < len; i++){
                Node attr = map.item(i);
                String attrName = attr.getNodeName();
                if(attrName.equals("id")){
                    sentence.id = Integer.parseInt(attr.getTextContent());
                }else if(attrName.equals("opinionated")){
                    sentence.opinionated = attr.getTextContent().equals("Y") ? true : false;
                }else if(attrName.equals("polarity")){
                    sentence.polarity = attr.getTextContent();
                }else if(attrName.startsWith("target")){//deal with targets
                    String[] parse = attrName.split("_");//possible type target_word_x, target_end_x, target_begin_x, target_polarity_x
                    int tID = Integer.parseInt(parse[2]);
                    int index = getTarget(targets, tID);
                    if(index < 0){
                        targets.add(new Target(tID));
                        index = targets.size() - 1;
                    }
                    Target t = targets.get(index);
                    if(parse[1].equals("polarity")){// assuming target_polarity_x is the begin of target x
                        t.polarity = attr.getTextContent();
                    }else if(parse[1].equals("end")){
                        t.end = Integer.parseInt(attr.getTextContent());
                    }else if(parse[1].equals("begin")){
                        t.begin = Integer.parseInt(attr.getTextContent());
                    }else if(parse[1].equals("word")){
                        t.text = attr.getTextContent();
//                        int beg = sentence.text.indexOf(t.text) + sentence.begin;
//                        int end = beg + t.text.length() - 1;
//                        System.out.println(t.text + " : " + beg + "_" + t.begin + "  " + end + "_" + t.end);
                    }else{
                        throw new Exception("wrong sentence attribute type attrName = " + attrName);
                    }
                }else{
                    throw new Exception("no such kind of attribute attrName = " + attrName);
                }
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return sentence;
    }

    public Element writeToElement(Document d){
        Element e = d.createElement("sentence");
        try{
            e.setAttribute("id", new Integer(this.id).toString());
            String op = this.opinionated ? "Y" : "N";
            e.setAttribute("opinionated", op);
            if(this.polarity != null)
                e.setAttribute("polarity", this.polarity);
            e.setTextContent(this.text);
            
            for(Target t : this.targets){
                Integer tid = t.id;
                e.setAttribute("target_polarity_" + tid, t.polarity);
                e.setAttribute("target_end_" + tid, new Integer(t.end).toString());
                e.setAttribute("target_begin_" + tid, new Integer(t.begin).toString());
                e.setAttribute("target_word_" + tid, t.text);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return e;
    }

    public static int getTarget(ArrayList<Target> targets, int id){
        int size = targets.size();
        for(int i = 0; i < size; i++){
            if(targets.get(i).id == id)
                return i;
        }
        return -1;//doesn't contain this id
    }
    @Override
    public String toString(){
        return new Integer(this.id).toString();
    }
    
}
