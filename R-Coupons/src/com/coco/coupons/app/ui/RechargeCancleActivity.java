package com.coco.coupons.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.coco.android.util.L;
import com.coco.coupons.app.BaseActivity;
import com.coco.coupons.app.R;
import com.coco.coupons.app.bean.Recharge;

/**
 * 
 * @Descriptio ��ֵ��������
 * @author Shawn
 * @Time 2013-8-28  ����4:08:56
 */
public class RechargeCancleActivity extends BaseActivity {
	/**
	 * �б���ͼ ����
	 */
	private ListView lv_consume;
	
	/**
	 * ����������
	 */
	private MyConSumeAdapter mConSumeAdapter;
	
	/**
	 * �����б�����
	 */
	private List<Recharge> mConsumes;
	
	/**
	 * ������������ť
	 */
	private Button btn_cancle;
	
	/**
	 * ���������ذ�ť
	 */
	private Button btn_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.recharge_cancle);
		((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_2);
		
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new BackListener());
		
		
		btn_cancle = (Button) findViewById(R.id.btn_sure);
		btn_cancle.setText(R.string.btn_cancel);
		btn_cancle.setVisibility(View.VISIBLE);
		btn_cancle.setOnClickListener(new CancleConsumeListener());
		
		lv_consume = (ListView) findViewById(R.id.lv_consume);
		
		
		mConsumes = new ArrayList<Recharge>();
		addTestData();
		
		mConSumeAdapter = new MyConSumeAdapter(this);
		mConSumeAdapter.setConsumes(mConsumes);
		
		lv_consume.setAdapter(mConSumeAdapter);
	}
	
	/**
	 * 
	 * @Descriptio ������һ������
	 * @author Shawn
	 * @Time 2013-8-23  ����3:50:43
	 */
	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			defaultFinish();
		}
		
	}
	
	
	//��Ӳ�������
	private void addTestData(){
		Recharge consume = new Recharge();
		consume.init(false, "2013-08-29 13:50", "90088801000001", "13960582347", "50");
		mConsumes.add(consume);
		consume = new Recharge();
		consume.init(false, "2013-08-28 14:30", "90088801000002", "13960582337", "200");
		mConsumes.add(consume);
		consume = new Recharge();
		consume.init(false, "2013-08-23 10:30", "90088801000003", "13960582342", "100");
		mConsumes.add(consume);
		consume = new Recharge();
		consume.init(false, "2013-07-20 10:30", "90088801000004", "13850582342", "150");
		mConsumes.add(consume);
	}
	
	
	
	
	/**
	 * 
	 * @Descriptio �������Ѽ�����
	 * @author Shawn
	 * @Time 2013-8-29  ����1:58:49
	 */
	private class CancleConsumeListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			L.i("CancleConsumeListener", "onClick");
		}
		
	}
	
	/**
	 * 
	 * @Descriptio �����б�������
	 * @author Shawn
	 * @Time 2013-8-28  ����4:29:34
	 */
	private class MyConSumeAdapter extends BaseAdapter{
		private List<Recharge> mConsumes;
		private Context mContext;
		private LayoutInflater mInflater;
		public MyConSumeAdapter(Context context){
			mContext = context;
			mConsumes = new ArrayList<Recharge>();
			mInflater = LayoutInflater.from(mContext);
		}
		
		public void setConsumes(List<Recharge> consumes){
			mConsumes = consumes; 
		}
		
		public void removeConsume(Recharge consume){
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
			Recharge consume = mConsumes.get(position);
			ViewHolder viewHolder = null;
			if(convertView==null){
				convertView = mInflater.inflate(R.layout.recharge_item, null);
				viewHolder = new ViewHolder();
				viewHolder.cb_cancle_consume = (CheckBox) convertView.findViewById(R.id.cb_cancle_consume);
				viewHolder.tv_consume_time = (TextView) convertView.findViewById(R.id.tv_consume_time);
				viewHolder.tv_consume_card_num = (TextView) convertView.findViewById(R.id.tv_consume_card_num);
				viewHolder.tv_consume_phone_num = (TextView) convertView.findViewById(R.id.tv_consume_phone_num);
				viewHolder.tv_sum_of_consume = (TextView) convertView.findViewById(R.id.tv_sum_of_consume);
				
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			if(consume!=null){
				viewHolder.tv_consume_time.setText(consume.getTime());
				viewHolder.tv_consume_card_num.setText(consume.getCard_num());
				viewHolder.tv_consume_phone_num.setText(consume.getPhone_num());
				viewHolder.tv_sum_of_consume.setText(consume.getSum_of_consume());
			}
			
			return convertView;
		}
		
		
		class ViewHolder{
			/**
			 * ��ѡ��ȡ������
			 */
			CheckBox cb_cancle_consume;
			/**
			 * �ı�����ʱ��
			 */
			TextView tv_consume_time;
			/**
			 * �ı����ѿ���
			 */
			TextView tv_consume_card_num;
			/**
			 * �ı������ֻ�����
			 */
			TextView tv_consume_phone_num;
			/**
			 * �ı����ѽ��
			 */
			TextView tv_sum_of_consume;
		}
	}
}
