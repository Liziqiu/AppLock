package com.gdut.popupwindow;

import com.gdut.applock.R;
import com.gdut.applock.log;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

public class window implements OnTouchListener{

	private Context context;
	private WindowManager wm;
	private WindowManager.LayoutParams wmParams=null;
	private View mView;
	private View customView;
	private static boolean isShow=false;
	private windowClickListener clickListener;
	
	public window(Context context) {
		this.context = context;
		oncreate();
	}

	private void oncreate() {
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wmParams = new WindowManager.LayoutParams();
		setDefultWindowParams(wmParams);
		mView = new ImageView(context);
		mView.setBackgroundResource(R.drawable.ic_launcher);
		mView.setOnTouchListener(this);
		log.d("w:"+mView.getWidth()+"//h:"+mView.getHeight());
	}

	private void setDefultWindowParams(LayoutParams Params) {
		wmParams.flags|=8;
		setWindowParams(Params,WindowManager.LayoutParams.TYPE_STATUS_BAR_OVERLAY,Gravity.LEFT | Gravity.TOP,100,100,
				WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,1);
	}

	private void setWindowParams(LayoutParams params, int type,
			int gravity, int x, int y, int width, int height, int format) {
		wmParams.type=type;
		wmParams.gravity=gravity;
		wmParams.x=x;
		wmParams.y=y;
		wmParams.format=format;
		wmParams.width=width;
		wmParams.height=height;
	}
	
	public void show(){
		if(!isShow){
			wm.addView(mView, wmParams);
			isShow=true;
		}
	}
	public void dismiss(){
		if(isShow){
			wm.removeView(mView);
			isShow=false;
		}
	}

	private float x,y,rawX,rawY;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			log.d("down x:"+event.getRawX()+"//y:"+event.getRawY());
			rawX=event.getRawX();
			rawY=event.getRawY();
			x= event.getX();
			y= event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			wmParams.x=(int) (event.getRawX()-x);
			wmParams.y=(int) (event.getRawY()-y-75);//75通知栏高度
			//log.d("x:"+wmParams.x+"//y:"+wmParams.y);
			wm.updateViewLayout(v, wmParams);
			break;
		case MotionEvent.ACTION_UP:
			log.d("up x:"+event.getRawX()+"//y:"+event.getRawY());
			if(IsOnClick(rawX,rawY,event.getRawX(),event.getRawY())){
				log.d("IsOnClick");
				if(clickListener != null){
					clickListener.OnClick(mView, event.getRawX(), event.getRawY());
				}
			}
			break;
		}
		return true;
	}

	private boolean IsOnClick(float x1, float y1, float x2, float y2) {
		int disX = (int) Math.abs(x1-x2);
		int disY = (int) Math.abs(y1-y2);
		if(disX<5 && disY<5){
			return true;
		}
		return false;
	}

	public void setClickListener(windowClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public void setCustomView(View customView) {
		this.customView = customView;
	}
	
	public void openCustomView(){
		if(customView != null){
			dismiss();
			wm.addView(customView, wmParams);
		}
	}
	
	public void closeCustomView(){
		if(customView!=null){
			wm.removeView(customView);
		}
		show();
	}
	
}
