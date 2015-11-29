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

public class Cholestrol extends ActionBarActivity {
	private EditText triglycerides;
	private EditText hdl;
	private Button analyse;
	private String Result="According to your Blood Report,\nYour Cholestrol Level is ";
	private String res="";
	int tglVal;
	int hdlVal;
	private ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cholestrol);
		triglycerides=(EditText)findViewById(R.id.editText1);
		hdl=(EditText)findViewById(R.id.editText2);
		progress = new ProgressDialog(this);
		analyse =(Button)findViewById(R.id.button1);
		/*
		 * Handling Values Extracted from the Image
		 */
		if(getIntent().getExtras().getInt("src")==0)
		{
			Bundle extras = getIntent().getExtras();
			if(extras.containsKey("triglyceride_value"))
			{
				triglycerides.setText(extras.getCharSequence("triglyceride_value"));
			}
			if(extras.containsKey("hdl_value"))
			{
				hdl.setText(extras.getCharSequence("hdl_value"));
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
		getMenuInflater().inflate(R.menu.cholestrol, menu);
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
		if (!triglycerides.getText().toString().equals(""))
		{
			isValid = true;
			tglVal=Integer.parseInt(triglycerides.getText().toString());
			//ErrMsg+="FBS is not entered\n";
		}
		else
		{
			tglVal=170;
		}
		if (!hdl.getText().toString().equals("")){
			
			isValid = true;
			hdlVal=Integer.parseInt(hdl.getText().toString());
			//ErrMsg+="RBS is not entered\n";
		}
		else
		{
			hdlVal=50;
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
        			Intent intent1=new Intent(Cholestrol.this,Result.class);
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
		if(tglVal > 240)
		{
			res="Critically High.. High Chances of Cardiac Disorders.";
		}
		else if(tglVal > 200 && tglVal <= 240)
		{
			res="in border of High Cholestrol";
		}
		else if(hdlVal <30)//hdlVal should be more (30-60)
		{
			res="Not Normal";
		}
		else
		{
			res="Normal";
		}
	}
}
