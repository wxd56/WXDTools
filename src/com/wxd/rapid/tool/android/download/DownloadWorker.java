package com.wxd.rapid.tool.android.download;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * ���������Worker,���Խ��жϵ�����
 * @author admin
 *
 */
public class DownloadWorker implements Runnable{
	
	private int startPosition;
	private int endPostion;
	private String  downloadURL;
	private String threadName;
	private int  actualDownloadDataSize = 0;
	private DownLoader downLoader;
	
	/**
	 * �ļ�����·��
	 */
	private String savePath;
	public DownloadWorker(DownLoader downloader,String threadName,String downloadURL,int startPosiont,int endPosition,String savePath){
		this.downloadURL = downloadURL;
		this.startPosition = startPosiont;
		this.endPostion = endPosition;
		this.savePath = savePath;
		this.threadName = threadName;
		this.downLoader = downloader;
	}	

	@Override
	public void run() {
		System.out.println("�߳�["+this.threadName+"]��ʼ���أ���ʼ��"+startPosition+"����:"+endPostion +"");
		
		HttpURLConnection conn = null;
		RandomAccessFile file = null;
		try{
			//�����ļ�
			file =new RandomAccessFile(new File(savePath), "rwd");
			file.seek(startPosition);
			
			URL url = new URL(downloadURL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setRequestProperty("Range", "bytes="+startPosition+"-" + endPostion);
			
			int responseCode = conn.getResponseCode();
			if(responseCode == HttpURLConnection.HTTP_PARTIAL  ||
			   responseCode == HttpURLConnection.HTTP_OK){
				InputStream is = conn.getInputStream();
				byte[] buffer = new byte[1024];
				
				int readCount = is.read(buffer,0,buffer.length);
				while(readCount >0){
					actualDownloadDataSize += readCount;
					file.write(buffer, 0, readCount);
					
					//�ļ������س�������
					this.downLoader.increaseDownloadedSize(readCount);
					
					readCount = is.read(buffer,0,buffer.length);
				}
			}			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn != null){
				conn.disconnect();
				conn = null;
			}
			if(file != null){
				try {
					file.close();
				} catch (IOException e) {				
					e.printStackTrace();
				}
				file = null;
			}
		}
		
		System.out.println("�߳�["+this.threadName+"]������ϣ��������ݴ�С["+this.actualDownloadDataSize+"]");
	}

}
