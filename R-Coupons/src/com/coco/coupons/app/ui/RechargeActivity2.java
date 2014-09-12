package com.coco.coupons.app.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coco.android.socket.SocketClient;
import com.coco.android.util.L;
import com.coco.android.util.YTFileHelper;
import com.coco.coupons.app.BaseActivity;
import com.coco.coupons.app.MyApplication;
import com.coco.coupons.app.R;
import com.coco.coupons.app.bean.GetRechargeKeyNode;
import com.coco.coupons.app.bean.QueryCustInfoNode;
import com.coco.coupons.app.bean.RechargeNode;
import com.coco.coupons.app.bean.RequestUtil;
import com.google.zxing.CaptureActivity;

/**
 * 
 * @Descriptio 充值界面
 * @author Shawn
 * @Time 2013-8-30
 */
public class RechargeActivity2 extends BaseActivity {

	public static final String REQUEST_SUM_OF_CONSUME = "REQUEST_SUM_OF_CONSUME";
	/**
	 * 卡号编辑框
	 */
	private EditText et_consume_card_num;
	/**
	 * 扫一扫按钮
	 */
	private Button btn_scan;

	/**
	 * 客户信息容器
	 */
	private LinearLayout panel_cust_info;

	/**
	 * 客户编号文本
	 */
	private TextView tv_cust_info_cust_num_value;

	/**
	 * 客户名称文本
	 */
	private TextView tv_cust_info_cust_name_value;

	/**
	 * 余额文本
	 */
	private TextView tv__cust_info_cust_yuer_value;

	/**
	 * 确认充值按钮
	 */
	private Button btn_consume_sure;

	/**
	 * 扫一扫请求指令
	 */
	private static final int REQUEST_GET_CONDE = 0;

	/**
	 * 返回按钮
	 */
	private Button btn_back;

	/**
	 * 获取的数量
	 */
	private String str_count;

	/**
	 * 确认按钮
	 */
	private Button btn_scan_sure;

	/**
	 * 手机号
	 */
	private TextView tv_cust_phone_num_value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_recharge2);
		// 初始化控件
		initView();
		// 为控件设置监听器
		setUpViewListener();
	}

	/**
	 * 
	 * @Description 为控件设置监听器
	 * @author Shawn
	 * @Time 2013-8-26
	 */
	private void setUpViewListener() {
		btn_back.setOnClickListener(new BackListener());
		btn_scan.setOnClickListener(new ScannerListener());

		btn_consume_sure.setOnClickListener(new RechargeListener());

		btn_scan_sure.setOnClickListener(new BtnScaneSureListener());
	}

	/**
	 * 确认卡号按钮监听器
	 * 
	 * @author shawn
	 * 
	 */
	private class BtnScaneSureListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			panel_cust_info.setVisibility(View.GONE);
			String card_num = et_consume_card_num.getText().toString();
			StringBuilder sb = new StringBuilder();
			if (card_num == null || card_num.length() == 0) {
				showShortToast(R.string.card_num_error);
				return;
			} else if (card_num.length() == 8) {
				sb.append(MyApplication.CARD_PREFIX);
				sb.append(card_num);
			} else if (card_num.length() == 16) {
				sb.append(card_num);
			} else {
				showShortToast(R.string.card_num_error);
				return;
			}
			L.e("card_num:", sb.toString());

			// 组织报文查询客户信息:根据卡号查询，客户编号，客户名称，余额
			String val_11 = MyApplication.getLineNum(getApplicationContext());
			// String val_60 =
			// MyApplication.getSixtyValue(MyApplication.COMM_NUM,
			// MyApplication.TERM_NUM);
			String value_60 = MyApplication.getSixtyValue2(
					MyApplication.PHONE_NUM, MyApplication.IMSI,
					MyApplication.IEMI);

			// 组织报文根据卡号查询客户名称、客户编号、余额
			byte[] request = QueryCustInfoNode.buildMsg(sb.toString(), val_11,
					value_60);

			mRunningTask = new QueryCustInfoTask(request);
			mRunningTask.execute();
		}

	}

	/**
	 * 
	 * @Descriptio 返回按钮监听器
	 * @author Shawn
	 * @Time 2013-8-23
	 */
	private class BackListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			defaultFinish();
		}

	}

	/**
	 * 
	 * @Descriptio 扫一扫按钮监听器
	 * @author Shawn
	 * @Time 2013-8-26
	 */
	private class ScannerListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			panel_cust_info.setVisibility(View.GONE);
			// 跳转扫一扫页面
			Intent intent_query = new Intent(RechargeActivity2.this,
					CaptureActivity.class);
			startActivityForResult(intent_query, REQUEST_GET_CONDE);
		}

	}

	/**
	 * 
	 * @Description 卡号金额是否为空
	 * @author Shawn
	 * @Time 2013-8-26
	 * @param card_num
	 *            卡号
	 * @param sum_of_consume
	 *            充值金额
	 * @return boolean 卡号和充值金额为空与否
	 */
	private boolean cardNumAndSumOfConsumeIsNull(String card_num,
			String sum_of_consume) {
		boolean result = true;
		if (TextUtils.isEmpty(card_num)) {
			result = true;
			showShortToast(R.string.consume_card_num_can_not_null);
		} else {
			if (TextUtils.isEmpty(sum_of_consume)) {
				result = true;
				showShortToast(R.string.sum_of_consume_can_not_null);
			} else {
				result = false;
			}
		}
		return result;
	}

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
			showProgressDialog(R.string.handing_process);
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
					// 卡号
					String card_num = et_consume_card_num.getText().toString();
					StringBuilder sb = new StringBuilder();
					if (card_num == null || card_num.length() == 0) {
						showShortToast(R.string.card_num_error);
						return;
					} else if (card_num.length() == 8) {
						sb.append(MyApplication.CARD_PREFIX);
						sb.append(card_num);
					} else if (card_num.length() == 16) {
						sb.append(card_num);
					} else {
						showShortToast(R.string.card_num_error);
						return;
					}
					L.e("card_num:", sb.toString());

					// 进入充值字段获取页面
					Intent intent = new Intent(RechargeActivity2.this,
							RechargeActivity.class);

					Bundle bundle = new Bundle();
					bundle.putSerializable(RechargeActivity.KEY_RECHARGE_KEY,
							node);
					// 将卡号传进去
					bundle.putString(RechargeActivity.KEY_CARD_NUM,
							sb.toString());
					intent.putExtras(bundle);
					RechargeActivity2.this.startActivity(intent);
					switchActivityAnimationIn();
				}
			}
				break;
			case RequestUtil.REQUEST_FAIELD: {
				
			}
				break;
			}
			mAlertDialog.dismiss();
		};
	};

	/**
	 * 
	 * @Descriptio 充值按钮监听器
	 * @author Shawn
	 * @Time 2013-8-26
	 */
	private class RechargeListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (!L.RechargeDebug) {
				String card_num = et_consume_card_num.getText().toString();
				if (TextUtils.isEmpty(card_num)) {
					showShortToast(R.string.card_num_can_not_null);
					return;
				}

				// 卡号
				// String card_num = et_consume_card_num.getText().toString();
				StringBuilder sb = new StringBuilder();
				if (card_num == null || card_num.length() == 0) {
					showShortToast(R.string.card_num_error);
					return;
				} else if (card_num.length() == 8) {
					sb.append(MyApplication.CARD_PREFIX);
					sb.append(card_num);
				} else if (card_num.length() == 16) {
					sb.append(card_num);
				} else {
					showShortToast(R.string.card_num_error);
					return;
				}
				L.e("card_num:", sb.toString());
				// 如果本地存储了充值字段，则使用充值字段直接把充值字段对象传递到下个界面

				GetRechargeKeyNode node = (GetRechargeKeyNode) fileHelper
						.getInstance().deSerialObject(
								MyApplication.RECHARGE_ITEM_FILE_NAME);
				if (node != null) {
					// 进入充值字段获取页面
					Intent intent = new Intent(RechargeActivity2.this,
							RechargeActivity.class);

					Bundle bundle = new Bundle();
					bundle.putSerializable(RechargeActivity.KEY_RECHARGE_KEY,
							node);
					// 将卡号传进去
					bundle.putString(RechargeActivity.KEY_CARD_NUM,
							sb.toString());
					intent.putExtras(bundle);
					RechargeActivity2.this.startActivity(intent);
					switchActivityAnimationIn();

				} else {

					String phone_info = MyApplication.getSixtyValue3(
							MyApplication.PHONE_NUM, MyApplication.IMSI,
							MyApplication.IEMI);
					// 这里组织获取充值字段报文
					byte[] request = GetRechargeKeyNode
							.buildGetRechargeKeyMsg(phone_info);
					mRunningTask = new GetRechargeKeyTask(request);
					mRunningTask.execute();
				}

				// //进入充值字段获取页面
				// Intent intent = new
				// Intent(RechargeActivity2.this,RechargeActivity.class);
				// //将卡号传进去
				// intent.putExtra(RechargeActivity.KEY_CARD_NUM, card_num);
				// RechargeActivity2.this.startActivity(intent);
				// switchActivityAnimationIn();
			} else {
				String card_num = et_consume_card_num.getText().toString();
				// String sum_of_consume =
				// et_sum_of_consume.getText().toString();
				String sum_of_consume = "28";

				if (!cardNumAndSumOfConsumeIsNull(card_num, sum_of_consume)) {
					// 组装充值报文
					String val_11 = MyApplication
							.getLineNum(getApplicationContext());
					// String val_60 =
					// MyApplication.getSixtyValue(MyApplication.COMM_NUM,
					// MyApplication.TERM_NUM);
					String value_60 = MyApplication.getSixtyValue2(
							MyApplication.PHONE_NUM, MyApplication.IMSI,
							MyApplication.IEMI);
					byte[] request = RechargeNode.buildRechareMsg(card_num,
							sum_of_consume, val_11, value_60, null);

					mRunningTask = new ConsumeSureTask(card_num,
							sum_of_consume, request);
					mRunningTask.execute(MyApplication.CONSUME_URL);

				}
			}

		}

	}

	/**
	 * 确定充值任务
	 * 
	 * @author ShawnXiao
	 * 
	 */
	private class ConsumeSureTask extends AsyncTask {
		private String mCardNum;
		private String mSumOfConsume;
		private byte[] mRequest;

		public ConsumeSureTask(String card_num, String sum_of_consume,
				byte[] request) {
			mCardNum = card_num;
			mSumOfConsume = sum_of_consume;
			mRequest = request;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.consume_processing);
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			SocketClient client = new SocketClient(mRequest, responseHandler);
			client.start();
		}

	}

	Handler responseHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RequestUtil.REQUEST_SUCCESS: {
				String hex_response = (String) msg.obj;
				if (hex_response != null && hex_response.length() > 0) {
					// 解析报文
					RechargeNode node = RechargeNode
							.parseRechargeNode(hex_response);
					String code = getResources().getStringArray(
							R.array.return_code)[0];
					L.e("code:", code);
					L.e("node.get(39):", "" + node.get(39));
					if (node.get(39).equals(code)) {
						showShortToast(R.string.recharge_succ);
						openActivity(MainMenuActivity.class);
						defaultFinish();
					} else {
						showShortToast(R.string.recharge_faield);
					}

				} else {
					showShortToast(R.string.recharge_faield);
				}
				mAlertDialog.dismiss();

			}
				break;
			case RequestUtil.REQUEST_FAIELD: {
				showShortToast(R.string.recharge_faield);
				mAlertDialog.dismiss();
			}
				break;
			}
		}
	};

	/**
	 * 初始化控件
	 * 
	 * @Description TODO
	 * @author Shawn
	 * @Time 2013-8-26
	 */
	private void initView() {

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.tv_title)).setText(R.string.title_fun_1);

		et_consume_card_num = (EditText) findViewById(R.id.et_consume_card_num);
		btn_scan = (Button) findViewById(R.id.btn_scan);
		btn_scan_sure = (Button) findViewById(R.id.btn_scan_sure);
		panel_cust_info = (LinearLayout) findViewById(R.id.panel_cust_info);
		panel_cust_info.setVisibility(View.GONE);

		tv_cust_info_cust_num_value = (TextView) findViewById(R.id.tv_cust_info_cust_num_value);
		tv_cust_info_cust_name_value = (TextView) findViewById(R.id.tv_cust_info_cust_name_value);
		tv__cust_info_cust_yuer_value = (TextView) findViewById(R.id.tv__cust_info_cust_yuer_value);
		tv_cust_phone_num_value = (TextView) findViewById(R.id.tv_cust_phone_num_value);

		// et_sum_of_consume = (EditText) findViewById(R.id.et_sum_of_consume);

		btn_consume_sure = (Button) findViewById(R.id.btn_consume_sure);

		// Intent intent = getIntent();
		// str_count = intent.getExtras().getString(REQUEST_SUM_OF_CONSUME);
		// L.i("str_count:", ""+str_count);
		// et_sum_of_consume.setText(str_count);

		if (L.RechargeDebug2) {
			et_consume_card_num.setText("9009320100001290");
			// et_consume_card_num.setText("9009320100011224");
			// et_sum_of_consume.setText("10");

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_GET_CONDE && resultCode == RESULT_OK) {
			String result = data.getExtras().getString("result");
			L.e("result", "" + result);
			if (TextUtils.isEmpty(result)) {
				showShortToast(R.string.scan_retry);
				return;
			}
			et_consume_card_num.setText(result);

			// //组织报文查询客户信息:根据卡号查询，客户编号，客户名称，余额
			// String val_11 =
			// MyApplication.getLineNum(getApplicationContext());
			// // String val_60 =
			// MyApplication.getSixtyValue(MyApplication.COMM_NUM,
			// MyApplication.TERM_NUM);
			// String value_60 =
			// MyApplication.getSixtyValue2(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
			//
			// //组织报文根据卡号查询客户名称、客户编号、余额
			// byte[] request = QueryCustInfoNode.buildMsg(result, val_11,
			// value_60);
			//
			// mRunningTask = new QueryCustInfoTask(request);
			// mRunningTask.execute();

		}
	}

	/**
	 * 查询客户信息(按卡号)任务
	 * 
	 * @author ShawnXiao
	 * 
	 */
	private class QueryCustInfoTask extends AsyncTask {
		private byte[] mRequest;

		public QueryCustInfoTask(byte[] request) {
			mRequest = request;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.consume_processing);
		}

		@Override
		protected Object doInBackground(Object... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			SocketClient client = new SocketClient(mRequest,
					queryCustInfoHandler);
			client.start();
		}
	}

	Handler queryCustInfoHandler = new Handler() {
		public void handleMessage(Message msg) {
			// mAlertDialog.dismiss();
			switch (msg.what) {
			case RequestUtil.REQUEST_SUCCESS: {
				String hex_response = (String) msg.obj;
				if(hex_response!=null&&hex_response.length()>0){
					QueryCustInfoNode node = QueryCustInfoNode
							.parseMsg(hex_response);
					String code = getResources()
							.getStringArray(R.array.return_code)[0];
					L.e("code:", code);
					L.e("node.get(39):", "" + node.get(39));
					if (node.get(39).equals(code)) {
	
						tv_cust_info_cust_num_value.setText(node.getmCustNum());
						tv_cust_info_cust_name_value.setText(node.getmCustName());
	
						tv_cust_phone_num_value.setText(node.getmCustPhoneNum());
						L.e("node.getZhangmian_yuer():",
								"" + node.getZhangmian_yuer());
						tv__cust_info_cust_yuer_value.setText(node
								.getZhangmian_yuer()
								+ getResources().getString(R.string.yuan));
						// 客户信息容器可见
						panel_cust_info.setVisibility(View.VISIBLE);
	
					} else {
						String err_msg = (String)node.get(63);
						if(err_msg!=null&&err_msg.length()>0){
							showShortToast(err_msg);
						}else{
							showShortToast(R.string.card_not_exist_or_opend);
						}
						
					}
				}else{
					showShortToast(R.string.get_cust_info_faield2);
				}
				mAlertDialog.dismiss();
			}
				break;
			case RequestUtil.REQUEST_FAIELD: {
				showShortToast(R.string.get_cust_info_faield2);
				mAlertDialog.dismiss();
			}
				break;
			}
		}
	};
}
