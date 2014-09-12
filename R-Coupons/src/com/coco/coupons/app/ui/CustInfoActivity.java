package com.coco.coupons.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.coco.android.util.L;
import com.coco.coupons.app.BaseActivity;
import com.coco.coupons.app.R;
import com.coco.coupons.app.bean.CustomerInfoListNode;

/**
 * 客户信息界面
 * @author ShawnXiao
 *
 */
public class CustInfoActivity extends BaseActivity{
	/**
	 * 文本客户名称
	 */
	private TextView tv_cust_name_value;
	/**
	 * 文本手机号
	 */
	private TextView tv_phone_num_value;
	
	/**
	 * 确定按钮
	 */
	private Button btn_cust_info_sure;
	
	public static final String DATA_KEY = "DATA_KEY";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cust_info);
		
		initView();
		
		setUpViewListener();
	}
	//初始化控件
	private void initView(){
		((TextView)findViewById(R.id.tv_title)).setText(R.string.cust_info_data_title);
		
		tv_cust_name_value = (TextView) findViewById(R.id.tv_cust_name_value);
		tv_phone_num_value = (TextView) findViewById(R.id.tv_phone_num_value);
		
		btn_cust_info_sure = (Button) findViewById(R.id.btn_cust_info_sure);
		
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		CustomerInfoListNode node = (CustomerInfoListNode) bundle.get(DATA_KEY);
		if(node.getmCustName()!=null&&node.getmCustName().length()>0){
			tv_cust_name_value.setText(node.getmCustName());
		}
		if(node.getmCustPhoneNum()!=null&&node.getmCustPhoneNum().length()>0){
			tv_phone_num_value.setText(node.getmCustPhoneNum());
		}
		
	}
	
	private void setUpViewListener(){
		btn_cust_info_sure.setOnClickListener(new BtnOnClickListener());
	}
	
	/**
	 * 确认按钮监听器
	 * @author ShawnXiao
	 *
	 */
	private class BtnOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			L.e("BtnOnClickListener", "BtnOnClickListener");
			CustInfoActivity.this.finish();
		}
		
	}
}	
