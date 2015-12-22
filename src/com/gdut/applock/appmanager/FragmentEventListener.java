package com.gdut.applock.appmanager;

import java.util.List;

public interface FragmentEventListener {

	public void changeFragment(int position);
	public void addBlockApp(List<appInfo> data);
	public void deleteBlockApp(appInfo data);
	public List<appInfo> getBlockAppList();
}
