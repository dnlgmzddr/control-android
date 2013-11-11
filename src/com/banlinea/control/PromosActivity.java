package com.banlinea.control;

import java.text.DateFormat;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.banlinea.control.bussiness.FinancialProductService;
import com.banlinea.control.entities.Promotion;
import com.banlinea.control.entities.UserFinancialProduct;

public class PromosActivity extends Activity {

	ListView promosListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promos);
		// Show the Up button in the action bar.
		setupActionBar();
		
		promosListView = (ListView) findViewById(R.id.promos_listView);
		
		final List<Promotion> promosList = new FinancialProductService(this).getUserPromotions();
		final PromotionsListAdapter adapter = new PromotionsListAdapter(this, promosList);
		promosListView.setAdapter(adapter);
		
		promosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String link = promosList.get(position).getLink();
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
				startActivity(browserIntent);
			}
		});
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
		//getMenuInflater().inflate(R.menu.promos, menu);
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
	
	private class PromotionsListAdapter extends ArrayAdapter<Promotion> {
		
		private Context context;
		private List<Promotion> promotions;
		
		public PromotionsListAdapter (Context context, List<Promotion> promotions) {
			super (context, R.layout.item_promo, promotions);
			this.context = context;
			this.promotions = promotions;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_promo, null);
			}
			
			TextView titleTextView = (TextView) convertView.findViewById(R.id.promo_name_textView);
			TextView productsTextView = (TextView) convertView.findViewById(R.id.promo_products_textView);
			TextView expirationTextView = (TextView) convertView.findViewById(R.id.expiration_date_textView);
			TextView descriptionTextView = (TextView) convertView.findViewById(R.id.description_textView);
			
			Promotion promotion = promotions.get(position);
			
			// Title
			titleTextView.setText(promotion.getTitle());
			
			// Products
			String relatedProductsText = "";
			List<UserFinancialProduct> relatedProducts = promotion.getRelatedProducts();
			int pos = 0;
			for (UserFinancialProduct userFinancialProduct : relatedProducts) {
				if (pos > 0) relatedProductsText += ", ";
				relatedProductsText += userFinancialProduct.getName();
				pos++;
			}
			relatedProductsText += ".";
			productsTextView.setText(relatedProductsText);
			
			// Expiration
			DateFormat formatter = DateFormat.getDateInstance();
			expirationTextView.setText(formatter.format(promotion.getToDate()));
			
			// Description
			descriptionTextView.setText(promotion.getExcerpt());
			return convertView;
		}
		
		
		
	}

}
