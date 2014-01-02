package com.wxd.tool.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * �̳߳ع����࣬�ⲿ����ֻ��Ҫ��Ҫִ�е�������ӵ��̳߳ؼ��ɡ�<br>
 * �̳߳ػ��Զ������߳���ִ������
 * 
 * @author ����
 *
 */
public class ThreadPoolManager {
	private static final ThreadPoolManager manager = new ThreadPoolManager(4);
	private ExecutorService service;
	
	private ThreadPoolManager (int threadCount){
		service = Executors.newFixedThreadPool(threadCount);
	}
	
	/**
	 * ��������ӵ��̳߳���ִ��
	 * @param runnable
	 */
	public void addTask(Runnable runnable){
		this.service.execute(runnable);
	}
	
	public static ThreadPoolManager getInstance(){
		return manager;
	}
}
