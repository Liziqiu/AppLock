package com.gdut.applock.appmanager;

import com.gdut.applock.log;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

public class appInfo {

	public String Name;
	public String PackageName;
	public String componentName; 
	public Drawable Icon=null;
	public ActivityInfo mPackageInfo;
	public boolean isPickon=false;
	public int position=0;//数组中的位置，方便查找

	public appInfo(String name, String packageName, String componentName) {
		super();
		Name = name;
		PackageName = packageName;
		this.componentName = componentName;
	}

	public appInfo(String name, String packageName, String componentName,
			Drawable icon) {
		super();
		Name = name;
		PackageName = packageName;
		this.componentName = componentName;
		Icon = icon;
	}
	public void asycLoadIcon(ImageView container,Context c){
		new asyncloader(c,container).execute(PackageName,componentName);
	}
	
	@Override
	public boolean equals(Object o) {
		appInfo info=null;
		try{
			info = (appInfo)o;
		}catch(ClassCastException e){
			
		}
		if(info == null){
			return super.equals(o);
		}
		if(PackageName.equals(info.PackageName)){
			return true;
		}
		return false;
		
	}





	class asyncloader extends AsyncTask<String, String, Drawable>{

		private Context c;
		private ImageView contain;
		private PackageManager pm;
		
		public asyncloader(Context c, ImageView contain) {
			super();
			this.c = c;
			this.contain = contain;
			pm=c.getPackageManager();
		}


		@Override
		protected Drawable doInBackground(String... name) {
			log.d("doInBackground");
			Drawable icon=null;
			if(mPackageInfo ==null){
				try {
					mPackageInfo=pm.getActivityInfo(new ComponentName(name[0], name[1]), 0);//getPackageInfo(PackageName, 0);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(mPackageInfo ==null)
				return null;
			log.d("mPackageInfo name:"+mPackageInfo.name);
			icon=mPackageInfo.loadIcon(pm);
			return icon;
		}


		@Override
		protected void onPostExecute(Drawable result) {
			Icon=result;
			contain.setImageDrawable(result);
			log.d("onPostExecute");
			super.onPostExecute(result);
		}
		
		
		
	}
}
