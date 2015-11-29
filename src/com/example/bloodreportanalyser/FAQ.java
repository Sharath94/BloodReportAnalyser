package com.example.bloodreportanalyser;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class FAQ extends ActionBarActivity implements OnItemSelectedListener{
	Spinner spinner;
	private TextView faqs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faq);
		faqs=(TextView)findViewById(R.id.textView1);
		spinner = (Spinner) findViewById(R.id.spinner1);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.languages, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			   long id) {
			  spinner.setSelection(position);
			  String selLang = (String) spinner.getSelectedItem();
			  //Toast.makeText(getApplicationContext(),selLang, Toast.LENGTH_SHORT).show();
			  if(selLang.equals("English"))
			  {
				  faqs.setText(R.string.eng);
			  }
			  else if(selLang.equals("Kannada"))
			  {
				  faqs.setText(R.string.kan);
			  }
			  else if(selLang.equals("Hindi"))
			  {
				  faqs.setText(R.string.hin);
			  }
			  else
			  {
				  faqs.setText("Unknown");
			  }
			  
			 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.faq, menu);
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
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
