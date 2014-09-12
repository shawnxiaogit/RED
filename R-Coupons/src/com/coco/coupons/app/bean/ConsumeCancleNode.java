package com.coco.coupons.app.bean;


import java.io.Serializable;
import java.util.ArrayList;

import com.coco.android.util.DigitalTrans;
import com.coco.android.util.MyUtil;

/**
 * 获取充值列表报文实体类
 * @author Shawn
 *
 */
public class ConsumeCancleNode extends BaseNode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5223102011978241880L;




	public static byte[] buildConsumeCancleNode(){
		ConsumeCancleNode isoMsg = new ConsumeCancleNode();
		try{
			isoMsg.setMTI("1001");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 手机信息
	 * 11位手机号+15位SIM卡标识+14位手机设备的标识
	 */
	private String phone_info;
	/**
	 * 当前返回记录位置
	 * 0表示第1行
	 */
	private String current_return_flag;
	/**
	 * 本次返回记录数
	 */
	private String current_return_count;
	/**
	 * 交易种类
	 * 1 – 消费
	 * 2 – 充值
	 */
	private String deal_type;
	/**
	 * 卡号
	 */
	private String card_num;
	
	/**
	 * 开始时间
	 */
	private String start_time;
	
	/**
	 * 结束时间
	 */
	private String end_time;
	
	
	private ArrayList<Node> nodes;
	
	
	
	
	public ArrayList<Node> getNodes() {
		return nodes;
	}



	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	public class Node implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2L;
		/**
		 * 交易日期
		 */
		private String deal_date;
		/**
		 * 交易时间
		 */
		private String deal_time;
		/**
		 * 流水号
		 */
		private String serial_num;
		/**
		 * 卡号
		 */
		private String card_num;
		/**
		 * 金额
		 */
		private String money;
		/**
		 * 备注
		 */
		private String mark;
		
		
		/**
		 * 客户编号
		 */
		private String cust_num;
		/**
		 * 客户名称
		 */
		private String cust_name;
		
		
		public String getCust_num() {
			return cust_num;
		}



		public void setCust_num(String cust_num) {
			this.cust_num = cust_num;
		}



		public String getCust_name() {
			return cust_name;
		}



		public void setCust_name(String cust_name) {
			this.cust_name = cust_name;
		}
		
		public String getDeal_date() {
			return deal_date;
		}
		public void setDeal_date(String deal_date) {
			this.deal_date = deal_date;
		}
		public String getDeal_time() {
			return deal_time;
		}
		public void setDeal_time(String deal_time) {
			this.deal_time = deal_time;
		}
		public String getSerial_num() {
			return serial_num;
		}
		public void setSerial_num(String serial_num) {
			this.serial_num = serial_num;
		}
		public String getCard_num() {
			return card_num;
		}
		public void setCard_num(String card_num) {
			this.card_num = card_num;
		}
		public String getMoney() {
			return money;
		}
		public void setMoney(String money) {
			this.money = money;
		}
		public String getMark() {
			return mark;
		}
		public void setMark(String mark) {
			this.mark = mark;
		}
		@Override
		public String toString() {
			return "Node [deal_date=" + deal_date + ", deal_time=" + deal_time
					+ ", serial_num=" + serial_num + ", card_num=" + card_num
					+ ", money=" + money + ", mark=" + mark + "]";
		}
		
		
	}
	
	
	
	public String getCard_num() {
		return card_num;
	}



	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}



	public String getPhone_info() {
		return phone_info;
	}



	public void setPhone_info(String phone_info) {
		this.phone_info = phone_info;
	}



	public String getCurrent_return_flag() {
		return current_return_flag;
	}



	public void setCurrent_return_flag(String current_return_flag) {
		this.current_return_flag = current_return_flag;
	}



	public String getCurrent_return_count() {
		return current_return_count;
	}



	public void setCurrent_return_count(String current_return_count) {
		this.current_return_count = current_return_count;
	}



	public String getDeal_type() {
		return deal_type;
	}



	public void setDeal_type(String deal_type) {
		this.deal_type = deal_type;
	}



	public String getStart_time() {
		return start_time;
	}



	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}



	public String getEnd_time() {
		return end_time;
	}



	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}



	/**
	 * 获取消费列表
	 * @param val_60
	 * 11位手机号+15位SIM卡标识+14位手机设备的标识
	 * @param card_num
	 * 卡号
	 * @param start_time
	 * 开始时间   
	 * @param end_time
	 * 结束时间
	 * @return
	 */
	public static byte[] buildQueryList(String val_60,String card_num,String start_time,String end_time){
		ConsumeCancleNode node = new ConsumeCancleNode();
		try{
			byte[] b_mti = MyUtil.hexStringToByte("1001");
			node.setMTI(new String(b_mti));
			node.setPhone_info(val_60);
			node.setCurrent_return_flag("0");
			node.setCurrent_return_count("10");
			node.setDeal_type("2");
			node.setCard_num(card_num);
			node.setStart_time(start_time);
			node.setEnd_time(end_time);
			
			
		}catch(Exception e){
			
		}
		return getQreryListRequest(node);
	}
	
	public static byte[] getQreryListRequest(ConsumeCancleNode node){
		StringBuilder sb = new StringBuilder();
		if(node.getMTI()!=null&&node.getMTI().length()>0){
			sb.append(node.getMTI());
		}
		if(node.getPhone_info()!=null&&node.getPhone_info().length()>0){
			sb.append(node.getPhone_info());
			sb.append("|");
		}
		if(node.getCurrent_return_flag()!=null&&node.getCurrent_return_flag().length()>0){
			sb.append(node.getCurrent_return_flag());
			sb.append("|");
		}
		if(node.getCurrent_return_count()!=null&&node.getCurrent_return_count().length()>0){
			sb.append(node.getCurrent_return_count());
			sb.append("|");
		}
		if(node.getDeal_type()!=null&&node.getDeal_type().length()>0){
			sb.append(node.getDeal_type());
			sb.append("|");
		}
		if(node.getCard_num()!=null&&node.getCard_num().length()>0){
			sb.append(node.getCard_num());
			sb.append("|");
		}
		if(node.getStart_time()!=null&&node.getStart_time().length()>0){
			sb.append(node.getStart_time());
			sb.append("|");
		}
		if(node.getEnd_time()!=null&&node.getEnd_time().length()>0){
			sb.append(node.getEnd_time());
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
	private static String printRequest(ConsumeCancleNode node,int rea_len){
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
		if(node.getPhone_info()!=null&&node.getPhone_info().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getPhone_info().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		if(node.getCurrent_return_flag()!=null&&node.getCurrent_return_flag().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getCurrent_return_flag().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		if(node.getCurrent_return_count()!=null&&node.getCurrent_return_count().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getCurrent_return_count().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		if(node.getDeal_type()!=null&&node.getDeal_type().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getDeal_type().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		if(node.getCard_num()!=null&&node.getCard_num().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getCard_num().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		if(node.getStart_time()!=null&&node.getStart_time().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getStart_time().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		if(node.getEnd_time()!=null&&node.getEnd_time().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getEnd_time().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		return sb.toString();
	}
	
	/**
	 * 返回码
	 */
	private String return_code;
	
	/**
	 * 总记录数
	 */
	private int total_count;
	
	/**
	 * 记录个数，本次记录个数
	 */
	private int recod_count;
	
	
	
	public int getTotal_count() {
		return total_count;
	}



	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}



	public int getRecod_count() {
		return recod_count;
	}



	public void setRecod_count(int recod_count) {
		this.recod_count = recod_count;
	}



	public String getReturn_code() {
		return return_code;
	}



	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}



	/**
	 * 解析获取列表报文
	 * @param response
	 * 返回报文
	 * @return
	 */
	public static ConsumeCancleNode parseMsg(String response){
		ConsumeCancleNode node = new ConsumeCancleNode();
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
				
				
//				System.out.println("remain_data:"+remain_data);
				String mData = DigitalTrans.hexStringToString(remain_data, 2);
//				System.out.println("mData:"+mData);
				String[] mDatas = mData.split("#");
				if(mData!=null&&mData.length()>0){
					if(mDatas!=null&&mDatas.length>0){
						for(int i=0;i<mDatas.length;i++){
							System.out.println("mDatas["+i+"]"+mDatas[i]);
							String detail_data = mDatas[i];
							if(i==0){
								String[] len_datas = detail_data.split("\\|");
								for(int k=0;k<len_datas.length;k++){
									System.out.println("len_datas["+k+"]"+len_datas[k]);
									String len_data = len_datas[k];
									if(k==0){
										node.setTotal_count(Integer.parseInt(len_data));
									}
									if(k==1){
										node.setRecod_count(Integer.parseInt(len_data));
									}
								}
							}
							
							if(i==1){
								String[] real_datas = detail_data.split("&");
								ArrayList<Node> nodes = new ArrayList<Node>();
								for(int m=0;m<real_datas.length;m++){
									System.out.println("real_datas["+m+"]"+real_datas[m]);
									String[] detail_item_datas = real_datas[m].split("\\|");
									Node item =node.new Node();
									for(int n=0;n<detail_item_datas.length;n++){
//										System.out.println("detail_item_datas["+n+"]"+detail_item_datas[n]);
										String item_data = detail_item_datas[n];
										if(n==0){
											item.setDeal_date(item_data);
										}
										if(n==1){
											item.setDeal_time(item_data);
										}
										if(n==2){
											item.setSerial_num(item_data);
										}
										if(n==3){
											item.setCard_num(item_data);
										}
										if(n==4){
											item.setMoney(item_data);
										}
										if(n==5){
											item.setCust_num(item_data);
										}
										if(n==6){
											item.setCust_name(new String(DigitalTrans.hex2byte(DigitalTrans.stringToHexString(item_data)),"GBK"));
										}
										if(n==7){
											item.setMark(item_data);
										}
									}
									nodes.add(item);
								}
								node.setNodes(nodes);
							}
							
							
						}
					}
				}
//				int count=0;
//				
//				
//				if(remain_data.length()>6){
//					//总记录数
//					String value = remain_data.substring(0, 4);
//					String real_value = value.substring(0,2);
//					count = Integer.parseInt(DigitalTrans.hexStringToString(real_value, 2));
//					node.setTotal_count(count);
//					remain_data = remain_data.substring(4);
//				}
//				
//				if(remain_data.length()>4){
//					//记录个数，本次记录个数
//					String value = remain_data.substring(0, 4);
//					String real_value = value.substring(0,2);
//					node.setRecod_count(Integer.parseInt(DigitalTrans.hexStringToString(real_value, 2)));
//					remain_data = remain_data.substring(4);
//				}
//				
////				System.out.println("remain_data:"+remain_data);
//				if(remain_data!=null&&remain_data.length()>40){
//					String[] datas = remain_data.split("26");
//					ArrayList<Node> nodes = new ArrayList<Node>();
//					for(int i=0;i<datas.length;i++){
////						System.out.println("data["+i+"]"+datas[i]);
//						if(datas[i]!=null&&datas[i].length()>0){
//							String data = datas[i];
//							String[] items = data.split("7C");
//							Node item =node.new Node();
//							for(int j=0;j<items.length;j++){
////								System.out.println("items["+j+"]"+items[j]);
//								if(j>=0){
//									if(items[0]!=null&&items[0].length()>0){
//										String value = items[0];
//										item.setDeal_date(DigitalTrans.hexStringToString(value, 2));
//									}
//								}
//								if(j>=1){
//									if(items[1]!=null&&items[1].length()>0){
//										String value = items[1];
//										item.setDeal_time(DigitalTrans.hexStringToString(value, 2));
//									}
//								}
//								if(j>=2){
//									if(items[2]!=null&&items[2].length()>0){
//										String value = items[2];
//										item.setSerial_num(DigitalTrans.hexStringToString(value, 2));
//									}
//								}
//								if(j>=3){
//									if(items[3]!=null&&items[3].length()>0){
//										String value = items[3];
//										item.setCard_num(DigitalTrans.hexStringToString(value, 2));
//									}
//								}
//								if(j>=4){
//									if(items[4]!=null&&items[4].length()>0){
//										String value = items[4];
//										item.setMoney(DigitalTrans.hexStringToString(value, 2));
//									}
//								}
//								if(j>=5){
//									if(items[5]!=null&&items[5].length()>0){
//										String value = items[5];
//										item.setMark(DigitalTrans.hexStringToString(value, 2));
//									}
//								}
//								
//								
//							}
////							System.out.println(item.toString());
//							nodes.add(item);
//						}
//						
//					}
//					
//					node.setNodes(nodes);
//				}
				
//				System.out.println("remain_data:"+remain_data);
				
				
				
				printConsumeCancleNode(node);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return node;
	}
	
	
	



	private static void printConsumeCancleNode(ConsumeCancleNode node){
		StringBuilder sb = new StringBuilder();
		if(node.getHeader()!=null&&node.getHeader().length()>0){
			sb.append("node.getHeader():");
			sb.append(node.getHeader());
			sb.append("\n");
		}
		if(node.getMTI()!=null&&node.getMTI().length()>0){
			sb.append("node.getMTI():");
			sb.append(node.getMTI());
			sb.append("\n");
		}
		if(node.getReturn_code()!=null&&node.getReturn_code().length()>0){
			sb.append("node.getReturn_code():");
			sb.append(node.getReturn_code());
			sb.append("\n");
		}
		if(node.getTotal_count()!=0){
			sb.append("node.getTotal_count():");
			sb.append(node.getTotal_count());
			sb.append("\n");
		}
		if(node.getRecod_count()!=0){
			sb.append("node.getRecod_count():");
			sb.append(node.getRecod_count());
			sb.append("\n");
		}
		if(node.getNodes()!=null&&node.getNodes().size()>0){
			for(int i=0;i<node.getNodes().size();i++){
				Node item = node.getNodes().get(i);
				if(item!=null){
					sb.append("item["+i+"]");
					if(item.getDeal_date()!=null&&item.getDeal_date().length()>0){
						sb.append("item.getDeal_date():"+item.getDeal_date());
					}
					if(item.getDeal_time()!=null&&item.getDeal_time().length()>0){
						sb.append("item.getDeal_time():"+item.getDeal_time());
					}
					if(item.getSerial_num()!=null&&item.getSerial_num().length()>0){
						sb.append("item.getSerial_num():"+item.getSerial_num());
					}
					if(item.getCard_num()!=null&&item.getCard_num().length()>0){
						sb.append("item.getCard_num():"+item.getCard_num());
					}
					if(item.getMoney()!=null&&item.getMoney().length()>0){
						sb.append("item.getMoney():"+item.getMoney());
					}
					if(item.getCust_num()!=null&&item.getCust_num().length()>0){
						sb.append("item.getCust_num():"+item.getCust_num());
					}
					if(item.getCust_name()!=null&&item.getCust_name().length()>0){
						sb.append("item.getCust_name():"+item.getCust_name());
					}
					
					if(item.getMark()!=null&&item.getMark().length()>0){
						sb.append("item.getMark():"+item.getMark());
					}
					sb.append("\n");
				}
			}
		}
		
		System.out.println(sb.toString());
	}
	
	public static void main(String[] args) {
//		String response="30313437100130307C337C337C32303133313130347C3034343234387C32383230337C393030393332303130303031313135387C312E30307C7C2632303133313130347C3034343435397C32383230347C393030393332303130303031313135387C312E30307C7C2632303133313130347C3034343735327C32383230357C393030393332303130303031313135387C312E30307C7C26";
		String response = "30333339100130307C367C362332303133313230397C3232303334317C34343432307C393030393332303130303030303038367C3137302E30307C7C7C7C2632303133313230397C3232343832357C34343530347C393030393332303130303030303038367C382E35307C7C7C7C2632303133313230397C3232343835327C34343530377C393030393332303130303030303038367C382E35307C7C7C7C2632303133313230397C3233303133347C34343630347C393030393332303130303030303038367C382E35307C303530333437353630357CBDADCBD8C3B7C9CCB5EA7C7C2632303133313230397C3233303231337C34343630377C393030393332303130303030303038367C382E35307C303530333437353630357CBDADCBD8C3B7C9CCB5EA7C7C2632303133313230357C3039353132317C34333331357C393030393332303130303030303038367C382E35307C7C7C7C26";
		parseMsg(response);
	}
}
