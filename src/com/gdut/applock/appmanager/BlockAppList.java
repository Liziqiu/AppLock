package com.gdut.applock.appmanager;

import java.util.List;

import com.gdut.applock.R;
import com.gdut.applock.log;
import com.gdut.applock.db.BlockAppDB;
import com.gdut.widget.ScrollListView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class BlockAppList extends Fragment implements OnClickListener{

	private FragmentEventListener eventListener;
	private ScrollListView blockList;
	private Button choose;
	private applistAdapter adapter;
	private List<appInfo> blocklistCache;
	@Override
	public void onAttach(Activity activity) {
		try{
			eventListener = (FragmentEventListener) activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString()+" must implement FragmentEventListener");
		}
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		log.d("BlockAppList onCreateView");
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.block_app_list, null);
		blockList = (ScrollListView) view.findViewById(R.id.block_app_lists);
		blockList.setScrollOutListener(new ScrollListView.ScrollOutListener() {
			
			@Override
			public void fadeOut(int direction, View item,int position) {
				log.d("position:"+position+"//"+direction);
				eventListener.deleteBlockApp(eventListener.getBlockAppList().get(position));
				adapter.notifyDataSetChanged();
				blockList.invalidate();
			}
		});
		choose = (Button) view.findViewById(R.id.create);
		choose.setOnClickListener(this);
		//blocklistCache = eventListener.getBlockAppList();
		adapter = new applistAdapter(eventListener.getBlockAppList(), getActivity(), false);
		blockList.setAdapter(adapter);
		return view;
	}

	@Override
	public void onClick(View v) {
		if(v == choose){
			eventListener.changeFragment(0);
		}
	}

	
}
