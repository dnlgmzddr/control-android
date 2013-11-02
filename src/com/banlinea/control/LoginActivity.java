package com.banlinea.control;

import java.sql.SQLException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.banlinea.control.bussiness.AuthenticationService;

public class LoginActivity extends Activity {
	
	private EditText eMailEditText;
	private EditText passwordEditText;
	private Button registerButton;
	private Button signInButton;
	private TextView passwordForgottenTextView;
	
	private BroadcastReceiver closeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.banlinea.control.closeapp");
        
        closeReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		};
        
        registerReceiver(closeReceiver, filter);
        
        eMailEditText = (EditText) findViewById(R.id.email_edittext);
        passwordEditText = (EditText) findViewById(R.id.password_edittext);
        
        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
        
        signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AuthenticationService authService = new AuthenticationService(LoginActivity.this);
				try {
					String username = eMailEditText.getText().toString();
					String password = passwordEditText.getText().toString();
					if (authService.Login(username, password).isSuccessfullOperation()) {
						Intent intent = new Intent(LoginActivity.this, BalanceActivity.class);
						startActivity(intent);
					}
					else {
						Toast.makeText(LoginActivity.this, R.string.login_error_message, Toast.LENGTH_SHORT).show();
					}
						
				} catch (SQLException e) {
					Toast.makeText(LoginActivity.this, R.string.connection_error_message, Toast.LENGTH_SHORT).show();
				}
				
				
			}
		});
        
        passwordForgottenTextView = (TextView) findViewById(R.id.password_forgotten_textview);
        passwordForgottenTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "redirect to website", Toast.LENGTH_SHORT).show();
			}
		});
        
        try {
			if (new AuthenticationService(LoginActivity.this).isLoggedIn()) {
				Intent intent = new Intent(LoginActivity.this, BalanceActivity.class);
				startActivity(intent);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		try {
			unregisterReceiver(closeReceiver);
		}
		catch (Exception e) {
		}
		super.finish();
	}
    
    
}
