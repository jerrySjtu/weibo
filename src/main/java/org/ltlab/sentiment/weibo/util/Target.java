/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ltlab.sentiment.weibo.util;

/**
 *
 * @author ucai
 */
public class Target {
    public int id = -1;
    public int begin = -1;
    public int end = -1;
    public String text = null;
    public String polarity = null;

    public Target(int id, int start, int end, String text, String polarity){
        this.id = id;
        this.begin = start;
        this.end = end;
        this.text = text;
        this.polarity = polarity;
    }
    public Target(int id){this.id = id;}
    public Target(){}

    @Override
    public String toString(){
        return new Integer(id).toString();
    }
    
}
