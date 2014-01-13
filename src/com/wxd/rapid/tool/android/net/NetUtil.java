package com.wxd.rapid.tool.android.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {
	
	/**
	 * ������������Ƿ����
	 */
	public static boolean hasNetWork(Context ctx){
		ConnectivityManager connManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info =  connManager.getActiveNetworkInfo();
		if(info == null || !info.isAvailable()){
			return false;
		}
		return true;
	}
	
	/**
	 * ���ͼ򵥵�GET����
	 * @return  ���������ص���Ϣ
	 */
	public static String get(String paramUrl){		
		HttpURLConnection conn = null;
		try {
			URL url = new URL(paramUrl);
			conn =  (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			
			InputStream is = conn.getInputStream();
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(conn != null){
				conn.disconnect();
			}
		}
		return null;
	}
}
