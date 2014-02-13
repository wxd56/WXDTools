package com.wxd.rapid.tool.android.download;
/**
 * 多线程下载器的状态
 * 
 * @author 王旭东
 */
public class DownLoaderState {
	/**
	 * 暂停状态
	 */
	public static final String PAUSE = "10";
	
	/**
	 * 正在下载
	 */
	public static final String START = "00";
	
	/**
	 * 下载完成
	 */
	public static final String FINISH = "20";
	
	/**
	 * 取消下载
	 */
	public static final String CANCEL = "30";
}