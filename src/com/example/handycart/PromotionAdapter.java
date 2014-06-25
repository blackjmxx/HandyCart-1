package com.example.handycart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;


public class PromotionAdapter extends BaseAdapter{
	private static ArrayList<Promotions> itemDetailsrrayList;
	 private Integer[] imgid = {
			   R.drawable.yoplait,
			   R.drawable.bledina,
			   R.drawable.coca,
			   R.drawable.toilettes,
			   R.drawable.tele
			   };
	 
	 private LayoutInflater l_Inflater;
	 

		public  PromotionAdapter(final Context context, final ArrayList<Promotions> results) {
		  itemDetailsrrayList = results;
		  l_Inflater = LayoutInflater.from(context);
		 }
		
		 public int getCount() {
			  return itemDetailsrrayList.size();
			 }
			 
			 public Object getItem(int position) {
			  return itemDetailsrrayList.get(position);
			 }
			 
			 public long getItemId(int position) {
			  return position;
			 }
			 
			 public View getView(int position, View convertView, ViewGroup parent) {
			  ViewHolder holder;
			  if (convertView == null) {
			   convertView = l_Inflater.inflate(R.layout.promotions, null);
			   holder = new ViewHolder();
			   holder.image1 = (ImageView) convertView.findViewById(R.id.image1);
			   holder.image2 = (ImageView) convertView.findViewById(R.id.image2);
			   holder.image3 = (ImageView) convertView.findViewById(R.id.image3);
			   holder.image4 = (ImageView) convertView.findViewById(R.id.image4);
			   holder.image5 = (ImageView) convertView.findViewById(R.id.image5);
			 
			   convertView.setTag(holder);
			  } else {
			   holder = (ViewHolder) convertView.getTag();
			  }
			   
			  holder.image1.setImageResource(imgid[0]);
			  holder.image2.setImageResource(imgid[1]);
			  holder.image3.setImageResource(imgid[2]);
			  holder.image4.setImageResource(imgid[3]);
			  holder.image5.setImageResource(imgid[4]);
			  return convertView;
			 }
			 
			 static class ViewHolder {
			  ImageView image1, image2, image3, image4, image5;
			 }

}
