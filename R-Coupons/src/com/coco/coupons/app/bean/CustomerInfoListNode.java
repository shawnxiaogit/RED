package com.coco.coupons.app.bean;

import java.io.Serializable;

import com.ajrd.SafeSoft;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.util.MyStringUtil;
import com.coco.android.util.MyUtil;
import com.coco.coupons.app.MyApplication;
import com.easier.util.StringUtil;

/**
 * 客户信息列表实体类
 * @author ShawnXiao
 *
 */
public class CustomerInfoListNode extends BaseNode implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8053519070313729579L;
	/**
	 * 客户号
	 */
	private String mCustNum;
	/**
	 * 客户名称
	 */
	private String mCustName;
	
	/**
	 * 客户手机号
	 */
	private String mCustPhoneNum;
	
	
	
	
	public String getmCustPhoneNum() {
		return mCustPhoneNum;
	}



	public void setmCustPhoneNum(String mCustPhoneNum) {
		this.mCustPhoneNum = mCustPhoneNum;
	}



	public String getmCustNum() {
		return mCustNum;
	}

	
	private String mCustInfo;
	
	


	public String getmCustInfo() {
		return mCustInfo;
	}



	public void setmCustInfo(String mCustInfo) {
		this.mCustInfo = mCustInfo;
	}



	public void setmCustNum(String mCustNum) {
		this.mCustNum = mCustNum;
	}



	public String getmCustName() {
		return mCustName;
	}



	public void setmCustName(String mCustName) {
		this.mCustName = mCustName;
	}



	public static byte[] buildMsg(String val_11,String val_60,String val_61){
		CustomerInfoListNode isoMsg = new CustomerInfoListNode();
		try{
			//信息类型     
			byte[] b_mti = MyUtil.hexStringToByte(MyApplication.CUSTOMER_INFO_LIST_MTI);
			isoMsg.setMTI(new String(b_mti));
			//主位元表(即位图)
//			String hex_bit_map = "2038000000C00011";
//			byte[] b_map=MyUtil.hexStringToByte(hex_bit_map);
			byte[] b_map=MyUtil.hexStringToByte(MyApplication.CUSTOMER_INFO_LIST_MAIN_ELEMENT);
			isoMsg.setMainElement(b_map);
			//处理代码
			isoMsg.set(3,MyApplication.CUSTOMER_INFO_LIST_HANDLE_CODE);
			//系统流水号
			isoMsg.set(11,val_11);
			//当地交易时间
			isoMsg.set(12,MyApplication.getCurrentTime());
			//当地交易日期
			isoMsg.set(13,MyApplication.getCurrentDate());
			//终端号
			isoMsg.set(41,MyApplication.TERM_NUM);
			//商户号
			isoMsg.set(42,MyApplication.COMM_NUM);
			//附加数据，终端手机号
			isoMsg.set(60,val_60);
			
			//持卡人手机号
			isoMsg.set(61, val_61);
			
			
			byte[] data = RequestUtil.getMacBytes(isoMsg);
			int len = RequestUtil.getMacLen(isoMsg);
			byte[] mac =  SafeSoft.GenerateMac(DigitalTrans.byte2hex(MyApplication.MAC_KEY).getBytes(), len, data);
			byte[] byte_mac =MyUtil.hexStringToByte(new String(mac));
			//第64域信息验证码
			isoMsg.set(64,byte_mac);
			
//			System.out.println("MAC_KEY:"+DigitalTrans.byte2hex(MyApplication.MAC_KEY));
//			System.out.println("长度:"+len);
//			System.out.println("数据:"+isoMsg.getMacHexString());
//			System.out.println("MAC:"+new String(mac));
			
			
			byte[] request = RequestUtil.getBytesRequest(isoMsg);
			L.i("reuqest", ""+RequestUtil.printRequest(isoMsg));
			
			
			return request;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public static CustomerInfoListNode parseMsg(String response){
		CustomerInfoListNode node = new CustomerInfoListNode();
		try{
			if (response != null && response.length() > 0) {
				String header = response.substring(0, 8);
				node.setHeader(DigitalTrans.hexStringToString(header, 2));
				String MTI = response.substring(8, 12);
				node.setMTI(MTI);
				String mainElement = response.substring(12, 28);
				node.setMainElement(DigitalTrans.hexStringToByte(mainElement));
				String remain_data = response.substring(28);
				String bit_str = MyStringUtil.hexToBin(mainElement);
				for (int i = 0; i < bit_str.length(); i++) {
					if (((bit_str.getBytes()[i]) == 49)) {
						int field = i + 1;
						// 截取第3域(处理代码)的信息
						if (field == 3) {
							String value = remain_data.substring(0, 12);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(12);
						}
						
						// 截取第11域(系统流水号)的信息
						if (field == 11) {
							String value = remain_data.substring(0, 12);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(12);
						}
						// 截取第12域(当地交易时间HHMMSS)的信息
						if (field == 12) {
							String value = remain_data.substring(0, 12);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(12);
						}
						// 截取第13域(当地交易日期MMDD)的信息
						if (field == 13) {
							String value = remain_data.substring(0, 8);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(8);
						}
						
						// 截取第39域(返回码)的信息
						if (field == 39) {
							String value = remain_data.substring(0, 4);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(4);
						}
						// 截取第41域(终端号)的信息
						if (field == 41) {
							String value = remain_data.substring(0, 16);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(16);
						}
						
						
						// 截取第60域(保留)的信息
						// 注意也是LLLVAR格式
						if (field == 60) {
							String len1=remain_data.substring(0, 6);
							String len2=DigitalTrans.hexStringToString(len1, 2);
//							System.out.println("len2:"+len2);
							int len = Integer.parseInt(len2);
//							System.out.println("60-len:" + len);
							remain_data = remain_data.substring(6);
							if(len>0){
								String value = remain_data.substring(0, len * 2);
//								System.out.println("60-value:"+value);
								node.set(field,
										DigitalTrans.hexStringToString(value, 2));
								remain_data = remain_data.substring(len * 2);
							}
						}
						// 截取61域(附加数据)的信息
						if (field == 61) {
							String len1=remain_data.substring(0, 6);
							String len2=DigitalTrans.hexStringToString(len1, 2);
//							System.out.println("len2:"+len2);
							int len = Integer.parseInt(len2);
//							System.out.println("61-len:" + len);
							remain_data = remain_data.substring(6);
							if(len>0){
								String value = remain_data.substring(0, len * 2);
								node.set(field,value);
//								System.out.println("61-value:"+value);
								String data = new String(DigitalTrans.hex2byte(value),"GBK");
//								node.setmCustName(data);
								System.out.println("data:"+data);
								String[] str = data.split("\\|");
//								System.out.println(str.length);
//								for(int k=0;k<str.length;k++){
//									System.out.println("str ["+k+"]"+str[k]);
//								}
								if(str!=null&&str.length>0){
									if(str[0]!=null&str[0].length()>0){
//										node.setmCustNum(str[0]);
										node.setmCustPhoneNum(str[0]);
									}
									if(str.length>1){
										if(str[1]!=null&&str[1].length()>0){
//											node.setmCustName(str[1]);
											node.setmCustName(str[1]);
										}
									}
								}
								//直接设置给一个属性
//								node.setmCustInfo(data);
								
								remain_data = remain_data.substring(len * 2);
							}
						}
						//第63域
						if(field == 63){
							String len1=remain_data.substring(0, 6);
							String len2=DigitalTrans.hexStringToString(len1, 2);
							int len = Integer.parseInt(len2);
//							System.out.println("63-len:" + len);
							remain_data = remain_data.substring(6);
							if(len>0){
								String value = remain_data.substring(0, len * 2);
								node.set(field,
										new String(DigitalTrans.hexStringToByte(value),"GBK"));
								remain_data = remain_data.substring(len * 2);
							}
						}
						// 截取64域(信息验证码)BIN码
						if (field == 64) {
							String value = remain_data.substring(0, 16);
							node.set(field, value);
							remain_data = remain_data.substring(16);
						}
					}
				}
				System.out.println("remain_data:" + remain_data);
			}
			System.out.println("node.getDataString():" + node.toString());
			
			return node;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
	public static void main(String[] args) {
//		String response = "30303734081020380000028000193939323030303032343130373032313233303130333030303030303030303032303134323031333130333030323132333030303432373134B5169DA81BB19841";
		String response = "303038350810203800000280001D34304130303030333037303930373438333431313035444C303030303030303230313432303133313130353037343833343031303035303338323137323130303236304D0E13B45FA7510E";
		parseMsg(response);
	}
	
}
