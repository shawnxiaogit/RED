package com.coco.coupons.app.service;


import com.coco.android.socket.SocketClient;
import com.coco.android.util.L;
import com.coco.coupons.app.MyApplication;
import com.coco.coupons.app.R;
import com.coco.coupons.app.bean.GetRechargeKeyNode;
import com.coco.coupons.app.bean.RequestUtil;
import com.coco.coupons.app.ui.RechargeActivity;
import com.coco.coupons.app.ui.RechargeActivity2;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;


/**
 * 
 * @ClassName MyRechargeItemLoadService 
 * @Description 充值项下载服务
 * @author Shawn(vsi.shawnxiao@gmail.com)
 * @date 2014年3月14日 下午5:35:16
 */
public class MyRechargeItemLoadService extends Service {
	
    private volatile Looper mServiceLooper;
    private volatile ServiceHandler mServiceHandler;
    
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        
        @Override
        public void handleMessage(Message msg) {
            Bundle arguments = (Bundle)msg.obj;
        
            String txt = arguments.getString("name");
            
            Log.i("ServiceStartArguments", "Message: " + msg + ", "
                    + arguments.getString("name"));
        
            if ((msg.arg2&Service.START_FLAG_REDELIVERY) == 0) {
                txt = "New cmd #" + msg.arg1 + ": " + txt;
            } else {
                txt = "Re-delivered #" + msg.arg1 + ": " + txt;
            }
            
        
            // Normally we would do some work here...  for our sample, we will
            // just sleep for 5 seconds.
            long endTime = System.currentTimeMillis() + 5*1000;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                    }
                }
            }
        
            
            Log.i("ServiceStartArguments", "Done with #" + msg.arg1);
            stopSelf(msg.arg1);
        }

    };
    
	@Override
	public void onCreate() {
		super.onCreate();
		
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("MyRechargeItemLoadService",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mServiceLooper.quit();
	}
	
	
	/**
	 * 
	 * @Title getRechargeItem 
	 * @Description TODO(这里用一句话描述这个方法的作用) 
	 * void    返回类型 
	 * @throws
	 */
	private void getRechargeItem(){
		String phone_info = MyApplication.getSixtyValue3(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
		//这里组织获取充值字段报文
		byte[] request = GetRechargeKeyNode.buildGetRechargeKeyMsg(phone_info);
		new GetRechargeKeyTask(request).execute();
	}
	
	/**
	 * 获取充值字段任务
	 * @author ShawnXiao
	 *
	 */
	
	private class GetRechargeKeyTask extends AsyncTask{
		private byte[] mRequest;
		
		public GetRechargeKeyTask(byte[] request){
			mRequest = request;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			showProgressDialog(R.string.handing_process);
		};

		@Override
		protected Object doInBackground(Object... arg0) {
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			SocketClient client = new SocketClient(mRequest,getRechargeKeyHandler);
			client.start();
		}
		
	}
	//获取充值字段处理助手
	private Handler getRechargeKeyHandler = new Handler(){
		public void handleMessage(Message msg) {
			
			switch(msg.what){
			case RequestUtil.REQUEST_SUCCESS: {
				String hex_response = (String) msg.obj;
				if (hex_response != null && hex_response.length() > 0) {
					GetRechargeKeyNode node = GetRechargeKeyNode.parseMsg(hex_response);
//					//卡号
//					String card_num = et_consume_card_num.getText().toString();
//					StringBuilder sb = new StringBuilder();
//					if(card_num==null||card_num.length()==0){
//						showShortToast(R.string.card_num_error);
//						return ;
//					}else if(card_num.length()==8){
//						sb.append(MyApplication.CARD_PREFIX);
//						sb.append(card_num);
//					}else if(card_num.length()==16){
//						sb.append(card_num);
//					}else{
//						showShortToast(R.string.card_num_error);
//						return ;
//					}
//					L.e("card_num:", sb.toString());
//					
//					
//					//进入充值字段获取页面
//					Intent intent  = new Intent(RechargeActivity2.this,RechargeActivity.class);
//					
//					Bundle bundle = new Bundle();
//					bundle.putSerializable(RechargeActivity.KEY_RECHARGE_KEY, node);
//					//将卡号传进去
//					bundle.putString(RechargeActivity.KEY_CARD_NUM, sb.toString());
//					intent.putExtras(bundle);
//					RechargeActivity2.this.startActivity(intent);
//					switchActivityAnimationIn();
				}
			}break;
			case RequestUtil.REQUEST_FAIELD: {
				
			}break;
			}
//			mAlertDialog.dismiss();
		};
	};

}
