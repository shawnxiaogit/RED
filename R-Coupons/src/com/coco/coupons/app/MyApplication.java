package com.coco.coupons.app;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ajrd.SafeSoft;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.util.MyUtil;
import com.coco.android.util.YTFileHelper;
import com.coco.coupons.app.bean.BaseNode;
import com.coco.coupons.app.bean.GetRechargeKeyNode;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

public class MyApplication extends Application {
	
	public static final String BASE_URL = "http://www.shenmei.com/";
	
	
	//接收错误日志的邮箱
	public static String RECIVE_LOG_EMAIL = "562779246@qq.com";
	public static String TMP_USER_NAME = "";
	
	
	
	
	//=============================撤销查询相关===================================
	public static int CHE_XIAO_QUERY_TYPE = -1;
	public static String CHE_XIAO_PHONE_INFO = "";
	public static String CHE_XIAO_START_TIME = "";
	public static String CHE_XIAO_END_TIME = "";
	public static String CHE_XIAO_QUERY_TAG = "";
	public static String CHE_XIAO_LINE_NUM = "";
	public static String CHE_XIAO_CARD_NUM = "";
	public static String CHE_XIAO_CUST_NUM = "";
	
	//=============================撤销查询相关===================================
	
	
	
	/**
	 * 充值项实体类
	 */
	public static GetRechargeKeyNode  RECHARGE_NODE = null;
	
	/**
	 * 管理员权限
	 */
	public static final int AUTHORITY_MANAGER = 9;
	
	
	//保存登录的密码
	public static String LOGIN_PWD ="";
			
	/**
	 * 主管
	 */
	public static final String MANAGER = "2";
	/**
	 * 不同操作员权限
	 */
	public static final int AUTHORITY_EMPLOYEE = 8;
	/**
	 * 自己
	 */
	public static final String EMPLOYEE = "1";
	
	/**
	 * 当前查询类型
	 */
	public static int CURRENT_QUERY_TYPE = -1;
	
	/**
	 * 当前线路号
	 */
	public static String CURRENT_LINE_NUM = "";
	
	/**
	 * 签到返回的操作员权限
	 */
	public static int myAuthority = 0;
	
	
	
	/**
	 * 查询的手机信息
	 */
	public static String PHONE_INFO2 = "";
	
	/**
	 * 查询开始时间
	 */
	public static String QUERY_START_TIME = "";
	/**
	 * 查询结束时间
	 */
	public static String QUERY_END_TIME = "";
	
	/**
	 * 充值撤销查询  消息类型
	 */
	public static final String RECHARGE_CANCLE_QUERY_MTI = "1003";
	
	/**
	 * 充值撤销查询   当前返回记录位置
	 */
	public static String RECHARGE_CANCLE_QUERY_CURRENT_RETURN_FLAG = "1";
	public static String RECHARGE_CANCLE_QUERY_CURRENT_RETURN_FLAG2 = "0";
	
	/**
	 * 充值撤销查询   本次返回记录数
	 */
	public static String RECHARGE_CANCLE_QUERY_CURRENT_RETURN_COUNT = "10";
	
	
	/**
	 * 充值项
	 */
	public static GetRechargeKeyNode RECHARGE_ITEM_OBJECT = null;
	
	
	
	/**
	 * 卡号前缀
	 */
	public static final String CARD_PREFIX = "90093201";
	
	//测试终端主秘钥
//	public static final String MAIN_KEY = "3836353132353235";
	public static final String MAIN_KEY = "2013110220131103";
	public static final String MAIN_KEY2 = "0000000000000000";
	
	//默认的IMSI号
	public static final String DEFAULT_IMSI = "000000000000000";
	
	//用户名数组
	public static final String[] USER_NAME_LIST = new String[]{
		"000000","000001"
	};
	
	//默认Mac加密密码
	public static final String DEFAULT_MAC_KEY 			 = "1234567890123456";
	
	//默认消费密码
	public static final String DEFAULT_CONSUME_KEY = "123456";
	
	//mac加密秘钥
	public static byte[] MAC_KEY					     = null;
	//密码加密秘钥
	public static byte[] PIN_KEY 						 = null;
	
	
	//公司的测试地址
	public static final String IP = "211.147.70.11";
	
	//可乐的地址
//	public static final String IP = "221.181.95.106";
	
	//可口可乐IP地址备份
//	public static final String IP = "116.228.232.11";
	//可乐正式环境端口
	public static final int PORT 						 = 7926;
	//可乐测试端口
//	public static final int PORT 						 = 7927;
	
	
	//保存是否修改了密码
	public static final String SP_HAS_MODIFY_LOGIN_PWD = "SP_HAS_MODIFY_LOGIN_PWD";
	//保存是否修改了密码key
	public static final String SP_HAS_MODIFY_LOGIN_PWD_KEY = "SP_HAS_MODIFY_LOGIN_PWD_KEY";
	
	/**
	 * 保存终端号，商户号SP的名称
	 */
	public static final String SP_TERM_COMM = "SP_TERM_COMM";
	/**
	 * SP终端号key
	 */
	public static final String SP_TERM_NUM_KEY = "SP_TERM_NUM_KEY";
	/**
	 * SP商户号key
	 */
	public static final String SP_COMM_NUM_KEY = "SP_COMM_NUM_KEY";
	
	
	//查询操作员操作明细，查询标识:1-自己2-主管   需要签到返回这个标识

	public static final String GET_OPERATER_DETAIL_QUERY_FLAG = "1";
	
	//查询操作员操作明细，查询类别:C-充值明细D-消费明细
	public static final String GET_OPERATER_DETAIL_QUERY_TYPE = "C";
	//查询操作员操作明细，查询类别:C-充值明细 E-可以充值撤销的查询
	public static final String GET_OPERATER_DETAIL_QUERY_TYPE2 = "E";
	
	//撤销类型
	public static final int REQUEST_TYPE_CHE_XIAO = 777;
	//查询类型
	public static final int REQUEST_TYPE_CHA_XUN = 888;
	
	
	//测试终端号
	public static String TERM_NUM = null;
	public static final String DEFAULT_TERM_NUM = "00000002";
	//测试商户号
	public static String COMM_NUM = null;
	public static final String DEFAULT_COMM_NUM ="999073154110001";
	
	
	//测试卡号
	public static final String CARD_NUM 				 = "9009320100000011";
//	public static final String CARD_NUM 				 = "9009320100011166";
	//测试密码
	public static final String CARD_PWD 				 = "877729";
//	public static final String CARD_PWD 				 = "111111";
	
	//测试卡号2
	public static final String CARD_NUM2 = "9009320100011166";
	//测试密码2
	public static final String CARD_PWD2 ="111111";
	
	
//	public static String PHONE_NUM				 = "13301797863";
	public static String PHONE_NUM_ENCRYPT				 = "13301797863";
	public static String PHONE_NUM = "";
//	public static String PHONE_NUM = "18368324380";
	
	
	//默认密码
	public static final String PWD_DEFAULT 				 = "111111";
	
	//保存线路号的SP
	public static final String SP_ACCOUNT = "SP_ACCOUNT";
	//保存线路号的KEY
	public static final String SP_ACCOUNT_KEY = "SP_ACCOUNT_KEY";
	
	
	
	//保存是否是从配置也进入的功能引导页
	public static final String SP_JUM_FOM_CON 			 = "SP_JUM_FOM_CON";
	//保存从配置页面进入功能引导页的键
	public static final String SP_JUM_FOM_CON_KEY		 = "SP_JUM_FOM_CON_KEY";
	
	//保存用户名和密码的SP名称
	public static final String SP_USER_NAM_PWD 			 = "SP_USER_NAM_PWD";
	
	//保存是否阅读引导屏的SP名称
	public static final String SP_IS_READ_FUN_GUID 		 = "SP_IS_READ_FUN_GUID";
	
	//保存是否阅读引导屏的键
	public static final String SP_KEY_READ_FUN_GUID 	 = "SP_KEY_READ_FUN_GUID";
	
	//保存用户是否首次使用APP SP的名称
	public static final String SP_IS_USE_APP_FIRST       = "SP_IS_USE_APP_FIRST";
	//保存用户是否首次使用APP的key
	public static final String SP_IS_USE_APP_FIRST_KEY   = "SP_IS_USE_APP_FIRST_KEY";
	
	
	
	//保存用户使用的手机号及手机的设备信息
	public static final String SP_PHONE_INFO  			 = "SP_PHONE_INFO";
	
	
	//保存用户使用的手机号及手机的设备信息 key
	public static final String SP_PHONE_INFO_KEY  		 = "SP_PHONE_INFO_KEY";
	
	//手机信息
	public static String PHONE_INFO = "";
	//默认手机信息
	public static final String DEFAULT_PHONE_INFO = "00000000000000000000000000000";
	
	/**
	 * 登陆接口地址
	 */
	public static final String LOGIN_URL         		 = BASE_URL + "login";
	
	/**
	 * 注册接口地址
	 */
	public static final String REGISTER_URL      		 = BASE_URL + "register";
		
	/**
	 * 查询余额接口地址
	 */
	public static final String QUERY_REMAIN_MONEY_URL 	= BASE_URL + "query_remain_money";
	
	/**
	 * 获取动态验证码接口地址
	 */
	public static final String GET_DYNAMIC_CODE_URL   	 = BASE_URL + "get_dynamic_code";
	/**
	 * 消费接口地址
	 */
	public static final String CONSUME_URL       		 = BASE_URL + "consume";
	
	/**
	 * 开卡接口地址
	 */
	
	public static final String OPEN_CARD_URL       		 = BASE_URL + "open_card";
	
	
	//=====================初始化报文相关====================
	/**
	 * 初始化信息类型
	 */
	public static final String INIT_MTI 				= "0800";
	/**
	 * 初始化主位元素
	 */
	public static final String INIT_MAIN_ELEMENT		= "";
	/**
	 * 初始化处理码
	 */
	public static final String INIT_HANDLE_CODE			= "992001";
		
	//=====================初始化报文相关====================
		
	
	//=====================签到报文相关====================
	/**
	 * 签到信息类型
	 */
	public static final String LOGIN_MTI 				= "0800";
	/**
	 * 签到主位元素
	 */
	public static final String LOGIN_MAIN_ELEMENT 		= "2038000000C01011";
	/**
	 * 签到处理代码
	 */
	public static final String LOGIN_HANDLE_CODE 		= "990000";
	
	//=====================签到报文相关====================
	
	
	//=====================查询余额报文相关====================
	/**
	 * 查询余额信息类型
	 */
	public static final String QUERY_MTI 				= "0100";
	/**
	 * 查询余额主位元素
	 */
	public static final String QUERY_MAIN_ELEMENT 		= "603C060030C01001";
	//31A000-余额查询,31A080-积分查询
	/**
	 * 查询余额处理码
	 */
	public static final String QUERY_HANDLE_CODE 		= "31A000";
	
	/**
	 * POS输入方式
	 */
	public static final String POS_INPUT_TYPE 			= "011";
	
	//=====================查询余额报文相关====================
	
	
	
	
	//=====================充值报文相关====================
	/**
	 * 充值消息类型
	 */
	public static final String RECHARGE_MTI = "0200";
	/**
	 * 充值主位元素
	 */
	public static final String RECHARGE_MAIN_ELEMENT = "7038040000C00005";
	/**
	 * 充值处理码
	 */
	public static final String RECHARGE_HANDLE_CODE = "40A001";
	
	//=====================充值报文相关====================
	
	
	//===================修改密码报文相关==============
	
	public static final String MODIFY_PWD_MTI = "0800";
	public static final String MODIFY_PWD_MAIN_ELEMENT = "";
	public static final String MODIFY_PWD_HANDLE_CODE = "993000";
	//===================修改密码报文相关==============
	
	//=====================充值撤销报文相关====================
	/**
	 * 充值撤销消息类型
	 */
	public static final String RECHARGE_CANCLE_MTI = "0200";
	public static final String RECHARGE_CANCLE_MAIN_ELEMENT = "";
	public static final String RECHARGE_CANCLE_HANDLE_CODE = "41A000";
	
	
	//=====================充值撤销报文相关====================
	
	
	
	//=====================开卡报文相关====================
	
	/**
	 * 开卡消息类型
	 */
	public static final String OPEN_CARD_MTI = "0800";
	/**
	 * 开卡主位元素
	 */
	public static final String OPEN_CARD_MAIN_ELEMENT = "6038000000C0001D";
	/**
	 * 开卡处理码
	 */
	public static final String OPEN_CARD_HANDLE_CODE = "40A000";
	
	/**
	 * 开卡测试卡号
	 */
//	public static final String OPEN_CARD_TEST_CARD_NUM = "9009320100011158";
	
//	public static final String OPEN_CARD_TEST_CARD_NUM = "9009320100011166"; 
	
	public static final String OPEN_CARD_TEST_CARD_NUM = "9009320100011174";
	
	
	//=====================开卡报文相关====================
	
	
	//=====================获取动态验证码报文相关====================
	
	/**
	 * 获取动态验证码信息类型
	 */
	public static final String GET_SYTA_MTI 			= "0800";
	/**
	 * 获取验证码主位元素
	 */
	public static final String GET_SYTA_MAIN_ELEMENT 	= "2038000000C00011";
	/**
	 * 获取验证码处理吗
	 */
	public static final String GET_SYTA_HANDLE_CODE     = "992000";
	
	//=====================获取动态验证码报文相关====================
	
	
	//=====================获取客户信息列表报文相关====================
	/**
	 * 查询客户信息消息类型
	 */
	public static final String CUSTOMER_INFO_LIST_MTI = "0800";
	/**
	 * 查询客户信息主位元素
	 */
	public static final String CUSTOMER_INFO_LIST_MAIN_ELEMENT = "2038000000C00019";
	/**
	 * 查询客户信息处理码
	 */
	public static final String CUSTOMER_INFO_LIST_HANDLE_CODE = "31A000";
	//=====================获取客户信息列表报文相关====================
	
	
	
	//=====================客户信息查询(按卡号)报文相关====================
	/**
	 * 客户信息查询(按卡号)消息类型
	 */
	public static final String QUERY_CUST_INFO_CARD_MTI = "0100";
	/**
	 * 客户信息查询(按卡号)主位元素
	 */
	public static final String QUERY_CUST_INFO_CARD_MAIN_ELEMENT = "";
	/**
	 * 客户信息查询(按卡号)处理码
	 */
	public static final String QUERY_CUST_INFO_CARD_HANDLE_CODE = "31A001";
	
	//=====================客户信息查询(按卡号)报文相关====================
	
//	/**
//	 * 文件助手
//	 */
//	public static YTFileHelper FILE_HELPER;
	
	/**
	 * 充值字段文件名
	 */
	public static final String RECHARGE_ITEM_FILE_NAME = "RECHARGE_ITEM_FILE_NAME";
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		 //注册App异常崩溃处理器
		Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
      
		
		SharedPreferences sp = getSharedPreferences(MyApplication.SP_JUM_FOM_CON, Context.MODE_PRIVATE);
		sp.edit().putBoolean(MyApplication.SP_JUM_FOM_CON_KEY, false).commit();
		
//		FILE_HELPER = YTFileHelper.getInstance();
//		FILE_HELPER.setContext(getApplicationContext());
	}
	
	/**
	 * 
	 * @Description 获取当前时间
	 * @author Shawn
	 * @Time 2013-9-24  下午5:25:21
	 * @param param
	 * @return String
	 * @exception exception
	 */
	//eg:121942
	public static String getCurrentTime(){
		SimpleDateFormat format = new SimpleDateFormat("HHmmss");
		return format.format(new Date());
	}
	
	/**
	 * 
	 * @Description 获取当前日期
	 * @author Shawn
	 * @Time 2013-9-24  下午5:25:21
	 * @param param
	 * @return String
	 * @exception exception
	 */
	//eg:1014
	public static String getCurrentDate(){
		SimpleDateFormat format = new SimpleDateFormat("MMdd");
		return format.format(new Date());
	}
	
	/**
	 * 获取当前完成的日期
	 * @return
	 */
	
	public static String getCurrentCompleteDate(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(new Date());
	}
	
	/**
	 * 获取当前完整的时间
	 * @return
	 */
	public static String getCurrentCompleteTime(){
		SimpleDateFormat format = new SimpleDateFormat("HHmmss");
		return format.format(new Date());
	}
	
	/**
	 * 获取Mac校验码
	 * @param isoMsg
	 * 签到消息实体类
	 * @param key
	 * mac秘钥
	 * @return
	 */
	public static byte[] getMsgCheckCode(BaseNode isoMsg,String key){
		L.i("getMsgCheckCode", "getMsgCheckCode()");
		try {
//			L.e("data:", new String(isoMsg.pack()));
//			byte[] data = isoMsg.pack();
			byte[] data = isoMsg.getDataString().getBytes();
			if(key!=null&&key.length()>0){
				return SafeSoft.GenerateMac(key.getBytes(), data.length, data);
			}else{
				return SafeSoft.GenerateMac(
						MyApplication.DEFAULT_MAC_KEY.getBytes(), data.length, data);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 获取Mac校验码
	 * @param isoMsg
	 * 签到消息实体类
	 * @param key
	 * mac秘钥
	 * @return
	 */
	public static byte[] getMsgCheckCode(BaseNode isoMsg,byte[] key){
		L.i("getMsgCheckCode", "getMsgCheckCode()");
		try {

			
			byte[] data = isoMsg.getDataString().getBytes();
			L.i("key!=null&&key.length>0:", ""+(key!=null&&key.length>0));
			L.i("data:", DigitalTrans.byte2hex(data));
			if(key!=null&&key.length>0){
				return SafeSoft.GenerateMac(key, data.length, data);
			}else{
				return SafeSoft.GenerateMac(
						MyApplication.DEFAULT_MAC_KEY.getBytes(), data.length, data);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
//	//测试Mac验证
//	private void macTest(){
//		String str_mac_key = "1234567890123456";
//		byte[] mak_key = str_mac_key.getBytes();
//		String str_data = "1234567890";
//		byte[] data = str_data.getBytes();
//		int data_len = data.length;
//		byte[] mac_test = SafeSoft.GenerateMac(mak_key, data_len, data);
//		L.e("mac_test", new String(mac_test));
//		//结果应为：7789AFAC888BA28B则正确
//	}
	
	
	//==============��ѱ������=================
	//��� ��Ϣ����
	public static final String CONSUME_MTI = "0200";
	
	//��� �������  00A000-��ͨ���
	public static final String CONSUME_HAND_CODE1 = "00A000";
	//��� �������  00A080-������
	public static final String CONSUME_HAND_CODE2 = "00A080";
	
	//����Ч��
	public static String CONSUME_CARD_DEAD_TIME = "9999";
	
	//POS���뷽ʽ   01  - �ֹ�
	public static String POS_INPUT_TYPE1	 = "001";
	//POS���뷽ʽ   02  - ����
	public static String POS_INPUT_TYPE2	 = "002";
	//POS���뷽ʽ   05  - ���ɵ�·��������Ϣ�ɿ�
	public static String POS_INPUT_TYPE3	 = "005";
	//POS���뷽ʽ  95  - ���ɵ�·��������Ϣ���ɿ�
	public static String POS_INPUT_TYPE4	 = "095";

	//��˳���
	public static String CARD_ORDER_NUM      = "123";
	
	
	//==============��ѱ������=================
	
	
	//===============获取充值字段报文相关============
	/**
	 * 获取充值字段消息类型
	 */
	public static final String GET_RECHARGE_KEY_MTI = "1002";
	public static final String GET_RECHARGE_KEY_MAIN_ELEMENT = "";
	public static final String GET_RECHARGE_KEY_HANDLE_CODE = "";
	
	//===============获取充值字段报文相关============
	
	
	//保存是否创建了快捷方式的sp名称
		public static final String SP_IS_SHORTCUT_EXIST = "SP_IS_SHORTCUT_EXIST";
		//保存是否创建了快捷方式的key
		public static final String KEY_SP_SHORTCUT = "KEY_SP_SHORTCUT";
		/**
		 * 是否创建了快捷方式
		 * @param activity
		 * @return
		 */
		public static boolean isShortCutExist(Activity activity){
			SharedPreferences sp =activity.getSharedPreferences(SP_IS_SHORTCUT_EXIST, Activity.MODE_PRIVATE);
			return sp.getBoolean(KEY_SP_SHORTCUT, false);
		}
		/**
		 * 设置创建了快捷方式
		 * @param activity
		 * @param isExist
		 */
		public static void setShorCut(Activity activity,boolean isExist){
			SharedPreferences sp = activity.getSharedPreferences(SP_IS_SHORTCUT_EXIST, Activity.MODE_PRIVATE);
			sp.edit().putBoolean(SP_IS_SHORTCUT_EXIST, isExist).commit();
		}
		
		/**
		 * 添加快捷方式
		 * 注意AndroidManifest.xml里添加权限:"com.android.permission.launcher.INSTALL_SHORTCUT"
		 * @param activity
		 * 启动Activity
		 */
		public static void addShortCut(Activity activity){
//			Intent shor_cut_intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
//			//快捷方式的名称
//			shor_cut_intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
//			//不允许重复创建
//			shor_cut_intent.putExtra("duplicate", false);
//			//制定当前的Activity为快捷方式的启动对象
//			//注意：ComponentName的第二个参数必须加上点号(.)，否则快捷方式无法启动相应程序
//			ComponentName componentName = new ComponentName(activity.getPackageName(), "."+activity.getLocalClassName());
//			shor_cut_intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(componentName));
//			//快捷方式的图标
//			ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(activity, R.drawable.ic_launcher);
//			shor_cut_intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
//			sendBroadcast(shor_cut_intent);
			
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.setClassName(activity.getPackageName(), "."+activity.getLocalClassName());
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			
			Intent short_cut_intent = new Intent();
			short_cut_intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
			//设置快捷方式名称
			short_cut_intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, activity.getString(R.string.app_name));
			//应用程序图标
			ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(activity, R.drawable.ic_launcher);
			//添加应用程序图标
			short_cut_intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
			//不允许重复创建
			short_cut_intent.putExtra("duplicate", false);
			
			short_cut_intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
			activity.sendBroadcast(short_cut_intent);
			
		}
		
		
		/**
		 * 
		 * @Description 获得系统流水号
		 * @author Shawn
		 * @Time 2013-9-24  下午5:12:49
		 * @param param
		 * @return String
		 * @exception exception
		 */
		public static String getLineNum(Context context){
			SharedPreferences sp = context.getSharedPreferences("num", Context.MODE_PRIVATE);
			
			return sp.getString("line_num", "000001"); 
		}

		
		/**
		 * 获取调整后60域的内容
		 * @param phoneNum
		 * 手机号
		 * @param IMSI
		 * 15位SIM卡标识
		 * @param IEMI
		 * 14位手机设备标识
		 * @return
		 */
		public static String getSixtyValue2(String phoneNum,String IMSI,String IEMI){
			StringBuilder sb = new StringBuilder();
			if(phoneNum!=null&&phoneNum.length()>0){
				sb.append(phoneNum);
			}
			if(IMSI!=null&&IMSI.length()>0){
				sb.append(IMSI);
			}
			if(IEMI!=null&&IEMI.length()>0){
				sb.append(IEMI);
			}
			String data = sb.toString();
			int len = data.length();
			StringBuilder sb2 = new StringBuilder();
			if(len>=0&&len<=9){
				sb2.append("00");
			}else if(len>9&&len<=99){
				sb2.append("0");
			}
			sb2.append(len);
			sb2.append(data);
			return sb2.toString();
		}	
		
	/**
	 * 
	 * @Description 获得系统流水号
	 * @author Shawn
	 * @Time 2013-9-24 下午5:12:49
	 * @param param
	 * @return String
	 * @exception exception
	 */
	public static void setLineNum(Context context) {
		SharedPreferences sp = context.getSharedPreferences("num",
				Context.MODE_PRIVATE);
		String num = sp.getString("line_num", "000001");
		int i_num = Integer.parseInt(num);
		i_num++;
		if (i_num == 999999) {
			i_num = 0;
		}
		String str_num = String.valueOf(i_num);
		if (str_num.length() == 1) {
			str_num = "00000" + str_num;
		} else if (str_num.length() == 2) {
			str_num = "0000" + str_num;
		} else if (str_num.length() == 3) {
			str_num = "000" + str_num;
		} else if (str_num.length() == 4) {
			str_num = "00" + str_num;
		} else if (str_num.length() == 5) {
			str_num = "0" + str_num;
		}

		sp.edit().putString("line_num", str_num).commit();
	}
	
	
	
	/**
	 * 
	 * @Description 获取60域数据
	 * @author Shawn
	 * @Time 2013-9-24  下午5:33:42
	 * @param param
	 * @return String
	 * @exception exception
	 */
	public static String getSixtyValue(String comm_num,String term_num){
		StringBuilder sb = new StringBuilder();
		sb.append(MyApplication.getCurrentCompleteDate());
		sb.append(MyApplication.getCurrentCompleteTime());
		sb.append("3");//POS设备类型
		sb.append("1");//预授权标识
		sb.append("1");//位币别识别
		sb.append(comm_num);
		sb.append("|");
		sb.append(term_num);
		sb.append("|");
		
		String value = sb.toString();
		StringBuilder sb2= new StringBuilder();
		int len= value.length();
		
		if(len>99){
			sb2.append("0");
		}else if(len>9&&len<=99){
			sb2.append("00");
		}
		sb2.append(len);
		sb2.append(value);
//		String strLen=new String(StringUtil.str2Bcd(sb2.toString()));
//		StringBuilder data_six=  new StringBuilder();
//		data_six.append(strLen);
//		data_six.append(value);
		L.i("getSixTeenValue(comm_num, term_num)", sb2.toString());
		
		return sb2.toString();
	}
	
	/**
	 * 本笔批次号
	 */
	public static String CURRENT_PICHI_HAO = "";
	/**
	 * 本笔票据号
	 */
	public static String CURRENT_PIAOJU_HAO = "";
	
	/**
	 * 查询号
	 */
	public static String CURRENT_CHAXUN_HAO = "";
	
	/**
	 * 日期
	 */
	public static String CURRENT_DATE = "";
	
	
	public static String get62Value(String serial_num){
		StringBuilder sb = new StringBuilder();
		
		sb.append(MyApplication.CURRENT_PICHI_HAO);
		sb.append(MyApplication.CURRENT_PIAOJU_HAO);
//		sb.append(MyApplication.CURRENT_CHAXUN_HAO);
		sb.append(serial_num);
		sb.append(MyApplication.getCurrentCompleteDate());
		String data = sb.toString();
		int len = data.length();
		StringBuilder sb2 = new StringBuilder();
		if(len>=0&&len<=9){
			sb2.append("00");
		}else if(len>9&&len<=99){
			sb2.append("0");
		}
		sb2.append(len);
		sb2.append(data);
		return sb2.toString();
		
	}
	
	public static String get62Value2(String serial_num,String date){
		StringBuilder sb = new StringBuilder();
		
		if(MyApplication.CURRENT_PICHI_HAO!=null&&MyApplication.CURRENT_PICHI_HAO.length()>0){
			sb.append(MyApplication.CURRENT_PICHI_HAO);
		}else{
			sb.append("000000");
		}
		if(MyApplication.CURRENT_PIAOJU_HAO!=null&&MyApplication.CURRENT_PIAOJU_HAO.length()>0){
			sb.append(MyApplication.CURRENT_PIAOJU_HAO);
		}else{
			sb.append("000000");
		}
		
		
//		sb.append(MyApplication.CURRENT_CHAXUN_HAO);
		sb.append(serial_num);
		sb.append(date);
		String data = sb.toString();
		int len = data.length();
		StringBuilder sb2 = new StringBuilder();
		if(len>=0&&len<=9){
			sb2.append("00");
		}else if(len>9&&len<=99){
			sb2.append("0");
		}
		sb2.append(len);
		sb2.append(data);
		return sb2.toString();
		
	}
	
	public static String get62Value2(){
		StringBuilder sb = new StringBuilder();
		sb.append("026");
//		byte[] b_len = MyUtil.hexStringToByte("026");
		
//		sb.append(new String(b_len));
		sb.append("000000");
		sb.append("000000");
		sb.append("000000");
		sb.append(MyApplication.getCurrentCompleteDate());
		return sb.toString();
		
	}
	
	public static String getSixtyValue3(String phoneNum,String IMSI,String IEMI){
		StringBuilder sb = new StringBuilder();
		if(phoneNum!=null&&phoneNum.length()>0){
			sb.append(phoneNum);
		}
		if(IMSI!=null&&IMSI.length()>0){
			sb.append(IMSI);
		}
		if(IEMI!=null&&IEMI.length()>0){
			sb.append(IEMI);
		}
//		String data = sb.toString();
//		int len = data.length();
//		StringBuilder sb2 = new StringBuilder();
//		if(len>=0&&len<=9){
//			sb2.append("00");
//		}else if(len>9&&len<=99){
//			sb2.append("0");
//		}
//		sb2.append(len);
//		sb2.append(data);
		return sb.toString();
	}
	
	/**
	 * 15位SIM卡唯一标识
	 */
	public static String IMSI = "";
	/**
	 * 14位手机设备唯一标识
	 */
	public static String IEMI = "";
	
	
	/**
	 * 
	 * @Description �����л�Ч��
	 * @author Shawn
	 * @Time 2013-8-23  ����4:56:36
	 */
	public static void switchActivityAnimationIn(Activity context){
		context.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
//		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}
	
	
	/**
	 * 
	 * @Description �����л�Ч��
	 * @author Shawn
	 * @Time 2013-8-23  ����4:56:36
	 */
	public static void switchActivityAnimationOut(Activity context){
//		context.overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in);
		context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}
	
	

	/**
	 * 判断结束时间是否小于开始时间
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static boolean isEndTimeSmallThanStartTime(String startTime,String endTime){
		boolean result = false;
		Date date_start=null;
		Date date_end=null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		try{
			date_start = format.parse(startTime);
			date_end = format.parse(endTime);
			if(date_end.before(date_start)){
				result = true;
			}
		}catch(Exception e){
			
		}
		return result;
	}
	
	
	/**
	 * 获取App安装包信息
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try { 
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {    
			e.printStackTrace(System.err);
		} 
		if(info == null) info = new PackageInfo();
		return info;
	}
	
}

