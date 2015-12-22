package com.gdut.applock.appmanager;

import java.util.List;

import com.gdut.applock.R;
import com.gdut.applock.log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class applistAdapter extends BaseAdapter{

	private List<appInfo> data;
	private Context context;
	private final boolean needCheckbox;
	private ItemOnClickListener itemOnClickListener;
		
	public applistAdapter(List<appInfo> data, Context context,
			boolean needCheckbox) {
		super();
		this.data = data;
		this.context = context;
		this.needCheckbox = needCheckbox;
	}

	
	public void setData(List<appInfo> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public appInfo getItem(int i) {
		// TODO Auto-generated method stub
		return data.get(i);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View ConverView, ViewGroup group) {
		viewholder mviewholder;
		if(ConverView != null){
			mviewholder=(viewholder) ConverView.getTag();
		}else{
			ConverView = LayoutInflater.from(context).inflate(R.layout.app_list_item, null);
			mviewholder = new viewholder();
			mviewholder.icon = (ImageView) ConverView.findViewById(R.id.app_icon);
			mviewholder.name = (TextView) ConverView.findViewById(R.id.app_name);
			mviewholder.appPickon = (CheckBox) ConverView.findViewById(R.id.app_pickon);
			ConverView.setTag(mviewholder);
		}
		mviewholder.name.setText(getItem(position).Name);
		if(getItem(position).Icon == null){
			getItem(position).asycLoadIcon(mviewholder.icon, context);
		}else{
			mviewholder.icon.setImageDrawable(getItem(position).Icon);
		}
		if(!needCheckbox){
			mviewholder.appPickon.setVisibility(View.GONE);
		}else{
			mviewholder.appPickon.setChecked(getItem(position).isPickon);
			ConverView.setOnClickListener(new checkboxlistener(position));
		}
		return ConverView;
	}
	public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
		this.itemOnClickListener = itemOnClickListener;
	}


	class viewholder{
		public ImageView icon;
		public TextView name;
		public CheckBox appPickon;
	}
	class checkboxlistener implements OnClickListener{

		private int position=0;
		
		public checkboxlistener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			CheckBox cb = (CheckBox) v.findViewById(R.id.app_pickon);
			cb.setChecked(!cb.isChecked());
			boolean checkon = cb.isChecked();
			log.d(getItem(position).PackageName+":"+getItem(position).Name+":"+checkon);
			if(itemOnClickListener!=null){
				itemOnClickListener.OnClick(getItem(position).PackageName, getItem(position).Name, checkon,getItem(position).position);
			}
			getItem(position).isPickon=checkon;
		}
		
	}
	interface ItemOnClickListener{
		public void OnClick(String PackageName,String Name,boolean checkbox,int position);
	}
}
