package com.gdut.applock.appmanager;

import java.util.List;

import com.gdut.applock.R;
import com.gdut.applock.log;
import com.gdut.applock.db.BlockAppCache;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class applist extends FragmentActivity implements FragmentEventListener{

	private Button success;
	private FragmentManager fm;
	private Fragment mInstalledAppList;
	private Fragment mBlockAppList;
	private BlockAppCache mBlockAppCache;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.applist);
		mBlockAppCache = new BlockAppCache(getApplicationContext());//用 application 的context 因为，这个context 不需要 UI 操作
		initFragment();
		addFragment(mInstalledAppList);
	}
	private void addFragment(Fragment fragment) {
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.applist_contain, fragment);
		ft.commit();
		
	}
	private void initFragment() {
		fm = getSupportFragmentManager();
		mInstalledAppList = new InstalledAppList();
		mBlockAppList = new BlockAppList();
	}

	@Override
	public void changeFragment(int position) {
		if(position == 0){
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.applist_contain, mInstalledAppList);
			ft.commit();
		}else if(position == 1){
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.applist_contain, mBlockAppList);
			ft.commit();
		}
		
	}
	@Override
	public void addBlockApp(List<appInfo> data) {
		for(appInfo info:data)
			mBlockAppCache.add(info);
		
	}
	@Override
	public void deleteBlockApp(appInfo data) {
		mBlockAppCache.deleted(data);
	}
	@Override
	public List<appInfo> getBlockAppList() {
		// TODO Auto-generated method stub
		return mBlockAppCache.getBlockList();
	}
	
}
