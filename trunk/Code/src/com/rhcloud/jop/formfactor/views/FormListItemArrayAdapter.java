package com.rhcloud.jop.formfactor.views;

import com.rhcloud.jop.formfactor.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FormListItemArrayAdapter extends ArrayAdapter<FormListItem>
{
	private FormListItem[] mFormListItems;
	
    public FormListItemArrayAdapter(Context context, int resource, FormListItem[] formListItems)
    {
        super(context, resource, formListItems);
        this.mFormListItems = formListItems;
    }
    
    public void setDrawerItems(FormListItem[] drawerItems)
    {
    	this.mFormListItems = drawerItems;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.form_list_item, parent, false);

        TextView tv = (TextView)convertView.findViewById(R.id.form_list_item_text);
        
        tv.setText(mFormListItems[position].getItemText());
    	
    	return convertView;
    }
}