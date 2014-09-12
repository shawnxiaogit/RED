package com.coco.coupons.app.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.coco.android.util.DigitalTrans;
import com.coco.android.util.MyUtil;
import com.coco.coupons.app.MyApplication;
import com.coco.coupons.app.ui.QueryRechargeDateSelectActivity;

/**
 * 获取操作员明细报文实体类
 * @author ShawnXiao
 *
 */
public class GetOperaterDetailNode extends BaseNode implements Serializable{

	private static final long serialVersionUID = -5688367131222576058L;
	
	
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
	 * 开始时间
	 */
	private String start_time;
	/**
	 * 结束时间
	 */
	private String end_time;
	
	/**
	 * 查询标识：1-自己 2-主管

	 */
	private String query_flag;
	/**
	 * 查询类别:C-充值明细D-消费明细
	 */
	private String query_type;
	
	/**
	 * 卡号
	 */
	private String card_num;
	/**
	 * 客户编号
	 */
	private String cust_num;
	
	/**
	 * 手机号
	 */
	private String phone_num;
	
	
	
	
	public String getPhone_num() {
		return phone_num;
	}




	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}




	public String getCard_num() {
		return card_num;
	}




	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}




	public String getCust_num() {
		return cust_num;
	}




	public void setCust_num(String cust_num) {
		this.cust_num = cust_num;
	}


	public ArrayList<DealNode> dealNodes;
	
	public GetOperaterDetailNode(){
		dealNodes = new ArrayList<DealNode>();
	}
	
	
	
	
	public ArrayList<DealNode> getDealNodes() {
		return dealNodes;
	}




	public void setDealNodes(ArrayList<DealNode> dealNodes) {
		this.dealNodes = dealNodes;
	}




	public String getQuery_flag() {
		return query_flag;
	}

	public void setQuery_flag(String query_flag) {
		this.query_flag = query_flag;
	}

	public String getQuery_type() {
		return query_type;
	}

	public void setQuery_type(String query_type) {
		this.query_type = query_type;
	}

	public static byte[] getOpraterDetailMsg(int type,
			String phone_info,
			String start_time,
			String end_time,
			String query_flag,
			String query_type,
			String line_num,
			String card_num,
			String cust_num,
			String start,
			String count,
			int request_type){
		GetOperaterDetailNode node = new GetOperaterDetailNode();
		try{
			byte[] b_mti = MyUtil.hexStringToByte(MyApplication.RECHARGE_CANCLE_QUERY_MTI);
			node.setMTI(new String(b_mti));
			node.setPhoneInfo(phone_info);
			
			//这里也许要改(通过翻页来做)，目前的做法是只返回10条记录
			//当前返回记录位置 ,0表示第1行。
			if(start!=null&&start.length()>0){
				node.setCurrent_return_falg(start);
			}else{
				if(request_type == MyApplication.REQUEST_TYPE_CHE_XIAO){
					node.setCurrent_return_falg(MyApplication.RECHARGE_CANCLE_QUERY_CURRENT_RETURN_FLAG2);
				}else{
					node.setCurrent_return_falg(MyApplication.RECHARGE_CANCLE_QUERY_CURRENT_RETURN_FLAG);
				}
			}
			
			
			//本次返回记录数 
			if(count!=null&&count.length()>0){
				node.setCurrent_return_count(count);
			}else{
				node.setCurrent_return_count(MyApplication.RECHARGE_CANCLE_QUERY_CURRENT_RETURN_COUNT);
			}
			
			if(card_num!=null&&card_num.length()>0&&!card_num.equals("")){
				node.setCard_num(card_num);
			}
			node.setStart_time(start_time);
			node.setEnd_time(end_time);
			node.setQuery_flag(query_flag);
			if(type == QueryRechargeDateSelectActivity.TYPE_QUERY_SOME_EMPLOYEE){
				node.setXianluhao(line_num);
			}else{
				node.setXianluhao("");
			}
			node.setQuery_type(query_type);
			if(cust_num!=null&&cust_num.length()>0&&!cust_num.equals("")){
				node.setCust_num(cust_num);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return getQueryListRequest(node);
	}
	
	
	/**
	 * 线路号
	 */
	private String xianluhao;
	
	
	
	public String getXianluhao() {
		return xianluhao;
	}




	public void setXianluhao(String xianluhao) {
		this.xianluhao = xianluhao;
	}




	public static byte[] getQueryListRequest(GetOperaterDetailNode node){
		
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
		if(node.getCard_num()!=null&&node.getCard_num().length()>0&&!node.getCard_num().equals("")){
			sb.append(node.getCard_num());
			sb.append("|");
		}else{
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
		if(node.getQuery_flag()!=null&&node.getQuery_flag().length()>0){
			sb.append(node.getQuery_flag());
			sb.append("|");
		}
		if(node.getXianluhao()!=null&&node.getXianluhao().length()>0&&!node.getXianluhao().equals("")){
			sb.append(node.getXianluhao());
			sb.append("|");
		}else{
			sb.append("|");
		}
		
		if(node.getQuery_type()!=null&&node.getQuery_type().length()>0){
			sb.append(node.getQuery_type());
			sb.append("|");
		}
		
		if(node.getPhone_num()!=null&&node.getPhone_num().length()>0&&!node.getPhone_num().equals("")){
			sb.append(node.getPhone_num());
			
			sb.append("|");
		}else{
			sb.append("|");
		}
		
		if(node.getCust_num()!=null&&node.getCust_num().length()>0&&!node.getCust_num().equals("")){
			sb.append(node.getCust_num());
			sb.append("|");
		}else{
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
	
	private static String printRequest(GetOperaterDetailNode node,int rea_len){
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
		if(node.getQuery_flag()!=null&&node.getQuery_flag().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getQuery_flag().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		if(node.getXianluhao()!=null&&node.getXianluhao().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getXianluhao().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		if(node.getQuery_type()!=null&&node.getQuery_type().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getQuery_type().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		if(node.getPhone_num()!=null&&node.getPhone_num().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getPhone_num().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}else{
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		
		if(node.getCust_num()!=null&&node.getCust_num().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getCust_num().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}else{
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		
		return sb.toString();
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
	
	/**
	 * 返回码
	 */
	private String return_code;
	
	public String getReturn_code() {
		return return_code;
	}
	

	/**
	 * 总记录数
	 */
	private String total_count;
	/**
	 * 记录个数
	 */
	private String record_count;

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	
	public static GetOperaterDetailNode parseMsg(String response){
		GetOperaterDetailNode node  = new GetOperaterDetailNode();
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
						if(i==1){
							String[] details = datas2[i].split("\\|");
							for(int j=0;j<details.length;j++){
								System.out.println("details["+j+"] : "+ details[j]);
								if(j>=1){
									if(details!=null&&details.length>0){
										String[] details2 = details[j].split("~");
										DealNode dealNode = new DealNode();
										for(int n=0;n<details2.length;n++){
											String mdata = details2[n];
											if(mdata!=null&&mdata.length()>0){
												if(n==0){
													if(mdata!=null&&mdata.length()>0){
														dealNode.setDeal_time(mdata.replace("\"", ""));
													}
												}
												if(n==1){
													if(mdata!=null&&mdata.length()>0){
														dealNode.setDeal_money(mdata.replace(" ", ""));
													}
												}
												if(n==2){
													if(mdata!=null&&mdata.length()>0){
														dealNode.setDeal_type(mdata.replace("\"", ""));
													}
												}
												if(n==3){
													if(mdata!=null&&mdata.length()>0){
														dealNode.setReamin(mdata.replace(" ", ""));
													}
												}
												if(n==4){
													if(mdata!=null&&mdata.length()>0){
														dealNode.setDeal_org(mdata.replace("\"", ""));
													}
												}
												if(n==5){
													if(mdata!=null&&mdata.length()>0){
														dealNode.setDeal_summry(mdata.replace("\"", ""));
													}
												}
												if(n==6){
													if(mdata!=null&&mdata.length()>0){
														dealNode.setOrinal_line(mdata);
													}
												}
												//卡号
												if(n==7){
													System.out
															.println("n==7"+"mdata");
													if(mdata!=null&&mdata.length()>0&&!mdata.equals("")){
														dealNode.setCardNum(mdata.replace("\"", ""));
													}
												}
												//客户编号
												if(n==8){
													if(mdata!=null&&mdata.length()>0){
														dealNode.setCust_num(mdata.replace("\"", ""));
													}
												}
												//客户名称
												if(n==9){
													if(mdata!=null&&mdata.length()>0){
														dealNode.setCust_name(mdata.replace("\"", ""));
													}
												}
												if(n==10){
													if(mdata!=null&&mdata.length()>0){
														dealNode.setXianluhao(mdata.replace("\"", ""));
													}
												}
											}
										}
										if(!dealNode.getDeal_summry().equals(DealNode.STRING_OPEN_CARD)){
											node.dealNodes.add(dealNode);
										}
									}
								}
							}
						}
					}
				}
				
				
			}
		}catch(Exception e){
			
		}
		printMsg(node);
		return node;
	}
	public static void printMsg(GetOperaterDetailNode node){
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
		
		if(node.getDealNodes()!=null&&node.getDealNodes().size()>0){
			for(int i=0;i<node.getDealNodes().size();i++){
				DealNode item = node.getDealNodes().get(i);
				if(item!=null){
					sb.append(item.toString());
				}
				sb.append("\n");
			}
		}
		
		System.out.println(sb.toString());
		
	}
	public static void main(String[] args) {
		String response = "31393230100330307C31347C317C7B7C7469746C653DB6E0B1CABDBBD2D7B2E9D1AF202020202020202020202020202020202020202020202020202020207C7472616E636F64653D2020202020207C666C643D5472616E446174657EBDBBD2D7CAB1BCE47E337E31397E327E4E554C4C7C666C643D5472616E416D747EBDBBD2D7BDF0B6EE7E347E31377E327E4E554C4C7C666C643D4344466C61677ED7CABDF0BDF8B3F67E307E347E327E3C433ED7AAC8EB3C443ED7AAB3F67C666C643D42616C7ED3E0B6EE7E347E31377E327E4E554C4C7C666C643D427263317EBDBBD2D7BBFAB9B97E307E397E327E4E554C4C7C666C643D4D656D6F7ED5AAD2AA7E307E34307E327E4E554C4C7C666C643D7074786E7365717ED4ADBDBBD2D7C1F7CBAEBAC57E317E32307E327E4E554C4C7C666C643D636172646E6F7EBFA8BAC57E307E31397E327E4E554C4C7C666C643D736574746C656163636E6F7EBFCDBBA7B1E0BAC57E307E387E327E4E554C4C7C666C643D437573744E616D657EBFCDBBA7C3FBB3C67E307E34307E327E4E554C4C7C666C643D6F706E62616E6B6E616D657ECFDFC2B7BAC57E307E357E327E4E554C4C7C7D7C22323031342D30342D30342031323A32373A3237227E20202020202020202020202020382E35307E2243227E20202020202020202020202032342E33307E225B3030303030315DC9EAC3C0B7A2BFA8BBFAB9B9227E22B3E4D6B5227E35327E2239303039333230313030303030303131227E2230353033343433383233227E22BFAAD0C4B1E3C0FBB5EA227E224144313033227C22323031342D30342D30342031323A33363A3236227E20202020202020202020202020382E35307E2243227E20202020202020202020202033322E38307E225B3030303030315DC9EAC3C0B7A2BFA8BBFAB9B9227E22B3E4D6B5227E35357E2239303039333230313030303030303131227E2230353033343433383233227E22BFAAD0C4B1E3C0FBB5EA227E224144313033227C22323031342D30342D30342031323A34313A3331227E20202020202020202020202020382E35307E2243227E20202020202020202020202034312E33307E225B3030303030315DC9EAC3C0B7A2BFA8BBFAB9B9227E22B3E4D6B5227E35367E2239303039333230313030303030303131227E2230353033343433383233227E22BFAAD0C4B1E3C0FBB5EA227E224144313033227C22323031342D30342D30342031323A35333A3036227E20202020202020202020202020382E35307E2243227E20202020202020202020202034392E38307E225B3030303030315DC9EAC3C0B7A2BFA8BBFAB9B9227E22B3E4D6B5227E36307E2239303039333230313030303030303131227E2230353033343433383233227E22BFAAD0C4B1E3C0FBB5EA227E224144313033227C22323031342D30342D30342031323A35333A3530227E20202020202020202020202020382E35307E2243227E20202020202020202020202035382E33307E225B3030303030315DC9EAC3C0B7A2BFA8BBFAB9B9227E22B3E4D6B5227E36317E2239303039333230313030303030303131227E2230353033343433383233227E22BFAAD0C4B1E3C0FBB5EA227E224144313033227C22323031342D30342D30342031323A35343A3435227E20202020202020202020202020382E35307E2243227E20202020202020202020202036362E38307E225B3030303030315DC9EAC3C0B7A2BFA8BBFAB9B9227E22B3E4D6B5227E36327E2239303039333230313030303030303131227E2230353033343433383233227E22BFAAD0C4B1E3C0FBB5EA227E224144313033227C22323031342D30342D30342031323A35353A3331227E20202020202020202020202020382E35307E2243227E20202020202020202020202037352E33307E225B3030303030315DC9EAC3C0B7A2BFA8BBFAB9B9227E22B3E4D6B5227E36337E2239303039333230313030303030303131227E2230DAA13DED67F18DA2DEEC72F80D8F9E5EBF95EE1D2EEB3854BF95EE1D2EEB38548600543B25768EDEFF2AD0FDB64673F989C0350DA9CBDB9A7484CF3315AAAEA806BDC5D31252E0563CD8CC43C2CB6998BF95EE1D2EEB3854BF95EE1D2EEB38543C99F823EE5E4AA2BF95EE1D2EEB3854BF95EE1D2EEB3854599755CE421B8B883A2B864DD5F3FD89BF95EE1D2EEB3854BF95EE1D2EEB385447EAC9C094E24F55BF95EE1D2EEB3854BF95EE1D2EEB38549758EBCAFB63A620588CAB8862CEB4149D6BE9D6B72E7E975CC8746D32048B7360CD18D9FB3DCDC472E6796FFD8619B772E6796FFD8619B7D88794F865754807F6D38DBA62953AC0BF95EE1D2EEB3854DD362C049D448F46BF95EE1D2EEB38548588806341E20341BF95EE1D2EEB3854E777BE1EFF5175D05CC8746D32048B73BF95EE1D2EEB3854BF95EE1D2EEB3854BF95EE1D2EEB3854BB52D3AFFC77069D4829F2B5E03EE89ABF95EE1D2EEB385462FB75D79E01E6B162FB75D79E01E6B1BF95EE1D2EEB385462FB75D79E01E6B121521BECC5968B78F6D38DBA62953AC0BF95EE1D2EEB38543A5E17C784AB14E2BF95EE1D2EEB38540743071801E5EFD9D4EECBA8B2305F8014F426DF8D934C8D7CD0D7211EABEC300925C7E6D6D4AB9DEBA83C64472D8282BF95EE1D2EEB38540000";
		parseMsg(response);
	}
	
}
