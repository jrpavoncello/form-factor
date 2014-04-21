package com.rhcloud.jop.formfactor.views;

import com.rhcloud.jop.formfactor.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerItemArrayAdapter extends ArrayAdapter<DrawerItem>
{
	private DrawerItem[] mDrawerItems;
	
    public DrawerItemArrayAdapter(Context context, int resource, DrawerItem[] drawerItems)
    {
        super(context, resource, drawerItems);
        this.mDrawerItems = drawerItems;
    }
    
    public void setDrawerItems(DrawerItem[] drawerItems)
    {
    	this.mDrawerItems = drawerItems;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);

        TextView tv = (TextView)convertView.findViewById(R.id.drawer_list_item_text);
        ImageView iv = (ImageView)convertView.findViewById(R.id.drawer_list_item_image);

        tv.setText(mDrawerItems[position].getItemText());
        iv.setImageDrawable(mDrawerItems[position].getImage());
    	
    	return convertView;
    }
}