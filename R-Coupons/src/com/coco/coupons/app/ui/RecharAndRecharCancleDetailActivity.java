package com.coco.coupons.app.ui;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.coco.android.socket.SocketClient;
import com.coco.android.util.L;
import com.coco.android.xlist.XListView;
import com.coco.android.xlist.XListView.IXListViewListener;
import com.coco.coupons.app.BaseActivity;
import com.coco.coupons.app.MyApplication;
import com.coco.coupons.app.R;
import com.coco.coupons.app.bean.DealNode;
import com.coco.coupons.app.bean.GetOperaterDetailNode;
import com.coco.coupons.app.bean.RechaAndRechaCancleNode;
import com.coco.coupons.app.bean.RechargeCancleNode;
import com.coco.coupons.app.bean.RequestUtil;
import com.coco.coupons.app.bean.ConsumeCancleNode.Node;


/**
 * 充值和充值撤销数据详情界面
 * @author ShawnXiao
 *
 */
public class RecharAndRecharCancleDetailActivity extends BaseActivity{
	
	
	/**
	 * 上个界面传过来的数据键
	 */
	public static final String KEY_QUERY_DATAS = "KEY_QUERY_DATAS";
	/**
	 * 请求类型
	 */
	public static final String KEY_TYPE = "KEY_TYPE";
	/**
	 * 撤销类型
	 */
	public static final int TYPE_CHEXIAO =111345; 
//	
//	 /**
//	  * 功能列表
//	 */
//	private ListView mListView;
	
	
	
	
	int pageNum = 1; // 当前页 从1开始
	int pageSize = 10; // 每页记录
	int totalPage = 0; // 记录总页数
	int totalRecords = 0; // 总记录条数
	
	private XListView mListView;
	
	/**
	 * 数据适配器
	 */
	private MyDataAdapter mAdapter;
	
	
	private ArrayList<RechaAndRechaCancleNode> mDatas;
	
	/**
	 * 标题栏返回按钮
	 */
	private Button btn_back;
	
	/**
	 * 上个界面传过来的操作员明细实体类
	 */
	private GetOperaterDetailNode rechargeNode;
	
	/**
	 * 界面数据汇总
	 */
	private TextView tv_total_money;
	
	/**
	 * 当前界面类型：撤销、查询
	 */
	private int mCurrentType;
	
	/**
	 * 充值明细汇总
	 */
	private LinearLayout ll_hui_zong;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		L.e("RecharAndRecharCancleDetailActivity", "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rechar_and_rechar_cancle_detail);
		
		
		ll_hui_zong = (LinearLayout) findViewById(R.id.ll_hui_zong);
		
		mListView = (XListView) findViewById(R.id.lv_rechar_data);
		
		mListView.setCacheColorHint(Color.TRANSPARENT);
		
		mListView.setXListViewListener(new MyXListViewListener());
		
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new BackListener());
		
	
		Intent intent = getIntent();
		rechargeNode = (GetOperaterDetailNode) intent.getExtras().getSerializable(KEY_QUERY_DATAS);
		mCurrentType = intent.getExtras().getInt(KEY_TYPE);
//		GetOperaterDetailNode.printMsg(rechargeNode);
		//初始化数据，后面要修改为网络获取
//		initData(rechargeNode);
		comparator = new MyComparator();
		ArrayList<DealNode> nodes = null;
		if(mCurrentType==TYPE_CHEXIAO){
			nodes = getRechargeNode(rechargeNode.getDealNodes());
			ll_hui_zong.setVisibility(View.GONE);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			mListView.setLayoutParams(lp);
			((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_2);
			pageNum = 1;
		}else{
			nodes = rechargeNode.getDealNodes();
			ll_hui_zong.setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_4);
			pageNum = 1;
		}
		if(nodes!=null&&nodes.size()>0){
			Collections.sort(nodes, comparator);
		}
		mAdapter = new MyDataAdapter(this,nodes);
		mListView.setAdapter(mAdapter);
		
		tv_total_money = (TextView) findViewById(R.id.tv_total_money);
		tv_total_money.setText(getTotalMoney(rechargeNode.getDealNodes())
				+getResources().getString(R.string.yuan));
		
		
		//设置总的记录数量
		totalRecords = Integer.parseInt(rechargeNode.getTotal_count());
		
	}
	MyComparator comparator;
	
	@Override
	protected void onResume() {
		super.onResume();
		if(mAdapter.getCount()<totalRecords){
			mListView.setPullLoadEnable(true);
		}else{
			mListView.setPullLoadEnable(false);
		}
	}
	
	
	//刷新获取数据
	private static final int _REFRESH_DATA = 222;
	//不是刷新获取数据
	private static final int _NOT_REFRESH_DATA = 111;
	
	private static int is_refresh = -1;
	
	private class MyXListViewListener implements IXListViewListener{
		// 下拉更新
		@Override
		public void onRefresh() {
			is_refresh = _REFRESH_DATA;
			
			getOperaterDetailHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					
					if(mCurrentType == TYPE_CHEXIAO){
						pageNum = 1;
						//如果是撤销的话需要请求不同的参数接口
						getDataCheXiao(pageNum,pageSize);
						
					}else{
						pageNum = 1;
						getData(pageNum,pageSize);
					}
				}
			}, 10);
			
		}
		// 上滑更多
		@Override
		public void onLoadMore() {
			is_refresh = _NOT_REFRESH_DATA;
			L.e("onLoadMore", "onLoadMore");
			getOperaterDetailHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					L.e("mAdapter.getCount():", ""+(mAdapter.getCount()));
					L.e("totalRecords:", ""+totalRecords);;
					L.e("mAdapter.getCount()<totalRecords:", ""+(mAdapter.getCount()<totalRecords));
					if(mAdapter.getCount()<totalRecords){
						mListView.setPullLoadEnable(true);
						int requ_start =-1;
						L.e("requ_start", ""+requ_start);
						if(mCurrentType == TYPE_CHEXIAO){
							//如果是撤销的话需要请求不同的参数接口
//							if(pageNum == 0){
//								requ_start = 0;
//							}else{
//								requ_start =(pageNum*pageSize);
//							}
							requ_start= ((pageNum*pageSize)+1);
							getDataCheXiao(requ_start,pageSize);
						}else{
							requ_start= ((pageNum*pageSize)+1);
							getData(requ_start,pageSize);
						}
						
						pageNum++;
					}
					
				}
			}, 10);
		}
		
	}
	
	
	/**
	 * 撤销查询
	 * @param pageNum
	 * @param pageSize
	 */
	private void getDataCheXiao(int pageNum,int pageSize){
		
		
		byte[] request = GetOperaterDetailNode.getOpraterDetailMsg(MyApplication.CHE_XIAO_QUERY_TYPE,
				MyApplication.CHE_XIAO_PHONE_INFO, 
				MyApplication.CHE_XIAO_START_TIME, 
				MyApplication.CHE_XIAO_END_TIME,
				MyApplication.CHE_XIAO_QUERY_TAG,
				MyApplication.GET_OPERATER_DETAIL_QUERY_TYPE,
				MyApplication.CHE_XIAO_LINE_NUM,
				MyApplication.CHE_XIAO_CARD_NUM,
				MyApplication.CHE_XIAO_CUST_NUM,
				String.valueOf(pageNum),
				String.valueOf(pageSize),
				MyApplication.REQUEST_TYPE_CHE_XIAO);
		mRunningTask = new GetOperaterDetailTask(request);
		mRunningTask.execute();
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
						L.e("node.getReturn_code():", "" + node.getReturn_code());
						if (node.getReturn_code().equals(code)) {
							if(is_refresh == _REFRESH_DATA){
								is_refresh = _NOT_REFRESH_DATA;
								if(mAdapter!=null){
									mAdapter.removeData();
									Collections.sort(node.getDealNodes(), comparator);
									mAdapter.addDatas(node.getDealNodes());
									
									
								}
							}else{
							
								if(mAdapter!=null){
									mAdapter.addDatas(node.getDealNodes());
								}
								
							}
							if(mAdapter.getData()!=null&&mAdapter.getData().size()>0){
								Collections.sort(mAdapter.getData(), comparator);
							}
							mAdapter.notifyDataSetChanged();
							
							
							totalRecords = Integer.parseInt(node.getTotal_count());
							L.e("totalRecords:", ""+totalRecords);
							L.e("mAdapter.getCount():", ""+mAdapter.getCount());
							L.e("mAdapter.getCount()<totalRecords：", ""+(mAdapter.getCount()<totalRecords));
							
							//显示总金额
							tv_total_money.setText(getTotalMoney(mAdapter.getData())
									+getResources().getString(R.string.yuan));
							
							
							//当前已加载的数据小于总数据条数时，显示底部更多，否则隐藏
							if(mAdapter.getCount()<totalRecords){
								mListView.setPullLoadEnable(true);
							}else{
								mListView.setPullLoadEnable(false);
								showShortToast(R.string.no_more_data);
							}
							onStopLoad();
							
						}else{
							showShortToast(R.string.no_data_2);
							mListView.setPullLoadEnable(false);
						}
					}else{
						showShortToast(R.string.query_faield);
					}
					if(mAlertDialog!=null&&mAlertDialog.isShowing()){
						mAlertDialog.dismiss();
					}
				}break;
				case RequestUtil.REQUEST_FAIELD: {
					showShortToast(R.string.query_faield);
					if(mAlertDialog!=null&&mAlertDialog.isShowing()){
						mAlertDialog.dismiss();
					}
				}break;
			}
		};
	};
	
	
	// 停止加载框
	private void onStopLoad() {
		L.e("onStopLoad", "onStopLoad");
		mListView.stopRefresh();
		mListView.stopLoadMore();
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		mListView.setRefreshTime("" + hour + ":" + (minute < 10 ? "0" : "")
				+ minute);
	}
	
	/**
	 * 获取充值的列表
	 * @param nodes
	 * 只包含充值的列表
	 * @return
	 */
	private ArrayList<DealNode> getRechargeNode(ArrayList<DealNode> nodes){
		ArrayList<DealNode> rechargeNodes = new ArrayList<DealNode>();
		if(nodes!=null&&nodes.size()>0){
			for(DealNode node:nodes){
				if(node.getDeal_summry().equals(DealNode.KEY_RECHARGE)){
					rechargeNodes.add(node);
				}
			}
		}
		return rechargeNodes;
	}
	
	
	//自定义排序
	private class MyComparator implements Comparator<DealNode>{

		@Override
		public int compare(DealNode node1, DealNode node2) {
			if(node1.getDeal_time().compareTo(node2.getDeal_time())<0){
				return 1;
			}else if(node1.getDeal_time().compareTo(node2.getDeal_time())==0){
				return 0;
			}else{
				return -1;
			}
		}
		
	}
	
	/**
	 * 获取操作明细总金额
	 * @param nodes
	 * @return
	 */
	private String getTotalMoney(ArrayList<DealNode> nodes){
		Double result = new Double("0");
		for(DealNode node:nodes){
			if(node.getDeal_money()!=null&&node.getDeal_money().length()>0){
//				if(node.getDeal_type().equals(DealNode.TYPE_C)){
					result = result + new Double(node.getDeal_money());
//				}else if(node.getDeal_type().equals(DealNode.TYPE_D)){
//					result = result - new Double(node.getDeal_money());
//				}
			}
		}
		DecimalFormat format = new DecimalFormat("0.00");
			
		return format.format(result);
	}
	private void initData(GetOperaterDetailNode rechargeNode){
		mDatas = new ArrayList<RechaAndRechaCancleNode>();
		RechaAndRechaCancleNode node = new RechaAndRechaCancleNode();
		node.setType(RechaAndRechaCancleNode.TYPE_RECHARGE);
		node.setmCardNum("9009320100000011");
		node.setmDate("2013-11-15 17:33");
		node.setmMoney("8.50");
		mDatas.add(node);
		
		RechaAndRechaCancleNode node2 = new RechaAndRechaCancleNode();
		node2.setType(RechaAndRechaCancleNode.TYPE_RECHARGE_CANCLE);
		node2.setmCardNum("9009320100000011");
		node2.setmDate("2013-11-15 17:33");
		node2.setmMoney("8.50");
		mDatas.add(node2);
		
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
			RecharAndRecharCancleDetailActivity.this.finish();
		}
		
	}
	
	
	private void getData(int start,int count){
		L.e("getData:", "getData");
		String phone_info = MyApplication.getSixtyValue3(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
////		byte[] request = GetOperaterDetailNode.getOpraterDetailMsg(MyApplication.PHONE_INFO2,
//		byte[] request = GetOperaterDetailNode.getOpraterDetailMsg(phone_info,
//				MyApplication.QUERY_START_TIME,
//				MyApplication.QUERY_END_TIME,
//				MyApplication.EMPLOYEE,
//				MyApplication.GET_OPERATER_DETAIL_QUERY_TYPE,
//				null,
//				null,
//				null,
//				String.valueOf(start),
//				String.valueOf(count));
////				byte[] request = GetOperaterDetailNode.getOpraterDetailMsg(phone_info, start_time2, end_time2,
////						"1","C");
//		mRunningTask = new GetOperaterDetailTask(request);
//		mRunningTask.execute();
		
		
		
//		byte[] request = GetHuiZongNode.buildMsg(phone_info, 
//				String.valueOf(start), 
//				String.valueOf(count), 
//				MyApplication.QUERY_START_TIME, 
//				MyApplication.QUERY_END_TIME,
//				MyApplication.COMM_NUM);
//				
////				byte[] request = GetOperaterDetailNode.getOpraterDetailMsg(phone_info, start_time2, end_time2,
////						"1","C");
//		mRunningTask = new GetHuiZongTask(request);
//		mRunningTask.execute();
		
		buildQueryMsg(MyApplication.CURRENT_LINE_NUM,null,null,start,count);
		
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
	 */
	private void buildQueryMsg(String line_num,String card_num,String cust_num
			,int start,int count){
		
		String phone_info = MyApplication.getSixtyValue3(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
//		String query_tag = MyApplication.EMPLOYEE;
		
		byte[] request = GetOperaterDetailNode.getOpraterDetailMsg(MyApplication.CURRENT_QUERY_TYPE,
				phone_info, 
				MyApplication.QUERY_START_TIME, 
				MyApplication.QUERY_END_TIME,
				String.valueOf(MyApplication.CURRENT_QUERY_TYPE),
				MyApplication.GET_OPERATER_DETAIL_QUERY_TYPE,
				line_num,
				null,
				null,
				String.valueOf(start),
				String.valueOf(count),
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
	 * 数据适配器
	 * @author ShawnXiao
	 *
	 */
	private class MyDataAdapter extends BaseAdapter{
		private ArrayList<DealNode> mDatas;
		private Context mContext;
		private LayoutInflater mInflater;
		public MyDataAdapter(Context context,ArrayList<DealNode> datas){
			mDatas = datas;
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
		}
		
		public void removeData(){
			if(mDatas!=null&&mDatas.size()>0){
				mDatas.clear();
			}
		}
		
		private void removeNode(DealNode node){
			if(mDatas!=null&&mDatas.size()>0){
				if(node!=null){
					mDatas.remove(node);
				}
			}
		}
		
		public void addData(DealNode node){
			if(!mDatas.contains(node)){
				mDatas.add(node);
			}
			
		}
		public void addDatas(ArrayList<DealNode> list){
			
			for(DealNode node:list){
				if(!mDatas.contains(node)){
					mDatas.add(node);
				}
			}
		}
		
		public ArrayList<DealNode> getData(){
			return mDatas;
		}

		@Override
		public int getCount() {
			return mDatas.size();
		}

		@Override
		public Object getItem(int position) {
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			final DealNode node = mDatas.get(position);
			ViewHolder holder = null;
			if(mCurrentType == TYPE_CHEXIAO){
				convertView = mInflater.inflate(R.layout.recha_and_recha_cancle_item2, null);
				holder = new ViewHolder();
				holder.tv_time_value = (TextView) convertView.findViewById(R.id.tv_time_value);
				holder.tv_type_value = (TextView) convertView.findViewById(R.id.tv_type_value);
				holder.tv_money_value = (TextView) convertView.findViewById(R.id.tv_money_value);
				holder.tv_cust_num_value = (TextView) convertView.findViewById(R.id.tv_cust_num_value);
				holder.tv_cust_name_value = (TextView) convertView.findViewById(R.id.tv_cust_name_value);
				holder.tv_xianluhao_value = (TextView) convertView.findViewById(R.id.tv_xianluhao_value);
				holder.btn_cancle = (Button) convertView.findViewById(R.id.btn_cancle);
//				convertView.setTag(holder);
//			}else{
//				holder = (ViewHolder) convertView.getTag();
//			}
			
			if(node!=null){
				if(!node.getDeal_summry().equals(DealNode.STRING_OPEN_CARD)){
					if(node.getDeal_time()!=null&&node.getDeal_time().length()>0){
						Date date = new Date();
						SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try{
							date = format1.parse(node.getDeal_time());
						}catch(Exception e){
							e.printStackTrace();
						}
						SimpleDateFormat format2 = new SimpleDateFormat("MM-dd HH:mm:ss");
						holder.tv_time_value.setText(format2.format(date));
					}
					if(node.getDeal_type()!=null&&node.getDeal_type().length()>0){
	//					if(node.getDeal_type().equals(DealNode.TYPE_C)){
	//						holder.tv_type_value.setText(DealNode.STRING_RECHARGE);
	//					}else if(node.getDeal_type().equals(DealNode.TYPE_D)){
	//						holder.tv_type_value.setText(DealNode.STRING_CANCLE_RECHARGE);
	//					}
						if(node.getDeal_summry().equals(DealNode.KEY_RECHARGE)){
							holder.tv_type_value.setText(DealNode.STRING_RECHARGE);
						}else if(node.getDeal_summry().equals(DealNode.KEY_RECHARGE_CANCLE)){
							holder.tv_type_value.setText(DealNode.STRING_CANCLE_RECHARGE);
						}
					}
					if(node.getDeal_money()!=null&&node.getDeal_money().length()>0){
						holder.tv_money_value.setText(node.getDeal_money()+mContext.getResources().getString(R.string.yuan));
					}
					
					if(node.getCust_num()!=null&&node.getCust_num().length()>0){
						holder.tv_cust_num_value.setText(node.getCust_num());
					}
					if(node.getCust_name()!=null&&node.getCust_name().length()>0){
						holder.tv_cust_name_value.setText(node.getCust_name());
					}
					
					if(node.getXianluhao()!=null&&node.getXianluhao().length()>0){
						holder.tv_xianluhao_value.setText(node.getXianluhao());
					}
					
					holder.btn_cancle.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							//请求网络，撤销充值
							final String val_52 = MyApplication.DEFAULT_CONSUME_KEY;
							final String val_60 = MyApplication.getSixtyValue2(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
							String str_date = node.getDeal_time();
							L.e("str_date:", ""+str_date);
							SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date date1 = new Date();
							try {
								date1 = format1.parse(str_date);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							SimpleDateFormat format2 = new SimpleDateFormat("HHmmss");
							SimpleDateFormat format3 = new SimpleDateFormat("MMdd");
							SimpleDateFormat format4 = new SimpleDateFormat("yyyyMMdd");
							final String str_date2=format3.format(date1);
							final String str_time2 = format2.format(date1);
							final String str_lin_date = format4.format(date1);
							final String val_serl_num = getSerialNum(node.getOrinal_line());
							System.out.println("流水号:"+val_serl_num);
							L.e("交易时间:", ""+str_time2);
							L.e("交易日期:", ""+str_date2);
							L.e("62域时间:", ""+str_lin_date);
							
							showAlertDialog(mContext.getResources().getString(R.string.warn_prompt),
									mContext.getResources().getString(R.string.sure_to_cancle_recharge), 
									new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss();
											//组织消费撤销报文
											byte[] request = RechargeCancleNode.buildRequest(str_lin_date,node.getCardNum(), node.getDeal_money(), 
													val_serl_num, str_time2, str_date2, val_52, val_60);
											
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
			}
			
			}else{
				convertView = mInflater.inflate(R.layout.recha_and_recha_cancle_item, null);
				holder = new ViewHolder();
				holder.tv_time_value = (TextView) convertView.findViewById(R.id.tv_time_value);
				holder.tv_type_value = (TextView) convertView.findViewById(R.id.tv_type_value);
				holder.tv_money_value = (TextView) convertView.findViewById(R.id.tv_money_value);
				holder.tv_cust_num_value = (TextView) convertView.findViewById(R.id.tv_cust_num_value);
				holder.tv_cust_name_value = (TextView) convertView.findViewById(R.id.tv_cust_name_value);
				holder.tv_xianluhao_value = (TextView) convertView.findViewById(R.id.tv_xianluhao_value);
//				convertView.setTag(holder);
//			}else{
//				holder = (ViewHolder) convertView.getTag();
//			}
			
			if(node!=null){
				if(!node.getDeal_summry().equals(DealNode.STRING_OPEN_CARD)){
					if(node.getDeal_time()!=null&&node.getDeal_time().length()>0){
						holder.tv_time_value.setText(node.getDeal_time());
					}
					if(node.getDeal_type()!=null&&node.getDeal_type().length()>0){
	//					if(node.getDeal_type().equals(DealNode.TYPE_C)){
	//						holder.tv_type_value.setText(DealNode.STRING_RECHARGE);
	//					}else if(node.getDeal_type().equals(DealNode.TYPE_D)){
	//						holder.tv_type_value.setText(DealNode.STRING_CANCLE_RECHARGE);
	//					}
						if(node.getDeal_summry().equals(DealNode.KEY_RECHARGE)){
							holder.tv_type_value.setText(DealNode.STRING_RECHARGE);
						}else if(node.getDeal_summry().equals(DealNode.KEY_RECHARGE_CANCLE)){
							holder.tv_type_value.setText(DealNode.STRING_CANCLE_RECHARGE);
						}
					}
					if(node.getDeal_money()!=null&&node.getDeal_money().length()>0){
						holder.tv_money_value.setText(node.getDeal_money()+mContext.getResources().getString(R.string.yuan));
					}
					
					if(node.getCust_num()!=null&&node.getCust_num().length()>0){
						holder.tv_cust_num_value.setText(node.getCust_num());
					}
					if(node.getCust_name()!=null&&node.getCust_name().length()>0){
						holder.tv_cust_name_value.setText(node.getCust_name());
					}
					
					if(node.getXianluhao()!=null&&node.getXianluhao().length()>0){
						holder.tv_xianluhao_value.setText(node.getXianluhao());
					}
				}
			}
			
			}
			
			return convertView;
		}
		
		
		
		
		class ViewHolder{
			Button btn_cancle;
			TextView tv_time_value;
			TextView tv_type_value;
			TextView tv_money_value;
			/**
			 * 客户编号
			 */
			TextView tv_cust_num_value;
			/**
			 * 客户名称
			 */
			TextView tv_cust_name_value;
			/**
			 * 线路号
			 */
			TextView tv_xianluhao_value;
		}
		
	}
	
	
	/**
	 * 充值撤销任务
	 * @author ShawnXiao
	 *
	 */
	private class RechargeCancleTask extends AsyncTask{

		private DealNode mNode;
		private byte[] mRequest;
		public RechargeCancleTask(DealNode node,byte[] request){
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
		private DealNode mNode;
		public RechargeCancleHandler(DealNode node){
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
						mAdapter.removeNode(mNode);
						mAdapter.notifyDataSetChanged();
					}else{
						String str = (String)node.get(63);
						if(str!=null&&str.length()>0&&!str.equals("")){
							showShortToast((String)node.get(63));
						}else{
							showShortToast(R.string.recharge_cancle_faield);
						}
						
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
}
