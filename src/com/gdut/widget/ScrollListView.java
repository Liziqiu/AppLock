package com.gdut.widget;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

import com.gdut.applock.R;
import com.gdut.applock.log;


public class ScrollListView extends ListView{

	private Scroller scroller;
	private VelocityTracker mVelocityTracker;
	private View item;
	private boolean isScroll=false;
	
	private int startX=0;
	private int dispatchStartX=0;
	private int dispatchStartY=0;
	private int mTouchSlop; 
	private int screenWidth;
	private int ListViewWidth;
	private static final int TRIGER_VELOCITY = 600;
	private static final int RIGHT =1;
	private static final int LEFT =2;
	private int direction;
	private int slidePosition;
	
	public ScrollOutListener mScrollOutListener;
	
	public ScrollListView(Context context) {
		super(context);
		initView(context);
	}

	public ScrollListView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView(context);
	}

	public ScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	public ScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context){
		scroller = new Scroller(context);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop(); 
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		screenWidth= dm.widthPixels;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			RegistedVelocityTracker(ev);
			if(!scroller.isFinished()){
				return super.dispatchTouchEvent(ev);
			}
			int downX=(int) ev.getX();
			int downY=(int) ev.getY();
			dispatchStartX=downX;
			dispatchStartY=downY;
			slidePosition = pointToPosition(downX,downY);
			if(slidePosition == AdapterView.INVALID_POSITION){
				return super.dispatchTouchEvent(ev);
			}
			item = getChildAt(slidePosition-getFirstVisiblePosition());
			
			log.d("getHeight:"+item.getHeight());
			break;
		case MotionEvent.ACTION_MOVE:
			if(Math.abs(getScrollVeloctity())>TRIGER_VELOCITY||(Math.abs(ev.getX()-dispatchStartX)>mTouchSlop 
					&& Math.abs(ev.getY()-dispatchStartY)<30)){
				//log.d("getScrollVeloctity:"+getScrollVeloctity()+"// "+(ev.getX()-dispatchStartX)+"// "+(ev.getY()-dispatchStartY));
				isScroll=true;
			}
			break;
		case MotionEvent.ACTION_UP:
			unRegistedVelocityTracker();
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(ev.getAction()==MotionEvent.ACTION_DOWN){
			startX=(int) ev.getX(); //多一次赋值以免因为 isScroll 为 false 导致没走 ACTION DOWN 直接走 ACTION MOVE
		}
		if(!isScroll){
			return super.onTouchEvent(ev);
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX=(int) ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int currentX=(int) ev.getX();
			//log.d("startX"+startX+"-currentX"+currentX+"="+(startX-currentX));
			if(Math.abs(startX-currentX)<50){
				item.scrollBy(startX-currentX, 0);
			}
			startX = currentX;
			break;
		case MotionEvent.ACTION_UP:
			if(!scrollDistanceEnoughTrigger()){
				item.scrollTo(0, 0);
			}
			isScroll=false;
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	private boolean scrollDistanceEnoughTrigger() {
		if(item.getScrollX()>=(screenWidth/3)){
			scroll2Left();
			return true;
		}else if(item.getScrollX()<=(-screenWidth/3)){
			scroll2Right();
			return true;
		}
		return false;
	}

	private void scroll2Right(){
		int scrollDistance = screenWidth + item.getScrollX();
		scroller.startScroll(item.getScrollX(), 0, -scrollDistance, 0, Math.abs(scrollDistance));
		postInvalidate();
		direction=RIGHT;
	}
	private void scroll2Left(){
		int scrollDistance = screenWidth - item.getScrollX();
		scroller.startScroll(item.getScrollX(), 0, scrollDistance, 0, Math.abs(scrollDistance));
		postInvalidate();
		direction=LEFT;
	}
	private void RegistedVelocityTracker(MotionEvent ev){
		if(mVelocityTracker ==null){
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
	}
	private void unRegistedVelocityTracker(){
		if(mVelocityTracker !=null){
			mVelocityTracker.recycle();
			mVelocityTracker=null;
		}
	}
	private int getScrollVeloctity(){
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return velocity;
	}

	
	@Override
	public void computeScroll() {
		if(scroller.computeScrollOffset()){
			item.scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();
			
			if(scroller.isFinished()){
				if(mScrollOutListener!=null){
					mScrollOutListener.fadeOut(direction,item,slidePosition);
				}
				item.scrollTo(0, 0);//恢复原来
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		ListViewWidth = this.getWidth();
		//log.d("ListViewWidth:"+ListViewWidth);
		super.onDraw(canvas);
	}
	
	public void setScrollOutListener(ScrollOutListener mScrollOutListener) {
		this.mScrollOutListener = mScrollOutListener;
	}

	public interface ScrollOutListener{
		public void fadeOut(int direction, View item,int position);
	}
}
