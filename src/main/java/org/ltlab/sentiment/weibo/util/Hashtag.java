/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ltlab.sentiment.weibo.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author ucai
 */
public class Hashtag {
    public int id = -1;
    public String text = null;

    public static Hashtag getInstance(Node node){
        Hashtag h = new Hashtag();
        
        try{
            if(node.getAttributes().getNamedItem("id") != null)
            {
                h.id = Integer.parseInt(node.getAttributes().getNamedItem("id").getTextContent());
                h.text = node.getTextContent();
            }else{
                throw new Exception("no id for hashtag");
            }                     
        }catch(Exception e){
            e.printStackTrace();
        }
        return h;
    }

    public Element writeToElement(Document d){
        Element e = d.createElement("hashtag");
        try{
            e.setAttribute("id", new Integer(this.id).toString());
            e.setTextContent(this.text);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return e;
    }

}
