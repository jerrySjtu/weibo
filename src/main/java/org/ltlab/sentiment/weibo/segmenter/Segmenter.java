package org.ltlab.sentiment.weibo.segmenter;

import java.util.Properties;

import org.ltlab.sentiment.weibo.util.PropertyReader;

import edu.stanford.nlp.ie.crf.CRFClassifier;

public class Segmenter {
	private static CRFClassifier classifier;
	
	public static void main(String[] args) {
		String[] strs = segment("中国人是好样的");
		for (String str : strs) {
			System.out.println(str);
		}
	}

	private static CRFClassifier getCRFClassifier() {
		if (classifier == null) {
			PropertyReader config = new PropertyReader();
			String data = config.getProp("segTrainData");
			Properties props = new Properties();
			props.setProperty("sighanCorporaDict", data);
			props.setProperty("serDictionary", data + "\\dict-chris6.ser.gz");
			// props.setProperty("testFile", file);
			props.setProperty("inputEncoding", "UTF-8");
			props.setProperty("sighanPostProcessing", "true");
			classifier = new CRFClassifier(props);
			classifier.loadClassifierNoExceptions(data + "\\ctb.gz", props);
			// flags must be re-set after data is loaded
			classifier.flags.setProperties(props);
			// classifier.classifyAndWriteAnswers(file);
		}
		return classifier;
	}

	public static String[] segment(String sentence) {
		CRFClassifier classifier = getCRFClassifier();
		return (String[]) classifier.segmentString(sentence).toArray();
	}

}
