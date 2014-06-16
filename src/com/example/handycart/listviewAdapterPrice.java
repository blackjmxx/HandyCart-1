package com.example.handycart;


import static com.example.handycart.Constant.TOTAL;
import static com.example.handycart.Constant.PRICE;


import java.util.ArrayList;
import java.util.HashMap;

import com.example.handycart.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class listviewAdapterPrice extends BaseAdapter{
	 public ArrayList<HashMap> list;
	    Activity activity;
	 
	    public listviewAdapterPrice(Activity activity, ArrayList<HashMap> list) {
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
	           TextView txtTotal;
	           TextView txtPrice;

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
	                    holder.txtTotal = (TextView) convertView.findViewById(R.id.Total);
	                    holder.txtPrice = (TextView) convertView.findViewById(R.id.Price);
	                    convertView.setTag(holder);
	                }
	                else
	                {
	                    holder = (ViewHolder) convertView.getTag();
	                }
	 
	                HashMap map = list.get(position);
	                holder.txtTotal.setText((CharSequence) map.get(TOTAL));
	                holder.txtPrice.setText((CharSequence) map.get(PRICE));

	 
	            return convertView;
	    }

	

}
