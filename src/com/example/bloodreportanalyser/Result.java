package com.example.bloodreportanalyser;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Result extends ActionBarActivity {
	private TextView result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		result=(TextView)findViewById(R.id.textView1);
		Intent intent = getIntent();
		result.setText(intent.getStringExtra("result"));
		final Button back = (Button) findViewById(R.id.button1);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent1=new Intent(Result.this,FirstScreen.class);
            	startActivity(intent1);
            }
        });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
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
