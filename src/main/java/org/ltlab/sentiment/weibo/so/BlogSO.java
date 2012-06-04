package org.ltlab.sentiment.weibo.so;

import org.ltlab.sentiment.weibo.util.BlogDocument;
import org.ltlab.sentiment.weibo.util.BlogPolarity;
import org.ltlab.sentiment.weibo.util.Weibo;

/**
 * 
 * @author qiang.wang 2012-5-31
 */
public class BlogSO {
	private String testDoc;
	private String trainDoc;
	
	public BlogSO(String trainDoc, String testDoc) {
		this.testDoc = testDoc;
		this.trainDoc = trainDoc;
	}
	
	private BlogPolarity getTopicSO() {
		BlogDocument doc = BlogDocument.getInstance(testDoc);
		for (Weibo blog : doc.weibos) {
			
		}
		return null;
	}

}
