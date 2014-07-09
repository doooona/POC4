package com.easy.app.chatmessagerlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class LockItemAdapter extends BaseAdapter{
	Context context;
	List<ListItems> listData; 
	SettingsActivity settingActivity;

	public LockItemAdapter(Context context,List<ListItems> listData, SettingsActivity setActivity) {  
		this.context = context;  
		this.listData = listData;
		this.settingActivity = setActivity;
	}  
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(listData!=null)
			return listData.size(); 
		else
			return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if(listData!=null)
			return listData.get(arg0);
		else
			return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		if(listData!=null)
			return arg0; 
		else
			return 0;
	}
	static class ContactsViewHolder {
		ImageView icon;
		TextView appName;
		ToggleButton tlb;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		final ContactsViewHolder viewHolder;
		  
		/*if(convertView == null){
	
			viewHolder = new ContactsViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.items, arg2,false);
			ImageView icon = (ImageView) convertView
					.findViewById(R.id.app_icon);
			TextView appName = (TextView) convertView
					.findViewById(R.id.app_name);
			ToggleButton tgl = (ToggleButton) convertView
					.findViewById(R.id.app_tgl);
			viewHolder.appName = appName;
			viewHolder.icon = icon;
			viewHolder.tlb = tgl;
			
			convertView.setTag(viewHolder);
			
		}else{
			Log.d("@@@", "hi");
			viewHolder = (ContactsViewHolder) convertView.getTag();
		}*/
		
		final ListItems items = listData.get(arg0);
		
		LayoutInflater mInflater = LayoutInflater.from(context);
		convertView = mInflater.inflate(R.layout.items,null);
		final ToggleButton tgl = (ToggleButton) convertView
				.findViewById(R.id.app_tgl);
		
		ImageView icon = (ImageView) convertView
				.findViewById(R.id.app_icon);
		TextView appName = (TextView) convertView
				.findViewById(R.id.app_name);
		
		
		tgl.setFocusable(false);
		tgl.setFocusableInTouchMode(false);
		tgl.setOnCheckedChangeListener(new OnCheckedChangeListener () {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked){
					settingActivity.addRecordIntoDB(items.getPkgName(), "TRUE");
				}else{
					settingActivity.addRecordIntoDB(items.getPkgName(), "FALSE");
				}
			}
			});	
		 
		icon.setImageDrawable(items.getIcon());	
		appName.setText(items.getAppName());
		tgl.setChecked(items.isLock());
		
		return convertView;
	}

}
