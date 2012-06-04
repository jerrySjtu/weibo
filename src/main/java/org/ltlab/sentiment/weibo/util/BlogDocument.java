/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ltlab.sentiment.weibo.util;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ucai
 */
public class BlogDocument {
    public ArrayList<Weibo> weibos;
    
    public static void main(String[] args){
        String path = "E:";
        BlogDocument td = BlogDocument.getInstance(path);
        List<Weibo> weibos = td.weibos;
    }

    public BlogDocument(){
        weibos = new ArrayList<Weibo>();
    }

    public static BlogDocument getInstance(String xmlPath){
        BlogDocument td = new BlogDocument();
        try{
            XMLProcessor p = XMLProcessor.getInstance();
            Document d = p.getXmlDocument(xmlPath);
            Node tdNode = d.getFirstChild();

            if(tdNode != null){
                Node weiboNode = tdNode.getFirstChild();
                while(weiboNode != null){
                    if(weiboNode.getNodeName().equals("weibo"))
                    {
                        Weibo w = Weibo.getInstance(weiboNode);
                        td.weibos.add(w);
                    }
                  weiboNode = weiboNode.getNextSibling();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return td;
    }
/**
 *
 * @param xmlPath
 * @param rootElementName Name of the root element, such as "topic"/"result"
 */
    public void writeToFile(String xmlPath, String rootElementName){
         XMLProcessor p = XMLProcessor.getInstance();
         try{
            Document d = p.createXmlDocument();
            Element tdElement = d.createElement(rootElementName);

            for(Weibo w : this.weibos){
                Element weiboElement = w.writeToElement(d);
                tdElement.appendChild(weiboElement);
            }
            d.appendChild(tdElement);
            p.writeToFile(d, xmlPath);
         }catch(Exception e){
            e.printStackTrace();
         }
    }    
}
