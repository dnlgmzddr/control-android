package com.banlinea.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.banlinea.control.bussiness.FinancialProductService;
import com.banlinea.control.dto.in.FinancialEntityDTO;
import com.banlinea.control.entities.Category;
import com.banlinea.control.entities.FinancialProduct;
import com.banlinea.control.remote.util.CallResult;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
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

public class ProductManagementActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_management);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mSectionsPagerAdapter.registerDataSetObserver(new DataSetObserver() {

			@Override
			public void onChanged() {
				int currPage = mViewPager.getCurrentItem();
				mViewPager.setAdapter(mSectionsPagerAdapter);
				mViewPager.setCurrentItem(currPage);
			}

		});

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_management, menu);
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
		case R.id.add_product:
			// Toast.makeText(this, "Create New Product not implemented yet",
			// Toast.LENGTH_SHORT).show();
			onCreateProduct();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void onCreateProduct() {
		int page = this.mViewPager.getCurrentItem();

		final int groupId = (page == 0) ? FinancialProduct.CATEGORY_SAVING_ACCOUNT
				: FinancialProduct.CATEGORY_CREDIT_CARD;

		AlertDialog.Builder builder = new AlertDialog.Builder(
				ProductManagementActivity.this);

		LayoutInflater inflater = ProductManagementActivity.this
				.getLayoutInflater();
		View layout = inflater.inflate(R.layout.alert_edit_product, null);
		builder.setTitle(R.string.create_product_title);

		builder.setView(layout);

		final EditText productAlias = (EditText) layout
				.findViewById(R.id.product_alias_textedit);
		Spinner banksSpinner = (Spinner) layout.findViewById(R.id.bank_spinner);
		final Spinner productsSpinner = (Spinner) layout
				.findViewById(R.id.product_spinner);

		ArrayAdapter<FinancialEntityDTO> banksSpinnerAdapter = new ArrayAdapter<FinancialEntityDTO>(
				ProductManagementActivity.this, R.layout.simple_spinner_item);

		banksSpinnerAdapter
				.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

		List<FinancialEntityDTO> banks;
		try {
			banks = new FinancialProductService(ProductManagementActivity.this)
					.getFinancialEntitiesByType(groupId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			banks = new ArrayList<FinancialEntityDTO>();
		}
		final List<FinancialEntityDTO> banksFinal = banks;
		banksSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> adapter,
							View view, int position, long id) {
						String bankId = banksFinal.get(position).getId();
						List<FinancialProduct> products;
						try {
							products = new FinancialProductService(
									ProductManagementActivity.this)
									.getFilteredProducts(groupId, bankId);
						} catch (Exception e) {
							products = new ArrayList<FinancialProduct>();
						}

						ArrayAdapter<FinancialProduct> productsSpinnerAdapter = new ArrayAdapter<FinancialProduct>(
								ProductManagementActivity.this,
								R.layout.simple_spinner_item);
						productsSpinnerAdapter
								.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
						productsSpinnerAdapter.addAll(products);
						productsSpinner.setAdapter(productsSpinnerAdapter);
						if (products.size() != 0) {
							productsSpinner.setSelection(0);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}

				});

		banksSpinnerAdapter.addAll(banks);
		banksSpinner.setAdapter(banksSpinnerAdapter);
		if (banks.size() != 0) {
			banksSpinner.setSelection(0);
		}

		builder.setPositiveButton(R.string.confirm,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Toast.makeText(ProductManagementActivity.this,
						// "Registrado", Toast.LENGTH_SHORT).show();
						CallResult result = new FinancialProductService(
								ProductManagementActivity.this).AddProduct(
								productAlias.getText().toString(),
								((FinancialProduct) productsSpinner
										.getSelectedItem()).getId(), groupId);
						if (result.isSuccessfullOperation()) {
							Toast.makeText(ProductManagementActivity.this,
									R.string.product_created,
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(ProductManagementActivity.this,
									result.getMessage(), Toast.LENGTH_SHORT)
									.show();
						}
					}
				});

		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Toast.makeText(ProductManagementActivity.this,
						// "cancelado", Toast.LENGTH_SHORT).show();
					}
				});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			fragment = new ListSectionFragment();
			Bundle args = new Bundle();
			switch (position) {
			case 0:
				args.putInt(ListSectionFragment.ARG_SECTION_TYPE,
						FinancialProduct.CATEGORY_SAVING_ACCOUNT);
				break;
			case 1:
				args.putInt(ListSectionFragment.ARG_SECTION_TYPE,
						FinancialProduct.CATEGORY_SAVING_ACCOUNT);
				break;
			}
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_bank_account).toUpperCase(l);
			case 1:
				return getString(R.string.title_credit_card).toUpperCase(l);
			}
			return null;
		}
	}

	// List Adapter
	public static class ListSectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */

		public static final String ARG_SECTION_TYPE = "section_type";
		private ListView productListView;
		private int groupId;

		public ListSectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_product_management, container, false);

			groupId = getArguments().getInt(ARG_SECTION_TYPE);

			List<FinancialProduct> products = new ArrayList<FinancialProduct>();
			
			FinancialProductService financialService = new FinancialProductService(getActivity());
			List<FinancialEntityDTO> entities;
			try {
				entities = financialService.getFinancialEntitiesByType(groupId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				entities = new ArrayList<FinancialEntityDTO>();
			}
			for (FinancialEntityDTO entity : entities) {
				try {
					products.addAll(financialService.getFilteredProducts(groupId, entity.getId()));
				} catch (Exception e) {}
			}

			ProductArrayAdapter adapter = new ProductArrayAdapter(
					getActivity(), products);

			productListView = (ListView) rootView.findViewById(R.id.listview);
			productListView.setAdapter(adapter);

			return rootView;
		}

		private class ProductArrayAdapter extends BaseAdapter {

			private final Context context;
			private List<FinancialProduct> products;

			public ProductArrayAdapter(Context context, List<FinancialProduct> products) {
				super();
				this.context = context;
				this.products = products;
			}

			@Override
			public int getCount() {
				return products.size();
			}

			@Override
			public Object getItem(int position) {
				return products.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View rowView = inflater.inflate(R.layout.row_product_manager,
						parent, false);
				
				TextView productNameTextView = (TextView) rowView.findViewById(R.id.product_name_textview);
				TextView bankNameTextView = (TextView) rowView.findViewById(R.id.product_bank_textview);
				ImageButton editButton = (ImageButton) rowView.findViewById(R.id.edit_imagebutton);
				ImageButton deleteButton = (ImageButton) rowView.findViewById(R.id.delete_imagebutton);
				
				productNameTextView.setText(products.get(position).getName() + " (" + products.get(position).ge + ")")
				
				return rowView;
			}

		}
	}

}
