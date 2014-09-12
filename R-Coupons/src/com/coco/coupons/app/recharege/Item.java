package com.coco.coupons.app.recharege;

import java.util.HashMap;
import java.util.Map;

/**
 * 充值实体类
 * @author Shawn
 *
 */
public class Item {
	
	public static final String BU_QUAN = "补券";
	
	public static final String BU_QUAN_YUAN_YIN = "补券原因";
	
	/**
	 * 结果文本
	 */
	public static final int TXT_FIELD = 1111;
	/**
	 * 功能按钮
	 */
	public static final int BUTTON_FIELD = 1112;
	
	/**
	 * 名称
	 */
	private String mName;
	
	/**
	 * 菜单代码
	 */
	private String mCode;
	/**
	 * 数量
	 */
	private String mCount;
	/**
	 * 补充信息
	 */
	private String mExtra;
	
	/**
	 * 布局类型
	 */
	private int mFieldType;
	
	
	
	public int getmFieldType() {
		return mFieldType;
	}
	public void setmFieldType(int mFieldType) {
		this.mFieldType = mFieldType;
	}
	public Map<String, Map<String, String>> getmEtValues() {
		return mEtValues;
	}
	public void setmEtValues(Map<String, Map<String, String>> mEtValues) {
		this.mEtValues = mEtValues;
	}
	private Map<String,Map<String,String>> mEtValues;
	
	
	
	public Item(){
		mEtValues = new HashMap<String,Map<String,String>>();
	}
	public Item(String name){
		mName = name;
		mEtValues = new HashMap<String,Map<String,String>>();
	}
	
	
	
	public String getmCode() {
		return mCode;
	}
	public void setmCode(String mCode) {
		this.mCode = mCode;
	}
	public void addEtValue(String key,Map<String,String> etValue){
		mEtValues.put(key, etValue);
	}
	
	public String getEtValue(String groupPosition,String childPosition){
		return mEtValues.get(groupPosition).get(childPosition);
	}
	
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getmCount() {
		return mCount;
	}
	public void setmCount(String mCount) {
		this.mCount = mCount;
	}
	public String getmExtra() {
		return mExtra;
	}
	public void setmExtra(String mExtra) {
		this.mExtra = mExtra;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mCode == null) ? 0 : mCode.hashCode());
		result = prime * result + ((mCount == null) ? 0 : mCount.hashCode());
		result = prime * result
				+ ((mEtValues == null) ? 0 : mEtValues.hashCode());
		result = prime * result + ((mExtra == null) ? 0 : mExtra.hashCode());
		result = prime * result + mFieldType;
		result = prime * result + ((mName == null) ? 0 : mName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (mCode == null) {
			if (other.mCode != null)
				return false;
		} else if (!mCode.equals(other.mCode))
			return false;
		if (mCount == null) {
			if (other.mCount != null)
				return false;
		} else if (!mCount.equals(other.mCount))
			return false;
		if (mEtValues == null) {
			if (other.mEtValues != null)
				return false;
		} else if (!mEtValues.equals(other.mEtValues))
			return false;
		if (mExtra == null) {
			if (other.mExtra != null)
				return false;
		} else if (!mExtra.equals(other.mExtra))
			return false;
		if (mFieldType != other.mFieldType)
			return false;
		if (mName == null) {
			if (other.mName != null)
				return false;
		} else if (!mName.equals(other.mName))
			return false;
		return true;
	}
	
	
	
	
}
