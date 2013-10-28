package com.banlinea.control;

import java.sql.SQLException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.banlinea.control.bussiness.CategoryService;

public class SplashScreenActivity extends Activity {
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		final SplashScreenActivity activity = this;
		Thread threadLoad =  new Thread(){
			@Override
			public void run(){
				try{
					new CategoryService(activity).ImportBaseCategories();
					
					Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		};
		
		threadLoad.start();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

}
