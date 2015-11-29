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

public class Liver extends ActionBarActivity {
	private EditText albumin;
	private EditText bilirubin;
	private EditText conjugated_bili;
	private Button analyse;
	private String Result="According to your Blood Report,\nYour ";
	private String res="";
	double albVal;
	double biliVal;
	double conjVal;
	private ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liver);
		albumin=(EditText)findViewById(R.id.editText1);
		bilirubin=(EditText)findViewById(R.id.editText2);
		conjugated_bili=(EditText)findViewById(R.id.editText3);
		progress = new ProgressDialog(this);
		analyse =(Button)findViewById(R.id.button1);
		/*
		 * Handling Values Extracted from the Image
		 */
		if(getIntent().getExtras().getInt("src")==0)
		{
			Bundle extras = getIntent().getExtras();
			if(extras.containsKey("albumin_value"))
			{
				albumin.setText(extras.getCharSequence("albumin_value"));
			}
			if(extras.containsKey("bilirubin_value"))
			{
				bilirubin.setText(extras.getCharSequence("bilirubin_value"));
			}
			if(extras.containsKey("conjugated_value"))
			{
				conjugated_bili.setText(extras.getCharSequence("conjugated_value"));
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
		getMenuInflater().inflate(R.menu.liver, menu);
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
		if ((!albumin.getText().toString().equals("")) && (!albumin.getText().toString().equals(".")))
		{
			isValid = true;
			albVal=Double.parseDouble(albumin.getText().toString());
			//ErrMsg+="FBS is not entered\n";
		}
		else
		{
			albVal=4.0;
		}
		if ((!bilirubin.getText().toString().equals("")) && (!bilirubin.getText().toString().equals("."))){
			isValid = true;
			biliVal=Double.parseDouble(bilirubin.getText().toString());
			//ErrMsg+="RBS is not entered\n";
		}
		else
		{
			biliVal=0.6;
		}
		if ((!conjugated_bili.getText().toString().equals("")) && (!conjugated_bili.getText().toString().equals("."))){	
			isValid = true;
			conjVal=Double.parseDouble(conjugated_bili.getText().toString());
			//ErrMsg+="RBS is not entered\n";
		}
		else
		{
			conjVal=0.5;
		}
		if(!isValid)
		{
			Toast.makeText(getApplicationContext(),"Enter the Entries", Toast.LENGTH_SHORT).show();
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
        			Intent intent1=new Intent(Liver.this,Result.class);
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
		if(albVal<5.0)
		{
			res="Liver is not functioning properly";
		}
		else if(biliVal > 1.2)
		{
			res="Liver is not functioning properly. High Chances of Jaundice";
		}
		else if(conjVal > 0.7)
		{
			res="Liver is not functioning properly";
		}
		else
		{
			res="Liver is functioning properly";
		}
	}
}
