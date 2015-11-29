package com.example.bloodreportanalyser;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class Haemoglobin extends ActionBarActivity implements OnItemSelectedListener{
	Spinner spinner;
	private EditText hb;
	int hb_val;
	private Button analyse;
	private String Result="According to your Blood Report,\nYour ";
	private String res="";
	private ProgressDialog progress;
	String gender="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_haemoglobin);
		spinner = (Spinner) findViewById(R.id.spinner1);
		hb=(EditText)findViewById(R.id.editText1);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.sex, android.R.layout.simple_spinner_dropdown_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		progress = new ProgressDialog(this);
		analyse = (Button) findViewById(R.id.button1);
		/*
		 * Handling Values Extracted from the Image
		 */
		if(getIntent().getExtras().getInt("src")==0)
		{
			Bundle extras = getIntent().getExtras();
			if(extras.containsKey("haemo_value"))
			{
				hb.setText(extras.getCharSequence("haemo_value"));
			}
		}  
		analyse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	boolean validity = fieldsValidation();
            	if(validity)
            	{
            		//Toast.makeText(getApplicationContext(),"Fields are Valid", Toast.LENGTH_SHORT).show();
            		analyser();
            		showProgress();	
            	}
            }
        });
	}
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			   long id) {
			  spinner.setSelection(position);
			  //Toast.makeText(getApplicationContext(),selLang, Toast.LENGTH_SHORT).show();
			  spinner.setSelection(position);
			  gender = (String) spinner.getSelectedItem();
			 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.haemoglobin, menu);
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
	 * Fields Validation Method
	 * Handles Exceptions
	 * Even if some parameters are not in a particular report, it initializes those parameters to a normal value
	 */
	private boolean fieldsValidation()
	{
		boolean isValid = true;
		if(hb.getText().toString().equals(""))
		{
			isValid=false;
		}
		else
		{
			hb_val=Integer.parseInt(hb.getText().toString());
		}
		if(!isValid)
		{
			Toast.makeText(getApplicationContext(),"Enter the entries", Toast.LENGTH_SHORT).show();
		}
		return isValid;
	}
	/*
	 * Method to display Circular Progress Bar
	 */
	public void showProgress()
	{
        progress.setMessage("Analysing the Report...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        final int totalProgressTime = 100;
        final Thread t = new Thread()
        {
        		@Override
        		public void run()
        		{
        			int jumpTime = 0;
        			while(jumpTime < totalProgressTime)
        			{
        				try 
        				{
        					sleep(200);
        					jumpTime += 10;
        					progress.setProgress(jumpTime);
        				} 
        				catch (InterruptedException e) 
        				{
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
        			}
        			progress.cancel();
        			Intent intent1=new Intent(Haemoglobin.this,Result.class);
        			intent1.putExtra("result",Result+"\'"+res+"\'.");
        			startActivity(intent1);
        		}
        	};
        	t.start();
     	}
	
	/*
	 * Analyser method: The fields are checked for Analysis here.
	 */
	private void analyser()
	{
		res="";
		if(gender.equals("Male"))
		{
			if(hb_val>=13 && hb_val<=17)
			{
				res="Haemoglobin Level is Normal";
			}
			else if(hb_val<13 && hb_val>9)
			{
				res="Haemoglobin Level is less than Normal";
			}
			else if(hb_val<=9)
			{
				res="Haemoglobin Level is critically Low";
			}
			else
			{
				res="Haemoglobin Level is more than Normal";
			}
		}
		else if(gender.equals("Female"))
		{
			if(hb_val>=12 && hb_val<=15)
			{
				res="Haemoglobin Level is Normal";
			}
			else if(hb_val<12 && hb_val>9)
			{
				res="Haemoglobin Level is less than Normal";
			}
			else if(hb_val<=9)
			{
				res="Haemoglobin Level is critically Low";
			}
			else
			{
				res="Haemoglobin Level is more than Normal";
			}
		}
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
