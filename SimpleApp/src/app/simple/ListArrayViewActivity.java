package app.simple;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class ListArrayViewActivity extends ListActivity {


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        adapter.add("One");
        adapter.add("Two");
        adapter.add("Three");

		getListView().setAdapter(adapter);


		Button btn = (Button)findViewById(R.id.btn_back);

		btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			});

    }

}