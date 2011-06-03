package app.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class SimpleApp extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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

}
