package com.wxd.tool.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池管理类，外部程序只需要将要执行的任务添加到线程池即可。<br>
 * 线程池会自动分配线程来执行任务。
 * 
 * @author 王旭东
 *
 */
public class ThreadPoolManager {
	private static final ThreadPoolManager manager = new ThreadPoolManager(4);
	private ExecutorService service;
	
	private ThreadPoolManager (int threadCount){
		service = Executors.newFixedThreadPool(threadCount);
	}
	
	/**
	 * 将任务添加到线程池中执行
	 * @param runnable
	 */
	public void addTask(Runnable runnable){
		this.service.execute(runnable);
	}
	
	public static ThreadPoolManager getInstance(){
		return manager;
	}
}
