package com.gdut.applock.db;

import java.util.ArrayList;
import java.util.List;

import com.gdut.applock.log;
import com.gdut.applock.appmanager.appInfo;

import android.content.Context;
import android.database.Cursor;

public class BlockAppCache {

	private Context context;
	private List<appInfo> blockList;
	private BlockAppDB DB;

	public BlockAppCache(Context context) {
		this.context = context;
		blockList = new ArrayList<appInfo>();
		DB = new BlockAppDB(context);
		initList();
	}

	private void initList() {
		Cursor cursor = DB.query();
		if(cursor ==null){
			cursor.close();
			return;
		}
		if(cursor.moveToFirst()){
			do{
				log.d("blockList:"+cursor.getString(1)+"//"+cursor.getString(2)+"//"+cursor.getString(3));
				blockList.add(new appInfo(cursor.getString(1), cursor.getString(2),cursor.getString(3)));
			}while(cursor.moveToNext());
		}
		cursor.close();		
	}

	public List<appInfo> getBlockList() {
		return blockList;
	}
	
	public void add(appInfo data){
		blockList.add(data);
		DB.add(data.Name, data.PackageName,data.componentName);
	}
	public void deleted(appInfo data){
		blockList.remove(data);
		DB.delete(data.PackageName);
	}
}
