package com.example.bloodreportanalyser;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Calcium extends ActionBarActivity {
	private EditText years;
	private EditText months;
	private EditText serum;
	private EditText ionized;
	private Button analyse;
	private String Result="According to your Blood Report,\nYour Calcium Level is ";
	private String res="";
	int yearVal;
	int monthVal;
	double serumVal;
	double ionizedVal;
	private ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calcium);
		years=(EditText)findViewById(R.id.editText1);
		months=(EditText)findViewById(R.id.editText2);
		serum=(EditText)findViewById(R.id.editText3);
		ionized=(EditText)findViewById(R.id.editText4);
		progress = new ProgressDialog(this);
		analyse =(Button)findViewById(R.id.button1);
		/*
		 * Handling Values Extracted from the Image
		 */
		if(getIntent().getExtras().getInt("src")==0)
		{
			Bundle extras = getIntent().getExtras();
			if(extras.containsKey("serum_value"))
			{
				serum.setText(extras.getCharSequence("serum_value"));
			}
			if(extras.containsKey("ionized_value"))
			{
				ionized.setText(extras.getCharSequence("ionized_value"));
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calcium, menu);
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
		boolean isValid = false;
		if (!years.getText().toString().equals(""))
		{
			yearVal=Integer.parseInt(years.getText().toString());
			//ErrMsg+="FBS is not entered\n";
		}
		else
		{
			yearVal=25;
		}
		if (!months.getText().toString().equals("")){
			monthVal=Integer.parseInt(months.getText().toString());
			//ErrMsg+="RBS is not entered\n";
		}
		else
		{
			monthVal=0;
		}
		if ((!serum.getText().toString().equals("")) && (!serum.getText().toString().equals(".")))
		{
			isValid = true;
			serumVal=Double.parseDouble(serum.getText().toString());
			//ErrMsg+="RBS is not entered\n";
		}
		else
		{
			serumVal=9.0;
		}
		if ((!ionized.getText().toString().equals("")) && (!ionized.getText().toString().equals("."))){
			
			isValid = true;
			ionizedVal=Double.parseDouble(ionized.getText().toString());
			//ErrMsg+="RBS is not entered\n";
		}
		else
		{
			ionizedVal=5.0;
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
        			Intent intent1=new Intent(Calcium.this,Result.class);
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
		if((yearVal==0 && monthVal <=6)&&(serumVal < 8.9))
		{
			res="Less than Normal";
		}
		else if((yearVal==0 && monthVal <=6)&&(serumVal > 12.0))
		{
			res="More than Normal";
		}
		else if(((yearVal==0 && monthVal >=6)||((yearVal < 21)))&&(serumVal < 8.9))
		{
			res="Less than Normal";
		}
		else if(((yearVal==0 && monthVal >=6)||((yearVal < 21)))&&(serumVal > 12.0))
		{
			res="More than Normal";
		}
		else if(yearVal < 21 && serumVal < 8.0)
		{
			res="Less than Normal";
		}
		else if(yearVal > 21 && serumVal > 12.5)
		{
			res="More than Normal";
		}
		else if (yearVal > 21 && ionizedVal < 4.5 )
		{
			res="Less than Normal";
		}
		else
		{
			res="Normal";
		}
	}
}
