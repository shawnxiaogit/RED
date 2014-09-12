package com.coco.coupons.app.bean;

import java.io.UnsupportedEncodingException;

import com.ajrd.SafeSoft;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.util.MyStringUtil;
import com.coco.android.util.MyUtil;
import com.coco.coupons.app.MyApplication;
import com.easier.util.StringUtil;

/**
 * 开卡报文实体类
 * @author Shawn
 *
 */
public class OpenCardNode extends BaseNode{
	
	/**
	 * 组长开卡报文
	 * @return
	 */
	/**
	 * 
	 * @param val_2
	 * 卡号
	 * @param val_11
	 * 流水号
	 * @param val_60
	 * 11位手机号+15位SIM卡标识+14位手机设备的标识 
	 * @param val_61
	 * @return
	 */
	public static byte[] buidOpenCardMsg(String val_2,String val_11,String val_60,String val_61){
		OpenCardNode isoMsg = new OpenCardNode();
		
		try{
			//信息类型
			byte[] b_mti = MyUtil.hexStringToByte(MyApplication.OPEN_CARD_MTI);
			isoMsg.setMTI(new String(b_mti));
			//主位元素(即位图)
			String hex_bit_map = "6038040000C0001D";
			byte[] b_map=MyUtil.hexStringToByte(hex_bit_map);
//			byte[] b_map=MyUtil.hexStringToByte(MyApplication.OPEN_CARD_MAIN_ELEMENT);
			isoMsg.setMainElement(b_map);
			//主帐号
			isoMsg.set(2, RequestUtil.getLLVARData(val_2));
			//处理码
			isoMsg.set(3, MyApplication.OPEN_CARD_HANDLE_CODE);
			//第11域流水号
			isoMsg.set(11, val_11);
			//第12域当地交易时间
			isoMsg.set(12, MyApplication.getCurrentTime());
			//POS输入方式
			isoMsg.set(22, MyApplication.POS_INPUT_TYPE);
			//第13域当地交易日期
			isoMsg.set(13, MyApplication.getCurrentDate());
			//第41域终端号
			isoMsg.set(41, MyApplication.TERM_NUM);
			//第42域商户号
			isoMsg.set(42, MyApplication.COMM_NUM);
			//附加数据 11位手机号+15位SIM卡标识+14位手机设备的标识 
			isoMsg.set(60,val_60);
			//第61域 客户号+“|”+客户名称
			L.i("val_61:", ""+val_61);
			isoMsg.set(61, val_61);
			//第62域票据号
			isoMsg.set(62, MyApplication.get62Value2());
//			L.i("MyApplication.get62Value2():", ""+MyApplication.get62Value2());
			
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
//			L.i("request:", isoMsg.getDataString());
			L.i("request:", ""+RequestUtil.printRequest(isoMsg));
			
			return request;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static OpenCardNode parseOpenCardMsg(String response){
		
		OpenCardNode node = new OpenCardNode();
		try{
			if(response!=null&&response.length()>0){
				//报文头，及长度
				String header = response.substring(0, 8);
				node.setHeader(DigitalTrans.hexStringToString(header, 2));
				//消息类型
				String MTI = response.substring(8, 12);
				node.setMTI(MTI);
				//主位元素(即位图)
				String mainElement = response.substring(12, 28);
				node.setMainElement(DigitalTrans.hex2byte(mainElement));
				String remain_data = response.substring(28);
//				System.out.println("remain_data:"+remain_data);
				//将位图转化为2进制
				String bit_str = MyStringUtil.hexToBin(mainElement);
				
				for(int i=0;i<bit_str.length();i++){
					if(bit_str.getBytes()[i]==49){
						//域为i+1
						int field = i+1;
						//第2域主帐号
						if(field == 2){
							//前4位为长度
							String len1=remain_data.substring(0, 4);
//							System.out.println("len1:"+len1);
							String len2 = DigitalTrans.hexStringToString(len1, 2);
//							System.out.println("len2:"+len2);
							int len = Integer.parseInt(len2);
							remain_data = remain_data.substring(4);
							if(len>0){
								String value = remain_data.substring(0, len*2);
								node.set(field, DigitalTrans.hexStringToString(value, 2));
								remain_data = remain_data.substring(len*2);
							}
						}
						
						//第3域处理代码
						if(field == 3){
							String value = remain_data.substring(0, 12);
							node.set(field, DigitalTrans.hexStringToString(value, 2));
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
						//第39域返回码
						if(field == 39){
							String value = remain_data.substring(0, 4);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(4);
						}
						//第41域终端号
						if(field==41){
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
						//第61域保留
						if(field == 61){
							String len1=remain_data.substring(0, 6);
							String len2=DigitalTrans.hexStringToString(len1, 2);
							int len = Integer.parseInt(len2);
//							System.out.println("61-len:" + len);
							remain_data = remain_data.substring(6);
							if(len>0){
								String value = remain_data.substring(0, len * 2);
								node.set(field,
										DigitalTrans.hexStringToString(value, 2));
								remain_data = remain_data.substring(len * 2);
							}
						}
						//第62域票据号
						if(field == 62){
							String len1=remain_data.substring(0, 6);
							String len2=DigitalTrans.hexStringToString(len1, 2);
							int len = Integer.parseInt(len2);
//							System.out.println("62-len:" + len);
							remain_data = remain_data.substring(6);
							if(len>0){
								String value = remain_data.substring(0, len * 2);
								node.set(field,
										DigitalTrans.hexStringToString(value, 2));
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
								byte[] data = DigitalTrans.hex2byte(value);
//								node.set(field,
//										DigitalTrans.hexStringToString(value, 2));
								try {
									node.set(field, new String(data,"GBK"));
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								remain_data = remain_data.substring(len * 2);
							}
						}
						//截取64域(信息验证码)BIN码
						if (field == 64) {
							if(remain_data!=null&&remain_data.length()>0){
								String value = remain_data.substring(0, 16);
									if(value!=null&&value.length()>0){
									node.set(field, value);
									remain_data = remain_data.substring(16);
								}
							}
						}
						
						
						
					}
				}
				System.out.println("remain_data:"+remain_data);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("node.toString():"+node.toString());
		return node;
	}
	
	public static void main(String[] args) {
		String response = "303039360810203800000280001D343041303030303236303036303931393037313033303030303030303030303230313432303133313033303039313930373032313130303030303130303030303030303030303033383030323630767357D182205BC4";
		parseOpenCardMsg(response);
	}
	
}
