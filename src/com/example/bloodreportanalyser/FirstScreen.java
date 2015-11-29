package com.example.bloodreportanalyser;

import java.io.File;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class FirstScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        /*
         * Creating a Folder in External Storage
         */
        File nfile=new File(Environment.getExternalStorageDirectory()+"/Blood Report Analyser");
        nfile.mkdir();
        Button NewAnalysis=(Button)findViewById(R.id.button1);
        NewAnalysis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent1=new Intent(FirstScreen.this,BloodReportType.class);
            	startActivity(intent1);
            }
        });
        final Button faqButton = (Button) findViewById(R.id.button2);
        faqButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent1=new Intent(FirstScreen.this,FAQ.class);
            	startActivity(intent1);
            }
        });
        final Button contactButton = (Button) findViewById(R.id.Button01);
        contactButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent1=new Intent(FirstScreen.this,ContactDeveloper.class);
            	startActivity(intent1);
            }
        });
        final Button rateButton = (Button) findViewById(R.id.Button02);
        rateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent1=new Intent(FirstScreen.this,Rate.class);
            	startActivity(intent1);
            }
        });
        final Button exitButton = (Button) findViewById(R.id.Button03);
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	finishAffinity();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.first_screen, menu);
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
