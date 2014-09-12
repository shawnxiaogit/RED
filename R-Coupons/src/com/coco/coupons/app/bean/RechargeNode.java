package com.coco.coupons.app.bean;

import com.ajrd.SafeSoft;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.util.MyStringUtil;
import com.coco.android.util.MyUtil;
import com.coco.coupons.app.MyApplication;
import com.easier.util.StringUtil;

/**
 * 充值报文实体类
 * @author Shawn
 *
 */
public class RechargeNode extends BaseNode{
	
	
	/**
	 * 组装充值报文
	 * @return
	 */
	public static byte[] buildRechareMsg(String val_2,String val_4,String val_11,String val_60,byte[] val_61){
		RechargeNode isoMsg = new RechargeNode();
		try{
			//信息类型
			byte[] b_mti = MyUtil.hexStringToByte(MyApplication.RECHARGE_MTI);
			isoMsg.setMTI(new String(b_mti));
			//主位元表(即位图)
			String hex_bit_map = "7038040000C00019";
			byte[] b_map=MyUtil.hexStringToByte(hex_bit_map);
//			byte[] b_map=MyUtil.hexStringToByte(MyApplication.RECHARGE_MAIN_ELEMENT);
			isoMsg.setMainElement(b_map);
			//主帐号
			isoMsg.set(2, RequestUtil.getLLVARData(val_2));
			//处理码
			isoMsg.set(3, MyApplication.RECHARGE_HANDLE_CODE);
			//第4域交易金额，以分为单位，12字节长度，不足，前面补0
			isoMsg.set(4, RequestUtil.getSumOfConsume2(val_4));
			//第11域流水号
			isoMsg.set(11, val_11);
			//第12域当地交易时间
			isoMsg.set(12, MyApplication.getCurrentTime());
			//第13域当地交易日期
			isoMsg.set(13, MyApplication.getCurrentDate());
			//第14域卡有效期
//			isoMsg.set(14, value);
			//第22域POS输入方式
			isoMsg.set(22, MyApplication.POS_INPUT_TYPE);
//			isoMsg.set(23, value);
			//第24域网络标识nii
//			isoMsg.set(24, value);
			//第25域服务点条件码
//			isoMsg.set(25, value);
			//第35域2磁道内容
//			isoMsg.set(35, value);
			//第36域3磁道内容
//			isoMsg.set(36, value);
			//第41域终端号
			isoMsg.set(41, MyApplication.TERM_NUM);
			//第42域商户号
			isoMsg.set(42, MyApplication.COMM_NUM);
			
			//附加数据
			isoMsg.set(60,val_60);
//			//第60域保留项
//			byte[] b_val_60 = StringUtil.str2Bcd(val_60);
//			L.i("val_60:", ""+val_60);
//			isoMsg.set(60, val_60);
			
			//61域充值字段及点数
			isoMsg.set(61, val_61);
			//第62域票据号
//			isoMsg.set(62, MyApplication.get62Value());
			
			byte[] data = RequestUtil.getMacBytes2(isoMsg);
			int len = RequestUtil.getMacLen2(isoMsg);
			byte[] mac =  SafeSoft.GenerateMac(DigitalTrans.byte2hex(MyApplication.MAC_KEY).getBytes(), len, data);
			byte[] byte_mac =MyUtil.hexStringToByte(new String(mac));
			//第64域信息验证码
			isoMsg.set(64,byte_mac);
			
//			System.out.println("MAC_KEY:"+DigitalTrans.byte2hex(MyApplication.MAC_KEY));
//			System.out.println("长度:"+len);
//			System.out.println("数据:"+isoMsg.getMacHexString());
//			System.out.println("MAC:"+new String(mac));
			
			byte[] request = RequestUtil.getBytesRequest2(isoMsg);
			L.i("reuqest", ""+RequestUtil.printRequest2(isoMsg));
			return request;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 解析充值返回报文
	 * @param response
	 * @return
	 */
	public static RechargeNode parseRechargeNode(String response){
		RechargeNode node = new RechargeNode();
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
				System.out.println("remain_data:"+remain_data);
				
				//将位图转化为2进制
				String bit_str = MyStringUtil.hexToBin(mainElement);
//				System.out.println("bit_str:"+bit_str);
				for(int i=0;i<bit_str.length();i++){
					//二进制的值为1的AscII值，则表示该域有
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
						//第14域卡有效期
						if(field == 14){
							String value = remain_data.substring(0, 8);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(8); 
						}
						//第25域服务点条件码
						if(field == 25){
							String value = remain_data.substring(0, 4);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(4); 
						}
						//第37域参考号
						if(field == 37){
							String value = remain_data.substring(0, 24);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(24); 
						}
						//第38域授权码
						if(field == 38){
							String value = remain_data.substring(0, 12);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(12);
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
						//第48域附加数据
						if(field == 48){
							String len1=remain_data.substring(0, 6);
							String len2=DigitalTrans.hexStringToString(len1, 2);
//							System.out.println("len2:"+len2);
							int len = Integer.parseInt(len2);
//							System.out.println("48-len:" + len);
							remain_data = remain_data.substring(6);
							if(len>0){
								String value = remain_data.substring(0, len*2);
								node.set(field, value);
								remain_data = remain_data.substring(len * 2);
							}
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
								
								byte[] b_data_61 = DigitalTrans.hex2byte(value);
								node.set(field,
										new String(b_data_61,"GBK"));
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
								byte[] b_data_63 = DigitalTrans.hex2byte(value);
								node.set(field,
										new String(b_data_63,"GBK"));
//								node.set(field,
//										DigitalTrans.hexStringToString(value, 2));
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
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		System.out.println("node.toString():"+node.toString());
		return node;
	}
	
	
	public static void main(String[] args) {
//		String response = "303135390210603C00800E81001F1639303039333230313030303030303131343041303031303232333035303734343535313032352020202030302020202020202020202020203030303030303030303030303030303200000014323031333130323530373434353500063937342E3230002630303030303130303030363130303030303020202020202020200012524D427CB5B1B5B1BFA87C7C8645D2127B73B171";
		String response = "303133360110603800000280001331363930303933323031303030303031303233314130303130353838343132323436323230353139453530303030313030323031343230313430353139323234363232303432BFA85B393030393332303130303030303130325DD2D1B1BBB6B3BDE1A3ACC7EBC1AACFB5D2B5CEF1D4B1F962D330896FC5B4000000000000";
		parseRechargeNode(response);
	}
}
