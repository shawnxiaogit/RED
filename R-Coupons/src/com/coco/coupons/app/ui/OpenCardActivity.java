package com.coco.coupons.app.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coco.android.socket.SocketClient;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.coupons.app.BaseActivity;
import com.coco.coupons.app.MyApplication;
import com.coco.coupons.app.R;
import com.coco.coupons.app.bean.CustomerInfoListNode;
import com.coco.coupons.app.bean.OpenCardNode;
import com.coco.coupons.app.bean.RequestUtil;
import com.google.zxing.CaptureActivity;

/**
 * 
 * @Descriptio 开卡界面
 * @author Shawn
 * @Time 2013-9-26  
 */
public class OpenCardActivity extends BaseActivity{
	
	
	/**
	 * 卡号编辑框
	 */
	private EditText et_open_card_num;
	
	
	/**
	 * 扫一扫按钮
	 */
	private Button btn_open_scane;
	
	/**
	 * 客户编号编辑框
	 */
	private EditText et_cust_num;
	/**
	 * 查询客户信息按钮
	 */
	private Button btn_query_cust_info;
	
	/**
	 * 手机号码编辑框
	 */
	private EditText et_open_phone_num;
	
//	/**
//	 * 经销商代码编号
//	 */
//	private EditText et_angency_code;
	
	
	/**
	 * 开卡按钮
	 */
	private Button btn_open_card;
	/**
	 * 返回按钮
	 */
	private Button btn_back;
	/**
	 * 获取二维码指令
	 */
	private static final int REQUEST_GET_CONDE = 0;
	
	/**
	 * 客户名称容器
	 */
	private RelativeLayout panel_cust_name;
	
	private TextView tv_cast_name_value;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open_card);
		
		//初始化控件
		initView();
		//为控件设置监听器
		setUpViewListener();
		
	}
	//初始化控件
	private void initView(){
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_6);
		
		
		
		
		et_open_card_num = (EditText) findViewById(R.id.et_open_card_num);
		et_open_card_num.setEnabled(false);
		
		btn_open_scane = (Button) findViewById(R.id.btn_open_scane);
		
		et_cust_num = (EditText) findViewById(R.id.et_cust_num);
		
		
		panel_cust_name = (RelativeLayout) findViewById(R.id.panel_cust_name);
		panel_cust_name.setVisibility(View.GONE);
		tv_cast_name_value = (TextView) findViewById(R.id.tv_cast_name_value);
		
		
		btn_query_cust_info = (Button) findViewById(R.id.btn_query_cust_info);
		
		et_open_phone_num = (EditText) findViewById(R.id.et_open_phone_num);
		
//		et_angency_code = (EditText) findViewById(R.id.et_angency_code);
		
		btn_open_card = (Button) findViewById(R.id.btn_open_card);
		
		if(L.OpenCardDebug){
			et_open_card_num.setText("9009320100011232");
			et_cust_num.setText("0503821721");
			et_open_phone_num.setText("18018623428");
//			et_angency_code.setText("0503821721");
		}
		
	}
	//为控件设置监听器
	private void setUpViewListener(){
		btn_back.setOnClickListener(new BackListener());
		
		btn_open_scane.setOnClickListener(new ScannerListener());
		
		btn_query_cust_info.setOnClickListener(new GetCustInfoListener());
		
		btn_open_card.setOnClickListener(new OpenCardListener());
	}
	
	
	
	private String getSixtOneValue(String value){
		int len=value.length();
		StringBuilder sb = new StringBuilder();
		if (len >= 0 && len <= 9) {
			sb.append("00");
		} else if (len > 9 && len <= 99) {
			sb.append("0");
		}
		sb.append(len);
		sb.append(value);
		return sb.toString();
	}
	
	/**
	 * 显示客户信息对话框
	 */
	private void showCustInfoDialog(Context context){
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(R.string.cust_info_title);
		
//		View
		
	}
	
	
	/**
	 * 查询客户信息任务
	 * @author ShawnXiao
	 *
	 */
	private class GetCustInfoTask extends AsyncTask{
		private String mCustCode;
		private byte[] mRequest;
		public GetCustInfoTask(String cust_code,byte[] request){
			mCustCode = cust_code;
			mRequest = request;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.getting_cust_code);
		}
		
		@Override
		protected Object doInBackground(Object... arg0) {
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			SocketClient client = new SocketClient(mRequest,custInfoHandler);
			client.start();
		}
		
	}
	
	//查询客户信息处理类
	private Handler custInfoHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mAlertDialog.dismiss();
		}
		
	}; 
	
	/**
	 * 获取客户信息监听器
	 * @author ShawnXiao
	 *
	 */
	private class GetCustInfoListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			
			
			String cust_code = et_cust_num.getText().toString();
			
			//判断客户编号是否为空
			if(TextUtils.isEmpty(cust_code)){
				showShortToast(R.string.cust_code_no_null);
				return;
			}
			
			String val_11 = MyApplication.getLineNum(getApplicationContext());
			String value_60 = MyApplication.getSixtyValue2(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
			
			String val_61 = getSixtOneValue(cust_code);
			
			byte[] request =  CustomerInfoListNode.buildMsg(val_11, value_60,val_61);
			mRunningTask = new GetCustomerInfoTask(request);
			mRunningTask.execute(MyApplication.LOGIN_URL);
		}
		
	}
	
	
	/**
	 * 获取验证码任务
	 * @author Shawn
	 *
	 */
	private class GetCustomerInfoTask extends AsyncTask{
		private byte[] mRequest;
		public GetCustomerInfoTask(byte[] request){
			mRequest = request;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.getting_cust_info);
		}
		@Override
		protected Object doInBackground(Object... params) {
			return null;
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			SocketClient client = new SocketClient(mRequest,responseHandler2);
			client.start();
		}
	}
	
	/**
	 * 查询客户信息处理助手
	 */
	Handler responseHandler2= new Handler(){
		public void handleMessage(Message msg) {
			
			switch(msg.what){
			case RequestUtil.REQUEST_SUCCESS:{
				String hex_response = (String) msg.obj;
				if(hex_response!=null&&hex_response.length()>0){
//					//解析返回报文
					CustomerInfoListNode node = CustomerInfoListNode.parseMsg(hex_response);
					String code = getResources().getStringArray(R.array.return_code)[0];
					L.e("code:", code);
					L.e("node.get(39)==null:",""+(node.get(39)==null));
					
					
					if(node.get(39).equals(code)){
						//设置客户编辑框的值为获取到的客户名称
//						String custName = node.getmCustName();
//						if(custName!=null&&custName.length()>0){
//							L.e("custName:", custName);
//							panel_cust_name.setVisibility(View.VISIBLE);
//							tv_cast_name_value.setText(custName);
//						}
						
						//开卡成功、展示获取的客户信息
						Intent intent = new Intent(OpenCardActivity.this,CustInfoActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable(CustInfoActivity.DATA_KEY,node);
						intent.putExtras(bundle);
						OpenCardActivity.this.startActivity(intent);
						
					}else{
						String err_msg = (String)node.get(63);
						if(err_msg!=null&&err_msg.length()>0){
							showShortToast(err_msg);
						}else{
							showShortToast(R.string.get_cust_info_faield);
						}
					}
					mAlertDialog.dismiss();
				}
			}break;
			case RequestUtil.REQUEST_FAIELD:{
				showShortToast(R.string.get_synt_code_faield);
				mAlertDialog.dismiss();
			}break;
			}
			
		}
	};
	
	
	/**
	 * 
	 * @Descriptio 开卡按钮监听器
	 * @author Shawn
	 * @Time 2013-9-29  
	 */
	private class OpenCardListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			final String card_num = et_open_card_num.getText().toString();
			final String cust_num = et_cust_num.getText().toString();
			final String phone_num = et_open_phone_num.getText().toString();
//			final String angency_code = et_angency_code.getText().toString();
			
			if(TextUtils.isEmpty(card_num)){
				showShortToast(R.string.please_scan_get_card_num);
				return;
			}
			if(TextUtils.isEmpty(cust_num)){
				showShortToast(R.string.cust_code_no_null);
				return;
			}
			if(TextUtils.isEmpty(phone_num)){
				showShortToast(R.string.toast_phone_num_not_null);
				return;
			}
//			if(TextUtils.isEmpty(angency_code)){
//				showShortToast(R.string.angency_code_not_null);
//				return;
//			}
			
			
			
			
			//弹出温馨提示对话框
			showAlertDialog(getResources().getString(R.string.warn_prompt),
					getResources().getString(R.string.sure_to_open_card),
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss();
							//组装开卡报文
							String val_11 = MyApplication.getLineNum(getApplicationContext());
							String value_60 = MyApplication.getSixtyValue2(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
							String val_61 = getSixtyOneVlaue(cust_num,phone_num,"");
							
							byte[] request = OpenCardNode.buidOpenCardMsg(card_num,val_11,value_60,val_61);
							mRunningTask = new OpenCardTask(card_num,phone_num,request);
							mRunningTask.execute(MyApplication.OPEN_CARD_URL);

						}
					}, 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}, null);
		}
		
	}
	
	
	/**
	 * 获取61域的值
	 * @return
	 */
	private String getSixtyOneVlaue(String custNum,String phone_num,String angency_code){
		
		StringBuilder sb3 = new StringBuilder();
		sb3.append(custNum);
		sb3.append(phone_num);
		sb3.append(angency_code);
		String data  = sb3.toString();
		int len = data.length();
		System.out.println("len:"+len);
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
	 * @Descriptio 开卡任务
	 * @author Shawn
	 * @Time 2013-9-29  
	 */
	private class OpenCardTask extends AsyncTask{
		private String mCardNum;
		private String mPhoneNum;
		private byte[] mRequest;
		public OpenCardTask(String card_num,String phone_num,byte[] request){
			mCardNum = card_num;
			mPhoneNum = phone_num;
			mRequest = request;
		}
		@Override
		protected Object doInBackground(Object... params) {
			return null;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.processing);
		}
		
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			SocketClient client = new SocketClient(mRequest,openCardHanlder);
			client.start();
		}
		
	}
	
	/**
	 * 开卡处理助手类
	 */
	Handler openCardHanlder= new Handler(){
		public void handleMessage(Message msg) {
//			mAlertDialog.dismiss();
			switch(msg.what){
			case RequestUtil.REQUEST_SUCCESS:{
				String hex_response = (String) msg.obj;
				if(hex_response!=null&&hex_response.length()>0){
					//解析返回报文
					OpenCardNode node = OpenCardNode.parseOpenCardMsg(hex_response);
					String code = getResources().getStringArray(R.array.return_code)[0];
					L.e("code:", code);
					L.e("node.get(39)==null:",""+(node.get(39)==null));
					if(node.get(39).equals(code)){
						showShortToast(R.string.open_card_succ);
						openActivity(MainMenuActivity.class);
						defaultFinish();
					}else{
						showShortToast(R.string.open_card_faield);
					}
					
				}else{
					showShortToast(R.string.open_card_faield);
				}
				mAlertDialog.dismiss();
			}break;
			case RequestUtil.REQUEST_FAIELD:{
				showShortToast(R.string.open_card_faield);
				mAlertDialog.dismiss();
				
			}break;
			}
		}
	};
	
	
	/**
	 * 
	 * @Descriptio 返回按钮监听器
	 * @author Shawn
	 * @Time 2013-8-23  
	 */
	private class BackListener implements OnClickListener{

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
	private class ScannerListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			//获取二维码
			Intent intent_query = new Intent(OpenCardActivity.this,CaptureActivity.class);
			startActivityForResult(intent_query,REQUEST_GET_CONDE);
		}
		
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_GET_CONDE && resultCode == RESULT_OK){
			String result = data.getExtras().getString("result");
			L.e("result", ""+result);
			if(TextUtils.isEmpty(result)){
				showShortToast(R.string.scan_retry);
				return;
			}
			et_open_card_num.setText(result);
		}
	}
}
