package com.wxd.rapid.tool.android.download;
/**
 * �������ؽ��ȵļ�����
 * 
 * @author ����
 *
 */
public interface DownloadSizeChangedLinstener {
	
	/**
	 * �ļ����س��ȷ����仯
	 * 
	 * @param downloadedSize	��ǰ�Ѿ����صĳ���
	 */
	public void onDownloadSizeChanged(int downloadedSize,int totalSize);
}
