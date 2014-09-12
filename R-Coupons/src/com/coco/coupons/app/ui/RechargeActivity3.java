package com.coco.coupons.app.ui;


import java.io.UnsupportedEncodingException;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coco.android.socket.SocketClient;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.coupons.app.BaseActivity;
import com.coco.coupons.app.MyApplication;
import com.coco.coupons.app.R;
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
public class RechargeActivity3 extends BaseActivity{
	
	/**
	 * 充值金额
	 */
	public static final String REQUEST_SUM_OF_CONSUME = "REQUEST_SUM_OF_CONSUME";
	
	/**
	 * 卡号
	 */
	public static final String KEY_CARD_NUM = "KEY_CARD_NUM";
	/**
	 * 充值的字段
	 */
	public static final String KEY_RECHARGE_KEYS = "KEY_RECHARGE_KEYS";
	
	/**
	 * 卡号文本
	 */
	private TextView tv_card_num_value;

	/**
	 * 充值金额文本
	 */
	private TextView tv_recharge_count_value;
	
	/**
	 * 充值确认按钮
	 */
	private Button btn_sure_recharge;
	
	/**
	 * 返回按钮
	 */
	private Button btn_back;
	
	/**
	 * 上个页面获取的卡号
	 */
	private String mGetCardNum;
	
	/**
	 * 上个页面获取的充值金额
	 */
	private String mGetRechargeCount;
	
	/**
	 * 充值字段
	 */
	private String mRechargeKeys;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_recharge3);
		//初始化控件
		initView();
		//为控件设置监听器
		setUpViewListener();
	}
	
	/**
	 * 
	 * @Description 为控件设置监听器
	 * @author Shawn
	 * @Time 2013-8-26 
	 */
	private void setUpViewListener(){
		btn_back.setOnClickListener(new BackListener());
		btn_sure_recharge.setOnClickListener(new RechargeListener());
	}
	
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
	 * @Descriptio 充值按钮监听器
	 * @author Shawn
	 * @Time 2013-8-26  
	 */
	private class RechargeListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			
			showAlertDialog(getResources().getString(R.string.warnming_prompt), 
					getResources().getString(R.string.sure_to_recharge),
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							//组装充值报文
							String val_11 = MyApplication.getLineNum(getApplicationContext());
//							String val_60 = MyApplication.getSixtyValue(MyApplication.COMM_NUM, MyApplication.TERM_NUM);
							String value_60 = MyApplication.getSixtyValue2(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
							byte[] val_61 = getSixtyOneValue(mRechargeKeys);
							
							try {
								System.out.println("充值项目:"+"" +new String(val_61,"GBK"));
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							byte[] request = RechargeNode.buildRechareMsg(mGetCardNum,mGetRechargeCount,val_11,value_60,val_61);
							
							mRunningTask = new ConsumeSureTask(mGetCardNum,mGetRechargeCount,
									request);
							mRunningTask.execute(MyApplication.CONSUME_URL);
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
	 * 获取充值字段数据
	 * @param recharKeys
	 * @return
	 */
	private byte[] getSixtyOneValue(String recharKeys){
		
		byte[] datas = null;
		
		try{
			datas = recharKeys.getBytes("GBK");
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		
		StringBuilder sb3 = new StringBuilder();
		
		for(int i = 0 ; i < datas.length ; i ++){
			sb3.append(DigitalTrans.integerToHexString3(datas[i]));
		}
		
		String hex_data = sb3.toString();
		
		byte[] b_data2 = DigitalTrans.hex2byte(hex_data);
		System.out.println("hex_data:"+hex_data);
		
		int len = datas.length;
		
		StringBuilder sb = new StringBuilder();
		if (len >= 0 && len <= 9) {
			sb.append("00");
		} else if (len > 9 && len <= 99) {
			sb.append("0");
		}
		sb.append(len);
		
		String header = sb.toString();
	
		String hex_header = DigitalTrans.stringToHexString2(header);
		System.out.println("hex_header:"+hex_header);
		
		byte[] b_header = DigitalTrans.hex2byte(hex_header);
		int header_len = b_header.length;
		
		int total_len = len+header_len;
		
		
		byte[] mData = new byte[total_len];
		
		int count = 0;
		for(int i=0;i<b_header.length;i++){
			mData[count] = b_header[i];
			count++;
		}
		for(int i=0;i<b_data2.length;i++){
			mData[count] = b_data2[i];
			count++;
		}
		
		System.out.println(DigitalTrans.byte2hex(mData));
		
		return mData;
	}
	
	/**
	 * 确定充值任务
	 * @author ShawnXiao
	 *
	 */
	private class ConsumeSureTask extends AsyncTask{
		private String mCardNum;
		private String mSumOfConsume;
		private byte[] mRequest;
		public ConsumeSureTask(String card_num,String sum_of_consume,
				byte[] request){
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
			SocketClient client = new SocketClient(mRequest,responseHandler);
			client.start();
		}
	
		
	}
	
	Handler responseHandler= new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case RequestUtil.REQUEST_SUCCESS:{
				String hex_response = (String) msg.obj;
				if(hex_response!=null&&hex_response.length()>0){
					//解析报文
					RechargeNode node = RechargeNode.parseRechargeNode(hex_response);
					String code = getResources().getStringArray(R.array.return_code)[0];
					L.e("code:", code);
					L.e("node.get(39):", ""+node.get(39));
					if(node.get(39).equals(code)){
						showShortToast(R.string.recharge_succ);
						openActivity(MainMenuActivity.class);
						defaultFinish();
					}else{
						String err_msg = (String)node.get(63);
						if(err_msg!=null&&err_msg.length()>0){
							showShortToast(err_msg);
						}else{
							showShortToast(R.string.recharge_faield);
						}
						
					}
					
				}else{
					showShortToast(R.string.recharge_faield);
				}
				mAlertDialog.dismiss();
				
			}break;
			case RequestUtil.REQUEST_FAIELD:{
				showShortToast(R.string.recharge_faield);
				mAlertDialog.dismiss();
			}break;
			}
		}
	};
	
	
	/**
	 * 初始化控件
	 * @Description TODO
	 * @author Shawn
	 * @Time 2013-8-26  
	 */
	private void initView(){
		
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_1);
		
		
		tv_card_num_value = (TextView) findViewById(R.id.tv_card_num_value);
		
		tv_recharge_count_value = (TextView) findViewById(R.id.tv_recharge_count_value);
		btn_sure_recharge = (Button) findViewById(R.id.btn_sure_recharge);
		
		
		Intent intent = getIntent();
		mGetCardNum = intent.getExtras().getString(KEY_CARD_NUM);
		mGetRechargeCount  = intent.getExtras().getString(REQUEST_SUM_OF_CONSUME);
		mRechargeKeys = intent.getExtras().getString(KEY_RECHARGE_KEYS);
		
		tv_card_num_value.setText(mGetCardNum);
		
		tv_recharge_count_value.setText(mGetRechargeCount
				+getResources().getString(R.string.yuan));
		
		
		if(L.RechargeDebug){
//			et_consume_card_num.setText("9009320100011158");
//			et_consume_card_num.setText("9009320100011224");
//			et_sum_of_consume.setText("10");
			
		}
		
		
	}
}
