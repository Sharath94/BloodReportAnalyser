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

public class Glucose extends ActionBarActivity {
	private EditText age;
	private EditText fbs;
	private EditText ppbs;
	private EditText rbs;
	private Button analyse;
	private String Result="According to your Blood Report,\nYour Blood Sugar Level is ";
	private String res="";
	int fbsVal;
	int ppbsVal;
	int rbsVal;
	private ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_glucose);
		age=(EditText)findViewById(R.id.editText1);
		fbs=(EditText)findViewById(R.id.editText2);
		ppbs=(EditText)findViewById(R.id.editText3);
		rbs=(EditText)findViewById(R.id.editText4);
		progress = new ProgressDialog(this);
		analyse = (Button) findViewById(R.id.button1);
		/*
		 * Handling Values Extracted from the Image
		 */
		if(getIntent().getExtras().getInt("src")==0)
		{
			Bundle extras = getIntent().getExtras();
			if(extras.containsKey("fbs_value"))
			{
				fbs.setText(extras.getCharSequence("fbs_value"));
			}
			if(extras.containsKey("prandian_value"))
			{
				ppbs.setText(extras.getCharSequence("prandian_value"));
			}
			if(extras.containsKey("random_value"))
			{
				rbs.setText(extras.getCharSequence("random_value"));
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
		getMenuInflater().inflate(R.menu.glucose, menu);
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
		if (!fbs.getText().toString().equals(""))
		{
			isValid = true;
			fbsVal=Integer.parseInt(fbs.getText().toString());
		}
		else
		{
			fbsVal=100;
		}
		if (!ppbs.getText().toString().equals("")){
			isValid = true;
			ppbsVal=Integer.parseInt(ppbs.getText().toString());
		}
		else
		{
			ppbsVal=150;
		}
		if (!rbs.getText().toString().equals("")){
			
			isValid = true;
			rbsVal=Integer.parseInt(rbs.getText().toString());
			//ErrMsg+="RBS is not entered\n";
		}
		else
		{
			rbsVal=150;
		}
		if (age.getText().toString().equals(""))
		{
			isValid=false;
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
        			Intent intent1=new Intent(Glucose.this,Result.class);
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
		if(fbsVal>200 || ppbsVal>200 || rbsVal >200)
		{
			res="Critically Diabetic";
		}
		else if((fbsVal<=200 && fbsVal>120) || (ppbsVal<=200 && ppbsVal > 180) || (rbsVal <=200 && rbsVal>180))
		{
			res="Diabetic";
		}
		else if((fbsVal<=120 && fbsVal>110) || (ppbsVal<=180 && ppbsVal > 160))
		{
			res="in Border of Diabetic Range";
		}
		else if(fbsVal<60 || ppbsVal<80 || rbsVal < 60)
		{
			res="Too Low";
		}
		else if((fbsVal<110 && fbsVal>60) || (ppbsVal<160 && ppbsVal > 80) || (rbsVal <180 && rbsVal>60))
		{
			res="Normal";
		}		
	}
}
