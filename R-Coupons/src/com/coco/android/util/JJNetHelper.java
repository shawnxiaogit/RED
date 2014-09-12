package com.coco.android.util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class JJNetHelper {
	
	public static boolean checkNet(Context context) {
		try {
			// 获取手机所有连接管理对象(包括对wifi, net等连接的管理)
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
    public static byte[] readByByte(InputStream dis, long len) throws IOException {
    	byte[] result = null;
    	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int bufferSize = 512 ;
        byte byteInput[] = new byte[bufferSize] ;
        int size = 0;
        if (len!=-1) {
            long askSize = 0;
            while (len > 0) {
                askSize = (len < (long)bufferSize) ? len : (long)bufferSize;
                if ((size = dis.read(byteInput, 0, (int)askSize)) != -1) {
                    len -= size;
                    buffer.write(byteInput, 0, size);
                } else {
                    break;
                }
            }
        } else {
            while ((size = dis.read(byteInput, 0, bufferSize)) != -1) {
                buffer.write(byteInput, 0, size);
            }
        }
        result = buffer.toByteArray();
        return result;
    }

}
