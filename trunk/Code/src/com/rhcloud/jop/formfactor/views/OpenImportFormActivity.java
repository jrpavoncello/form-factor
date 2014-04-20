package com.rhcloud.jop.formfactor.views;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.views.MainActivity.FormFactorPagerAdapter;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class OpenImportFormActivity extends FormFactorFragmentActivity
{
	private ViewPager mViewPager;
	private ListView mFormList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.activity_create);
		super.onCreate(savedInstanceState);
        
        this.setTitle(this.getResources().getString(R.string.activity_create_title));
		
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new FormFactorPagerAdapter(getSupportFragmentManager(), this));
        
        mFormList = (ListView)this.findViewById((R.id.form_list));
        
        mFormList.setAdapter(new FormListItemArrayAdapter(this, R.layout.form_list_item, gatherFormListItems()));
	}
	
	private FormListItem[] gatherFormListItems()
	{
		
		
        String[] textRefs = getResources().getStringArray(R.array.drawer_items_text);
        
        TypedArray imageRefs = getResources().obtainTypedArray(R.array.drawer_items_image);

        FormListItem[] items = new FormListItem[textRefs.length];
        for(int i = 0; i < textRefs.length; i++)
        {
        	FormListItem item = new FormListItem(imageRefs.getDrawable(i), textRefs[i]);
        	items[i] = item;
        }
        
        imageRefs.recycle();
        
        return items;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.open_import_form, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.menu_create_settings)
		{
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}