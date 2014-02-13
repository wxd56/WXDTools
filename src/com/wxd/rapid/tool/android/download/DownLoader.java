package com.wxd.rapid.tool.android.download;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * ������
 * 
 * @author ����
 *
 */
public class DownLoader {
	/**
	 * ���ص�ַ
	 */
	private String downLoadURL;	
	/**
	 * �����ļ������·��
	 */
	private String fileSavePath;	
	/**
	 * �����߳�����
	 */
	private int  tCount;
	/**
	 * ÿ���߳����ص����ݿ�Ĵ�С
	 */
	private int blokSize;
	/**
	 * �Ѿ����س���
	 */
	private int downloadedSize;
	/**
	 * �ļ��ܳ���
	 */
	private int fileSize;
	
	private DownloadSizeChangedLinstener downloadSizeChangedLinstener;
	
	/**
	 * �������ӳ�ʱʱ��
	 */
	private static final int CONN_TIMEOUT = 5000;
	
	/**
	 * ����״̬
	 */
	private String state = DownLoaderState.PAUSE;
	
	
	/**
	 * ���캯����ÿ������ʱ��Ӧ�ù���һ���µ�ʵ��
	 * 
	 * @param url  		���ص�ַ
	 * @param savePath	����·��
	 * @param tCount	�����߳�����
	 */
	public DownLoader(String downloadPath ,String savePath,int tCount){
		this.downLoadURL = downloadPath;
		this.fileSavePath = savePath;
		this.tCount = tCount;
	}
	
	/**
	 * �����ļ����ص��ܳ���
	 * 
	 * @param size  ���ӵ���
	 */
	public synchronized void increaseDownloadedSize(int size){
		this.downloadedSize += size;
	}
	
	/**
	 * ��ʼ�������� 
	 * @throws Exception ���ع����г��ֵ��κ��쳣���ᱻ�׳�
	 */
	public void startDownLoad() throws Exception{
		//�õ������ļ��Ĵ�С
		int fileSize = getFileSizeFromNet();
		this.fileSize = fileSize;
		debug("�ļ����صĴ�СΪ��"+fileSize);
		
		//���ش������ص��ļ�
		createLocalFile();
		
		//�������ݿ�
		this.blokSize = (fileSize%this.tCount == 0 ? fileSize/tCount:fileSize/tCount+1); 
		
		//���������߳̿�ʼ����
		startDownloadThread();
		
		//�������ؽ��ȣ�֪ͨ��ص�Listener
		while(this.state.equals(DownLoaderState.START)){
			if(downloadSizeChangedLinstener != null){
				downloadSizeChangedLinstener.onDownloadSizeChanged(this.downloadedSize,this.fileSize);
			}			
			
			//�ж��������
			if(this.downloadedSize >= this.fileSize){
				this.state = DownLoaderState.FINISH;
			}
			
			Thread.sleep(900);
		}
	}

	/**
	 * ���ü������ؽ��ȵļ�����
	 * @param downloadSizeChangedLinstener
	 */
	public void setDownloadSizeChangedLinstener(
			DownloadSizeChangedLinstener downloadSizeChangedLinstener) {
		this.downloadSizeChangedLinstener = downloadSizeChangedLinstener;
	}

	private void startDownloadThread() {
		this.state = DownLoaderState.START;
		//�������������߳̿�ʼ����
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
