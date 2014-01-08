package com.wxd.rapid.tool.android.net;

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
}
