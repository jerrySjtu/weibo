package org.ltlab.sentiment.weibo.dic;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.ltlab.sentiment.weibo.util.NounExp;
import org.ltlab.sentiment.weibo.util.PropertyReader;


public class NounExpTask implements Runnable {
	//需要判断的名词
	private String noun;
	//用于互斥访问文件的锁
	private static final Lock LOCK = new ReentrantLock();
	
	public NounExpTask(String noun) {
		this.noun = noun;
	}

	public void run() {
		NounExp result = NounExpCollector.getNounExp(noun);
		PropertyReader props = new PropertyReader();
		String path = props.getProp("nounAdjExp");
		try {
			FileWriter fw = new FileWriter(path, true);
			//加锁
			LOCK.lock();
			fw.write(result + "\n");
			//解锁
			LOCK.unlock();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
