package com.example.bloodreportanalyser;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactDeveloper extends ActionBarActivity {
	private EditText msg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_developer);
		msg=(EditText)findViewById(R.id.editText1);
		final Button sendButton = (Button) findViewById(R.id.button1);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	sendEmail();
            }
        });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_developer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*
	 * Method to send E-mail to the Developer
	 */
	protected void sendEmail() {
		  String[] TO = {"developer.blood.report@gmail.com"};
	      Intent emailIntent = new Intent(Intent.ACTION_SEND);
	      emailIntent.setData(Uri.parse("mailto:"));
	      emailIntent.setType("text/plain");
	      emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
	      emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Issue");
	      emailIntent.putExtra(Intent.EXTRA_TEXT, msg.getText().toString());

	      try {
	         startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	         finish();
	       } catch (android.content.ActivityNotFoundException ex) {
	         Toast.makeText(getApplicationContext(),"There is no email client installed.", Toast.LENGTH_SHORT).show();
	      }
	   }
}
