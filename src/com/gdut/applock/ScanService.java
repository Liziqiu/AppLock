package com.gdut.applock;

import java.util.Iterator;
import java.util.List;

import com.gdut.popupwindow.window;
import com.gdut.popupwindow.windowClickListener;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ScanService extends Service{

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what ==0){
				String result = (String) msg.obj;
				log.d("handleMessage:"+result);
				apptextview.setText(result);
			}
			super.handleMessage(msg);
		}
		
	};
	
	private window pupop;
	private View applistView;
	private Button close;
	private TextView apptextview;
	private ActivityManager am;
	private static boolean isOpenCustomView=false;
	@Override
	public IBinder onBind(Intent binder) {
		return null;
	}

	@Override
	public void onCreate() {
		log.d("ScanService onCreate");
		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		
		
		pupop = new window(this);
		applistView = LayoutInflater.from(this).inflate(R.layout.forgroundapp, null);
		apptextview = (TextView) applistView.findViewById(R.id.checking);
		close=(Button) applistView.findViewById(R.id.close);
		
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pupop.closeCustomView();
			}
		});
		pupop.setCustomView(applistView);
		pupop.setClickListener(new windowClickListener() {
			
			@Override
			public void OnClick(View v, float x, float y) {
				//if(!isOpenCustomView){
					pupop.openCustomView();
				//	isOpenCustomView=true;
				//}else{
				//	pupop.closeCustomView();
				//	isOpenCustomView=false;
				//}
			}
		});
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		log.d("ScanService onDestroy");
		if(pupop!=null){
			pupop.dismiss();
		}
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent == null)return super.onStartCommand(intent, flags, startId);
		log.d("ScanService onStartCommand:"+intent.getAction());
		String action = intent.getAction().toString();
		if("start".equals(action)){
			pupop.show();
			new BGThread().start();
		}else if("stop".equals(action)){
			pupop.dismiss();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	class BGThread extends Thread{

		@Override
		public void run() {
			log.d("BGThread run");
			while(true){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handler.sendMessage(handler.obtainMessage(0, getFGAppPkg()));
			}
		}
		private String getFGActivity(){
			String result="error";
			List<RunningTaskInfo> infolist = am.getRunningTasks(300);
			for(Iterator<RunningTaskInfo> iterator = infolist.iterator();iterator.hasNext();){
				RunningTaskInfo info = iterator.next();
				result=info.topActivity.getClassName();
				log.d("BGThread getapp:"+result);
			}
			return result;
		}
		private String getFGAppPkg(){
			String result="error";
			List<RunningAppProcessInfo> infolist = am.getRunningAppProcesses();
			for(RunningAppProcessInfo info:infolist){
				if(info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
					result = info.processName;
					break;
				}
			}
			log.d("BGThread getapp:"+result);
			return result;
		}
		
	}
}
