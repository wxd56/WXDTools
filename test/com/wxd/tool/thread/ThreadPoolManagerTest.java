package com.wxd.tool.thread;

public class ThreadPoolManagerTest {
	public static void main(String args[]){
		ThreadPoolManager manager = ThreadPoolManager.getInstance();		
		
		Runnable t1 = new Runnable() {			
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + " start working...");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {				
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + " end work...");
			}
		};
		
		manager.addTask(t1);
		manager.addTask(t1);
		manager.addTask(t1);
		manager.addTask(t1);
		manager.addTask(t1);
		manager.addTask(t1);
		
		
		System.out.println(Thread.currentThread().getName()+" ½áÊø¡£");
	}
}
