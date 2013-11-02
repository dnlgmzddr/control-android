package com.banlinea.control;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.banlinea.control.bussiness.CategoryService;
import com.banlinea.control.bussiness.TransactionService;
import com.banlinea.control.entities.Category;
import com.banlinea.control.entities.UserFinancialProduct;
import com.banlinea.control.remote.util.CallResult;

public class RegisterTransactionActivity extends Activity {

	RadioGroup typeRadioGroup;
	Spinner parentCategorySpinner;
	Spinner childrenCategorySpinner;
	CheckBox useProductCheckBox;
	Spinner productSpinner;
	Button registerTransactionButton;
	EditText amountEditText;
	
	ResultReceiver receiverTag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.receiverTag = getIntent().getParcelableExtra("receiverTag");
		
		setContentView(R.layout.activity_register_transaction);

		typeRadioGroup = (RadioGroup) findViewById(R.id.typeRadioGroup);
		parentCategorySpinner = (Spinner) findViewById(R.id.parentCategorySpinner);
		childrenCategorySpinner = (Spinner) findViewById(R.id.childrenCategorySpinner);
		amountEditText = (EditText) findViewById(R.id.amountEditText);
		typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				try {
					CategoryService catService = new CategoryService(getApplicationContext());
					List<Category> parentCategories = null;
					switch (checkedId) {
					case R.id.radioExpense:
						parentCategories =  catService.GetParentCategoriesPerGroup(Category.GROUP_EXPENSE);
						break;
					case R.id.radioIncome:
						parentCategories =  catService.GetParentCategoriesPerGroup(Category.GROUP_INCOME);
						break;
					case R.id.radioSavings:
						parentCategories =  catService.GetParentCategoriesPerGroup(Category.GROUP_SAVING);
						break;
					}
					ArrayAdapter<Category> parentCategoryAdapter = new ArrayAdapter<Category> (
							RegisterTransactionActivity.this, 
							R.layout.simple_spinner_item,
							parentCategories);
					parentCategoryAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
					parentCategorySpinner.setAdapter(parentCategoryAdapter);
					
					if (parentCategories.size() == 0) {
						ArrayAdapter<Category> childrenCategoryAdapter = new ArrayAdapter<Category>(
								RegisterTransactionActivity.this,
								R.layout.simple_spinner_item,
								parentCategories);
						childrenCategoryAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
						childrenCategorySpinner.setAdapter(childrenCategoryAdapter);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		typeRadioGroup.check(R.id.radioExpense);
		
		List<Category> parentCategories = null;
		try {
			CategoryService catService = new CategoryService(getApplicationContext());
			parentCategories = catService.GetParentCategoriesPerGroup(Category.GROUP_EXPENSE);
			
			ArrayAdapter<Category> parentCategoryAdapter = new ArrayAdapter<Category> (
					RegisterTransactionActivity.this, 
					R.layout.simple_spinner_item,
					parentCategories);
			parentCategoryAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
			parentCategorySpinner.setAdapter(parentCategoryAdapter);
			
		} catch (SQLException e) {
			Log.d("RedisterTransaction", e.getMessage());
		}
		
		parentCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				Toast.makeText(RegisterTransactionActivity.this, 
		                "On Parent Category Select : \n" + parent.getItemAtPosition(pos).toString(),
		                Toast.LENGTH_LONG).show();
				
				try {
					CategoryService catService = new CategoryService(getApplicationContext());
					List<Category> childrenCategories = catService.GetChilds(((Category)parent.getItemAtPosition(pos)).getId());
					
					ArrayAdapter<Category> childrenCategoryAdapter = new ArrayAdapter<Category> (
							RegisterTransactionActivity.this, 
							R.layout.simple_spinner_item,
							childrenCategories);
					childrenCategoryAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
					childrenCategorySpinner.setAdapter(childrenCategoryAdapter);
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
		
		
		
		childrenCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				Toast.makeText(RegisterTransactionActivity.this, 
		                "On Child Category Select : \n" + parent.getItemAtPosition(pos).toString(),
		                Toast.LENGTH_LONG).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});		
		
		useProductCheckBox = (CheckBox) findViewById(R.id.productCheckBox);
		useProductCheckBox.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (useProductCheckBox.isChecked()) {
					productSpinner.setVisibility(View.VISIBLE);
				}
				else {
					productSpinner.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		productSpinner = (Spinner) findViewById(R.id.productSpinner);
		productSpinner.setVisibility(View.INVISIBLE);
		
		registerTransactionButton = (Button) findViewById(R.id.register_button);
		registerTransactionButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String selectedCategory = ((Category)childrenCategorySpinner.getSelectedItem()).getId();
				float amount = Float.parseFloat(amountEditText.getText().toString());
				CallResult result = new TransactionService(RegisterTransactionActivity.this).AddTransaction(selectedCategory, amount, UserFinancialProduct.DEFAULT_PRODUCT);
				Bundle bundle = new Bundle();
				bundle.putBoolean("result", result.isSuccessfullOperation());
				receiverTag.send(0, bundle);
				RegisterTransactionActivity.this.finish();	
			}
		});
		
		// Show the Up button in the action bar.
		setupActionBar();
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
		getMenuInflater().inflate(R.menu.register_transaction, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}

}
