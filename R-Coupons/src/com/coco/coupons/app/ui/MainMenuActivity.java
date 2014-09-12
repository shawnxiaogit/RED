package com.coco.coupons.app.ui;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.coco.android.socket.SocketClient;
import com.coco.android.util.L;
import com.coco.android.util.StringUtil;
import com.coco.coupons.app.AppManager;
import com.coco.coupons.app.BaseActivity;
import com.coco.coupons.app.MyApplication;
import com.coco.coupons.app.R;
import com.coco.coupons.app.bean.CustomerInfoListNode;
import com.coco.coupons.app.bean.GetRechargeKeyNode;
import com.coco.coupons.app.bean.ModifyPwdNode;
import com.coco.coupons.app.bean.RequestUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @Descriptio 主菜单界面
 * @author Shawn
 * @Time 2013-8-19
 */
public class MainMenuActivity extends BaseActivity {
	/**
	 * 功能列表
	 */
	private ListView mListView;
	/**
	 * 菜单适配器
	 */
	private MyMenuAdapter mAdapter;

	/**
	 * 菜单名称
	 */
	private String[] mMainMenus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main_menu);
		mListView = (ListView) findViewById(R.id.lv_menus);

		((TextView) findViewById(R.id.tv_title))
				.setText(R.string.title_main_menu);

		mAdapter = new MyMenuAdapter(this);
		mMainMenus = getResources().getStringArray(R.array.main_menu);

		mAdapter.addMenu(mMainMenus);
		L.e("MyApplication.myAuthority", ""+MyApplication.myAuthority);
		if (!L.RECHARGE_CANCLE_DEBUG) {
			if (MyApplication.myAuthority == MyApplication.AUTHORITY_EMPLOYEE) {
				mAdapter.removeMenu(getResources().getStringArray(
						R.array.main_menu)[1]);
			}
		}
		mListView.setAdapter(mAdapter);

		timer.schedule(timerTask, update_delay, period);
		
		// 保存已经修改了密码
		SharedPreferences sp = getSharedPreferences(
				MyApplication.SP_HAS_MODIFY_LOGIN_PWD, Activity.MODE_PRIVATE);
		boolean is_modify_pwd = sp.getBoolean(
				MyApplication.SP_HAS_MODIFY_LOGIN_PWD_KEY, false);

		if (!is_modify_pwd) {
			showDialogModifyPwd(MainMenuActivity.this);
		}else{
			outTimerHandler.postDelayed(runnable, delay_time);
		}
	}
	private long delay_time = 3*60*1000;
	
	Handler outTimerHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 0:{
				showShortToast(R.string.please_login_again);
				openActivity(LoginActivity.class);
				defaultFinish();
			}break;
			default:
				break;
			}
			
		}
		
	};
	
	
	
	private Runnable runnable= new Runnable() {
		
		@Override
		public void run() {
			L.e("runnable", "runnable");
			Message msg = new Message();
			msg.what = 0;
			outTimerHandler.handleMessage(msg);
			outTimerHandler.postDelayed(runnable, delay_time);
		}
	};
	
	
	
	

	@Override
	protected void onPause() {
		super.onPause();
		outTimerHandler.removeCallbacks(runnable);
	}

	
	
	/**
	 * 
	 * @Description 弹出对话框修改密码
	 * @author Shawn
	 * @Time 2013-10-9 下午3:52:28
	 */
	private void showDialogModifyPwd(Context context) {
		L.e("showDialogModifyPwd", "showDialogModifyPwd()");
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_modify_pwd, null);
		/**
		 * 编辑框旧密码
		 */
		final EditText et_old_pwd = (EditText) view
				.findViewById(R.id.et_old_pwd);
		final EditText et_new_pwd = (EditText) view
				.findViewById(R.id.et_pwd_new);
		final EditText et_again_new_pwd = (EditText) view
				.findViewById(R.id.et_again_new_pwd);

		AlertDialog.Builder build = new Builder(context);
		build.setTitle(R.string.title_modify_pwd);
		build.setView(view);
		build.setPositiveButton(R.string.dia_btn_sure_txt,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						// Field field =
						// dialog.getClass().getSuperclass().getDeclaredField("mShowing");
						// field.setAccessible(true);
						// //设置mShowing值，欺骗android系统
						// field.set(dialog, false); //需要关闭的时候 将这个参数设置为true
						// 他就会自动关闭了

						String oldPwd = et_old_pwd.getText().toString();
						String newPwd = et_new_pwd.getText().toString();
						String newAgainPwd = et_again_new_pwd.getText()
								.toString();
						Field field;
						try {
							field = dialog.getClass().getSuperclass()
									.getDeclaredField("mShowing");
							field.setAccessible(true);
							// 设置mShowing值，欺骗android系统
							field.set(dialog, false); // 需要关闭的时候 将这个参数设置为true
														// 他就会自动关闭了

							// 首先判断是否为空
							if (!allPwdNotNull(oldPwd, newPwd, newAgainPwd)
									&& isValidPwd(newPwd, newAgainPwd)) {
								if(!oldPwd.equals(MyApplication.LOGIN_PWD)){
									showShortToast(R.string.pwd_incorrect);
									return;
								}
								if (newPwd.equals(newAgainPwd)) {
									String val_11 = MyApplication
											.getLineNum(getApplicationContext());
									String value_60 = MyApplication
											.getSixtyValue2(
													MyApplication.PHONE_NUM,
													MyApplication.IMSI,
													MyApplication.IEMI);
									// 组织修改密码报文
									byte[] request = ModifyPwdNode.buildMsg(
											val_11, oldPwd, value_60, newPwd);
									mRunningTask = new ModifyPwdTask(request);
									mRunningTask.execute();
									field.set(dialog, true);
								} else {
									showShortToast(R.string.pwd_not_the_same);
								}

							}
						} catch (NoSuchFieldException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
		build.setNegativeButton(R.string.dia_btn_canc_txt,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Field field;
						try {
							field = dialog.getClass().getSuperclass()
									.getDeclaredField("mShowing");
							field.setAccessible(true);
							// 设置mShowing值，欺骗android系统
							field.set(dialog, true); // 需要关闭的时候 将这个参数设置为true
														// 他就会自动关闭了
							// 保存已经修改了密码
							SharedPreferences sp = getSharedPreferences(
									MyApplication.SP_HAS_MODIFY_LOGIN_PWD,
									Activity.MODE_PRIVATE);
							boolean is_modify_pwd = sp.getBoolean(
									MyApplication.SP_HAS_MODIFY_LOGIN_PWD_KEY, false);
	
							if (!is_modify_pwd) {
								MainMenuActivity.this.finish();
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				});

		build.create().show();

	}
	
	
	/**
	 * 修改密码任务
	 * 
	 * @author ShawnXiao
	 * 
	 */
	private class ModifyPwdTask extends AsyncTask {
		private byte[] mRequest;

		public ModifyPwdTask(byte[] request) {
			mRequest = request;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.consume_processing);
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			SocketClient client = new SocketClient(mRequest, modifyPwdHandler);
			client.start();
		}

	}

	Handler modifyPwdHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RequestUtil.REQUEST_SUCCESS: {
				String hex_response = (String) msg.obj;
				if (hex_response != null && hex_response.length() > 0) {
					ModifyPwdNode node = ModifyPwdNode.parseMsg(hex_response);
					String code = getResources().getStringArray(
							R.array.return_code)[0];
					L.e("code:", code);
					L.e("node.get(39):", "" + node.get(39));
					if (node.get(39).equals(code)) {
						showShortToast(R.string.modify_login_pwd_success);
						
						// 保存已经修改了密码
						SharedPreferences sp = getSharedPreferences(
								MyApplication.SP_HAS_MODIFY_LOGIN_PWD,
								Activity.MODE_PRIVATE);
						sp.edit()
								.putBoolean(
										MyApplication.SP_HAS_MODIFY_LOGIN_PWD_KEY,
										true).commit();
						showShortToast(R.string.please_login_again);
						openActivity(LoginActivity.class);
						defaultFinish();
					} else {
						String err_msg = (String)node.get(63);
						if(err_msg!=null&&err_msg.length()>0){
							showShortToast(err_msg);
						}else{
							showShortToast(R.string.modify_login_pwd_faield);
						}
					}
				} else {
					showShortToast(R.string.modify_login_pwd_faield);
				}
				mAlertDialog.dismiss();
			}
				break;
			case RequestUtil.REQUEST_FAIELD: {
				showShortToast(R.string.modify_login_pwd_faield);
				mAlertDialog.dismiss();
			}
				break;
			}

		}
	};
	
	
	/**
	 * 
	 * @param newPwd
	 *            新密码
	 * @param newPwdAgain
	 *            新密码重复
	 * @return
	 */
	private boolean isValidPwd(String newPwd, String newPwdAgain) {
		boolean result = true;
		
		if(newPwd.length() < 6){
			showShortToast(R.string.pwd_alarm1);
			result = false;
		}
		
		if (newPwd.length() > 8) {
			showShortToast(R.string.pwd_alarm5);
			result = false;
		}
		if (StringUtil.gbk(newPwd)) {
			showShortToast(R.string.pwd_alarm2);
			result = false;
		}
		if (StringUtil.checkStrong(newPwd) < 2) {
			showShortToast(R.string.pwd_alarm3);
			result = false;
		}

		if (!newPwd.equals(newPwdAgain)) {
			showShortToast(R.string.pwd_alarm4);
			result = false;
		}

		return result;
	}
	
	
	/**
	 * 
	 * @Description 判断所有密码都不为空
	 * @author Shawn
	 * @Time 2013-10-9 下午4:14:58
	 * @param oldPwd
	 *            原始密码
	 * @param newPwd
	 *            新密码
	 * @param newAgainPwd
	 *            再次输入的新密码
	 * @return boolean
	 */
	private boolean allPwdNotNull(String oldPwd, String newPwd,
			String newAgainPwd) {
		boolean result = true;
		if (TextUtils.isEmpty(oldPwd)) {
			result = true;
			showShortToast(R.string.old_pwd_not_null);
		} else {
			if (TextUtils.isEmpty(newPwd)) {
				result = true;
				showShortToast(R.string.new_pwd_not_null);
			} else {
				if (TextUtils.isEmpty(newAgainPwd)) {
					result = true;
					showShortToast(R.string.new_pwd_sure_not_null);
				} else {
					result = false;
				}
			}
		}

		return result;
	}
	

	// 获取充值项目的定时任务
	private final long update_delay = 200;
	private final Timer timer = new Timer();
	private TimerTask timerTask = new TimerTask() {

		@Override
		public void run() {
			Message msg = new Message();
			msg.what = GET_RECHARGE_TASK;
			handler.sendMessage(msg);
		}
	};
	private long period = 14 * 24 * 60 * 60 * 1000;// 2周

	private final int GET_RECHARGE_TASK = 333;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			L.e("GET_RECHARGE_TASK:", "" + GET_RECHARGE_TASK);
			L.e("msg.what:", "" + GET_RECHARGE_TASK);
			switch (msg.what) {
			case GET_RECHARGE_TASK: {
				// 获取充值项
				String phone_info = MyApplication.getSixtyValue3(
						MyApplication.PHONE_NUM, MyApplication.IMSI,
						MyApplication.IEMI);
				// 这里组织获取充值字段报文
				byte[] request = GetRechargeKeyNode
						.buildGetRechargeKeyMsg(phone_info);
				mRunningTask = new GetRechargeKeyTask(request);
				mRunningTask.execute();
			}
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 获取充值字段任务
	 * 
	 * @author ShawnXiao
	 * 
	 */

	private class GetRechargeKeyTask extends AsyncTask {
		private byte[] mRequest;

		public GetRechargeKeyTask(byte[] request) {
			mRequest = request;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// showProgressDialog(R.string.handing_process);
		};

		@Override
		protected Object doInBackground(Object... arg0) {
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			SocketClient client = new SocketClient(mRequest,
					getRechargeKeyHandler);
			client.start();
		}

	}

	// 获取充值字段处理助手
	private Handler getRechargeKeyHandler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case RequestUtil.REQUEST_SUCCESS: {
				String hex_response = (String) msg.obj;
				if (hex_response != null && hex_response.length() > 0) {
					GetRechargeKeyNode node = GetRechargeKeyNode
							.parseMsg(hex_response);

					String code = getResources().getStringArray(
							R.array.return_code)[0];
					L.e("code:", code);
					L.e("node.getReturn_code():", "" + node.getReturn_code());
					if (node.getReturn_code().equals(code)) {
						L.e("获取充值对象成功", "获取充值对象成功");
						// 1、把最后获取充值项的时间记录
						SimpleDateFormat format = new SimpleDateFormat(
								"MM-dd HH:mm:ss");
						String update_recharge_time = format.format(new Date());
						node.setUpdate_recharge_time(update_recharge_time);
						// 2、将这个充值对象序列化为文件
						L.e("fileHelper==null", "" + (fileHelper == null));
						fileHelper.serialObject(
								MyApplication.RECHARGE_ITEM_FILE_NAME, node);
					}

				}
			}
				break;
			case RequestUtil.REQUEST_FAIELD: {

			}
				break;
			}
			// mAlertDialog.dismiss();
		};
	};

	/**
	 * 返回按钮按下次数
	 */
	private int back_key_click_count = 0;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		timer.cancel();
		outTimerHandler.removeCallbacks(runnable);
	}

	@Override
	protected void onResume() {
		super.onResume();
		back_key_click_count = 0;
		// if(MyApplication.myAuthority==MyApplication.AUTHORITY_EMPLOYEE){
		// mAdapter.removeMenu(getResources().getStringArray(R.array.main_menu)[1]);
		// }
		// mListView.setAdapter(mAdapter);
		outTimerHandler.postDelayed(runnable, delay_time);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (back_key_click_count++) {
			case 0: {
				showShortToast(R.string.back_againe_to_exit);
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						back_key_click_count = 0;
					}
				}, 3000);
			}
				break;
			case 1: {
//				defaultFinish();
				AppManager.getAppManager().AppExit(getApplicationContext());
			}
				break;
			default:
				break;
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 
	 * @Descriptio 菜单适配器
	 * @author Shawn
	 * @Time 2013-8-19
	 */
	private class MyMenuAdapter extends BaseAdapter {
		private Context mContext;
		private LayoutInflater mInflater;
		private List<String> mMenus;

		public MyMenuAdapter(Context context) {
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
			mMenus = new ArrayList<String>();

		}

		public void addMenu(String menu) {
			mMenus.add(menu);
		}

		public void removeMenu(String menu) {
			mMenus.remove(menu);
		}

		public void addMenu(String[] menus) {
			if (menus != null && menus.length > 0) {
				for (String menu : menus) {
					mMenus.add(menu);
				}
			}
		}

		@Override
		public int getCount() {
			return mMenus.size();
		}

		@Override
		public Object getItem(int position) {
			return mMenus.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			String menu = null;
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.menu_list_item, null);
				holder.btn = (Button) convertView.findViewById(R.id.menu_btn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (mMenus != null && mMenus.size() > 0) {
				menu = mMenus.get(position);
				holder.btn.setText(menu);
			}

			holder.btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					L.i("holder.btn", "onClick");
					String select = ((Button) v).getText().toString();
					L.i("select:", select);
					L.i("mMainMenus:" + "[" + position + "]",
							mMainMenus[position]);
					Log.i("select.equals(mMainMenus" + "[+" + position + "+]"
							+ ")", "" + (select.equals(mMainMenus[position])));

					if (select.equals(mMainMenus[0])) {
						// 充值
						// 跳转充值扫卡界面
						Intent intent_recharge = new Intent(
								MainMenuActivity.this, RechargeActivity2.class);
						MainMenuActivity.this.startActivity(intent_recharge);
						switchActivityAnimation();
					} else if (select.equals(mMainMenus[1])) {
						// 充值撤销
						// 跳转查询充值列表界面
						Intent intent_cancle_consume = new Intent(
								MainMenuActivity.this,
								ConsumeDataCollectActivity2.class);
						intent_cancle_consume.putExtra(
								ConsumeDataCollectActivity2.REQUEST_TYPE,
								ConsumeDataCollectActivity2.CHE_XIAO);
						MainMenuActivity.this
								.startActivity(intent_cancle_consume);
						switchActivityAnimation();
						// Intent intent_recharge_cancle = new
						// Intent(MainMenuActivity.this,RechargeCancleActivity.class);
						// MainMenuActivity.this.startActivity(intent_recharge_cancle);
						// switchActivityAnimation();
					} else if (select.equals(mMainMenus[2])) {
						// 查询卡片可用余额
						// 跳转查询余额界面
						Intent intent_query_remain = new Intent(
								MainMenuActivity.this,
								QueryRemainActivity.class);
						MainMenuActivity.this
								.startActivity(intent_query_remain);
						switchActivityAnimation();
					} else if (select.equals(mMainMenus[3])) {
						// 查询充值明细
						// 如果是管理员，则跳转管理员界面
						if (MyApplication.myAuthority == MyApplication.AUTHORITY_MANAGER) {
							// 跳转查询条件界面、查询自己的、查询所有员工的、查询某个员工的
							Intent query_condi = new Intent(
									MainMenuActivity.this,
									QueryCondiActivity.class);
							MainMenuActivity.this.startActivity(query_condi);
							switchActivityAnimation();
						} else {
							// 跳转查询充值列表界面
							Intent intent_cancle_consume = new Intent(
									MainMenuActivity.this,
									QueryRechargeDateSelectActivity.class);
							MainMenuActivity.this
									.startActivity(intent_cancle_consume);
							switchActivityAnimation();
						}
						// //跳转查询充值明细界面
						// Intent intent_check_information = new
						// Intent(MainMenuActivity.this,CheckInformationActivity.class);
						// MainMenuActivity.this.startActivity(intent_check_information);
						// switchActivityAnimation();
					} 
//					else if (select.equals(mMainMenus[4])) {
//						// 开卡
//						Intent intent_open_card = new Intent(
//								MainMenuActivity.this, OpenCardActivity.class);
//						MainMenuActivity.this.startActivity(intent_open_card);
//						switchActivityAnimation();
//					}
					else if (select.equals(mMainMenus[4])) {
						//修改密码
						showDialogModifyPwd(MainMenuActivity.this);
					}
				}
			});

			return convertView;
		}

		class ViewHolder {
			Button btn;
		}

	}

	//
	// /**
	// *
	// * @Description �����л�Ч��
	// * @author Shawn
	// * @Time 2013-8-23 ����4:56:36
	// */
	// private void switchActivityAnimation(){
	// overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	// // overridePendingTransition(R.anim.slide_right_in,
	// R.anim.slide_right_out);
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 0, 0, getString(R.string.confi_info));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0: {
			// showShortToast("�ҵĲ˵������");
			openActivity(ConfigurationActivity.class);
			return true;
		}

		}
		return false;
	}

}
