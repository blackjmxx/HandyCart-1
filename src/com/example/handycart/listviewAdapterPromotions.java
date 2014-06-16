package com.example.handycart;
import static com.example.handycart.Constant.PROMO1;
import static com.example.handycart.Constant.PROMO2;
import static com.example.handycart.Constant.PROMO3;
import static com.example.handycart.Constant.PROMO4;
import static com.example.handycart.Constant.PROMO5;

import java.util.ArrayList;
import java.util.HashMap;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class listviewAdapterPromotions extends BaseAdapter{
	public ArrayList<HashMap> list;
    Activity activity;
 
    public  listviewAdapterPromotions (Activity activity, ArrayList<HashMap> list) {
    	 super();
        this.activity = activity;
        this.list = list;
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }
 
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
 
    public class ViewHolder {
           ImageView promotion1;
           ImageView promotion2;
           ImageView promotion3;
           ImageView promotion4;
           ImageView promotion5;

      }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
 
        // TODO Auto-generated method stub
                ViewHolder holder;
                LayoutInflater inflater =  activity.getLayoutInflater();
 
                if (convertView == null)
                {
                    convertView = inflater.inflate(R.layout.addition, null);
                    holder = new ViewHolder();
                    holder.promotion1 = (ImageView) convertView.findViewById(R.id.list_image1);
                    holder.promotion2 = (ImageView) convertView.findViewById(R.id.list_image2);
                    holder.promotion3 = (ImageView) convertView.findViewById(R.id.list_image3);
                    holder.promotion4 = (ImageView) convertView.findViewById(R.id.list_image4);
                    holder.promotion5 = (ImageView) convertView.findViewById(R.id.list_image5);
                    convertView.setTag(holder);
                }
                else
                {
                    holder = (ViewHolder) convertView.getTag();
                }


 
            return convertView;
    }

 
}
