package app.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;

public class SimpleApp extends Activity
{

	private final String LOG_TAG = "=== Parent === " + SimpleApp.class.getSimpleName();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		Log.i(LOG_TAG, "onCreate()");
        setContentView(R.layout.main);

		Button btn = (Button)findViewById(R.id.btn_lifecycle);
		btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(SimpleApp.this, LifeCycleActivity.class);
					startActivity(intent);
				}
			});

		btn = (Button)findViewById(R.id.btn_list);
		btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(SimpleApp.this, ListArrayViewActivity.class);
					startActivity(intent);
				}
			});

    }

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(LOG_TAG, "onStart()");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i(LOG_TAG,"onRestart()");
	}


	@Override
	protected void onResume() {
		super.onResume();
		Log.i(LOG_TAG,"onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(LOG_TAG,"onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(LOG_TAG,"onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(LOG_TAG,"onDestroy");
	}

}
