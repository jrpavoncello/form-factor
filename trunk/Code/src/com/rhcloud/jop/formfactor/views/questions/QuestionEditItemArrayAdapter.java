package com.rhcloud.jop.formfactor.views.questions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.rhcloud.jop.formfactor.R;

public class QuestionEditItemArrayAdapter extends ArrayAdapter<QuestionEditItem>
{
	private QuestionEditItem[] mEditItems;
	
    public QuestionEditItemArrayAdapter(Context context, int resource, QuestionEditItem[] editItems)
    {
        super(context, resource, editItems);
        this.mEditItems = editItems;
    }
    
    public void setDrawerItems(QuestionEditItem[] editItems)
    {
    	this.mEditItems = editItems;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.view_group_question_edit_item, parent, false);

        TextView itemName = (TextView)convertView.findViewById(R.id.activity_multiple_choice_question_edit_answer_min_text);
        EditText item = (EditText)convertView.findViewById(R.id.activity_multiple_choice_question_edit_answer_min);

        itemName.setText(this.mEditItems[position].getItemName());
        item.setText(this.mEditItems[position].getItem());
        
        item.addTextChangedListener(this.mEditItems[position].getTextWatcher());
    	
    	return convertView;
    }
}
