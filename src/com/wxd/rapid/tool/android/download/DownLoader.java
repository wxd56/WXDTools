package com.wxd.rapid.tool.android.download;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载器
 * 
 * @author 王旭东
 *
 */
public class DownLoader {
	/**
	 * 下载地址
	 */
	private String downLoadURL;	
	/**
	 * 下载文件保存成路径
	 */
	private String fileSavePath;	
	/**
	 * 下载线程总数
	 */
	private int  tCount;
	/**
	 * 每个线程下载的数据块的大小
	 */
	private int blokSize;
	/**
	 * 已经下载长度
	 */
	private int downloadedSize;
	/**
	 * 文件总长度
	 */
	private int fileSize;
	
	private DownloadSizeChangedLinstener downloadSizeChangedLinstener;
	
	/**
	 * 网络连接超时时间
	 */
	private static final int CONN_TIMEOUT = 5000;
	
	/**
	 * 下载状态
	 */
	private String state = DownLoaderState.PAUSE;
	
	
	/**
	 * 构造函数，每次下载时都应该构造一个新的实例
	 * 
	 * @param url  		下载地址
	 * @param savePath	保存路径
	 * @param tCount	下载线程总数
	 */
	public DownLoader(String downloadPath ,String savePath,int tCount){
		this.downLoadURL = downloadPath;
		this.fileSavePath = savePath;
		this.tCount = tCount;
	}
	
	/**
	 * 增加文件下载的总长度
	 * 
	 * @param size  增加的量
	 */
	public synchronized void increaseDownloadedSize(int size){
		this.downloadedSize += size;
	}
	
	/**
	 * 开始进行下载 
	 * @throws Exception 下载过程中出现的任何异常都会被抛出
	 */
	public void startDownLoad() throws Exception{
		//得到网络文件的大小
		int fileSize = getFileSizeFromNet();
		this.fileSize = fileSize;
		debug("文件下载的大小为："+fileSize);
		
		//本地创建下载的文件
		createLocalFile();
		
		//计算数据块
		this.blokSize = (fileSize%this.tCount == 0 ? fileSize/tCount:fileSize/tCount+1); 
		
		//启动下载线程开始下载
		startDownloadThread();
		
		//监听下载进度，通知相关的Listener
		while(this.state.equals(DownLoaderState.START)){
			if(downloadSizeChangedLinstener != null){
				downloadSizeChangedLinstener.onDownloadSizeChanged(this.downloadedSize,this.fileSize);
			}			
			
			//判断下载完成
			if(this.downloadedSize >= this.fileSize){
				this.state = DownLoaderState.FINISH;
			}
			
			Thread.sleep(900);
		}
	}

	/**
	 * 设置监听下载进度的监听器
	 * @param downloadSizeChangedLinstener
	 */
	public void setDownloadSizeChangedLinstener(
			DownloadSizeChangedLinstener downloadSizeChangedLinstener) {
		this.downloadSizeChangedLinstener = downloadSizeChangedLinstener;
	}

	private void startDownloadThread() {
		this.state = DownLoaderState.START;
		//启动所有下载线程开始下载
		for(int threadID =0; threadID<this.tCount;threadID++){
			int startPosition = threadID*this.blokSize;
			int endPosition = (threadID+1)*this.blokSize -1;
			
			new Thread(new DownloadWorker(this,"Thread"+threadID,downLoadURL, startPosition, endPosition, this.fileSavePath)).start();;
		}
	}

	private boolean createLocalFile() throws IOException {
		RandomAccessFile raf = new RandomAccessFile(new File(this.fileSavePath), "rwd");
		raf.setLength(fileSize);
		raf.close();
		return true;
	}

	private int getFileSizeFromNet() {
		int fileSize = 0;
		HttpURLConnection conn = null;
		try{
			URL url = new URL(this.downLoadURL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(CONN_TIMEOUT);
			
			if(HttpURLConnection.HTTP_OK == conn.getResponseCode()){
				fileSize = conn.getContentLength();				
			}			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn != null){
				conn.disconnect();
				conn = null;
			}
		}
		
		return fileSize ;
	}
	
	private void debug(String msg){
		System.out.println(msg);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}	
}
