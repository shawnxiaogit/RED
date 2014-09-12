package com.coco.android.util;

public class L {
	public static boolean isDebug = true;
	
	public static boolean LoginDebug = false;
	//获取验证码调试
	public static boolean GetSyntaCodeDebug = false;
	//充值调试
	public static boolean RechargeDebug = false;
	public static boolean RechargeDebug2 = false;
	//开卡调试
	public static boolean OpenCardDebug = false;
	
	//充值测试
	public static boolean RechargeDebugTest = false;
	
	//如果是用模拟器测试
	public static boolean ISDEUB_BY_EMULATER = false;
	
	//查询调试
	public static boolean IS_QUERY_DEBUG = false;
	
	//获取客户信息调试
	public static boolean GET_CUST_INFO_DEBUG = false;
	
	/**
	 * 没有网络调试
	 */
	public static boolean NO_NET_DEBUG = false;
	/**
	 * 什么都忘带了
	 */
	public static boolean NO_PHONE = false;
	
	/**
	 * 获取消费、充值列表调试
	 */
	public static boolean GET_DEAL_LIST_DEBUG  = false;
	
	/**
	 * 没有充值和充值撤销数据调试
	 */
	public static boolean NOT_RECHARGE_DEBUG = false;
	
	/**
	 * 充值撤销测试，即不是主管也显示充值撤销菜单
	 */
	public static boolean RECHARGE_CANCLE_DEBUG = false;
	
	
	public static void v(String tag, String msg) {
		if (isDebug)
			android.util.Log.v(tag, msg);
	}

	public static void v(String tag, String msg, Throwable t) {
		if (isDebug)
			android.util.Log.v(tag, msg, t);
	}

	public static void d(String tag, String msg) {
		if (isDebug)
			android.util.Log.d(tag, msg);
	}

	public static void d(String tag, String msg, Throwable t) {
		if (isDebug)
			android.util.Log.d(tag, msg, t);
	}

	public static void i(String tag, String msg) {
		if (isDebug)
			android.util.Log.i(tag, msg);
	}

	public static void i(String tag, String msg, Throwable t) {
		if (isDebug)
			android.util.Log.i(tag, msg, t);
	}

	public static void w(String tag, String msg) {
		if (isDebug)
			android.util.Log.w(tag, msg);
	}

	public static void w(String tag, String msg, Throwable t) {
		if (isDebug)
			android.util.Log.w(tag, msg, t);
	}

	public static void e(String tag, String msg) {
		if (isDebug)
			android.util.Log.e(tag, msg);
	}

	public static void e(String tag, String msg, Throwable t) {
		if (isDebug)
			android.util.Log.e(tag, msg, t);
	}
}
