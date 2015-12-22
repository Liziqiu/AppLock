package com.gdut.applock;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	private Button start,stop;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initRes();
	}
	private void initRes() {
		start = (Button) findViewById(R.id.start);
		stop = (Button) findViewById(R.id.stop);
		start.setOnClickListener(this);
		stop.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v == start){
			Intent i = new Intent();
			i.setClass(this, com.gdut.applock.ScanService.class);
			i.setAction("start");
			startService(i);
		}else if(v == stop){
			/*Intent i = new Intent();
			i.setClass(this, com.gdut.applock.ScanService.class);
			i.setAction("stop");
			startService(i);*/
			Intent i =new Intent();
			i.setClass(this, com.gdut.applock.appmanager.applist.class);
			startActivity(i);
		}
	}

}
