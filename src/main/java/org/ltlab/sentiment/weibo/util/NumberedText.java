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
public class NumberedText {
    public int id = -1;
    public String text = null;

    public static NumberedText getInstance(Node node){
        NumberedText nt = new NumberedText();
        try{
            if(node.getAttributes().getNamedItem("id") != null)
            {
                nt.id = Integer.parseInt(node.getAttributes().getNamedItem("id").getTextContent());
                nt.text = node.getTextContent();
            }else{
                throw new Exception("no id for NumberedText");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return nt;
    }

    public Element writeToElement(Document d, String elementName){
        Element e = d.createElement(elementName);
        try{
            e.setAttribute("id", new Integer(this.id).toString());
            e.setTextContent(this.text);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return e;
    }
}
