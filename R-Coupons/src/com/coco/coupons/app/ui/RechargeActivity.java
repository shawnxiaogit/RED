package com.coco.coupons.app.ui;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coco.android.util.L;
import com.coco.coupons.app.BaseActivity;
import com.coco.coupons.app.MyApplication;
import com.coco.coupons.app.R;
import com.coco.coupons.app.bean.GetRechargeKeyNode;
import com.coco.coupons.app.recharege.Item;
import com.coco.coupons.app.recharege.MyRechargeAdapter;
import com.google.zxing.CaptureActivity;

/**
 * 
 * @Descriptio 充值界面
 * @author Shawn
 * @Time 2013-8-30  
 */
public class RechargeActivity extends BaseActivity{
	
	/**
	 * 点数和金额兑换比例
	 */
	private static final Double double2 = 8.5;
	
	/**
	 * 上个界面传过来的卡号关键字
	 */
	public static final String KEY_CARD_NUM = "KEY_CARD_NUM";
	/**
	 * 上个界面传过来的充值字段关键字
	 */
	public static final String KEY_RECHARGE_KEY = "KEY_RECHARGE_KEY";
	/**
	 * 卡号编辑框
	 */
	private EditText et_consume_card_num;
	/**
	 * 充值金额编辑框
	 */
	private EditText et_sum_of_consume;
	/**
	 * 充值手机号码
	 */
	private EditText et_consume_phone_num;
	/**
	 * 扫一扫按钮
	 */
	private Button btn_scan;
	
	/**
	 * 充值密码
	 */
	private EditText et_consume_pwd;
	/**
	 *确认充值按钮
	 */
	private Button btn_consume_sure;
	
	/**
	 * 扫一扫指令
	 */
	private static final int REQUEST_GET_CONDE = 0;
	
	/**
	 * 返回按钮
	 */
	private Button btn_back;
	
	/**
	 * 终端类型选择输入框
	 */
	private AutoCompleteTextView et_term_type;
	/**
	 * 终端类型下拉按钮
	 */
	private ImageButton btn_term_type_drop_down;
	
	/**
	 * 二级列表视图
	 */
	private ExpandableListView ex_lv;
	
	/**
	 * 二级列表适配器
	 */
	private MyRechargeAdapter  mExAdapter;
	/**
	 * 列表名称
	 */
	private String[] mGroupName;
	/**
	 * 数据
	 */
	private List<List<Item>> mData;
	
	
	/**
	 * 确认按钮，获取总的券数，如果有补券原因的话也获取
	 */
	private Button btn_get_recharge_count;
	
	/**
	 * 本月实发券数
	 */
	private TextView tv_total_count;
	
	/**
	 * 合计金额
	 */
	private TextView tv_total_money;
	
	/**
	 * 详情数据容器
	 */
	private LinearLayout panel_list;
	
	/**
	 * 上个界面传递过来的卡号
	 */
	private String mGetCardNum;
	
	/**
	 * 真诚合作容器
	 */
	private LinearLayout panel_hezuo;
	
	/**
	 * 真诚合作填写的券数
	 */
	private EditText et_hezuo_quanshu;
	
	/**
	 * 真诚合作总券数
	 */
	private TextView tv_hezuo_total_count;
	
	/**
	 * 真诚合作总金额
	 */
	private TextView tv_hezuo_total_money;
	
	
	/**
	 * 真诚合作充值按钮
	 */
	private Button btn_hezuo_get_recharge_count;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_recharge);
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
		btn_scan.setOnClickListener(new ScannerListener());
		
		btn_consume_sure.setOnClickListener(new RechargeListener());
		
		et_term_type.setOnItemClickListener(new TermTypeItemClickListener());
		
		btn_term_type_drop_down.setOnClickListener(new TermTypeDropClickListener());
		
		
		btn_get_recharge_count.setOnClickListener(new GetTotalCountClickListener());
		
		btn_hezuo_get_recharge_count.setOnClickListener(new GetTotalCountClickListener2());
		
	}
	
	/**
	 * 获取总的券数，如果有补券原因的话也要获取
	 * @author Shawn
	 *
	 */
	private class GetTotalCountClickListener2 implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			
			String total_count = et_hezuo_quanshu.getText().toString();
			
			
			if(TextUtils.isEmpty(total_count)){
				showShortToast(R.string.txt_count_can_not_null);
				return;
			}
			
			Double double1 = Double.parseDouble(total_count);
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			Double double3 = (double1*double2);
			L.e("double3:", ""+double3);
			
			handler.removeCallbacks(runable);
//			L.e("mCount:", ""+mCount);
			//跳转充值界面，把充值金额传过去
			Intent intent = new Intent(RechargeActivity.this,RechargeActivity3.class);
			intent.putExtra(RechargeActivity3.REQUEST_SUM_OF_CONSUME, decimalFormat.format(double3));
			intent.putExtra(RechargeActivity3.KEY_CARD_NUM, mGetCardNum);
			RechargeActivity.this.startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			
		}
	}
	/**
	 * 0个点数
	 */
	private static final int ZERO = 0;
	
	/**
	 * 获取总的券数，如果有补券原因的话也要获取
	 * @author Shawn
	 *
	 */
	private class GetTotalCountClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			L.e("GetTotalCountClickListener", "GetTotalCountClickListener");
			StringBuilder sb = new StringBuilder();
			List<List<Item>> data = mExAdapter.getData();
			L.i("data!=null:", ""+(data!=null));
			L.i("data.size()", ""+data.size());
			String buquan_yuanyin = "";
			int mCount = 0;
			if(data!=null&&data.size()>0){
				for(int i=0;i<data.size();i++){
					List<Item> items = data.get(i);
					if(items!=null&&items.size()>0){
						for(int j=0;j<items.size();j++){
							Item item = items.get(j);
							if(item!=null){
								//三级菜单代码
								String code = item.getmCode();
								//三级菜单名称
								String name = item.getmName();
								//数值
								String count = item.getmCount();
								
								if(count!=null&&count.length()>0
										&&!count.equals("0")&&!count.equals("")){
//									System.out.println("code3:"+code);
//									System.out.println("name3:"+name);
//									System.out.println("count3:"+count);
									//获取二级菜单
									Item secondMenuItem = rechargeKeyNode.getSecondMenuItem(name);
									
//									if(secondMenuItem.getmCode()!=null&&secondMenuItem.getmCode().length()>0){
//										System.out.println("secondMenuItem.getmCode():"+secondMenuItem.getmCode());
//									}
//									if(secondMenuItem.getmName()!=null&&secondMenuItem.getmName().length()>0){
//										System.out.println("secondMenuItem.getmName():"+secondMenuItem.getmName());
//									}
									
									//获取一级菜单
//									Item firstMenuItem = rechargeKeyNode.getFirstMenuItem(name,secondMenuItem.getmName());
									Item firstMenuItem = rechargeKeyNode.getFirstMenuItem(selectFirstMenuName);
									
//									if(firstMenuItem.getmCode()!=null&&firstMenuItem.getmCode().length()>0){
//										System.out.println("firstMenuItem.getmCode():"+firstMenuItem.getmCode());
//									}
									
//									if(firstMenuItem.getmName()!=null&&firstMenuItem.getmName().length()>0){
//										System.out.println("firstMenuItem.getmName():"+firstMenuItem.getmName());
//									}
									//组装61域充值报文
									String recharge_key = getRechargeKey(firstMenuItem,secondMenuItem,item);
									if(recharge_key!=null&&recharge_key.length()>0){
										sb.append(recharge_key);
									}
									System.out.println("recharge_key["+j+"]: "+recharge_key);
								}
//								String extra = item.getmExtra();
								
								
								if(count!=null&&count.length()>0){
									mCount += Integer.parseInt(count);
								}
//								L.i("name:", ""+name);
//								L.i("count:", ""+count);
//								L.i("extra:", ""+extra);
							}
						}
					}
				}
				
				
				System.out.println("recharge_key: "+sb.toString());
				if(mCount==ZERO){
					showShortToast(R.string.quanshu_can_not_zero);
					return;
				}
				
				Double double1 = Double.parseDouble(String.valueOf(mCount));
				DecimalFormat decimalFormat = new DecimalFormat("0.00");
				Double double3 = (double1*double2);
				L.e("double3:", ""+double3);
				
				handler.removeCallbacks(runable);
//				L.e("mCount:", ""+mCount);
				//跳转充值界面，把充值金额传过去
				//报充值字段传送过去
				
				String recharge_keys = null;
				if(sb.toString().length()>1){
					recharge_keys = sb.deleteCharAt(sb.toString().length()-1).toString();
				}
				L.e("recharge_keys:", ""+recharge_keys);
				if(recharge_keys!=null&&recharge_keys.length()>0){
					Intent intent = new Intent(RechargeActivity.this,RechargeActivity3.class);
					intent.putExtra(RechargeActivity3.REQUEST_SUM_OF_CONSUME, decimalFormat.format(double3));
					intent.putExtra(RechargeActivity3.KEY_CARD_NUM, mGetCardNum);
					intent.putExtra(RechargeActivity3.KEY_RECHARGE_KEYS, recharge_keys);
					RechargeActivity.this.startActivity(intent);
					overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
				}
//				finish();
			}
			
		}
		
	}
	
	
	/**
	 * 组装充值字段及点数
	 * @param firstMenuItem
	 * 一级菜单实体类
	 * @param secondMenuItem
	 * 二级菜单实体类
	 * @param thirdMenuItem
	 * 三级菜单实体类
	 * @return
	 */
	private String getRechargeKey(Item firstMenuItem,Item secondMenuItem,Item thirdMenuItem){
		L.e("getRechargeKey", "getRechargeKey");
		StringBuilder sb =  new StringBuilder();
		
		if(firstMenuItem.getmCode()!=null&&firstMenuItem.getmCode().length()>0){
			sb.append(firstMenuItem.getmCode());
			sb.append(",");
		}
		
		if(firstMenuItem.getmName()!=null&&firstMenuItem.getmName().length()>0){
			sb.append(firstMenuItem.getmName());
			sb.append(",");
		}
		
		
		if(secondMenuItem.getmCode()!=null&&secondMenuItem.getmCode().length()>0){
			sb.append(secondMenuItem.getmCode());
			sb.append(",");
		}
		if(secondMenuItem.getmName()!=null&&secondMenuItem.getmName().length()>0){
			sb.append(secondMenuItem.getmName());
			sb.append(",");
		}
		
		
		if(thirdMenuItem.getmCode()!=null&&thirdMenuItem.getmCode().length()>0){
			sb.append(thirdMenuItem.getmCode());
			sb.append(",");
		}
		if(thirdMenuItem.getmName()!=null&&thirdMenuItem.getmName().length()>0){
//			String bu_quan_reson = "";
//			if(thirdMenuItem.getmName().equals(Item.BU_QUAN_YUAN_YIN)){
//				bu_quan_reson = thirdMenuItem.getmExtra();
//			}
//			else 
			if(thirdMenuItem.getmName().equals(Item.BU_QUAN)){
				sb.append(thirdMenuItem.getmName());
				if(thirdMenuItem.getmExtra()!=null&&thirdMenuItem.getmExtra().length()>0){
					sb.append("(");
					sb.append(thirdMenuItem.getmExtra());
					sb.append(")");
				}
				sb.append(",");
				
			}else{
				sb.append(thirdMenuItem.getmName());
				sb.append(",");
			}
		}
		if(thirdMenuItem.getmCount()!=null&&thirdMenuItem.getmCount().length()>0){
			sb.append(thirdMenuItem.getmCount());
		}
		sb.append("|");
		
		return sb.toString();
	}
	
//	
//	protected void onResume() {
//		super.onResume();
//		if(mExAdapter.getData()!=null&&mExAdapter.getData().size()>0){
//			handler.postDelayed(runable, 1000);
//		}
//	};
	
	
	
	Handler handler = new Handler();
	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		L.i("onResume", "onResume");
//		L.i("mExAdapter.getData()!=null", ""+(mExAdapter.getData()!=null));
//		L.i("mData!=null:",""+(mData!=null));
//		if(mExAdapter.getData()!=null&&mExAdapter.getData().size()>0){
//			handler.postDelayed(runable, 1000);
//		}
//	}

	
	

	@Override
	protected void onRestart() {
		super.onRestart();
//		L.i("onRestart", "onRestart");
//		L.i("mExAdapter.getData()!=null", ""+(mExAdapter.getData()!=null));
//		L.i("mData!=null:",""+(mData!=null));
//		if(mExAdapter.getData()!=null&&mExAdapter.getData().size()>0){
//			handler.postDelayed(runable, 1000);
//		}
		handler.postDelayed(runable, 1000);
		
	}

	
	



	@Override
	protected void onPause() {
		super.onPause();
		handler.removeCallbacks(runable);
	}






	Runnable runable = new Runnable(){

		@Override
		public void run() {
			String buquan_yuanyin = "";
			if(mTermType == no_single_mark||mTermType==single_mark){
				List<List<Item>> data = mExAdapter.getData();
				
				int mCount = 0;
				if(data!=null&&data.size()>0){
					for(int i=0;i<data.size();i++){
						List<Item> items = data.get(i);
						if(items!=null&&items.size()>0){
							for(int j=0;j<items.size();j++){
								Item item = items.get(j);
								if(item!=null){
									String name = item.getmName();
									String count = item.getmCount();
									String extra = item.getmExtra();
									if(count!=null&&count.length()>0){
										mCount += Integer.parseInt(count);
									}
									
									
//									L.e("name.equals("+Item.BU_QUAN_YUAN_YIN+"):", ""+(name.equals(Item.BU_QUAN_YUAN_YIN)));
//									if(name.equals(Item.BU_QUAN_YUAN_YIN)){
//										buquan_yuanyin = item.getmExtra();
//										L.e("buquan_yuanyin:", ""+buquan_yuanyin);
//									}
//									L.e("name.equals("+Item.BU_QUAN+"):", ""+name.equals(Item.BU_QUAN));
//									if(name.equals(Item.BU_QUAN)){
//										item.setmExtra(buquan_yuanyin);
//										L.e("item.getmExtra():", ""+item.getmExtra());
//									}
	//								L.i("name:", ""+name);
	//								L.i("count:", ""+count);
	//								L.i("extra:", ""+extra);
								}
							}
						}
					}
				}
				tv_total_count.setText(String.valueOf(mCount)+getResources().getString(R.string.zhang));
				
				Double double1 = Double.parseDouble(String.valueOf(mCount));
				DecimalFormat decimalFormat = new DecimalFormat("0.00");
				Double double3 = (double1*double2);
//				L.e("double3:", ""+double3);
				
				tv_total_money.setText(decimalFormat.format(double3)+getResources().getString(R.string.yuan));
			}else if(mTermType ==sincere_team_work){
				String mCount = et_hezuo_quanshu.getText().toString();
				if(mCount!=null&&mCount.length()>0){
					tv_hezuo_total_count.setText(mCount+getResources().getString(R.string.zhang));
					Double double1 = Double.parseDouble(mCount);
					DecimalFormat decimalFormat = new DecimalFormat("0.00");
					Double double3 = (double1*double2);
					L.e("double3:", ""+double3);
					tv_hezuo_total_money.setText(decimalFormat.format(double3)+getResources().getString(R.string.yuan));
				}else{
					tv_hezuo_total_count.setText("0"+getResources().getString(R.string.zhang));
					tv_hezuo_total_money.setText("0.00"+getResources().getString(R.string.yuan));
				}
			}else{
				List<List<Item>> data = mExAdapter.getData();
				
				int mCount = 0;
				
				if(data!=null&&data.size()>0){
					for(int i=0;i<data.size();i++){
						List<Item> items = data.get(i);
						if(items!=null&&items.size()>0){
							for(int j=0;j<items.size();j++){
								Item item = items.get(j);
								if(item!=null){
									String name = item.getmName();
									String count = item.getmCount();
//									String extra = item.getmExtra();
									if(count!=null&&count.length()>0){
										mCount += Integer.parseInt(count);
									}
									
//									L.e("name.equals("+Item.BU_QUAN_YUAN_YIN+"):", ""+(name.equals(Item.BU_QUAN_YUAN_YIN)));
//									if(name.equals(Item.BU_QUAN_YUAN_YIN)){
//										buquan_yuanyin = item.getmExtra();
//										L.e("buquan_yuanyin:", ""+buquan_yuanyin);
//									}
//									L.e("name.equals("+Item.BU_QUAN+"):", ""+name.equals(Item.BU_QUAN));
//									if(name.equals(Item.BU_QUAN)){
//										if(buquan_yuanyin!=null&&buquan_yuanyin.length()>0&&!buquan_yuanyin.equals("")){
//											item.setmExtra(buquan_yuanyin);
//											L.e("item.getmExtra():", ""+item.getmExtra());
//										}
//									}
	//								L.i("name:", ""+name);
	//								L.i("count:", ""+count);
	//								L.i("extra:", ""+extra);
								}
							}
						}
					}
				}
				tv_total_count.setText(String.valueOf(mCount)+getResources().getString(R.string.zhang));
				
				Double double1 = Double.parseDouble(String.valueOf(mCount));
				DecimalFormat decimalFormat = new DecimalFormat("0.00");
				Double double3 = (double1*double2);
//				L.e("double3:", ""+double3);
				
				tv_total_money.setText(decimalFormat.format(double3)+getResources().getString(R.string.yuan));
			}
			handler.postDelayed(runable, 1000);
		}
	};
	
	
	
	/**
	 * 终端类型下拉按钮
	 * @author Shawn
	 *
	 */
	private class TermTypeDropClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
//			String str_ter_type = et_term_type.getText().toString();
//			if(str_ter_type!=null&&str_ter_type.length()>0){
//				et_term_type.setText("");
//			}
			et_term_type.showDropDown();
		}
		
	}
	
	/**
	 * 非单体超市
	 */
	private final int no_single_mark 		= 0;
	/**
	 * 单体超市
	 */
	private final int single_mark 		= 1;
	/**
	 * 真诚合作
	 */
	private final int sincere_team_work 	= 2;
	
	/**
	 * 选中的终端类型
	 */
	private int mTermType = -1;
	
	
	/**
	 * 选中的一级菜单名称
	 */
	private String selectFirstMenuName;
	/**
	 * 终端类型监听器
	 * @author Shawn
	 *
	 */
	private class TermTypeItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			TextView tv_menu_name = (TextView) arg1.findViewById(R.id.tv_menu_name);
			System.out.println("tv_menu_name:"+tv_menu_name.getText().toString());
			selectFirstMenuName = tv_menu_name.getText().toString();
			initData(tv_menu_name.getText().toString());
			for(int i=0;i<mData.size();i++){
				List<Item> items = mData.get(i);
				for(int j=0;j<items.size();j++){
					System.out.println("mData["+i+"]"+"items["+j+"]"+items.get(j).getmName());
				}
			}
			mExAdapter = new MyRechargeAdapter(RechargeActivity.this, groupItems, mData);
			ex_lv.setAdapter(mExAdapter);
			panel_list.setVisibility(View.VISIBLE);
			handler.postDelayed(runable, 1000);
//			L.i("position:", ""+position);
//			mTermType = position;
//			switch(position){
//			case no_single_mark:{
//				//非单体超市
//				initData();
//				mExAdapter = new MyRechargeAdapter(RechargeActivity.this, mGroupName, mData);
//				ex_lv.setAdapter(mExAdapter);
//				panel_list.setVisibility(View.VISIBLE);
//				panel_hezuo.setVisibility(View.GONE);
//			}break;
//			case single_mark:{
//				//单体超市
//				initData2();
//				mExAdapter = new MyRechargeAdapter(RechargeActivity.this, mGroupName, mData);
//				ex_lv.setAdapter(mExAdapter);
//				panel_list.setVisibility(View.VISIBLE);
//				panel_hezuo.setVisibility(View.GONE);
//			}break;
//			case sincere_team_work:{
//				//真诚合作
//				//这里调整为一个EditText手机券数
//				panel_list.setVisibility(View.GONE);
//				
//				panel_hezuo.setVisibility(View.VISIBLE);
//				
//				
//			}break;
//			}
			
		}
		
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
	 * @Descriptio 扫一扫按钮监听器
	 * @author Shawn
	 * @Time 2013-8-26  
	 */
	private class ScannerListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			//进入扫一扫界面
			Intent intent_query = new Intent(RechargeActivity.this,CaptureActivity.class);
			startActivityForResult(intent_query,REQUEST_GET_CONDE);
		}
		
	}
	
	/**
	 * 
	 * @Descriptio 获取动态码按钮监听器
	 * @author Shawn
	 * @Time 2013-8-26  ����2:11:35
	 */
	private class GetDynamicCodeListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			//获取界面数据
			String card_num = et_consume_card_num.getText().toString();
			String sum_of_consume = et_sum_of_consume.getText().toString();
			String phone_num = et_consume_phone_num.getText().toString();
			if(!numAndConsumeIsNull(card_num,sum_of_consume,phone_num)){
				mRunningTask  = new GetDynamicCodeTask(card_num,sum_of_consume,phone_num);
				mRunningTask.execute(MyApplication.CONSUME_URL);
			}
		}
		
	}
	
	/**
	 * 
	 * @Descriptio 获取动态码任务
	 * @author Shawn
	 * @Time 2013-8-26  
	 */
	private class GetDynamicCodeTask extends AsyncTask<String,Integer,String>{
		private String mCardNum;
		private String mSumOfConsume;
		private String mPhoneNum;
		public GetDynamicCodeTask(String card_num,String sum_of_consume,String phone_num){
			mCardNum = card_num;
			mSumOfConsume = sum_of_consume;
			mPhoneNum = phone_num;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.getting_dynamic_code);
		}
		
		@Override
		protected String doInBackground(String... params) {
			String result = "";
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mAlertDialog.dismiss();
		}
		
	}
	
	
	/**
	 * 
	 * @Description 判断卡号和充值金额及手机号码是否为空
	 * @author Shawn
	 * @Time 2013-8-26  
	 * @param card_num 卡号
	 * @param sum_of_consume 充值金额
	 * @param phone_num 手机号
	 * @return boolean
	 */
	private boolean numAndConsumeIsNull(String card_num,String sum_of_consume,String phone_num){
		boolean result = true;
		if(TextUtils.isEmpty(card_num)){
			result = true;
			showShortToast(R.string.consume_card_num_can_not_null);
		}else{
			if(TextUtils.isEmpty(sum_of_consume)){
				result = true;
				showShortToast(R.string.sum_of_consume_can_not_null);
			}else{
				if(TextUtils.isEmpty(phone_num)){
					result = true;
					showShortToast(R.string.phone_num_can_not_null);
				}else{
					result = false;
				}
				
			}
		}
		return result;
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
			String card_num = et_consume_card_num.getText().toString();
			String sum_of_consume = et_sum_of_consume.getText().toString();
			String phone_num = et_consume_phone_num.getText().toString();
			String consume_pwd = et_consume_pwd.getText().toString();
			
			if(!numAndConsumeIsNull(card_num,sum_of_consume,phone_num)
					&&!codeAndPwdIsNull("",consume_pwd)){
				
				
				//组装充值报文
				
				mRunningTask = new ConsumeSureTask(card_num,sum_of_consume,
						phone_num,"",consume_pwd);
				mRunningTask.execute(MyApplication.CONSUME_URL);
				
				
			}
			
		}
		
	}
	
	
	private class ConsumeSureTask extends AsyncTask<String,Integer,String>{
		private String mCardNum;
		private String mSumOfConsume;
		private String mPhoneNum;
		private String mDynamicCode;
		private String mConsumePwd;
		public ConsumeSureTask(String card_num,String sum_of_consume,
				String phone_num,String dynamic_code,String consume_pwd){
			mCardNum = card_num;
			mSumOfConsume = sum_of_consume;
			mPhoneNum = phone_num;
			mDynamicCode = dynamic_code;
			mConsumePwd = consume_pwd;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.consume_processing);
		}
		
		@Override
		protected String doInBackground(String... params) {
			String result = "";
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mAlertDialog.dismiss();
		}
		
	}
	
	
	/**
	 * 
	 * @Description 动态码和密码是否为空
	 * @author Shawn
	 * @Time 2013-8-26  
	 * @param dynamic_code 
	 * @return boolean
	 * @exception exception
	 */
	private boolean codeAndPwdIsNull(String dynamic_code,String consume_pwd){
		boolean result = true;
		if(TextUtils.isEmpty(dynamic_code)){
			result = true;
			showShortToast(R.string.dynamic_code_can_not_null);
		}else{
			if(TextUtils.isEmpty(consume_pwd)){
				result = true;
				showShortToast(R.string.consume_pwd_can_not_null);
			}else{
				result = false;
			}
		}
		return result;
	}
	
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
		
		et_consume_card_num = (EditText) findViewById(R.id.et_consume_card_num);
		et_sum_of_consume = (EditText) findViewById(R.id.et_sum_of_consume);
		et_consume_phone_num = (EditText) findViewById(R.id.et_consume_phone_num);
		
		
		btn_scan= (Button) findViewById(R.id.btn_scan);
		
		et_consume_pwd = (EditText) findViewById(R.id.et_consume_pwd);
		
		btn_consume_sure = (Button) findViewById(R.id.btn_consume_sure);
		
		
		et_term_type = (AutoCompleteTextView) findViewById(R.id.et_term_type);
		btn_term_type_drop_down = (ImageButton) findViewById(R.id.btn_term_type_drop_down);
		
//		ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.tern_type_array,
//				R.layout.simple_dropdown_item_1line);
		
		
//		et_term_type.setAdapter(adapter);
		

		
		ex_lv = (ExpandableListView) findViewById(R.id.ex_lv);
		
		
//		initData();
//		mExAdapter = new MyRechargeAdapter(RechargeActivity.this, mGroupName, mData);
//		ex_lv.setAdapter(mExAdapter);
		
		btn_get_recharge_count = (Button) findViewById(R.id.btn_get_recharge_count);
		tv_total_count = (TextView) findViewById(R.id.tv_total_count);
		
		tv_total_money = (TextView) findViewById(R.id.tv_total_money);
		
		panel_list = (LinearLayout) findViewById(R.id.panel_list);
		panel_list.setVisibility(View.GONE);
	
		
		
		Intent intent = getIntent();
//		mGetCardNum = intent.getExtras().getString(KEY_CARD_NUM);
		Bundle bundle = intent.getExtras();
		rechargeKeyNode = (GetRechargeKeyNode) bundle.get(KEY_RECHARGE_KEY);
		mGetCardNum = bundle.getString(KEY_CARD_NUM);
		panel_hezuo = (LinearLayout) findViewById(R.id.panel_hezuo);
		panel_hezuo.setVisibility(View.GONE);
		et_hezuo_quanshu = (EditText) findViewById(R.id.et_hezuo_quanshu);
		tv_hezuo_total_count = (TextView) findViewById(R.id.tv_hezuo_total_count);
		tv_hezuo_total_money = (TextView) findViewById(R.id.tv_hezuo_total_money);
		
		btn_hezuo_get_recharge_count = (Button) findViewById(R.id.btn_hezuo_get_recharge_count);
		
		
		
//		ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.tern_type_array,
//		R.layout.simple_dropdown_item_1line);
		String[] firstMenuArray = rechargeKeyNode.getFirstMenuArray();
		//去掉空值
		ArrayList<String> tmp_arr = new ArrayList<String>();
		for(String str:firstMenuArray){
			if(str!=null&&str.length()>0&&!str.equals("")&&!str.equals("null")){
				tmp_arr.add(str);
			}
		}
		firstMenuArray = tmp_arr.toArray(new String[0]);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.simple_dropdown_item_1line,firstMenuArray);
		

		et_term_type.setAdapter(adapter);
	}
	
	

	
	/**
	 * 充值字段实体类
	 */
	private GetRechargeKeyNode rechargeKeyNode;
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(runable);
	}
	
	
	private int[] mGroupArray = new int[]{
			R.array.base_succ_image_array,
			R.array.brand_display_array,
			R.array.re_ticket_array
	};
	
	private int[] mGroupArray2 = new int[]{
			R.array.single_base_succ_image_array,
			R.array.single_brand_display_array,
			R.array.re_ticket_array
	};
	
	/**
	 * 初始化二级列表数据
	 */
	private void initData(){
		mGroupName = getStringArray(R.array.groups);
		mData = new ArrayList<List<Item>>();
		for(int i=0;i<mGroupName.length;i++){
			List<Item> items = new ArrayList<Item>();
			String[] childs = getStringArray(mGroupArray[i]);
			for(int j=0;j<childs.length;j++){
				Item item = new Item(childs[j]);
				items.add(item);
			}
			mData.add(items);
		}
		
//		Item item = new Item();
//		item.setmFieldType(Item.TXT_FIELD);
//		mData.add(object)
		
		
	}
	
	
	private List<Item> groupItems;
	/**
	 * 初始化二级列表数据
	 */
	private void initData(String firstMenuName){
//		mGroupName = rechargeKeyNode.getSecondMenuArray(firstMenuName);
		L.i("initData", "initData");
		groupItems = rechargeKeyNode.getSecondMenus(firstMenuName);
		for(Item item:groupItems){
			L.e("item.getmName():", ""+item.getmName());
		}
		
		mData = new ArrayList<List<Item>>();
		for(int i=0;i<groupItems.size();i++){
//			List<Item> items = new ArrayList<Item>();
////			String[] childs = getStringArray(mGroupArray[i]);
//			String[] childs = rechargeKeyNode.getThirdMenuArray(firstMenuName, mGroupName[i]);
//			if(childs!=null&&childs.length>0){
//				for(int j=0;j<childs.length;j++){
//					if(childs[j]!=null&&childs[j].length()>0){
//						Item item = new Item(childs[j]);
//						items.add(item);
//					}
//				}
//			}
			List<Item> items1 = new ArrayList<Item>();
			List<Item> items = rechargeKeyNode.getThirdMenus(firstMenuName, groupItems.get(i).getmName());
			
			for(Item item:items){
				if(item.getmName()!=null&&item.getmName().length()>0){
					items1.add(item);
					//如果是补券的话，新加一个补券原因的项
//					if(item.getmName().equals(Item.BU_QUAN)){
//						Item item3 = new Item();
//						item3.setmName(Item.BU_QUAN_YUAN_YIN);
//						items1.add(item3);
//					}
				}
			}
			if(items1!=null&&items1.size()>0){
				mData.add(items1);
			}
		}
		
//		Item item = new Item();
//		item.setmFieldType(Item.TXT_FIELD);
//		mData.add(object)
		
		
	}
	
	
	/**
	 * 初始化二级列表数据2
	 */
	private void initData2(){
		mGroupName = getStringArray(R.array.groups);
		mData = new ArrayList<List<Item>>();
		for(int i=0;i<mGroupName.length;i++){
			List<Item> items = new ArrayList<Item>();
			String[] childs = getStringArray(mGroupArray2[i]);
			for(int j=0;j<childs.length;j++){
				Item item = new Item(childs[j]);
				items.add(item);
			}
			mData.add(items);
		}
	}
	
	private String[] getStringArray(int resId){
		return getResources().getStringArray(resId);
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
			et_consume_card_num.setText(result);
		}
	}
}
