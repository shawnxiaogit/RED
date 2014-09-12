package com.coco.coupons.app.ui;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;






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
import android.widget.TextView;

import com.coco.android.socket.SocketClient;
import com.coco.android.util.L;
import com.coco.coupons.app.BaseActivity;
import com.coco.coupons.app.MyApplication;
import com.coco.coupons.app.R;
import com.coco.coupons.app.bean.ConsumeCancleNode;
import com.coco.coupons.app.bean.GetOperaterDetailNode;
import com.coco.coupons.app.bean.ConsumeCancleNode.Node;
import com.coco.coupons.app.bean.RequestUtil;
import com.easier.ui.CalendarActivity;
import com.google.zxing.CaptureActivity;

/**
 * 
 * @Descriptio 查询充值列表，信息收集界面
 * @author Shawn
 * @Time 2013-8-22  下午2:14:58
 */
public class ConsumeDataCollectActivity2 extends BaseActivity {
	/**
	 * 请求类型
	 */
	public static final String REQUEST_TYPE = "REQUEST_TYPE";
	
	/**
	 * 撤销充值
	 */
	public static final int CHE_XIAO = 111;
	/**
	 * 查询充值明细
	 */
	public static final int CHA_XUN_MING_XI = 112;
	
	private int mRequestType;
	
	/**
	 * 编辑框卡号
	 */
	private EditText et_card_num;
	/**
	 * 编辑框结束时间
	 */
	private EditText et_start_date;
	/**
	 * 编辑康结束时间
	 */
	private EditText et_end_date;
	
	
	/**
	 * 按钮 扫一扫
	 */
	private Button btn_scan;
	/**
	 * 按钮 查询
	 */
	private Button btn_query;
	
	/**
	 * 获取选择的开始日期 
	 */
	private static final int GET_START_SELECT_DATE = 222;
	/**
	 * 获取选择的结束日期
	 */
	private static final int GET_END_SELECT_DATE = 224;
	
	/**
	 * 获取扫描的二维码 request_code
	 */
	private static final int GET_CARD_NUM    = 223;
	/**
	 * 编辑框客户编号
	 */
	private EditText et_cust_num;
	
	
	/**
	 * 标题栏返回按钮
	 */
	private Button btn_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.consume_data_collect2);
		
		//初始化控件
		initView();
		//设置控件监听器
		setUpViewListener();
		
	}
	
	/**
	 * 
	 * @Description 为控件设置监听器
	 * @author Shawn
	 * @Time 2013-8-22  下午5:49:34
	 * @param param
	 * @return void
	 * @exception exception
	 */
	private void setUpViewListener(){
		btn_scan.setOnClickListener(new ScanListener());
		
		et_start_date.setOnClickListener(new SelectDateListener(GET_START_SELECT_DATE));
		et_end_date.setOnClickListener(new SelectDateListener(GET_END_SELECT_DATE));
		
		
		btn_query.setOnClickListener(new QueryListener());
		
		btn_back.setOnClickListener(new BackListener());
		
	}
	/**
	 * 查询按钮监听器
	 * @author ShawnXiao
	 *
	 */
	private class QueryListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			String card_num = et_card_num.getText().toString();
			String start_time = et_start_date.getText().toString();
			String end_time = et_end_date.getText().toString();
			String cust_num = et_cust_num.getText().toString();
			
			
			if(!TextUtils.isEmpty(card_num)){
				if(card_num.length()<8||card_num.length()>16){
					showShortToast(R.string.card_num_error);
					return ;
				}
			}
			if(!TextUtils.isEmpty(cust_num)){
				if(cust_num.length()<10){
					showShortToast(R.string.cust_num_error);
					return ;
				}
			}
			
			if(isDataEmpty(card_num,start_time,end_time,cust_num)){
				return;
			}
			
			
			StringBuilder sb = new StringBuilder();
//			if(card_num==null||card_num.length()==0){
//				showShortToast(R.string.card_num_error);
//				return ;
//			}else 
			if(card_num!=null&&card_num.length()>0)	{
			if(card_num.length()==8){
				sb.append(MyApplication.CARD_PREFIX);
				sb.append(card_num);
			}else if(card_num.length()==16){
				sb.append(card_num);
			}
//			else{
//				showShortToast(R.string.card_num_error);
//				return ;
//			}
			}
			
			//判断结束时间是否小于开始时间
			if(MyApplication.isEndTimeSmallThanStartTime(start_time,end_time)){
				showShortToast(R.string.end_time_can_not_small_than_start_time);
				return;
			}
			
			
			L.e("card_num:", sb.toString());
			
			Date  start_date=null;
			Date end_date=null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
			try {
				start_date  = format.parse(start_time);
				end_date = format.parse(end_time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			String start_time2 = format2.format(start_date);
			String end_time2 =  format2.format(end_date);
			L.e("start_time2:", ""+start_time2);
			L.e("end_time2", end_time2);
			
			
			//保存查询的手机信息
//			MyApplication.PHONE_INFO2 = phone_info;
			//保存查询开始时间
			MyApplication.QUERY_START_TIME = start_time2;
			//保存查询结束时间
			MyApplication.QUERY_END_TIME = end_time2;
			
			String value_60 = MyApplication.getSixtyValue3(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
			//这里要调整为查询柜员明细的报文
//			byte[] request = ConsumeCancleNode.buildQueryList(value_60,sb.toString(),start_time2,end_time2);
//			mRunningTask = new GetConsumeListTask(request);
//			mRunningTask.execute();
			buildQueryMsg(-1,start_time,end_time,null,sb.toString(),cust_num,query_type);
			
			
			
			
		}
		
	}
	
	/**
	 * 组织查询报文
	 * @param type
	 * 查询类型
	 * @param start_time
	 * 开始时间
	 * @param end_time
	 * 结束时间
	 * @param
	 * line_num
	 * 线路号
	 *@param
	 * query_type
	 * 查询类型
	 */
	private void buildQueryMsg(int type,
			String start_time,
			String end_time,
			String line_num,
			String card_num,
			String cust_num,
			String query_type){
		//组织查询操作员明细报文
		Date  start_date=null;
		Date end_date=null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			start_date  = format.parse(start_time);
			end_date = format.parse(end_time);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		String start_time2 = format2.format(start_date);
		String end_time2 =  format2.format(end_date);
		L.e("start_time2:", ""+start_time2);
		L.e("end_time2", end_time2);
		
		String phone_info = MyApplication.getSixtyValue3(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
		String query_tag = MyApplication.EMPLOYEE;
		if(type == TYPE_QUERY_SELF){
			query_tag = MyApplication.EMPLOYEE;
		}else if(type == TYPE_QUERY_ALL_EMPLOYEE){
			query_tag = MyApplication.MANAGER;
		}else if(type == TYPE_QUERY_SOME_EMPLOYEE){
			query_tag = MyApplication.MANAGER;
		}else{
			query_tag = MyApplication.EMPLOYEE;
		}
		
		MyApplication.CHE_XIAO_QUERY_TYPE = type;
		MyApplication.CHE_XIAO_PHONE_INFO = phone_info;
		MyApplication.CHE_XIAO_START_TIME = start_time2;
		MyApplication.CHE_XIAO_END_TIME = end_time2;
		MyApplication.CHE_XIAO_QUERY_TAG = query_tag;
		MyApplication.CHE_XIAO_LINE_NUM = line_num;
		MyApplication.CHE_XIAO_CARD_NUM = card_num;
		MyApplication.CHE_XIAO_CUST_NUM = cust_num;
		
		L.e("query_type", ""+query_type);
		byte[] request = GetOperaterDetailNode.getOpraterDetailMsg(type,
				phone_info, 
				start_time2, 
				end_time2,
				query_tag,
				query_type,//MyApplication.GET_OPERATER_DETAIL_QUERY_TYPE,
				line_num,
				card_num,
				cust_num,
				null,
				null,
				MyApplication.REQUEST_TYPE_CHA_XUN);
		mRunningTask = new GetOperaterDetailTask(request);
		mRunningTask.execute();
	}
	/**
	 * 获取操作员明细任务
	 * @author ShawnXiao
	 *
	 */
	private class GetOperaterDetailTask extends AsyncTask{
		private byte[] mRequest;
		public GetOperaterDetailTask(byte[] request){
			mRequest = request;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.handing_process);
		}
		@Override
		protected Object doInBackground(Object... params) {
			return null;
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			SocketClient client = new SocketClient(mRequest,getOperaterDetailHandler);
			client.start();
		}
		
	}
	/**
	 * 获取操作员明细处理助手
	 */
	Handler getOperaterDetailHandler = new Handler(){
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
				case RequestUtil.REQUEST_SUCCESS: {
					String hex_response = (String) msg.obj;
					if (hex_response != null && hex_response.length() > 0) {
						//解析报文
						GetOperaterDetailNode node = GetOperaterDetailNode.parseMsg(hex_response);
						String code = getResources().getStringArray(
								R.array.return_code)[0];
						L.e("code:", code);
						L.e("node.get(39):", "" + node.get(39));
						if (node.getReturn_code().equals(code)) {
							Intent intent  = new Intent(ConsumeDataCollectActivity2.this,
									RecharAndRecharCancleDetailActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable(RecharAndRecharCancleDetailActivity.KEY_QUERY_DATAS, node);
							bundle.putInt(RecharAndRecharCancleDetailActivity.KEY_TYPE, RecharAndRecharCancleDetailActivity.TYPE_CHEXIAO);
							intent.putExtras(bundle);
							ConsumeDataCollectActivity2.this.startActivity(intent);
							overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
						}else{
							showShortToast(R.string.query_faield);
						}
					}else{
						showShortToast(R.string.query_faield);
					}
					if(mAlertDialog!=null){
						mAlertDialog.dismiss();
					}
				}break;
				case RequestUtil.REQUEST_FAIELD: {
					showShortToast(R.string.query_faield);
					if(mAlertDialog!=null){
						mAlertDialog.dismiss();
					}
				}break;
			}
		};
	}; 
	/**
	 * 查询自己操作明细
	 */
	public static final int TYPE_QUERY_SELF = 1111;
	/**
	 * 查询所有员工的操作明细
	 */
	public static final int TYPE_QUERY_ALL_EMPLOYEE = 1112;
	
	/**
	 * 查询指定员工的操作明细
	 */
	public static final int TYPE_QUERY_SOME_EMPLOYEE = 1113;
	
	/**
	 * 获取消费列表任务
	 * @author ShawnXiao
	 *
	 */
	private class GetConsumeListTask extends AsyncTask{
		private byte[] mRequest;
		public GetConsumeListTask(byte[] request){
			mRequest = request;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.handing_process);
		}
		

		@Override
		protected Object doInBackground(Object... params) {
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
				if (hex_response != null && hex_response.length() > 0) {
					//解析报文
					ConsumeCancleNode node = ConsumeCancleNode.parseMsg(hex_response);
					node.setCard_num(et_card_num.getText().toString());
					if(node!=null){
						String code = getResources().getStringArray(
								R.array.return_code)[0];
						L.e("code:", code);
						if(node.getReturn_code()!=null){
							L.e("node.getReturn_code():", "" + node.getReturn_code());
							if(node.getReturn_code().equals(code)){
								ArrayList<Node> nodes = node.getNodes();
								if(nodes!=null&&nodes.size()>0){
									Intent intent = new Intent(ConsumeDataCollectActivity2.this,ConsumeCancleActivity.class);
									Bundle bundle = new Bundle();
									bundle.putSerializable("data", node);
									bundle.putInt(REQUEST_TYPE, mRequestType);
									intent.putExtras(bundle);
									ConsumeDataCollectActivity2.this.startActivity(intent);
								}else{
									showShortToast(R.string.no_data);
								}
								
							}else{
								showShortToast(R.string.get_data_success);
							}
							mAlertDialog.dismiss();
						}
					}
					else{
						showShortToast(R.string.get_data_success);
					}
					mAlertDialog.dismiss();
				}
			}break;
			case RequestUtil.REQUEST_FAIELD:{
				showShortToast(R.string.get_data_success);
				mAlertDialog.dismiss();
			}break;
			}
			
		}
	};
	
	private boolean isDataEmpty(String card_num,String start_time,String end_time,String cust_num){
		boolean result = true;
		if(TextUtils.isEmpty(card_num)&&TextUtils.isEmpty(cust_num)){
			showShortToast(R.string.consume_card_num_or_cust_numcan_not_null);
			result = true;
		}else{
			if(TextUtils.isEmpty(start_time)){
				showShortToast(R.string.start_time_can_not_null);
				result = true;
			}else{
				if(TextUtils.isEmpty(end_time)){
					showShortToast(R.string.end_time_can_not_null);
				}else{
					result = false;
				}
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @Descriptio 扫一扫监听器
	 * @author Shawn
	 * @Time 2013-8-20  下午6:09:27
	 */
	private class ScanListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent_query = new Intent(ConsumeDataCollectActivity2.this,CaptureActivity.class);
			startActivityForResult(intent_query,GET_CARD_NUM);
		}
		
	}
	
	/**
	 * 
	 * @Descriptio 返回上一个界面
	 * @author Shawn
	 * @Time 2013-8-23  下午3:50:43
	 */
	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			ConsumeDataCollectActivity2.this.finish();
		}
		
	}
	
	
	/**
	 * 
	 * @Descriptio 选择日期监听器
	 * @author Shawn
	 * @Time 2013-8-22  下午5:50:36
	 */
	private class SelectDateListener implements OnClickListener{
	
		private int mType;
		public SelectDateListener(int type){
			mType = type;
		}

		@Override
		public void onClick(View v) {
			switch(mType){
			case GET_START_SELECT_DATE:{
				Intent intent_select_date = new Intent(ConsumeDataCollectActivity2.this,
						CalendarActivity.class);
				ConsumeDataCollectActivity2.this.startActivityForResult(intent_select_date, 
						GET_START_SELECT_DATE);
			}break;
			case GET_END_SELECT_DATE:{
				Intent intent_select_date = new Intent(ConsumeDataCollectActivity2.this,
						CalendarActivity.class);
				ConsumeDataCollectActivity2.this.startActivityForResult(intent_select_date, 
						GET_END_SELECT_DATE);
			}break;
			}
			
			
		}
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			switch(requestCode){
			case GET_START_SELECT_DATE:{
				String select_date = data.getExtras().getString("result");
				L.e("select_date:", ""+select_date);
				if(select_date!=null&&select_date.length()>0){
					et_start_date.setText(select_date);
				}
			}break;
			case GET_END_SELECT_DATE:{
				String select_date = data.getExtras().getString("result");
				L.e("select_date:", ""+select_date);
				if(select_date!=null&&select_date.length()>0){
					et_end_date.setText(select_date);
				}
			}break;
			case GET_CARD_NUM:{
				String result = data.getExtras().getString("result");
				L.e("result", ""+result);
				if(TextUtils.isEmpty(result)){
					showShortToast(R.string.scan_retry);
					return;
				}
				et_card_num.setText(result);
			}break;
			default:
				break;
			}
		}
	}
	
	private String query_type;
	/**
	 * 
	 * @Description 初始化控件
	 * @author Shawn
	 * @Time 2013-8-22  下午5:43:27
	 * @return void
	 */
	private void initView(){
		
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		
		
		et_card_num = (EditText) findViewById(R.id.et_card_num);
		et_start_date = (EditText) findViewById(R.id.et_start_date);
		et_end_date = (EditText) findViewById(R.id.et_end_date);
		
		
		btn_scan = (Button) findViewById(R.id.btn_scan);
		btn_query = (Button) findViewById(R.id.btn_query);
		et_cust_num = (EditText) findViewById(R.id.et_cust_num);
		
		Intent intent = getIntent();
		mRequestType = intent.getExtras().getInt(REQUEST_TYPE);
		switch(mRequestType){
		case CHE_XIAO:{
			((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_2);
			query_type = MyApplication.GET_OPERATER_DETAIL_QUERY_TYPE2;
		}break;
		case CHA_XUN_MING_XI:{
			((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_4);
			query_type = MyApplication.GET_OPERATER_DETAIL_QUERY_TYPE;
		}break;
		default :
			query_type = MyApplication.GET_OPERATER_DETAIL_QUERY_TYPE;
			break;
		}
		
		L.e("query_type:", ""+query_type);
		if(L.GET_DEAL_LIST_DEBUG){
			et_card_num.setText("9009320100001290");
//			et_start_date.setText("2013年10月25日");
//			et_end_date.setText("2013年11月08日");
		}
	}
}
