package com.banlinea.control;

import java.sql.SQLException;

import com.banlinea.control.bussiness.AuthenticationService;
import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.remote.util.CallResult;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
	private EditText firstNameTextView;
	private EditText lastNameTextView;
	private EditText eMailTextView;
	private EditText passwordTextView;
	private EditText passwordConfirmTextView;
	
	
	
	private AuthenticationService authService;
	
	
	private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Show the Up button in the action bar.
        setupActionBar();
        
        authService = new AuthenticationService(this);
        
        firstNameTextView = (EditText) findViewById(R.id.first_name_edittext);
        lastNameTextView = (EditText) findViewById(R.id.last_name_edittext);
        eMailTextView = (EditText) findViewById(R.id.email_edittext);
        passwordTextView = (EditText) findViewById(R.id.password_edittext);
        passwordConfirmTextView = (EditText) findViewById(R.id.password_confirm_edittext);
        
        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (passwordTextView.getText().toString().equals(passwordConfirmTextView.getText().toString())) {
					try {
						UserProfile userProfile = new UserProfile();
						userProfile.setMail(eMailTextView.getText().toString());
						userProfile.setName(firstNameTextView.getText().toString() + " " + lastNameTextView.getText().toString());
						userProfile.setPassword(passwordTextView.getText().toString());
						CallResult result = authService.Register(userProfile);
						if(result.isSuccessfullOperation()) {
							Intent intent = new Intent(RegisterActivity.this, ReminderSetupActivity.class);
							startActivity(intent);
							finish();
						}
						else {
							Toast.makeText(RegisterActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					Toast.makeText(RegisterActivity.this, R.string.password_error_message, Toast.LENGTH_SHORT).show();
				}
			}
		});
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
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
