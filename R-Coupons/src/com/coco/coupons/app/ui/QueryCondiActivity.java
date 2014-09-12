package com.coco.coupons.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coco.coupons.app.BaseActivity;
import com.coco.coupons.app.R;

/**
 * 查询条件选择界面:
 * 1、查询自己的
 * 2、查询所有员工的
 * 3、查询指定员工的
 * @author shawn
 *
 */
public class QueryCondiActivity extends BaseActivity {
	/**
	 * 查询自己按钮
	 */
	private Button btn_query_self;
	/**
	 * 查询所有员工按钮
	 */
	private Button btn_query_all_employee;
	/**
	 * 查询指定员工
	 */
	private Button btn_query_some_employee;
	/**
	 * 容器根据线路号查询
	 */
	private LinearLayout panel_query_by_employee_num;
	
	/**
	 * 编辑框线路号
	 */
	private EditText et_employee_num;
	/**
	 * 按钮根据线路号查询
	 */
	private Button btn_query_oper_detail;
	/**
	 * 返回按钮
	 */
	private Button btn_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_condi);
		//初始化控件
		initView();
		//为控件设置监听器
		setUpViewListener();
	}
	//为控件设置监听器
	public void setUpViewListener(){
		btn_back.setOnClickListener(new BackListener());
		btn_query_self.setOnClickListener(new BtnQuerySelfListener());
		btn_query_all_employee.setOnClickListener(new BtnQueryAllEmployeeListener());
		btn_query_some_employee.setOnClickListener(new BtnQuerySomeEmployeeListener());
		btn_query_oper_detail.setOnClickListener(new BtnQueryOperDetailListener());
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		panel_query_by_employee_num.setVisibility(View.GONE);
	}
	
	
	/**
	 * 根据线路号查询操作明细
	 * @author shawn
	 *
	 */
	private class BtnQuerySomeEmployeeListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			panel_query_by_employee_num.setVisibility(View.VISIBLE);
		}
	}
	
	
	/**
	 * 查询指定操作员的操作明细
	 */
	private class BtnQueryOperDetailListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			//线路号
			String line_num = et_employee_num.getText().toString();
			if(TextUtils.isEmpty(line_num)){
				showShortToast(R.string.line_num_not_null);
				return ;
			}
			Intent query_condi = new Intent(QueryCondiActivity.this,QueryRechargeDateSelectActivity.class);
			query_condi.putExtra(QueryRechargeDateSelectActivity.QUERY_TYPE_KEY, 
					QueryRechargeDateSelectActivity.TYPE_QUERY_SOME_EMPLOYEE);
			query_condi.putExtra(QueryRechargeDateSelectActivity.KEY_LINE_NUM, line_num);
			QueryCondiActivity.this.startActivity(query_condi);
			switchActivityAnimation();
			
		}
	}
	
	/**
	 * 查询自己的操作明细
	 * @author shawn
	 *
	 */
	private class BtnQuerySelfListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			Intent query_condi = new Intent(QueryCondiActivity.this,QueryRechargeDateSelectActivity.class);
			query_condi.putExtra(QueryRechargeDateSelectActivity.QUERY_TYPE_KEY, QueryRechargeDateSelectActivity.TYPE_QUERY_SELF);
			QueryCondiActivity.this.startActivity(query_condi);
			switchActivityAnimation();
		}
	}
	
	/**
	 * 查询所有员工的操作明细
	 * @author shawn
	 *
	 */
	private class BtnQueryAllEmployeeListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent query_condi = new Intent(QueryCondiActivity.this,QueryRechargeDateSelectActivity.class);
			query_condi.putExtra(QueryRechargeDateSelectActivity.QUERY_TYPE_KEY, QueryRechargeDateSelectActivity.TYPE_QUERY_ALL_EMPLOYEE);
			QueryCondiActivity.this.startActivity(query_condi);
			switchActivityAnimation();
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
	//初始化控件
	private void initView(){
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_4);
		btn_query_self = (Button) findViewById(R.id.btn_query_self);
		btn_query_all_employee = (Button) findViewById(R.id.btn_query_all_employee);
		btn_query_some_employee = (Button) findViewById(R.id.btn_query_some_employee);
		panel_query_by_employee_num = (LinearLayout) findViewById(R.id.panel_query_by_employee_num);
		panel_query_by_employee_num.setVisibility(View.GONE);
		et_employee_num = (EditText) findViewById(R.id.et_employee_num);
		btn_query_oper_detail = (Button) findViewById(R.id.btn_query_oper_detail);
	}
}
