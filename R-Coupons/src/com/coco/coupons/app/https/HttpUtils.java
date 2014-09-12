package com.coco.coupons.app.https;

import java.net.Socket;

import org.apache.http.NameValuePair;

import com.coco.coupons.app.MyApplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class HttpUtils {
	// 网络连接部分
	public static String postByHttpURLConnection(String strUrl,
			NameValuePair... nameValuePairs) {
		return CustomHttpURLConnection.postFromWebByHttpURLConnection(strUrl,
				nameValuePairs);
	}

	public static String getByHttpURLConnection(String strUrl,
			NameValuePair... nameValuePairs) {
		return CustomHttpURLConnection.getFromWebByHttpUrlConnection(strUrl,
				nameValuePairs);
	}

	public static String postByHttpClient(Context context,String strUrl,
			NameValuePair... nameValuePairs) {
		return CustomHttpClient.postFromWebByHttpClient(context,strUrl, nameValuePairs);
	}

	public static String getByHttpClient(Context context,String strUrl,
			NameValuePair... nameValuePairs) throws Exception {
		return CustomHttpClient.getFromWebByHttpClient(context,strUrl, nameValuePairs);
	}

	
	//判断网络是否已经连接
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	
	
	public static boolean isPortOpened(){
		boolean result=false;
		try {
			Socket socket = new Socket(MyApplication.IP, MyApplication.PORT);
			System.out.println("端口已开放");
			socket.close();
			result = true;
		} catch (Exception e) {
			System.out.println("端口未开放");
			result=false;
//			e.printStackTrace();
		}
		return result;
	}
	
	// ------------------------------------------------------------------------------------------
	// 网络连接判断
	// 判断是否有可用网络
	public static boolean isNetworkAvailable(Context context) {
		return NetWorkHelper.isNetworkAvailable(context);
	}

	// 判断mobile网络是否可用
	public static boolean isMobileDataEnable(Context context) {
		String TAG = "httpUtils.isMobileDataEnable()";
		try {
			return NetWorkHelper.isMobileDataEnable(context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
 
	// 判断wifi网络是否可用
	public static boolean isWifiDataEnable(Context context) {
		String TAG = "httpUtils.isWifiDataEnable()";
		try {
			return NetWorkHelper.isWifiDataEnable(context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			return false;
		}
	}


	// 判断是否为漫游
	public static boolean isNetworkRoaming(Context context) {
		return NetWorkHelper.isNetworkRoaming(context);
	}
}
