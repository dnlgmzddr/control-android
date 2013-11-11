package com.banlinea.control;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.banlinea.control.bussiness.CategoryService;

public class SplashScreenActivity extends Activity {
	
	private static final long SPLASH_SCREEN_DELAY = 1500;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		final SplashScreenActivity activity = this;
		TimerTask timerLoad =  new TimerTask() {
			
			@Override
			public void run() {
				try{
					CategoryService ser = new CategoryService(activity);
					if (!ser.IsAlreadyImported()) {
						ser.ImportBaseCategories();
					}
					Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		};
		
		Timer timer = new Timer();
        timer.schedule(timerLoad, SPLASH_SCREEN_DELAY);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

}
