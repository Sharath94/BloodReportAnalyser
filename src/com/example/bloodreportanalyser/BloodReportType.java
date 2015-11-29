package com.example.bloodreportanalyser;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class BloodReportType extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blood_report_type);
		Button glucose=(Button)findViewById(R.id.button1);
		/*
		 * Intent for glucose
		 */
        glucose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent1=new Intent(BloodReportType.this,AnalyseOptions.class);
            	intent1.putExtra("type", "gluocse");
            	intent1.putExtra("id", "1");
            	startActivity(intent1);
            }
        });
        /*
		 * Intent for White Blood Cells
		 */
        Button wb=(Button)findViewById(R.id.Button01);
        wb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent1=new Intent(BloodReportType.this,AnalyseOptions.class);
            	intent1.putExtra("type", "wb");
            	intent1.putExtra("id", "2");
            	startActivity(intent1);
            }
        });
        
        /*
		 * Intent for Haemoglobin
		 */
        Button haemo=(Button)findViewById(R.id.Button02);
        haemo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent1=new Intent(BloodReportType.this,AnalyseOptions.class);
            	intent1.putExtra("type", "haemo");
            	intent1.putExtra("id", "3");
            	startActivity(intent1);
            }
        });
        
        /*
		 * Intent for Calcium
		 */
        Button calcium=(Button)findViewById(R.id.Button03);
        calcium.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent1=new Intent(BloodReportType.this,AnalyseOptions.class);
            	intent1.putExtra("type", "calcium");
            	intent1.putExtra("id", "4");
            	startActivity(intent1);
            }
        });
        
        /*
		 * Intent for Cholestrol
		 */
        Button cholestrol=(Button)findViewById(R.id.Button04);
        cholestrol.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent1=new Intent(BloodReportType.this,AnalyseOptions.class);
            	intent1.putExtra("type", "cholestrol");
            	intent1.putExtra("id", "5");
            	startActivity(intent1);
            }
        });
        
        /*
		 * Intent for Liver
		 */
        Button liver=(Button)findViewById(R.id.Button05);
        liver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent1=new Intent(BloodReportType.this,AnalyseOptions.class);
            	intent1.putExtra("type", "liver");
            	intent1.putExtra("id", "6");
            	startActivity(intent1);
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.blood_report_type, menu);
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
}
