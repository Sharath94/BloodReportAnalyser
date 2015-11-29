package com.example.bloodreportanalyser;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import com.googlecode.tesseract.android.TessBaseAPI;

public class AnalyseOptions extends ActionBarActivity {
	String Report_type="";
	int id;
	public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Blood Report Analyser/";
	public static final String lang = "eng";
	protected String _path;
	protected boolean _taken;
	private static final String TAG = "AnalyseOption";
	protected static final String PHOTO_TAKEN = "photo_taken";
	String recognizedText="";
	Intent intent_cholestrol;
	Intent intent_liver;
	Intent intent_glucose;
	Intent intent_wb;
	Intent intent_calcium;
	Intent intent_haemo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String path = DATA_PATH + "tessdata/" ;
		File dir = new File(path);
		if (!dir.exists()) 
		{
			if (!dir.mkdirs()) 
			{
				Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
				return;
			} 
			else 
			{
				Log.v(TAG, "Created directory " + path + " on sdcard");
			}
		}
		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) 
		{
			try 
			{
				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
				//GZIPInputStream gin = new GZIPInputStream(in);
				OutputStream out = new FileOutputStream(DATA_PATH+ "tessdata/" + lang + ".traineddata");
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				//while ((length = gin.read(buff)) > 0) {
				while ((len = in.read(buf)) > 0) 
				{
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				Log.v(TAG, "Copied " + lang + " traineddata");
			} 
			catch (IOException e) 
			{
				Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
			}
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_analyse_options);
		Intent intent = getIntent();
		Report_type=intent.getStringExtra("type");
		id=Integer.parseInt(intent.getStringExtra("id"));
		final Button cameraButton = (Button) findViewById(R.id.button1);
		_path = DATA_PATH + "/ocr.jpg";
        cameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	startCameraActivity();
            }
        });
        /*
         * Manual Entry Button.
         * Sending intent to corresponding classes with source as 1
         * Wheras when value is extracted from the image, source is 0
         */
        final Button entryButton = (Button) findViewById(R.id.button2);
        entryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	switch(id)
        		{
        			case 1: Intent intent1=new Intent(AnalyseOptions.this,Glucose.class);
        					intent1.putExtra("src",1);
        					startActivity(intent1);
        					break;
        			case 2: Intent intent2=new Intent(AnalyseOptions.this,WBC.class);
        					intent2.putExtra("src",1);
        					startActivity(intent2);
        					break;
        			case 3: Intent intent3=new Intent(AnalyseOptions.this,Haemoglobin.class);
        					intent3.putExtra("src",1);
        					startActivity(intent3);
        					break;
        			case 4: Intent intent4=new Intent(AnalyseOptions.this,Calcium.class);
        					intent4.putExtra("src",1);
        					startActivity(intent4);
        					break;
        			case 5: Intent intent5=new Intent(AnalyseOptions.this,Cholestrol.class);
        					intent5.putExtra("src",1);
        					startActivity(intent5);
        					break;
        			case 6: Intent intent6=new Intent(AnalyseOptions.this,Liver.class);
        					intent6.putExtra("src",1);
        					startActivity(intent6);
        					break;
        			default: Toast.makeText(getApplicationContext(), "Unknown", Toast.LENGTH_SHORT).show();
        		}
            }
        });		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.analyse_options, menu);
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
	
	protected void startCameraActivity() {
		File file = new File(_path);
		Uri outputFileUri = Uri.fromFile(file);

		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i(TAG, "resultCode: " + resultCode);

		if (resultCode == -1) {
			onPhotoTaken();
		} else {
			Log.v(TAG, "User cancelled");
		}
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(AnalyseOptions.PHOTO_TAKEN, _taken);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i(TAG, "onRestoreInstanceState()");
		if (savedInstanceState.getBoolean(AnalyseOptions.PHOTO_TAKEN)) {
			onPhotoTaken();
		}
	}
	
	protected void onPhotoTaken() {
		_taken = true;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;

		Bitmap bitmap = BitmapFactory.decodeFile(_path, options);

		try {
			ExifInterface exif = new ExifInterface(_path);
			int exifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			Log.v(TAG, "Orient: " + exifOrientation);

			int rotate = 0;

			switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			}

			Log.v(TAG, "Rotation: " + rotate);

			if (rotate != 0) {

				// Getting width & height of the given image.
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();

				// Setting pre rotate
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);

				// Rotating Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
			}

			// Convert to ARGB_8888, required by tess
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

		} catch (IOException e) {
			Log.e(TAG, "Couldn't correct orientation: " + e.toString());
		}

		// _image.setImageBitmap( bitmap );
		Log.v(TAG, "Before baseApi");
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);
		recognizedText = baseApi.getUTF8Text();
		baseApi.end();
		/*
		 * We now have the text in recognizedText variable,we can do anything with it.
		 * We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
		 * So that garbage doesn't make it to the display.
		 */

		Log.v(TAG, "OCRED TEXT: " + recognizedText);

		if ( lang.equalsIgnoreCase("eng") ) {
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
		}
		
		recognizedText = recognizedText.trim();

		if ( recognizedText.length() != 0 ) {
			//_field.setSelection(_field.getText().toString().length());
			switch(id)
			{					
				case 1 : checkForPattern_Glucose();
						 break;
					
				case 2 : checkForPattern_wb();
						 break;
					
				case 3 : checkForPattern_haemo();
						 break;
					
				case 4 : checkForPattern_calcium();
						 break;
					
				case 5 : checkForPattern_cholestrol();
						 break;
					
				case 6 : checkForPattern_liver();
						 break;	
			}
		}
	}
	
	/*
	 * To extract data need for glucose test
	 */
	public void checkForPattern_Glucose()
	{
		String[] extracted_words = recognizedText.split(" ");
		boolean bool1=false;
		boolean bool2=false;
		boolean bool3=false;
		boolean any=false;
		int i;
		for(i=0;i<extracted_words.length;++i)
		{
			 if((computeLevenshteinDistance(extracted_words[i],"fasting") <=2)&&(computeLevenshteinDistance(extracted_words[i],"fbs") <=2))
			 {
				 bool1=true;
				 break;
			 }
		}
		if(bool1)
		{
			while(i<extracted_words.length)
			{
				if(isInteger(extracted_words[i]))
				{
					if(!any)
					{
						 create_intent_glucose();
						 any=true;
					}
					intent_glucose.putExtra("fbs_value",extracted_words[i]);
					break;
				}
				++i;
			}
		}
		for(i=0;i<extracted_words.length;++i)
		{
			if((computeLevenshteinDistance(extracted_words[i],"prandian") <=2)&&(computeLevenshteinDistance(extracted_words[i],"ppbs") <=2))
			 {
				 bool2=true;
				 break;
			 }
		}
		if(bool2)
		{
			while(i<extracted_words.length)
			{
				if(isInteger(extracted_words[i]))
				{
					 if(!any)
					 {
						 create_intent_glucose();
						 any=true;
					 }
					Toast.makeText(getApplicationContext(), extracted_words[i], Toast.LENGTH_SHORT).show();
					intent_glucose.putExtra("prandian_value",extracted_words[i]);
					break;
				}
				++i;
			}
		}
		for(i=0;i<extracted_words.length;++i)
		{
			if((computeLevenshteinDistance(extracted_words[i],"random") <=2)&&(computeLevenshteinDistance(extracted_words[i],"rbs") <=2))
			 {
				 bool3=true;
				 break;
			 }
		}
		if(bool3)
		{
			while(i<extracted_words.length)
			{
				if(isInteger(extracted_words[i]))
				{
					if(!any)
					{
						 create_intent_glucose();
						 any=true;
					}
					intent_glucose.putExtra("random_value",extracted_words[i]);
					break;
				}
				++i;
			}
		}
		if(any)
		{
			startActivity(intent_glucose);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Not Matching\nTake the Picture Again", Toast.LENGTH_SHORT).show();
		}		
	}
	
	/*
	 * To create intent for glucose class.
	 */
	public void create_intent_glucose()
	{
		intent_glucose=new Intent(AnalyseOptions.this,Glucose.class);
		intent_glucose.putExtra("src",0);
	}
	
	/*
	 * To extract data needed for White Blood cell report.
	 */
	public void checkForPattern_wb()
	{
		String[] extracted_words = recognizedText.split(" ");
		boolean bool1=false;
		boolean bool2=false;
		boolean bool3=false;
		boolean bool4=false;
		boolean bool5=false;
		boolean bool6=false;
		boolean bool7=false;
		boolean any=false;
		int i;
		for(i=0;i<extracted_words.length;++i)
		{
			 if(computeLevenshteinDistance(extracted_words[i],"wbc") <=2)
			 {
				 bool1=true;
				 break;
			 }
		}
		if(bool1)
		{
			while(i<extracted_words.length)
			{
				if(isInteger(extracted_words[i]))
				{
					if(!any)
					{
						 create_intent_wb();
						 any=true;
					}
					intent_wb.putExtra("total_count",extracted_words[i]);
					break;
				}
				++i;
			}
		}
		for(i=0;i<extracted_words.length;++i)
		{
			 if(computeLevenshteinDistance(extracted_words[i],"neutrophils") <=2)
			 {
				 bool2=true;
				 break;
			 }
		}
		if(bool2)
		{
			while(i<extracted_words.length)
			{
				if(isInteger(extracted_words[i]))
				{
					if(!any)
					{
						 create_intent_wb();
						 any=true;
					}
					intent_wb.putExtra("neutrophils",extracted_words[i]);
					break;
				}
				++i;
			}
		}
		for(i=0;i<extracted_words.length;++i)
		{
			 if(computeLevenshteinDistance(extracted_words[i],"lymphocytes") <=2)
			 {
				 bool3=true;
				 break;
			 }
		}
		if(bool3)
		{
			while(i<extracted_words.length)
			{
				if(isInteger(extracted_words[i]))
				{
					if(!any)
					{
						 create_intent_wb();
						 any=true;
					}
					intent_wb.putExtra("lymphocytes",extracted_words[i]);
					break;
				}
				++i;
			}
		}
		for(i=0;i<extracted_words.length;++i)
		{
			 if(computeLevenshteinDistance(extracted_words[i],"eosinophills") <=2)
			 {
				 bool4=true;
				 break;
			 }
		}
		if(bool4)
		{
			while(i<extracted_words.length)
			{
				if(isInteger(extracted_words[i]))
				{
					if(!any)
					{
						 create_intent_wb();
						 any=true;
					}
					intent_wb.putExtra("eosinophills",extracted_words[i]);
					break;
				}
				++i;
			}
		}
		for(i=0;i<extracted_words.length;++i)
		{
			 if(computeLevenshteinDistance(extracted_words[i],"basophills") <=2)
			 {
				 bool5=true;
				 break;
			 }
		}
		if(bool5)
		{
			while(i<extracted_words.length)
			{
				if(isDouble(extracted_words[i]))
				{
					if(!any)
					{
						 create_intent_wb();
						 any=true;
					}
					intent_wb.putExtra("basophills",extracted_words[i]);
					break;
				}
				++i;
			}
		}
		for(i=0;i<extracted_words.length;++i)
		{
			 if(computeLevenshteinDistance(extracted_words[i],"monocytes") <=2)
			 {
				 bool6=true;
				 break;
			 }
		}
		if(bool6)
		{
			while(i<extracted_words.length)
			{
				if(isInteger(extracted_words[i]))
				{
					if(!any)
					{
						 create_intent_wb();
						 any=true;
					}
					intent_wb.putExtra("monocytes",extracted_words[i]);
					break;
				}
				++i;
			}
		}
		for(i=0;i<extracted_words.length;++i)
		{
			 if(computeLevenshteinDistance(extracted_words[i],"platelets") <=2)
			 {
				 bool7=true;
				 break;
			 }
		}
		if(bool7)
		{
			while(i<extracted_words.length)
			{
				if(isDouble(extracted_words[i]))
				{
					if(!any)
					{
						 create_intent_wb();
						 any=true;
					}
					intent_wb.putExtra("platelets",extracted_words[i]);
					break;
				}
				++i;
			}
		}
		if(any)
		{
			startActivity(intent_wb);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Not Matching\nTake the Picture Again", Toast.LENGTH_SHORT).show();
		}		
	
	}
	/*
	 * To create Intent for Whiteblood cell class
	 */
	public void create_intent_wb()
	{
		intent_wb=new Intent(AnalyseOptions.this,WBC.class);
		intent_wb.putExtra("src",0);
	}
	/*
	 * To extract data needed for Haemoglobin test
	 */
	public void checkForPattern_haemo()
	{
		String[] extracted_words = recognizedText.split(" ");
		boolean bool1=false;
		boolean any=false;
		int i;
		for(i=0;i<extracted_words.length;++i)
		{
			 if(computeLevenshteinDistance(extracted_words[i],"hb") <=1)
			 {
				 bool1=true;
				 break;
			 }
		}
		if(bool1)
		{
			while(i<extracted_words.length)
			{
				if(isInteger(extracted_words[i]))
				{
					if(!any)
					{
						 create_intent_haemo();
						 any=true;
					}
					intent_haemo.putExtra("haemo_value",extracted_words[i]);
					break;
				}
				++i;
			}
		}
		if(any)
		{
			startActivity(intent_haemo);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Not Matching\nTake the Picture Again", Toast.LENGTH_SHORT).show();
		}		
	}
	/*
	 * To create Intent for Haemoglobin class
	 */
	public void create_intent_haemo()
	{
		intent_haemo=new Intent(AnalyseOptions.this,Haemoglobin.class);
		intent_haemo.putExtra("src",0);
	}
	
	/*
	 * To extract data needed for Calcium Test
	 */
	public void checkForPattern_calcium()
	{
		String[] extracted_words = recognizedText.split(" ");
		boolean bool1=false;
		boolean bool2=false;
		boolean any=false;
		int i;
		for(i=0;i<extracted_words.length;++i)
		{
			 if(computeLevenshteinDistance(extracted_words[i],"serum") <=2)
			 {
				 bool1=true;
				 break;
			 }
		}
		if(bool1)
		{
			while(i<extracted_words.length)
			{
				if(isDouble(extracted_words[i]))
				{
					if(!any)
					{
						 create_intent_calcium();
						 any=true;
					}
					intent_calcium.putExtra("serum_value",extracted_words[i]);
					break;
				}
				++i;
			}
		}
		for(i=0;i<extracted_words.length;++i)
		{
			 if(computeLevenshteinDistance(extracted_words[i],"ionized") <=2)
			 {
				 bool2=true;
				 break;
			 }
		}
		if(bool2)
		{
			while(i<extracted_words.length)
			{
				if(isDouble(extracted_words[i]))
				{
					 if(!any)
					 {
						 create_intent_calcium();
						 any=true;
					 }
					Toast.makeText(getApplicationContext(), extracted_words[i], Toast.LENGTH_SHORT).show();
					intent_calcium.putExtra("ionized_value",extracted_words[i]);
					break;
				}
				++i;
			}
		}
		if(any)
		{
			startActivity(intent_calcium);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Not Matching\nTake the Picture Again", Toast.LENGTH_SHORT).show();
		}		
	}
	/*
	 * To Create Intent for Calcium class
	 */
	public void create_intent_calcium()
	{
		intent_calcium=new Intent(AnalyseOptions.this,Calcium.class);
		intent_calcium.putExtra("src",0);
	}
	/*
	 * To extract data needed for Cholestrol test
	 */
	public void checkForPattern_cholestrol()
	{
		String[] extracted_words = recognizedText.split(" ");
		boolean bool1=false;
		boolean bool2=false;
		boolean any=false;
		int i;
		for(i=0;i<extracted_words.length;++i)
		{
			 if(computeLevenshteinDistance(extracted_words[i],"triglyceride") <=2)
			 {
				 bool1=true;
				 break;
			 }
		}
		if(bool1)
		{
			while(i<extracted_words.length)
			{
				if(isInteger(extracted_words[i]))
				{
					if(!any)
					{
						 create_intent_cholestrol();
						 any=true;
					}
					intent_cholestrol.putExtra("triglyceride_value",extracted_words[i]);
					break;
				}
				++i;
			}
		}
		for(i=0;i<extracted_words.length;++i)
		{
			 if(computeLevenshteinDistance(extracted_words[i],"hdl") <=2)
			 {
				 bool2=true;
				 break;
			 }
		}
		if(bool2)
		{
			while(i<extracted_words.length)
			{
				if(isInteger(extracted_words[i]))
				{
					 if(!any)
					 {
						 create_intent_cholestrol();
						 any=true;
					 }
					 intent_cholestrol.putExtra("hdl_value",extracted_words[i]);
					 break;
				}
				++i;
			}
		}
		if(any)
		{
			startActivity(intent_cholestrol);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Not Matching\nTake the Picture Again", Toast.LENGTH_SHORT).show();
		}
	}
	/*
	 * To create Intent for Cholestrol Test
	 */
	public void create_intent_cholestrol()
	{
		intent_cholestrol=new Intent(AnalyseOptions.this,Cholestrol.class);
		intent_cholestrol.putExtra("src",0);
	}
	
	/*
	 * To Extract data for Liver test
	 */
	public void checkForPattern_liver()
	{
		String[] extracted_words = recognizedText.split(" ");
		boolean bool1=false;
		boolean bool2=false;
		boolean bool3=false;
		boolean any=false;
		int i;
		for(i=0;i<extracted_words.length;++i)
		{
			 if(computeLevenshteinDistance(extracted_words[i],"albumin") <=2)
			 {
				 bool1=true;
				 break;
			 }
		}
		if(bool1)
		{
			while(i<extracted_words.length)
			{
				if(isDouble(extracted_words[i]))
				{
					if(!any)
					{
						 create_intent_liver();
						 any=true;
					}
					intent_liver.putExtra("albumin_value",extracted_words[i]);
					break;
				}
				++i;
			}
		}
		for(i=0;i<extracted_words.length;++i)
		{
			 if(computeLevenshteinDistance(extracted_words[i],"bilirubin") <=2)
			 {
				 bool2=true;
				 break;
			 }
		}
		if(bool2)
		{
			while(i<extracted_words.length)
			{
				if(isDouble(extracted_words[i]))
				{
					 if(!any)
					 {
						 create_intent_liver();
						 any=true;
					 }
					Toast.makeText(getApplicationContext(), extracted_words[i], Toast.LENGTH_SHORT).show();
					intent_liver.putExtra("bilirubin_value",extracted_words[i]);
					break;
				}
				++i;
			}
		}
		for(i=0;i<extracted_words.length;++i)
		{
			 if(computeLevenshteinDistance(extracted_words[i],"conjugated") <=2)
			 {
				 bool3=true;
				 break;
			 }
		}
		if(bool3)
		{
			while(i<extracted_words.length)
			{
				if(isDouble(extracted_words[i]))
				{
					if(!any)
					{
						 create_intent_liver();
						 any=true;
					}
					intent_liver.putExtra("conjugated_value",extracted_words[i]);
					break;
				}
				++i;
			}
		}
		if(any)
		{
			startActivity(intent_liver);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Not Matching\nTake the Picture Again", Toast.LENGTH_SHORT).show();
		}		
	}
	/*
	 * To create Intent for Liver test
	 */
	public void create_intent_liver()
	{
		intent_liver=new Intent(AnalyseOptions.this,Liver.class);
		intent_liver.putExtra("src",0);
	}
	
	/*
	 * To get minimum value among 3 values
	 * Helper method for calculating minimum distance
	 */
	private static int minimum(int a, int b, int c) {                            
        return Math.min(Math.min(a, b), c);                                      
    }   
	/*
	 * Minimum Distance Algorithm
	 */
	public static int computeLevenshteinDistance(String str1,String str2) {
		str1=str1.toLowerCase();
		str2=str2.toLowerCase();
        int[][] distance = new int[str1.length() + 1][str2.length() + 1];        
        for (int i = 0; i <= str1.length(); i++)                                 
            distance[i][0] = i;                                                  
        for (int j = 1; j <= str2.length(); j++)                                 
            distance[0][j] = j;                                                  
 
        for (int i = 1; i <= str1.length(); i++)                                 
            for (int j = 1; j <= str2.length(); j++)                             
                distance[i][j] = minimum(                                        
                        distance[i - 1][j] + 1,                                  
                        distance[i][j - 1] + 1,                                  
                        distance[i - 1][j - 1] + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1));
 
        return distance[str1.length()][str2.length()];                           
    }
	/*
	 * Method to check whether the string can be casted to Interger or not
	 */
	public boolean isInteger(String str) {
	    try {
	        Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException nfe) {}
	    return false;
	}
/*
 * Method to check whether the string can be casted to Double or not
 */
	public boolean isDouble(String str) {
	    try {
	        Double.parseDouble(str);
	        return true;
	    } catch (NumberFormatException nfe) {}
	    return false;
	}
	
}
