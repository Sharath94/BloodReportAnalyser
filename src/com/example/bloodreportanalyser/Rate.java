package com.example.bloodreportanalyser;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RatingBar;
import android.widget.Toast;

public class Rate extends ActionBarActivity {
	private RatingBar ratingBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rate);
		addListenerOnRatingBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rate, menu);
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
	 * Adding listener to Rating Bar
	 */
	public void addListenerOnRatingBar() 
	{
		ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
		public void onRatingChanged(RatingBar ratingBar, float rating,  boolean fromUser) 
		{
			Toast.makeText(getApplicationContext(), "You have Selected "+String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();
		}});
	}
}
