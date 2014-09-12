package com.coco.coupons.app.ui;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.coco.android.socket.SocketClient;
import com.coco.android.util.L;
import com.coco.coupons.app.BaseActivity;
import com.coco.coupons.app.MyApplication;
import com.coco.coupons.app.R;
import com.coco.coupons.app.bean.ConsumeCancleNode;
import com.coco.coupons.app.bean.ConsumeCancleNode.Node;
import com.coco.coupons.app.bean.DealNode;
import com.coco.coupons.app.bean.QueryCustInfoNode;
import com.coco.coupons.app.bean.RechargeCancleNode;
import com.coco.coupons.app.bean.RequestUtil;

/**
 * 
 * @Descriptio 充值列表界面
 * @author Shawn
 * @Time 2013-8-28  下午4:08:56
 */
public class ConsumeCancleActivity extends BaseActivity {
	
	
	/**
	 * 上个界面传过来的数据键
	 */
	public static final String KEY_QUERY_DATAS = "KEY_QUERY_DATAS";
	/**
	 * 列表视图 消费
	 */
	private ListView lv_consume;
	
	/**
	 * 消费适配器
	 */
	private MyConSumeAdapter mConSumeAdapter;
	
	
	/**
	 * 标题栏撤销按钮
	 */
//	private Button btn_cancle;
	
	/**
	 * 标题栏返回按钮
	 */
	private Button btn_back;
	
	private int mRequestType;
	
	/**
	 * 卡号值
	 */
	private TextView tv_card_num_value;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.consume_cancle);
		((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_4);
		
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new BackListener());
		
		
//		btn_cancle = (Button) findViewById(R.id.btn_sure);
//		btn_cancle.setText(R.string.btn_cancel);
//		btn_cancle.setVisibility(View.VISIBLE);
//		btn_cancle.setOnClickListener(new CancleConsumeListener());
		
		lv_consume = (ListView) findViewById(R.id.lv_consume);
		tv_card_num_value = (TextView) findViewById(R.id.tv_card_num_value);
		
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		ConsumeCancleNode consu_cancle_node = (ConsumeCancleNode) bundle.get("data");
		if(consu_cancle_node.getCard_num().length()==8){
			tv_card_num_value.setText(MyApplication.CARD_PREFIX+consu_cancle_node.getCard_num());
		}else{
			tv_card_num_value.setText(consu_cancle_node.getCard_num());
		}
		
		ArrayList<Node> nodes = consu_cancle_node.getNodes();
		mRequestType = bundle.getInt(ConsumeDataCollectActivity2.REQUEST_TYPE);
		switch(mRequestType){
		case ConsumeDataCollectActivity2.CHE_XIAO:{
			((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_2);
		}break;
		case ConsumeDataCollectActivity2.CHA_XUN_MING_XI:{
			((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_4);
		}break;
		}
		
		
		mConSumeAdapter = new MyConSumeAdapter(this);
		if(nodes!=null&&nodes.size()>0){
//			for(int i=0;i<nodes.size();i++){
//				L.e("node["+i+"]", ""+nodes.get(i).toString());
//			}
			MyComparator comparator = new MyComparator();
			Collections.sort(nodes, comparator);
			mConSumeAdapter.setConsumes(nodes);
		}
		
		
		lv_consume.setAdapter(mConSumeAdapter);
	}
	
	//自定义排序
	private class MyComparator implements Comparator<Node>{

		@Override
		public int compare(Node node1, Node node2) {
			if(node1.getDeal_date().compareTo(node2.getDeal_date())<0
					){
				return 1;
			}else if(node1.getDeal_date().compareTo(node2.getDeal_date())==0){
				if(node1.getDeal_time().compareTo(node2.getDeal_time())<0){
					return 1;
				}else if(node1.getDeal_time().compareTo(node2.getDeal_time())==0){
					return 0;
				}else{
					return -1;
				}
			}else{
				return -1;
			}
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
			defaultFinish();
		}
		
	}
	
	
	
	/**
	 * 
	 * @Descriptio 消费列表适配器
	 * @author Shawn
	 * @Time 2013-8-28  下午4:29:34
	 */
	private class MyConSumeAdapter extends BaseAdapter{
		private List<Node> mConsumes;
		private Context mContext;
		private LayoutInflater mInflater;
		public MyConSumeAdapter(Context context){
			mContext = context;
			mConsumes = new ArrayList<Node>();
			mInflater = LayoutInflater.from(mContext);
		}
		
		public void setConsumes(List<Node> consumes){
			mConsumes = consumes; 
		}
		
		public void removeConsume(Node consume){
			if(mConsumes!=null && mConsumes.size()>0){
				if(consume!=null){
					mConsumes.remove(consume);
				}
			}
		}
		
		
		@Override
		public int getCount() {
			return mConsumes.size();
		}

		@Override
		public Object getItem(int position) {
			return mConsumes.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Node node = mConsumes.get(position);
			ViewHolder viewHolder = null;
//			if(convertView==null){
				convertView = mInflater.inflate(R.layout.consume_item, null);
				viewHolder = new ViewHolder();
				viewHolder.btn_cancle = (Button) convertView.findViewById(R.id.btn_cancle);
				viewHolder.tv_deal_date_value = (TextView) convertView.findViewById(R.id.tv_deal_date_value);
				viewHolder.tv_deal_time_value = (TextView) convertView.findViewById(R.id.tv_deal_time_value);
//				viewHolder.tv_card_num_value = (TextView) convertView.findViewById(R.id.tv_card_num_value);
				viewHolder.tv_sum_of_consume_value = (TextView) convertView.findViewById(R.id.tv_sum_of_consume_value);
				viewHolder.tv_deal_cust_num_value = (TextView) convertView.findViewById(R.id.tv_deal_cust_num_value);
				viewHolder.tv_deal_cust_name_value = (TextView) convertView.findViewById(R.id.tv_deal_cust_name_value);
//				convertView.setTag(viewHolder);
//			}
//			else{
//				viewHolder = (ViewHolder) convertView.getTag();
//			}
			
			if(node!=null){
				if(mRequestType == ConsumeDataCollectActivity2.CHE_XIAO){
					viewHolder.btn_cancle.setVisibility(View.VISIBLE);
				}else if(mRequestType==ConsumeDataCollectActivity2.CHA_XUN_MING_XI){
					viewHolder.btn_cancle.setVisibility(View.GONE);
				}
				Date deal_date = null;
				Date deal_time = null;
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat format2 = new SimpleDateFormat("yyyy年MM月dd日");
				SimpleDateFormat format3 = new SimpleDateFormat("HHmmss");
				SimpleDateFormat format4 = new SimpleDateFormat("HH:mm:ss");
				try {
					if(node.getDeal_date()!=null&&node.getDeal_date().length()>0){
						deal_date=format.parse(node.getDeal_date());
					}
					if(node.getDeal_time()!=null&&node.getDeal_time().length()>0){
						deal_time = format3.parse(node.getDeal_time());
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
				
				viewHolder.tv_deal_date_value.setText(format2.format(deal_date));
				viewHolder.tv_deal_time_value.setText(format4.format(deal_time));
//				viewHolder.tv_card_num_value.setText(node.getCard_num().subSequence(8, 16));
				viewHolder.tv_sum_of_consume_value.setText(node.getMoney()+mContext.getResources().getString(R.string.yuan));
				viewHolder.tv_deal_cust_num_value.setText("");
				viewHolder.tv_deal_cust_name_value.setText("");
				if(node.getCust_num()!=null&&node.getCust_num().length()>0){
					viewHolder.tv_deal_cust_num_value.setText(node.getCust_num());
				}else{
					viewHolder.tv_deal_cust_num_value.setText("");
				}
				if(node.getCust_name()!=null&&node.getCust_name().length()>0){
					viewHolder.tv_deal_cust_name_value.setText(node.getCust_name());
				}else{
					viewHolder.tv_deal_cust_name_value.setText("");
				}
				
				viewHolder.btn_cancle.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						final String val_52 = MyApplication.DEFAULT_CONSUME_KEY;
						final String val_60 = MyApplication.getSixtyValue2(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
						String str_date = node.getDeal_date();
						L.e("str_date:", ""+str_date);
						final String str_date2=(String) str_date.subSequence(4, 8);
						L.e("str_date2:", ""+str_date2);
						
						L.e("交易时间:", ""+node.getDeal_time());
						L.e("交易日期:", ""+str_date2);
						final String val_serl_num = getSerialNum(node.getSerial_num());
						System.out.println("流水号:"+val_serl_num);
						
						showAlertDialog(mContext.getResources().getString(R.string.warn_prompt),
								mContext.getResources().getString(R.string.sure_to_cancle_recharge), 
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										//组织消费撤销报文
										byte[] request = RechargeCancleNode.buildRequest(node.getDeal_date(),node.getCard_num(), node.getMoney(), 
												val_serl_num, node.getDeal_time(), str_date2, val_52, val_60);
										
										mRunningTask = new RechargeCancleTask(node,request);
										mRunningTask.execute();
									}
								}, 
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								},  null);
						
						
						
					}
				});
			}
			
			return convertView;
		}
		
		
		class ViewHolder{
			/**
			 * 按钮撤销
			 */
			Button btn_cancle;
			/**
			 * 文本客户编号
			 */
			TextView tv_deal_cust_num_value;
			/**
			 * 文本客户名称
			 */
			TextView tv_deal_cust_name_value;
			/**
			 * 文本消费日期
			 */
			TextView tv_deal_date_value;
			/**
			 * 文本消费时间
			 */
			TextView tv_deal_time_value;
			/**
			 * 文本消费卡号
			 */
//			TextView tv_card_num_value;
			/**
			 * 文本消费金额
			 */
			TextView tv_sum_of_consume_value;
			
		}
	}
	
	/**
	 * 获取流水号
	 * @param val
	 * 流水号
	 * @return
	 */
	private String getSerialNum(String value){
		int len = value.length();
		StringBuilder sb = new StringBuilder();
		if(len == 0){
			sb.append("000000");
		}else if(len == 1){
			sb.append("00000");
		}else if(len == 2){
			sb.append("0000");
		}else if(len == 3){
			sb.append("000");
		}else if(len == 4){
			sb.append("00");
		}else if(len == 5){
			sb.append("0");
		}
		sb.append(value);
		
		return sb.toString();
	}
	
	/**
	 * 充值撤销任务
	 * @author ShawnXiao
	 *
	 */
	private class RechargeCancleTask extends AsyncTask{

		private Node mNode;
		private byte[] mRequest;
		public RechargeCancleTask(Node node,byte[] request){
			mNode = node;
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
			SocketClient client = new SocketClient(mRequest, new RechargeCancleHandler(mNode));
			client.start();
		}
		
	}
	/**
	 * 消费撤销处理助手
	 * @author ShawnXiao
	 *
	 */
	private class RechargeCancleHandler extends Handler{
		private Node mNode;
		public RechargeCancleHandler(Node node){
			mNode = node;
		}
		@Override
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
			switch(msg.what){
			case RequestUtil.REQUEST_SUCCESS:{
				mAlertDialog.dismiss();
//				openActivity(MainMenuActivity.class);
//				defaultFinish();
				String hex_response = (String) msg.obj;
				if(hex_response!=null&&hex_response.length()>0){
					RechargeCancleNode node = RechargeCancleNode.parseMsg(hex_response);
					String code = getResources().getStringArray(R.array.return_code)[0];
					L.e("code:", code);
					L.e("node.get(39):", ""+node.get(39));
					if(node.get(39).equals(code)){
						showShortToast(R.string.recharge_cancle_success);
						mConSumeAdapter.removeConsume(mNode);
						mConSumeAdapter.notifyDataSetChanged();
					}else{
						showShortToast(R.string.recharge_cancle_faield);
					}
				}else{
					showShortToast(R.string.recharge_cancle_faield);
					
				}
				mAlertDialog.dismiss();
				
			}break;
			case RequestUtil.REQUEST_FAIELD:{
				showShortToast(R.string.recharge_cancle_faield);
				mAlertDialog.dismiss();
			}break;
			}
		}
	}
}
