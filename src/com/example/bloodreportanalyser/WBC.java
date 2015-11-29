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

public class WBC extends ActionBarActivity {
	private EditText total;
	private EditText neutro;
	private EditText lymp;
	private EditText eosi;
	private EditText baso;
	private EditText mono;
	private EditText plate;
	private Button analyse;
	private String Result="According to your Blood Report,\nYour ";
	private String res="";
	int totalVal;
	int neutroVal;
	int lympVal;
	int eosiVal;
	double basoVal;
	int monoVal;
	double plateVal;
	private ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wbc);
		total=(EditText)findViewById(R.id.editText1);
		neutro=(EditText)findViewById(R.id.editText2);
		lymp=(EditText)findViewById(R.id.editText3);
		eosi=(EditText)findViewById(R.id.editText4);
		baso=(EditText)findViewById(R.id.editText5);
		mono=(EditText)findViewById(R.id.editText6);
		plate=(EditText)findViewById(R.id.editText7);
		progress = new ProgressDialog(this);
		analyse =(Button)findViewById(R.id.button1);
		/*
		 * Handling Values Extracted from the Image
		 */
		if(getIntent().getExtras().getInt("src")==0)
		{
			Bundle extras = getIntent().getExtras();
			if(extras.containsKey("total_count"))
			{
				total.setText(extras.getCharSequence("total_count"));
			}
			if(extras.containsKey("neutrophils"))
			{
				neutro.setText(extras.getCharSequence("neutrophils"));
			}
			if(extras.containsKey("lymphocytes"))
			{
				lymp.setText(extras.getCharSequence("lymphocytes"));
			}
			if(extras.containsKey("eosinophills"))
			{
				eosi.setText(extras.getCharSequence("eosinophills"));
			}
			if(extras.containsKey("basophills"))
			{
				baso.setText(extras.getCharSequence("basophills"));
			}
			if(extras.containsKey("monocytes"))
			{
				mono.setText(extras.getCharSequence("monocytes"));
			}
			if(extras.containsKey("platelets"))
			{
				plate.setText(extras.getCharSequence("platelets"));
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
		getMenuInflater().inflate(R.menu.wbc, menu);
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
		if (!total.getText().toString().equals(""))
		{
			isValid = true;
			totalVal=Integer.parseInt(total.getText().toString());
			//ErrMsg+="FBS is not entered\n";
		}
		else
		{
			totalVal=8000;
		}
		if (!neutro.getText().toString().equals("")){
			
			isValid = true;
			neutroVal=Integer.parseInt(neutro.getText().toString());
			//ErrMsg+="RBS is not entered\n";
		}
		else
		{
			neutroVal=55;
		}
		if (!lymp.getText().toString().equals("")){
			
			isValid = true;
			lympVal=Integer.parseInt(lymp.getText().toString());
			//ErrMsg+="RBS is not entered\n";
		}
		else
		{
			lympVal=30;
		}
		if (!eosi.getText().toString().equals("")){
			
			isValid = true;
			eosiVal=Integer.parseInt(eosi.getText().toString());
			//ErrMsg+="RBS is not entered\n";
		}
		else
		{
			eosiVal=4;
		}
		if ((!baso.getText().toString().equals("")) && (!baso.getText().toString().equals("."))){
			
			isValid = true;
			basoVal=Double.parseDouble(baso.getText().toString());
			//ErrMsg+="RBS is not entered\n";
		}
		else
		{
			basoVal=0.5;
		}
		if (!mono.getText().toString().equals("")){
			
			isValid = true;
			monoVal=Integer.parseInt(mono.getText().toString());
			//ErrMsg+="RBS is not entered\n";
		}
		else
		{
			monoVal=4;
		}
		if ((!plate.getText().toString().equals("")) && (!plate.getText().toString().equals("."))){
			
			isValid = true;
			plateVal=Double.parseDouble(plate.getText().toString());
			//ErrMsg+="RBS is not entered\n";
		}
		else
		{
			plateVal=2.5;
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
        			Intent intent1=new Intent(WBC.this,Result.class);
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
		if((totalVal < 4500)||(neutroVal < 50)||(lympVal < 25)||(eosiVal <1)||(basoVal<0.0)||(monoVal < 3)||(plateVal < 1.5))
		{
			res="White Blood Cell Content is very low. Resulting in low resistance power";
		}
		else if(plateVal > 5.0)
		{
			res="White Blood Cell Content is too high. Chances of having Cancer. Please consult the doctor soon";
		}
		else
		{
			res="White Blood Cell Content is Normal";
		}
	}
}
