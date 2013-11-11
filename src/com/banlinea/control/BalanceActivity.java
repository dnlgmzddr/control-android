package com.banlinea.control;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.NumberFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.banlinea.control.bussiness.AuthenticationService;
import com.banlinea.control.bussiness.BudgetService;
import com.banlinea.control.entities.definitions.SafeSpendPeriod;

public class BalanceActivity extends Activity {

	LinearLayout dailyBalanceWrapper;
	TextView dailyBalance;
	TextView weeklyBalance;
	TextView monthlyBalance;
	
	ResultReceiver registerTransactionResultReceiver;
	ResultReceiver InitialSetupResultReceiver;

	BudgetService budgetService;

	private final NumberFormat formatter = NumberFormat.getCurrencyInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		budgetService = new BudgetService(this);

		
		
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(ReminderReceiver.REMINDER_NOTIFICATION);

		setContentView(R.layout.activity_balance);
		getOverflowMenu();

		// Result receiver to refresh balance when registering a new
		// transaction.
		this.registerTransactionResultReceiver = new ResultReceiver(
				new Handler()) {

			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				Log.d("NEW TRANSACTION REGISTERED", resultData
						.getBoolean("result") ? "Successful" : "Error");
				if (resultData.getBoolean("result")) {
						updateBudgetsLabels();
				}
			}
		};

		// Result receiver to refresh balance when finished initial setup.
		this.InitialSetupResultReceiver = new ResultReceiver(
				new Handler()) {

			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				Log.d("INITIAL SETUP FINISHED", resultData
						.getBoolean("result") ? "Successful" : "Error");
				if (resultData.getBoolean("result")) {
					updateBudgetsLabels();
				}
			}
		};

		dailyBalanceWrapper = (LinearLayout) findViewById(R.id.daily_balance_wrapper);

		dailyBalance = (TextView) findViewById(R.id.dailySafeToSpend);
		weeklyBalance = (TextView) findViewById(R.id.weeklySafeToSend);
		monthlyBalance = (TextView) findViewById(R.id.monthlySafeToSend);
		
		updateBudgetsLabels();

		dailyBalanceWrapper.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(BalanceActivity.this,
						RegisterTransactionActivity.class);
				intent.putExtra("receiverTag",
						registerTransactionResultReceiver);
				startActivity(intent);
			}
		});
		
		Log.d("FIRST TIME", Boolean.toString(BalanceActivity.this.getIntent().getBooleanExtra("com.banlinea.control.firsTime", false)));
		if (BalanceActivity.this.getIntent().getBooleanExtra("com.banlinea.control.firstTime", false)) {
			Intent intent = new Intent(BalanceActivity.this,
					ReminderSetupActivity.class);
			intent.putExtra("com.banlinea.control.suggestSetup", true);
			startActivity(intent);
		}

	}

	private void getOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.balance, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_category_manager:
			/*
			 * Toast.makeText(getApplicationContext(), "category manager",
			 * Toast.LENGTH_SHORT).show();
			 */
			Intent categoryManagerIntent = new Intent(getApplicationContext(),
					CategoryManagementActivity.class);
			startActivity(categoryManagerIntent);
			break;

		case R.id.menu_product_manager:
			/*
			 * Toast.makeText(getApplicationContext(), "product manager",
			 * Toast.LENGTH_SHORT).show();
			 */
			Intent productManagerIntent = new Intent(getApplicationContext(),
					ProductManagementActivity.class);
			startActivity(productManagerIntent);
			break;

		case R.id.menu_set_reminders:
			/*
			 * Toast.makeText(getApplicationContext(), "set reminders",
			 * Toast.LENGTH_SHORT).show();
			 */
			Intent setRemindersIntent = new Intent(getApplicationContext(),
					ReminderSetupActivity.class);
			startActivity(setRemindersIntent);
			break;

		case R.id.menu_action_initial_setup:
			/*
			 * Toast.makeText(getApplicationContext(), "action initial setup",
			 * Toast.LENGTH_SHORT).show();
			 */
			Intent initialSetupIntent = new Intent(getApplicationContext(),
					InitialSetupActivity.class);
			initialSetupIntent.putExtra("receiverTag",
					InitialSetupResultReceiver);
			startActivity(initialSetupIntent);
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		AlertDialog.Builder builder = new AlertDialog.Builder(
				BalanceActivity.this);
		builder.setTitle(R.string.exit_app).setMessage(R.string.exit_message);
		builder.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent closeApp = new Intent(
								"com.banlinea.control.closeapp");
						sendBroadcast(closeApp);
						finish();
					}
				});
		builder.setNeutralButton(R.string.logout,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						/*
						 * Toast.makeText(getApplicationContext(), "logout",
						 * Toast.LENGTH_SHORT).show();
						 */
						try {
							new AuthenticationService(BalanceActivity.this)
									.Logout();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						finish();
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void updateBudgetsLabels(){
		dailyBalance.setText("" + formatter.format(budgetService.getDailyBudget(SafeSpendPeriod.DAY)));
		weeklyBalance.setText("" + formatter.format(budgetService.getDailyBudget(SafeSpendPeriod.WEEK)));
		monthlyBalance.setText("" + formatter.format(budgetService.getDailyBudget(SafeSpendPeriod.MONTH)));
	}

}
