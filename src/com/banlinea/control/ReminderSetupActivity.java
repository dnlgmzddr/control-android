package com.banlinea.control;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

public class ReminderSetupActivity extends Activity {
	
	private ToggleButton setRemindersToggle;
	private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_setup);
        
        setRemindersToggle = (ToggleButton) findViewById(R.id.set_reminders_toggle);
        continueButton = (Button) findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ReminderSetupActivity.this);
				builder.setTitle(R.string.initial_setup_prompt_title).setMessage(R.string.initial_setup_prompt);
				builder.setPositiveButton(R.string.continue_text, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(ReminderSetupActivity.this, InitialSetupActivity.class);
						startActivity(intent);
						finish();
					}
				});
				builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(ReminderSetupActivity.this, BalanceActivity.class);
						startActivity(intent);
						finish();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reminder_setup, menu);
        return true;
    }
    
}
