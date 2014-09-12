package com.coco.android.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 文件处理工具类
 * 
 * @author ShawnXiao
 * 
 */
public class YTFileHelper {
	
	
	private static final String ROOT_DIR = "/RED/";
	private static final String ROOT_DIR_ = "/RED";
	private static final String _SPLIT_LINE = "/";

	private Context mContext;

	public static YTFileHelper mSelf;

	private YTFileHelper() {

	}

	public static YTFileHelper getInstance() {

		if (null == mSelf) {
			mSelf = new YTFileHelper();
		}
		return mSelf;

	}

	public void setContext(Context context) {
		mContext = context;
	}

	/**
	 * 检查是否有SD卡，并有读写权限
	 * 
	 * @return
	 */
	public static final boolean checkSDCard() {

		boolean result = false;
		String state = Environment.getExternalStorageState();

		if (state.toLowerCase().equals(Environment.MEDIA_MOUNTED.toLowerCase())) {
			result = true;
		}
		return result;

	}
	/**
	 * 将Url转化为Bitmap
	 * @param logoUrl
	 * @return
	 */
	public static Bitmap url2Bitmap(String logoUrl){
		InputStream is = null;
		Bitmap image=null;
		try{
			URL url = new URL(logoUrl);        
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();        
			conn.setReadTimeout(10000 /* milliseconds */);        
			conn.setConnectTimeout(15000 /* milliseconds */);        
			conn.setRequestMethod("GET");       
			conn.setDoInput(true);        
			conn.connect();        
			int response = conn.getResponseCode();
			if(response != 200){
				return null;        				
			}
			is = conn.getInputStream();
			image=BitmapFactory.decodeStream(is);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return image;
	}
	/**
	 * 创建一个手机文件
	 * 
	 * @param filename
	 * @return
	 */
	public File createFile(String filename) {
		File file = null;
		try {
			if (checkSDCard()) {
				File rootDir = Environment.getExternalStorageDirectory();
				String rootPath = rootDir.getAbsolutePath();
				file = new File(rootPath + ROOT_DIR + filename);
			}else{
				file = new File(Environment.getDataDirectory().getAbsolutePath() + "/data/" + mContext.getPackageName()
						+ "/red");
			}
		} catch (Exception e) {
		}
		return file;
	}

	/**
	 * 创建一个手机文件
	 * 
	 * @param filename
	 *            文件名
	 * @return 布尔值
	 */
	public boolean isFileExist(String filename) {
		File file = null;
		try {
			if (checkSDCard()) {
				File rootDir = Environment.getExternalStorageDirectory();
				String rootPath = rootDir.getAbsolutePath();
				file = new File(rootPath + ROOT_DIR + filename);
//				if (D2EConfigures.TEST) {
//					Log.e("fileExist----------->", file.exists() ? "存在" : "不存在");
//				}
				if (file.exists()) {

					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 删除一个文件
	 * 
	 * @param filename
	 *            文件名
	 */
	public void deletExistFile(String filename) {
		File file = null;
		try {
			if (checkSDCard()) {
				File rootDir = Environment.getExternalStorageDirectory();
				String rootPath = rootDir.getAbsolutePath();
				file = new File(rootPath + ROOT_DIR + filename);
				if (file.exists()) {
					file.delete();
//					boolean isSuccess = file.delete();
//					if (D2EConfigures.TEST) {
//						Log.e("删除文件是否成功：", isSuccess ? "是" : "否");
//					}
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 读取文件中的数据
	 * 
	 * @param filename
	 *            文件名
	 * @return 字节数组数据
	 */
	public final byte[] readFile(String filename) {
		FileInputStream fis = null;
		byte[] bytes = null;
		try {
			if (checkSDCard()) {
				File rootDir = Environment.getExternalStorageDirectory();
				String rootPath = rootDir.getAbsolutePath();
				File file = new File(rootPath + ROOT_DIR + filename);
				fis = new FileInputStream(file);
				bytes = JJNetHelper.readByByte(fis, -1);
			} else {
				fis = mContext.openFileInput(filename);
				bytes = JJNetHelper.readByByte(fis, -1);
			}
		} catch (Exception e) {
			bytes = null;
		} finally {
			try {
				if (fis != null) {
					fis.close();
					fis = null;
				}
			} catch (Exception e) {
			}
		}

		return bytes;
	}

	/**
	 * 保存文件
	 * 
	 * @param filename
	 *            要保存数据为文件的文件名
	 * @param data
	 *            要保存的字节数组数据
	 */
	public final void saveFile(String filename, byte[] data) {

		FileOutputStream fos = null;

		try {
			if (checkSDCard()) {
				File rootDir = Environment.getExternalStorageDirectory();
				String rootPath = rootDir.getAbsolutePath();
				File file = new File(rootPath + ROOT_DIR_);
				if (!file.exists()) {
					file.mkdir();
				}
				File png = new File(file.getPath() + _SPLIT_LINE + filename);
				if (png.exists()) {
					png.delete();
				}
				fos = new FileOutputStream(png, true);
				fos.write(data);
				fos.close();
			} else {
				fos = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
				fos.write(data);
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
					fos = null;
				} catch (Exception e) {
				}
			}
		}

	}

	/**
	 * 将对象数据序列化为文件
	 * @param filename
	 * 要序列化为文件的文件名
	 * @param data
	 * 对象数据
	 */
	public final void serialObject(String filename, Object data) {
//		if (D2EConfigures.TEST) {
//			Log.e("data----------->", "" + data);
//		}

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try {
			if (checkSDCard()) {
				File rootDir = Environment.getExternalStorageDirectory();
				String rootPath = rootDir.getAbsolutePath();
				File file = new File(rootPath + ROOT_DIR_);
				if (!file.exists()) {
					file.mkdir();
				}
				File png = new File(file.getPath() + _SPLIT_LINE + filename);
				if (png.exists()) {
					png.delete();
				}
				fos = new FileOutputStream(png, true);
				oos = new ObjectOutputStream(fos);
				oos.writeObject(data);
//				if (D2EConfigures.TEST) {
//					Log.e("xxxxxxxxxxx", "" + (oos.toString()));
//				}
				oos.close();
				fos.close();
			} else {
//				createFile(filename);
				fos = mContext.openFileOutput(filename, Context.MODE_APPEND);
				oos = new ObjectOutputStream(fos);
				oos.writeObject(data);
//				if (D2EConfigures.TEST) {
//					Log.e("xxxxxxxxxxx", "" + (oos.toString()));
//				}
				oos.close();
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null || oos != null) {
				try {
					oos.close();
					oos = null;
					fos.close();
					fos = null;
				} catch (Exception e) {
				}
			}
		}

	}

	/**
	 * 反序列化文件为对象
	 * @param filename
	 * 保存的文件名
	 * @return
	 * 对象数据
	 */
	public final Object deSerialObject(String filename) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		Object data = null;
		try {
			if (checkSDCard()) {
				File rootDir = Environment.getExternalStorageDirectory();
				String rootPath = rootDir.getAbsolutePath();
				File file = new File(rootPath + ROOT_DIR + filename);
				fis = new FileInputStream(file);
				ois = new ObjectInputStream(fis);
				data = ois.readObject();
				ois.close();
				fis.close();
			} else {
				fis = mContext.openFileInput(filename);
				ois = new ObjectInputStream(fis);
				data = ois.readObject();
				ois.close();
				fis.close();
			}
		} catch (Exception e) {
			data = null;
		} finally {
			try {
				if (fis != null || ois != null) {
					ois.close();
					ois = null;
					fis.close();
					fis = null;
				}
			} catch (Exception e) {
			}
		}

		return data;
	}

}
