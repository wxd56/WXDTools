package com.wxd.rapid.tool.android.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.wxd.rapid.tool.android.json.JSONObjectParser;
import com.wxd.rapid.tool.io.IOReadUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {
	
	/**
	 * 检查网络连接是否可用
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
	 * 发送简单的GET请求
	 * @return  服务器返回的信息
	 */
	public static String get(String paramUrl){		
		HttpURLConnection conn = null;
		try {
			URL url = new URL(paramUrl);
			conn =  (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			
			InputStream is = conn.getInputStream();
			String result = IOReadUtil.read(is);
			return result;
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
	
	/**
	 * 发送简单的GET请求,对返回的Json对象进行解析，通过反射技术，转换成指定的类型。
	 * @return  服务器返回的信息
	 */
	public static <T> T get(String paramUrl,Class<T> clazz) throws Exception{	
		String resulString = get(paramUrl);
		return JSONObjectParser.parseObject(resulString, clazz);
	}
}
