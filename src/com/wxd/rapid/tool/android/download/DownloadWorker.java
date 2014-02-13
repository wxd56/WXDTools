package com.wxd.rapid.tool.android.download;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 具体的下载Worker,可以进行断点下载
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
	 * 文件保存路径
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
		System.out.println("线程["+this.threadName+"]开始下载，开始："+startPosition+"结束:"+endPostion +"");
		
		HttpURLConnection conn = null;
		RandomAccessFile file = null;
		try{
			//创建文件
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
					
					//文件总下载长度增加
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
		
		System.out.println("线程["+this.threadName+"]下载完毕，下载数据大小["+this.actualDownloadDataSize+"]");
	}

}
