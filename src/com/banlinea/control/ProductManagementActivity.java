package com.banlinea.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.banlinea.control.entities.Category;

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
import android.widget.BaseAdapter;
import android.widget.ListView;
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
			//Toast.makeText(this, "Create New Product not implemented yet",
			//		Toast.LENGTH_SHORT).show();
			onCreateProduct();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void onCreateProduct() {
		int page = this.mViewPager.getCurrentItem();

		final String groupId = (page == 0)? "Cuenta": "Tarjeta";
		
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ProductManagementActivity.this);

		LayoutInflater inflater = ProductManagementActivity.this
				.getLayoutInflater();
		View layout = inflater.inflate(R.layout.alert_edit_product, null);
		builder.setTitle(R.string.create_product_title);

		builder.setView(layout);
		
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(ProductManagementActivity.this, "Registrado", Toast.LENGTH_SHORT).show();
			}
		});
		
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(ProductManagementActivity.this, "cancelado", Toast.LENGTH_SHORT).show();
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
				args.putString(ListSectionFragment.ARG_SECTION_TYPE, "Cuenta");
				break;
			case 1:
				args.putString(ListSectionFragment.ARG_SECTION_TYPE, "Tarjeta");
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
		private String groupId;

		public ListSectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_product_management, container, false);

			groupId = getArguments().getString(ARG_SECTION_TYPE);

			List<String> products = new ArrayList<String>();
			products.add("Test1");
			products.add("Test2");

			ProductArrayAdapter adapter = new ProductArrayAdapter(
					getActivity(), products);

			productListView = (ListView) rootView.findViewById(R.id.listview);
			productListView.setAdapter(adapter);

			return rootView;
		}

		private class ProductArrayAdapter extends BaseAdapter {

			private final Context context;
			private List<String> products;

			public ProductArrayAdapter(Context context, List<String> products) {
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
				
				return rowView;
			}

		}
	}

}
