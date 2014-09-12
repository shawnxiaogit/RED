package com.coco.coupons.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.util.Log;

import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.util.MyUtil;
import com.coco.coupons.app.recharege.Item;


/**
 * 获取充值字段报文实体类
 * @author ShawnXiao
 *
 */
public class GetRechargeKeyNode extends BaseNode implements Serializable{

	private static final long serialVersionUID = 2899098592578089778L;
	
	/**
	 * 手机相关信息 11位手机号+15位SIM卡标识+14位手机设备的标识
	 */
	private String phoneInfo;
	/**
	 * 当前返回记录位置
	 */
	private String current_return_falg;
	/**
	 * 本次返回记录数
	 */
	private String current_return_count;
	/**
	 * 总记录数
	 */
	private String total_count;
	/**
	 * 记录个数
	 */
	private String record_count;
	
	/**
	 * 充值项更新时间
	 */
	private String update_recharge_time;
	
	private ArrayList<RechargeKey> rechargeKeys;
	public GetRechargeKeyNode(){
		rechargeKeys = new ArrayList<RechargeKey>();
	}
	
	
	
	
	
	public String getUpdate_recharge_time() {
		return update_recharge_time;
	}





	public void setUpdate_recharge_time(String update_recharge_time) {
		this.update_recharge_time = update_recharge_time;
	}





	public ArrayList<RechargeKey> getRechargeKeys() {
		return rechargeKeys;
	}


	public void setRechargeKeys(ArrayList<RechargeKey> rechargeKeys) {
		this.rechargeKeys = rechargeKeys;
	}





	private class RechargeKey implements Serializable{

		private static final long serialVersionUID = -6331270203065714197L;
		/**
		 * 一级列表代码
		 */
		private String code1;
		/**
		 * 一级列表代码
		 */
		private String name1;
		/**
		 * 二级列表代码
		 */
		private String code2;
		/**
		 * 二级列表名称
		 */
		private String name2;
		/**
		 * 三级列表代码
		 */
		private String code3;
		/**
		 * 三级列表名称
		 */
		private String name3;
		/**
		 * 券数
		 */
		private String quanshu;
		public String getCode1() {
			return code1;
		}
		public void setCode1(String code1) {
			this.code1 = code1;
		}
		public String getName1() {
			return name1;
		}
		public void setName1(String name1) {
			this.name1 = name1;
		}
		public String getCode2() {
			return code2;
		}
		public void setCode2(String code2) {
			this.code2 = code2;
		}
		public String getName2() {
			return name2;
		}
		public void setName2(String name2) {
			this.name2 = name2;
		}
		public String getCode3() {
			return code3;
		}
		public void setCode3(String code3) {
			this.code3 = code3;
		}
		public String getName3() {
			return name3;
		}
		public void setName3(String name3) {
			this.name3 = name3;
		}
		public String getQuanshu() {
			return quanshu;
		}
		public void setQuanshu(String quanshu) {
			this.quanshu = quanshu;
		}
		@Override
		public String toString() {
			return "RechargeKey [code1=" + code1 + ", name1=" + name1
					+ ", code2=" + code2 + ", name2=" + name2 + ", code3="
					+ code3 + ", name3=" + name3 + ", quanshu=" + quanshu + "]";
		}
		
		
	}
	
	
	
	/**
	 * 获取一级菜单
	 * @return
	 */
	public ArrayList<Item> getFirstMenus(){
		ArrayList<Item> datas = new ArrayList<Item>();
		if(rechargeKeys!=null&&rechargeKeys.size()>0){
			for(int i=0;i<rechargeKeys.size();i++){
				RechargeKey key = rechargeKeys.get(i);
				if(key!=null){
					Item item = new Item();
					if(key.getName1()!=null&&
							key.getName1().length()>0){
						item.setmName(key.getName1());
					}
					if(key.getCode1()!=null&&
							key.getCode1().length()>0){
						item.setmCode(key.getCode1());
					}
					if(key.getQuanshu()!=null&&
							key.getQuanshu().length()>0){
						item.setmCount(key.getQuanshu());
					}
					
					datas.add(item);
				}
			}
		}
//		ArrayList<Item> datas2 = new ArrayList<Item>();
//		for(int i=datas.size()-1;i>=0;i--){
//			Item item = datas.get(i);
//			if(datas2.indexOf(i)==-1){
//				datas2.add(item);
//			}
//		}
		return removeDuplicate(datas);
//		return datas;
	}
	
	public Item getFirstMenuItem(String firstMenuName){
		Log.e("firstMenuName", ""+firstMenuName);
		ArrayList<Item> items = getFirstMenus();
		L.e("items!=null", ""+(items!=null));
		L.e("items.size()>0", ""+(items.size()>0));
		if(items!=null&&items.size()>0){
//			for(Item item:items){
//				
//				if(item!=null&&item.getmName().equalsIgnoreCase(firstMenuName)){
//					return item;
//				}
//			}
			for(int i=0;i<items.size();i++){
				Item item = items.get(i);
				if(item!=null){
					if(item.getmName()!=null){
						L.e("item.getmName():", i+""+item.getmName());
						if(item.getmName().equalsIgnoreCase(firstMenuName)){
							return item;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取一级字段字符串数组
	 * @return
	 */
	public String[] getFirstMenuArray(){
		ArrayList<Item> menus = getFirstMenus();
		ArrayList<String> menu_array = new ArrayList<String>();
		for(Item menu:menus){
			menu_array.add(menu.getmName());
		}
//		return (String[]) menu_array.toArray();
		String[] array = new String[]{};
		return menu_array.toArray(array);
	}
	
//	/**
//	 * 剔除相同的元素，注意Item要重写hashCode和equals的方法
//	 * @param list
//	 * @return
//	 */
//	public static ArrayList<Item> removeDuplicate(ArrayList<Item> list) {
//		HashSet<Item> h = new HashSet<Item>(list);
//		list.clear();
//		list.addAll(h);
////		System.out.println(list);
//		return list;
//	} 
	
	
	// 删除ArrayList中重复元素，保持顺序 
	public static ArrayList<Item> removeDuplicate(ArrayList<Item> list) {
		Set<Item> set = new HashSet<Item>();
		List<Item> newList = new ArrayList<Item>();
		for (Iterator<Item> iter = list.iterator(); iter.hasNext();) {
		Item element = iter.next();
		if (set.add(element))
			newList.add(element);
		} 
		list.clear();
		list.addAll(newList);
	//	System.out.println( " remove duplicate " + list);
		return list;
	} 
	
	/**
	 * 根据三级目录获取二级目录
	 * @param thirdMenuName
	 * @return
	 */
	public Item getSecondMenuItem(String thirdMenuName){
		System.out.println("getSecondMenuItem"+"getSecondMenuItem()");
		Item item = null;
		if(rechargeKeys!=null&&rechargeKeys.size()>0){
			
			for(int i=0;i<rechargeKeys.size();i++){
				RechargeKey key = rechargeKeys.get(i);
				if(key.getName2()!=null&&
						key.getName2().length()>0){
					System.out.println("key.getName2():"+key.getName2());
				}
				if(key.getCode2()!=null&&
						key.getCode2().length()>0){
					System.out.println("key.getCode2():"+key.getCode2());
				}

				if(key.getName3()!=null&&key.getName3().length()>0){
					System.out.println("key.getName3():"+key.getName3());
				}
				if(key!=null){
					item= new Item();
					if(key.getName3().equals(thirdMenuName)){
						if(key.getName2()!=null&&
								key.getName2().length()>0){
							item.setmName(key.getName2());
						}
						if(key.getCode2()!=null&&
								key.getCode2().length()>0){
							item.setmCode(key.getCode2());
						}
						if(key.getQuanshu()!=null&&
								key.getQuanshu().length()>0){
							item.setmCount(key.getQuanshu());
						}
						return item;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据三级目录获取二级目录
	 * @param thirdMenuName
	 * @return
	 */
	public Item getFirstMenuItem(String thirdMenuName,String secondMenuName){
		Item item = null;
		
		if(rechargeKeys!=null&&rechargeKeys.size()>0){
			for(int i=0;i<rechargeKeys.size();i++){
				RechargeKey key = rechargeKeys.get(i);
				if(key!=null){
					if(key.getName3().equals(thirdMenuName)
							&&key.getName2().equals(secondMenuName)){
						item= new Item();
						if(key.getName1()!=null&&
								key.getName1().length()>0){
							item.setmName(key.getName1());
						}
						if(key.getCode1()!=null&&
								key.getCode1().length()>0){
							item.setmCode(key.getCode1());
						}
						if(key.getQuanshu()!=null&&
								key.getQuanshu().length()>0){
							item.setmCount(key.getQuanshu());
						}
					}
				}
			}
		}
		return item;
	}
	
	/**
	 * 获取一级菜单下的二级菜单
	 * @param firstMenus
	 * 
	 * @return
	 */
	public ArrayList<Item> getSecondMenus(String firstMenus){
		ArrayList<Item> datas = new ArrayList<Item>();
		if(rechargeKeys!=null&&rechargeKeys.size()>0){
			for(int i=0;i<rechargeKeys.size();i++){
				RechargeKey key = rechargeKeys.get(i);
				if(key!=null&&key.getName1()!=null&&key.getName1().length()>0
						&&!key.getName1().equalsIgnoreCase("null")
						&&!key.getName1().equals("")){
					Item item = null;
					if(key.getName1().equals(firstMenus)){
						item= new Item();
						if(key.getName2()!=null&&
								key.getName2().length()>0&&
								!key.getName2().equals("")&&!key.getName2().equalsIgnoreCase("null")){
							item.setmName(key.getName2());
						}
						if(key.getCode2()!=null&&
								key.getCode2().length()>0){
							item.setmCode(key.getCode2());
						}
						if(key.getQuanshu()!=null&&
								key.getQuanshu().length()>0){
							item.setmCount(key.getQuanshu());
						}
					}
					if(item!=null&&item.getmName()!=null&&item.getmName().length()>0
							&&!item.getmName().equals("")&&!item.getmName().equalsIgnoreCase("null")){
						datas.add(item);
					}
					
				}
			}
		}
//		ArrayList<Item> datas2 = new ArrayList<Item>();
//		for(int i=datas.size()-1;i>=0;i--){
//			Item item = datas.get(i);
//			if(datas2.indexOf(i)==-1){
//				datas2.add(item);
//			}
//		}
//		
//		return datas2;
		return removeDuplicate(datas);
		
	}
	
	/**
	 * 根据一级菜单获取二级列表字符串数组
	 * @param firstMenuName
	 * @return
	 */
	public String[] getSecondMenuArray(String firstMenuName){
		ArrayList<Item> menus = getSecondMenus(firstMenuName);
		ArrayList<String> menu_array = new ArrayList<String>();
		for(Item menu:menus){
			menu_array.add(menu.getmName());
		}
//		return (String[]) menu_array.toArray();
		String[] array = new String[]{};
		return menu_array.toArray(array);
	} 
	
	
	/**
	 * 根据一级菜单和二级菜单获取三级列表字符串数组
	 * @param firstMenuName
	 * @return
	 */
	public String[] getThirdMenuArray(String firstMenuName,String secondMenuName){
		ArrayList<Item> menus = getThirdMenus(firstMenuName,secondMenuName);
		ArrayList<String> menu_array = new ArrayList<String>();
		for(Item menu:menus){
			menu_array.add(menu.getmName());
		}
//		return (String[]) menu_array.toArray();
		String[] array = new String[]{};
		return menu_array.toArray(array);
	}
	
	
	/**
	 * 根据一级菜单和二级菜单获取三级列表字符串数组
	 * @param firstMenus
	 * 
	 * @return
	 */
	public ArrayList<Item> getThirdMenus(String firstMenus,String secondMenus){
		ArrayList<Item> datas = new ArrayList<Item>();
		if(rechargeKeys!=null&&rechargeKeys.size()>0){
			for(int i=0;i<rechargeKeys.size();i++){
				RechargeKey key = rechargeKeys.get(i);
				if(key!=null&&key.getName1()!=null&&key.getName1().length()>0
						&&!key.getName1().equals("")
						&&!key.getName1().equalsIgnoreCase("null")){
					Item item = new Item();
					if(key.getName1().equals(firstMenus)){
						if(key.getName2().equals(secondMenus)){
							if(key.getName3()!=null&&
									key.getName3().length()>0){
								item.setmName(key.getName3());
							}
							if(key.getCode3()!=null&&
									key.getCode3().length()>0){
								item.setmCode(key.getCode3());
							}
							if(key.getQuanshu()!=null&&
									key.getQuanshu().length()>0){
								item.setmCount(key.getQuanshu());
							}
						}
					}
					datas.add(item);
				}
			}
		}
//		ArrayList<Item> datas2 = new ArrayList<Item>();
//		for(int i=datas.size()-1;i>=0;i--){
//			Item item = datas.get(i);
//			if(datas2.indexOf(i)==-1){
//				datas2.add(item);
//			}
//		}
		
//		return datas2;
		return removeDuplicate(datas);
		
	}
	
	
	
	public String getTotal_count() {
		return total_count;
	}


	public void setTotal_count(String total_count) {
		this.total_count = total_count;
	}


	public String getRecord_count() {
		return record_count;
	}


	public void setRecord_count(String record_count) {
		this.record_count = record_count;
	}


	public String getPhoneInfo() {
		return phoneInfo;
	}


	public void setPhoneInfo(String phoneInfo) {
		this.phoneInfo = phoneInfo;
	}


	public String getCurrent_return_falg() {
		return current_return_falg;
	}


	public void setCurrent_return_falg(String current_return_falg) {
		this.current_return_falg = current_return_falg;
	}


	public String getCurrent_return_count() {
		return current_return_count;
	}


	public void setCurrent_return_count(String current_return_count) {
		this.current_return_count = current_return_count;
	}


	public static byte[] buildGetRechargeKeyMsg(String phone_info){
		GetRechargeKeyNode node = new GetRechargeKeyNode();
		
		try{
			byte[] b_mti = MyUtil.hexStringToByte("1002");
			node.setMTI(new String(b_mti));
			node.setPhoneInfo(phone_info);
			node.setCurrent_return_falg("1");
			node.setCurrent_return_count("99");
			
		}catch(Exception e){
			
		}
		
		return getQueryListRequest(node);
	}
	
	
	public static byte[] getQueryListRequest(GetRechargeKeyNode node){
		StringBuilder sb = new StringBuilder();
		if(node.getMTI()!=null&&node.getMTI().length()>0){
			sb.append(node.getMTI());
		}
		if(node.getPhoneInfo()!=null&&node.getPhoneInfo().length()>0){
			sb.append(node.getPhoneInfo());
			sb.append("|");
		}
		if(node.getCurrent_return_falg()!=null&&node.getCurrent_return_falg().length()>0){
			sb.append(node.getCurrent_return_falg());
			sb.append("|");
		}
		if(node.getCurrent_return_count()!=null&&node.getCurrent_return_count().length()>0){
			sb.append(node.getCurrent_return_count());
			sb.append("|");
		}
		
		String data = sb.toString();
		StringBuilder sb2 = new StringBuilder();
		int rea_len = data.length();
		if(rea_len>99&&rea_len<999){
			sb2.append("0");
		}else if(rea_len>9&&rea_len<=99){
			sb2.append("00");
		}
		sb2.append(rea_len);
		sb2.append(data);


		System.out.println("request:"+printRequest(node,rea_len));
		return sb2.toString().getBytes();
	}
	
	private static final String SPLITE_FLAG = "|";
	
	private static String printRequest(GetRechargeKeyNode node,int rea_len){
		StringBuilder sb2 = new StringBuilder();
		if(rea_len>99&&rea_len<999){
			sb2.append("0");
		}else if(rea_len>9&&rea_len<=99){
			sb2.append("00");
		}
		sb2.append(rea_len);
		
		StringBuilder sb = new StringBuilder();
		sb.append(DigitalTrans.stringToHexString(sb2.toString()));
		if(node.getMTI()!=null&&node.getMTI().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getMTI().getBytes()));
		}
		if(node.getPhoneInfo()!=null&&node.getPhoneInfo().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getPhoneInfo().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		
		
		if(node.getCurrent_return_falg()!=null&&node.getCurrent_return_falg().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getCurrent_return_falg().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		if(node.getCurrent_return_count()!=null&&node.getCurrent_return_count().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getCurrent_return_count().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		
		return sb.toString();
	}
	
	/**
	 * 返回码
	 */
	private String return_code;
	
	public String getReturn_code() {
		return return_code;
	}



	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public static GetRechargeKeyNode parseMsg(String response){
		GetRechargeKeyNode node = new GetRechargeKeyNode();
		
		try{
			if(response!=null&&response.length()>0){
				String remain_data = response;
				//报文头，及长度
				if(remain_data.length()>8){
					String header = remain_data.substring(0, 8);
					node.setHeader(DigitalTrans.hexStringToString(header, 2));
					remain_data = remain_data.substring(8);
				}
				if(remain_data.length()>4){
					//消息类型
					String MTI = remain_data.substring(0, 4);
					node.setMTI(MTI);
					remain_data = remain_data.substring(4);
				}
				if(remain_data.length()>6){
					//返回码
					String value = remain_data.substring(0, 6);
					String real_value = value.substring(0,4);
					node.setReturn_code(DigitalTrans.hexStringToString(real_value, 2));
					remain_data = remain_data.substring(6);
				}
				
				
				byte[] b_data = DigitalTrans.hex2byte(remain_data);
				String str_data = new String(b_data,"GBK");
				System.out.println("str_data:"+str_data);
				String[] datas = str_data.split("\\|");
				for(int i=0;i<datas.length;i++){
//					System.out.println("datas["+i+"] : "+datas[i]);
					if(datas!=null&&datas.length>0){
						if(i==0){
							node.setTotal_count(datas[0]);
						}
						if(datas.length>1){
							if(i==1){
								node.setRecord_count(datas[1]);
							}
						}
					}
				}
				String[] datas2 = str_data.split("\\}");
				for(int i=0;i<datas2.length;i++){
					System.out.println("datas2["+i+"] : "+datas2[i]);
					if(datas2!=null&&datas2.length>0){
						if(datas2.length>=1){
							if(i==1){
							String[] details = datas2[i].split("\\|");
								for(int j=0;j<details.length;j++){
									System.out.println("details["+j+"] : "+ details[j]);
									if(j>=1){
										if(details!=null&&details.length>0){
											if(details[j]!=null&&details[j].length()>0&&!details[j].equals("")&&!details[j].equals("null")){
											String[] details2 = details[j].split("~");
											RechargeKey key = node.new RechargeKey();
											for(int n=0;n<details2.length;n++){
//												System.out.println("details["+j+"] : "+"details2["+n+"] : "+details2[n]);
												String mdata = details2[n].replace("\"", "");
//												System.out.println("mdata:"+mdata);
												if(n==0){
													key.setCode1(mdata);
												}
												if(n==1){
													key.setName1(mdata);
												}
												if(n==2){
													key.setCode2(mdata);
												}
												if(n==3){
													key.setName2(mdata);
												}
												if(n==4){
													key.setCode3(mdata);
												}
												if(n==5){
													key.setName3(mdata);
												}
												if(n==6){
													key.setQuanshu(mdata);
												}
												
											}
											node.rechargeKeys.add(key);
											}
										}
									}
								}
							}
						}
					}
				}
				
				
//				System.out.println("mData:"+str_data);
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		printMsg(node);
//		ArrayList<Item> firstMenus = node.getFirstMenus();
//		for(int i=0;i<firstMenus.size();i++){
//			System.out.println("firstMenus["+i+"]"+firstMenus.get(i).getmName());
//		}
		return node;
	}
	
	
	private static String printMsg(GetRechargeKeyNode node){
		StringBuilder sb = new StringBuilder();
		if(node.getHeader()!=null&&node.getHeader().length()>0){
			sb.append("node.getHeader():"+node.getHeader());
			sb.append("\n");
		}
		if(node.getMTI()!=null&&node.getMTI().length()>0){
			sb.append("node.getMTI():"+node.getMTI());
			sb.append("\n");
		}
		if(node.getReturn_code()!=null&&node.getReturn_code().length()>0){
			sb.append("node.getReturn_code():"+node.getReturn_code());
			sb.append("\n");   
		}
		
		if(node.getTotal_count()!=null&&node.getTotal_count().length()>0){
			sb.append("node.getTotal_count():"+node.getTotal_count());
			sb.append("\n");
		}
		if(node.getRecord_count()!=null&&node.getRecord_count().length()>0){
			sb.append("node.getRecord_count():"+node.getRecord_count());
			sb.append("\n");
		}
		
		ArrayList<RechargeKey> keys = node.getRechargeKeys();
		for(int i=0;i<keys.size();i++){
			RechargeKey key = keys.get(i);
			if(key!=null){
				sb.append("keys["+i+"] :"+key.toString());
			}
		}
		
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	public static void main(String[] args) {
		String response = "3131353230307C31367C317C7B7C7469746C653DB2E9D1AFBFC9C0D6B3E4D6B5B5E3CAFDBBF1C8A1A3B2A3A8B6E0CCF5A3A9202020202020202020207C7472616E636F64653D2020202020207C666C643D636F6465317E31BCB6B4FAC2EB7E307E317E327E4E554C4C7C666C643D6E616D65317E31BCB6C3FBB3C67E307E32307E327E4E554C4C7C666C643D636F6465327E32BCB6B4FAC2EB7E307E317E327E4E554C4C7C666C643D6E616D65327E32BCB6C3FBB3C67E307E32307E327E4E554C4C7C666C643D636F6465337E33BCB6B4FAC2EB7E307E317E327E4E554C4C7C666C643D6E616D65337E33BCB6C3FBB3C67E307E32307E327E4E554C4C7C666C643D737562696E666F7ECAC7B7F1D3D0CFC2BCB67E307E317E327E3C303EC3BBD3D03C313ED3D07C7D7C2231227E22B7C7B5A5CCE5B3ACCAD0227E223031227E22BBF9B4A1B3C9B9A6CDBCCFF1227E223033227E22B1D8B1B8B0FCD7B0227E2230227C2231227E22B7C7B5A5CCE5B3ACCAD0227E223031227E22BBF9B4A1B3C9B9A6CDBCCFF1227E223034227E22B6E0B5E3B3C2C1D0227E2230227C2231227E22B7C7B5A5CCE5B3ACCAD0227E223032227E22C6B7C5C6B3C2C1D0227E223035227E223133CFE4B6D1227E2230227C2231227E22B7C7B5A5CCE5B3ACCAD0227E223032227E22C6B7C5C6B3C2C1D0227E223036227E22C4CCD3C5342F38CFE4B6D1227E2230227C2231227E22B7C7B5A5CCE5B3ACCAD0227E223032227E22C6B7C5C6B3C2C1D0227E223037227E22C4CCD3C5B2CABAE7B3C2C1D0227E2230227C2231227E22B7C7B5A5CCE5B3ACCAD0227E223033227E22B2B9C8AF227E223038227E22B2B9C8AF227E2230227C2232227E22B5A5CCE5B3ACCAD0227E223031227E22BBF9B4A1B3C9B9A6CDBCCFF1227E223039227E22B1F9CFE4227E2230227C2232227E22B5A5CCE5B3ACCAD0227E223031227E22BBF9B4A1B3C9B9A6CDBCCFF1227E223130227E22D6F7BBF5BCDC227E2230227C2232227E22B5A5CCE5B3ACCAD0227E223031227E22BBF9B4A1B3C9B9A6CDBCCFF1227E223131227E22B1D8B1B8B0FCD7B0227E2230227C2232227E22B5A5CCE5B3ACCAD0227E223031227E22BBF9B4A1B3C9B9A6CDBCCFF1227E223132227E22B6E0B5E3B3C2C1D0227E2230227C2232227E22B5A5CCE5B3ACCAD0227E223032227E22C6B7C5C6B3C2C1D0227E223133227E22C4CCD3C5A1A2B9FBD6ADB6D1CFE4227E2230227C2232227E22B5A5CCE5B3ACCAD0227E223032227E22C6B7C5C6B3C2C1D0227E223134227E22C4CCD3C5A1A2B9FBD6ADB6CBBCDC227E2230227C2232227E22B5A5CCE5B3ACCAD0227E223033227E22B2B9C8AF227E223135227E22B2B9C8AF227E2230227C2233227E22D5E6B3CFBACFD7F7227E223031227E22D5E6B3CFBACFD7F7227E223136227E22D5E6B3CFBACFD7F7227E2230227C2231227E22B7C7B5A5CCE5B3ACCAD0227E223031227E22BBF9B4A1B3C9B9A6CDBCCFF1227E223137227E22C9FAB6AFBBAF227E2230227C2231227E22B7C7B5A5CCE5B3ACCAD0227E223032227E22C6B7C5C6B3C2C1D0227E223138227E2236CFE4B6D1227E2230227C7C7C7C7C00000000";
		parseMsg(response);
	}
	
}
