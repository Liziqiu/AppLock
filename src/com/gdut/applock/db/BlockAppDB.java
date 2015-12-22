package com.gdut.applock.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BlockAppDB extends SQLiteOpenHelper{

	public static final String DB_NAME = "block_app_db";
	public static final int DB_VERSION=1;
	
	private SQLiteDatabase Readable_DB=this.getReadableDatabase();
	private SQLiteDatabase Writeable_DB=this.getWritableDatabase();
	private ContentValues cv = new ContentValues();
	
	public static final String BLOCK_APP_TABLE="block_app_table";
	public static final String NO="no";
	public static final String APP_NAME="app_name";
	public static final String PACKAGE_NAME = "package_name";
	public static final String COMPONENT_NAME = "component_name";
	
	public BlockAppDB(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createBlockAppTable(db);
	}

	private void createBlockAppTable(SQLiteDatabase db) {
		String sql="CREATE TABLE IF NOT EXISTS " + BLOCK_APP_TABLE +" ("+
				NO+" integer primary key autoincrement, "+
				APP_NAME +" VARCHAR," +
				PACKAGE_NAME +" VARCHAR," +
				COMPONENT_NAME +" VARCHAR" +
				")";
		db.execSQL(sql);
	}
	public Cursor query(){
		return Readable_DB.query(BLOCK_APP_TABLE, null, null, null, null, null, null,null);
	}
	public void add(String appName,String pkgName,String componentName){
		cv.clear();
		cv.put(APP_NAME, appName);
		cv.put(PACKAGE_NAME, pkgName);
		cv.put(COMPONENT_NAME, componentName);
		Writeable_DB.insert(BLOCK_APP_TABLE, null, cv);
	}

	public void delete(String pkgName){
		String whereClause = "PACKAGE_NAME=?";
		String[] whereArgs ={pkgName};
		Writeable_DB.delete(BLOCK_APP_TABLE, whereClause, whereArgs);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	private void deleteTable(String table){
		try{
			Writeable_DB.execSQL("drop table "+table);
		}catch(SQLException e){
			
		}
	}
}
