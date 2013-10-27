package com.banlinea.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.banlinea.control.bussiness.CategoryService;
import com.banlinea.control.entities.Category;

@SuppressLint("NewApi")
public class CategoryManagerActivity extends Activity {

	private ListView categoryListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_manager);
		// Show the Up button in the action bar.
		setupActionBar();
		
		categoryListView = (ListView) findViewById(R.id.listview);
		
		List<Category> categories = new ArrayList<Category>();
		
		CategoryService catService = new CategoryService(getApplicationContext());
		try {
			List<Category> parents = catService.GetParentCategoriesPerGroup(Category.GROUP_EXPENSE);
			for (Category category : parents) {
				categories.add(category);
				List<Category> children = catService.GetChilds(category.getId());
				for (Category category2 : children) {
					categories.add(category2);
				}
			}
			
		} catch (SQLException e) {
			Log.d(CategoryManagerActivity.class.getName(), e.getMessage());
		}
		
		final CategoryArrayAdapter adapter = new CategoryArrayAdapter(this.getApplicationContext(), categories);
		
		categoryListView.setAdapter(adapter);
		
		categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				Toast.makeText(getApplicationContext(),
					      "1Click ListItem Number " + position, Toast.LENGTH_LONG)
					      .show();
			}
		});
		
	}
	
	private class CategoryArrayAdapter extends BaseAdapter {

		private final Context context;
		private final List<Category> categories;
		
		public CategoryArrayAdapter(Context context, List<Category> categories) {
			super();
			this.context = context;
			this.categories = categories;
		}
		
		@Override
		public int getCount() {
			return categories.size();
		}

		@Override
		public Object getItem(int position) {
			return categories.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.row_category_manager, parent, false);
			
			final int pos = position;
			rowView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(),
						      "Click ListItem Number " + pos, Toast.LENGTH_LONG)
						      .show();
				}
			});
			
			TextView categoryNameTextView = (TextView) rowView.findViewById(R.id.category_name_textview);
			categoryNameTextView.setText(categories.get(position).getName());
			
			CategoryService catService = new CategoryService(getApplicationContext());
			Category parentCategory;
			try {
				parentCategory = catService.GetCategory(categories.get(position).getIdParent());
				TextView categoryParentTextView = (TextView) rowView.findViewById(R.id.parent_category_textview);
				if (parentCategory != null) {
					categoryParentTextView.setText(parentCategory.getName());
				}
				else {
					categoryParentTextView.setText("");
				}
			} catch (SQLException e) {
				Log.d(CategoryManagerActivity.class.getName(), e.getMessage());
			}
			
			
			
			ImageButton editButton = (ImageButton) rowView.findViewById(R.id.edit_imagebutton);
			ImageButton deleteButton = (ImageButton) rowView.findViewById(R.id.delete_imagebutton);
			
			if (categories.get(position).getIdOwner() == Category.SYSTEM_OWNER_ID) {
			
				editButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						AlertDialog.Builder builder = new AlertDialog.Builder(CategoryManagerActivity.this);
						
						LayoutInflater inflater = CategoryManagerActivity.this.getLayoutInflater();
						View layout = inflater.inflate(R.layout.alert_edit_category, null);
						builder.setTitle(R.string.edit_category_title);
						builder.setView(layout);
						final EditText categoryName = (EditText) layout.findViewById(R.id.category_name_textedit);
						final Spinner parentCategory = (Spinner) layout.findViewById(R.id.parent_spinner);
						
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(CategoryManagerActivity.this, R.layout.simple_spinner_item);
						adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
						adapter.add("Selecciona una categoría padre");
						parentCategory.setAdapter(adapter);
						
						builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Toast.makeText(getApplicationContext(), "Edited: " + categoryName.getText().toString(), Toast.LENGTH_SHORT).show();
							}
						});
						builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
						AlertDialog dialog = builder.create();
						dialog.show();
						
						Toast.makeText(getApplicationContext(), "Edit", Toast.LENGTH_SHORT).show();
					}
				});
				
				
				deleteButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						AlertDialog.Builder builder = new AlertDialog.Builder(CategoryManagerActivity.this);
						builder.setTitle(R.string.delete_category_title).setMessage(R.string.delete_category_message);
						builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
							}
						});
						builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
						AlertDialog dialog = builder.create();
						dialog.show();
					}
				});
			}
			else {
				editButton.setVisibility(View.INVISIBLE);
				deleteButton.setVisibility(View.INVISIBLE);
			}
			
			return rowView;
		}
		
		
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.category_manager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
			
		case R.id.add_category:
			Toast.makeText(this, "Create New Category", Toast.LENGTH_SHORT).show();			
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
