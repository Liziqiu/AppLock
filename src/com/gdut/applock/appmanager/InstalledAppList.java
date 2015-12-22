package com.gdut.applock.appmanager;

import java.util.ArrayList;
import java.util.List;

import com.gdut.applock.R;
import com.gdut.applock.log;
import com.gdut.applock.appmanager.applistAdapter.ItemOnClickListener;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class InstalledAppList extends Fragment implements OnClickListener{

	private ListView applist;
	private applistAdapter adapter;
	private List<appInfo> data;
	private PackageManager pm;
	private List<ResolveInfo> PmInfoList;
	private Button add;
	private FragmentEventListener eventListener;
	private List<appInfo> cache;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try{
			eventListener = (FragmentEventListener) activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString()+" must implement FragmentEventListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		pm = getActivity().getPackageManager();
		data=new ArrayList<appInfo>();
		cache = new ArrayList<appInfo>();
		final Intent LauncherIntent = new Intent("android.intent.action.MAIN");
		LauncherIntent.addCategory("android.intent.category.LAUNCHER");
		PmInfoList =pm.queryIntentActivities(LauncherIntent, PackageManager.GET_INTENT_FILTERS);
		createAppList();
		super.onCreate(savedInstanceState);
	}

	private void createAppList() {
		ResolveInfo info = PmInfoList.get(0);
		String packageName = info.activityInfo.packageName;
		String name = info.activityInfo.loadLabel(pm).toString();
		Drawable icon= info.activityInfo.loadIcon(pm);
		appInfo mappinfo = new appInfo(name, packageName,info.activityInfo.name, icon);
		data.add(mappinfo);
	}

	private List<appInfo> LoadAllAppList() {
		List<appInfo> datas = new ArrayList<appInfo>();
		int i=0;
		boolean isPick=false;
		for(ResolveInfo info:PmInfoList){
			//if(((info.applicationInfo.flags )&(1<<0))==0){
				String packageName = info.activityInfo.packageName;
				for(appInfo infoCache:eventListener.getBlockAppList()){
					if(packageName.equals(infoCache.PackageName)){
						isPick=true;
						break;
					}
					isPick=false;
				}
				if(isPick){
					
				}else{
					String name = info.activityInfo.loadLabel(pm).toString();
					Drawable icon= info.activityInfo.loadIcon(pm);
					log.d(packageName+" 's parentActivityName:"+info.activityInfo.name);
					appInfo mappinfo = new appInfo(name, packageName,info.activityInfo.name, icon);
					
					mappinfo.position=i;
					i++;
					datas.add(mappinfo);
				}
			//}
		}
		return datas;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.installed_app_list, null);
		applist = (ListView) view.findViewById(R.id.installed_list);
		adapter =new applistAdapter(data, getActivity(), true);
		adapter.setItemOnClickListener(new ItemOnClickListener() {
			
			@Override
			public void OnClick(String PackageName, String Name, boolean checkbox,int position) {
				log.d("setItemOnClickListener:"+PackageName+":"+checkbox);
				if(checkbox){
					cache.add(data.get(position));
				}else{
					cache.remove(data.get(position));
				}
			}
		});
		applist.setAdapter(adapter);
		add = (Button) view.findViewById(R.id.add);
		add.setOnClickListener(this);
		new AsyncLoaddata().execute();
		return view;
	}

	class AsyncLoaddata extends AsyncTask<Void,Void,List<appInfo>>{

		@Override
		protected List<appInfo> doInBackground(Void... arg0) {
			return LoadAllAppList();
			
		}

		@Override
		protected void onPostExecute(List<appInfo> result) {
			adapter.setData(result);
			adapter.notifyDataSetChanged();
			applist.invalidate();
			data=result;
			super.onPostExecute(result);
		}
		
		
	}

	@Override
	public void onClick(View v) {
		if(v==add){
			log.d("add onclick");
			for(appInfo info:cache){
				log.d("add :"+info.PackageName);
			}
			eventListener.addBlockApp(cache);
			eventListener.changeFragment(1);
		}
	}
	
}
