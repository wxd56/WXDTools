package com.wxd.rapid.tool.android.download;
/**
 * 监听下载进度的监听器
 * 
 * @author 王旭东
 *
 */
public interface DownloadSizeChangedLinstener {
	
	/**
	 * 文件下载长度发生变化
	 * 
	 * @param downloadedSize	当前已经下载的长度
	 */
	public void onDownloadSizeChanged(int downloadedSize,int totalSize);
}
