package com.banlinea.control;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.banlinea.control.bussiness.TransactionService;
import com.banlinea.control.dto.out.FullTransaction;
import com.banlinea.control.entities.definitions.SafeSpendPeriod;

public class TransactionListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction_list);
		// Show the Up button in the action bar.
		setupActionBar();

		SafeSpendPeriod period = (SafeSpendPeriod) getIntent()
				.getSerializableExtra("com.banlinea.control.balance.period");

		List<FullTransaction> transactions = new TransactionService(this)
				.getTransactions(period);

		TransactionListAdapter adapter = new TransactionListAdapter(this,
				transactions);

		this.setListAdapter(adapter);
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
		// getMenuInflater().inflate(R.menu.transaction_list, menu);
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

	private class TransactionListAdapter extends ArrayAdapter<FullTransaction> {

		private Context context;
		private List<FullTransaction> transactions;

		public TransactionListAdapter(Context context,
				List<FullTransaction> objects) {
			super(context, R.layout.item_full_transaction, objects);
			this.context = context;
			this.transactions = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_full_transaction,
						null);
			}
			
			TextView categoryTextView = (TextView) convertView.findViewById(R.id.category_textView);
			TextView dateTextView = (TextView) convertView.findViewById(R.id.date_textView);
			TextView productTextView = (TextView) convertView.findViewById(R.id.product_textView);
			TextView amountTextView = (TextView) convertView.findViewById(R.id.amount_textView);
			
			FullTransaction transaction = transactions.get(position);
			
			categoryTextView.setText(transaction.getParentCategoryName() + "/" + transaction.getCategoryName());
			
			DateFormat formatter = DateFormat.getDateInstance();
			dateTextView.setText(formatter.format(transaction.getDate()));
			
			productTextView.setText(transaction.getProductName());
			
			NumberFormat currFormatter = NumberFormat.getCurrencyInstance();
			amountTextView.setText(currFormatter.format(transaction.getAmount()));

			return convertView;
		}

	}

}
