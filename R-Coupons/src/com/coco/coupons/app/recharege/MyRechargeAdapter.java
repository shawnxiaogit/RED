package com.coco.coupons.app.recharege;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coco.android.util.L;
import com.coco.coupons.app.R;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 充值列表适配器
 * @author Shawn
 *
 */
public class MyRechargeAdapter extends BaseExpandableListAdapter{
	private Context mContext;
//	private String[] mGroupName;
	private List<Item> mGroupItem;
	private List<List<Item>> mData;
	private LayoutInflater mInfalter;
	
	
	public MyRechargeAdapter(Context context,List<Item> groupItem,
			List<List<Item>> data){
		mContext = context;
//		mGroupName = groupName;
		mGroupItem = groupItem;
		mData = data;
		mInfalter = LayoutInflater.from(mContext);
	}
	
	
	public List<List<Item>> getData(){
		return mData;
	}
	
	@Override
	public Item getChild(int groupPosition, int childPosition) {
		return mData.get(groupPosition).get(childPosition);
	}
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		final Item  child = getChild(groupPosition, childPosition);
		//判断是否为补券原因
		if(child.getmName().equals(Item.BU_QUAN)){
			ChildViewHolder2 childViewHolder = null;
//			if(convertView==null){
				childViewHolder = new ChildViewHolder2();
				convertView = mInfalter.inflate(R.layout.child_item_layout2, null);
				childViewHolder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
				childViewHolder.et_count = (TextView) convertView.findViewById(R.id.et_count);
				childViewHolder.et_bu_quan_reason = (EditText) convertView.findViewById(R.id.et_bu_quan_reason);
//				
				int [] tag = new int[]{groupPosition,childPosition};
				childViewHolder.et_bu_quan_reason.setTag(tag);
				childViewHolder.et_bu_quan_reason.addTextChangedListener(new CustTextWatcher2(childViewHolder) {
					
					@Override
					public void afterTextChanged(Editable s, ChildViewHolder2 holder) {
						int[] po = (int[]) holder.et_bu_quan_reason.getTag();
						cacheData2(s.toString(),po[0],po[1]);
					}
				});
				childViewHolder.et_count.setTag(tag);
				childViewHolder.et_count.addTextChangedListener(new CustTextWatcher3(childViewHolder) {
					
					@Override
					public void afterTextChanged3(Editable s, ChildViewHolder2 holder) {
						int[] po = (int[]) holder.et_count.getTag();
						cacheData(s.toString(),po[0],po[1]);
					}
				});
				
//				convertView.setTag(childViewHolder);
//			}
//				else{
//				childViewHolder = (ChildViewHolder2) convertView.getTag();
//				int [] tag = new int[]{groupPosition,childPosition};
//				childViewHolder.et_bu_quan_reason.setTag(tag);
//			}
			
			if(child.getmName()!=null&&child.getmName().length()>0){
				childViewHolder.tv_item_name.setText(child.getmName()+":");
			}
			if(child.getmCount()!=null&&child.getmCount().length()>0&&!child.getmCount().equals("0")){
				childViewHolder.et_count.setText(child.getmCount());
			}else{
				childViewHolder.et_count.setText("");
			}
			if(child.getmExtra()!=null&&child.getmExtra().length()>0){
				childViewHolder.et_bu_quan_reason.setText(child.getmExtra());
			}
			
			
			
			return convertView;
		}
		else{
			ChildViewHolder childViewHolder=null;
//			if(convertView==null){
				childViewHolder = new ChildViewHolder();
				convertView = mInfalter.inflate(R.layout.child_item_layout, null);
				childViewHolder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
				childViewHolder.et_count = (EditText) convertView.findViewById(R.id.et_count);
				int [] tag = new int[]{groupPosition,childPosition};
				childViewHolder.et_count.setTag(tag);
				childViewHolder.et_count.addTextChangedListener(new CustTextWatcher(childViewHolder) {
					
					@Override
					public void afterTextChanged(Editable s, ChildViewHolder holder) {
						int[] po = (int[]) holder.et_count.getTag();
						cacheData(s.toString(),po[0],po[1]);
					}
				});
				
//				convertView.setTag(childViewHolder);
//			}else{
//				childViewHolder = (ChildViewHolder) convertView.getTag();
//				int [] tag = new int[]{groupPosition,childPosition};
//				childViewHolder.et_count.setTag(tag);
//			}
			
			if(child.getmName()!=null&&child.getmName().length()>0){
				childViewHolder.tv_item_name.setText(child.getmName()+":");
				
			}
			if(child.getmCount()!=null&&child.getmCount().length()>0&&!child.getmCount().equals("0")){
				childViewHolder.et_count.setText(child.getmCount());
			}else{
				childViewHolder.et_count.setText("");
			}
			return convertView;
		}
//		return null;
		
	}
	
	
	private void cacheData(String data,int groupPostion,int childPosition){
//		L.e("cacheData", "cacheData()");
//		L.e("cacheData", "data:"+data+" groupPostion:"+groupPostion+" childPosition"+childPosition);
		List<Item> items = mData.get(groupPostion);
		Item item = items.get(childPosition);
		item.setmCount(data);
		items.set(childPosition, item);
		mData.set(groupPostion, items);
	}
	private void cacheData2(String data,int groupPostion,int childPosition){
//		L.e("cacheData", "cacheData()");
//		L.e("cacheData", "data:"+data+" groupPostion:"+groupPostion+" childPosition"+childPosition);
		List<Item> items = mData.get(groupPostion);
		Item item = items.get(childPosition);
		item.setmExtra(data);
		items.set(childPosition, item);
		mData.set(groupPostion, items);
	}
	
	private abstract class CustTextWatcher3 implements TextWatcher{
		
		private ChildViewHolder2 mHolder;
		public CustTextWatcher3(ChildViewHolder2 holder){
			mHolder = holder;
		}
		
		@Override
		public void afterTextChanged(Editable arg0) {
			afterTextChanged3(arg0, mHolder);
			
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		public abstract void afterTextChanged3(Editable s,ChildViewHolder2 holder);
	}
	
	private abstract class CustTextWatcher implements TextWatcher{
		
		private ChildViewHolder mHolder;
		public CustTextWatcher(ChildViewHolder holder){
			mHolder = holder;
		}
		
		@Override
		public void afterTextChanged(Editable arg0) {
			afterTextChanged(arg0, mHolder);
			
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		public abstract void afterTextChanged(Editable s,ChildViewHolder holder);
	}
	
private abstract class CustTextWatcher2 implements TextWatcher{
		
		private ChildViewHolder2 mHolder;
		public CustTextWatcher2(ChildViewHolder2 holder){
			mHolder = holder;
		}
		
		@Override
		public void afterTextChanged(Editable arg0) {
			afterTextChanged(arg0, mHolder);
			
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		public abstract void afterTextChanged(Editable s,ChildViewHolder2 holder);
	}
	
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return mData.get(groupPosition).size();
	}
	@Override
	public List<Item> getGroup(int groupPosition) {
		return mData.get(groupPosition);
	}
	@Override
	public int getGroupCount() {
		return mData.size();
	}
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInfalter.inflate(R.layout.group_item_layout, null);
		}
//		String groupName = mGroupName[groupPosition];
		Item item = mGroupItem.get(groupPosition);
		GroupViewHolder groupViewHolder = new GroupViewHolder();
		groupViewHolder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
		groupViewHolder.iv_indicator = (ImageView) convertView.findViewById(R.id.iv_indicator);
		if(!isExpanded){
			groupViewHolder.iv_indicator.setBackgroundResource(R.drawable.selector_down_arrows);
		}else{
			groupViewHolder.iv_indicator.setBackgroundResource(R.drawable.selector_up_arrows);
		}
		
		groupViewHolder.tv_group_name.setText(item.getmName());
		return convertView;
	}
	@Override
	public boolean hasStableIds() {
		return false;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	
	private class ChildViewHolder{
		TextView tv_item_name;
		EditText et_count;
	}
	
	private class ChildViewHolder2{
		TextView tv_item_name;
		TextView et_count;
		EditText et_bu_quan_reason;
	}

	
	private class GroupViewHolder{
		TextView  tv_group_name;
		ImageView iv_indicator;
	}
}
