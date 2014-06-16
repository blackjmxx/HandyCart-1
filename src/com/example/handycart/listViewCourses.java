package com.example.handycart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class listViewCourses extends BaseAdapter{
	private static ArrayList<ListeCourses> itemDetailsrrayList;
	  
	 private Integer[] imgid = {
	   R.drawable.right,
	   R.drawable.wrong
	   };
	  
	 private LayoutInflater l_Inflater;
	 

	public  listViewCourses(final Context context, final ArrayList<ListeCourses> results) {
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
	   convertView = l_Inflater.inflate(R.layout.listview, null);
	   holder = new ViewHolder();
	   holder.txt_itemName = (TextView) convertView.findViewById(R.id.FirstText);
	   holder.txt_itemDescription = (TextView) convertView.findViewById(R.id.SecondText);
	   holder.txt_itemPrice = (TextView) convertView.findViewById(R.id.ThirdText);
	   holder.itemImage = (ImageView) convertView.findViewById(R.id.img);
	 
	   convertView.setTag(holder);
	  } else {
	   holder = (ViewHolder) convertView.getTag();
	  }
	   
	  holder.txt_itemName.setText(itemDetailsrrayList.get(position).getName());
	  holder.txt_itemDescription.setText(itemDetailsrrayList.get(position).getItemQuantite());
	  holder.txt_itemPrice.setText(itemDetailsrrayList.get(position).getPrice());
	  holder.itemImage.setImageResource(imgid[itemDetailsrrayList.get(position).getImageNumber()]);
	 
	  return convertView;
	 }
	 
	 static class ViewHolder {
	  TextView txt_itemName;
	  TextView txt_itemDescription;
	  TextView txt_itemPrice;
	  ImageView itemImage;
	 }

}
