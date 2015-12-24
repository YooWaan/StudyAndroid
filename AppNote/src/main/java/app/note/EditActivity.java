/**
 * @(#)EditActivity.java		2015/12/25
 *
 * Copyright (c) 2015 YooWaan. All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * BrainPad, Inc. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement
 * you entered into with YooWaan.
 */
package app.note;

import android.app.Activity;
import android.content.Intent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import app.note.DB.EntryTable;

/*
 * EditActivity
 *
 * @author YooWaan
 */
public class EditActivity extends Activity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

		Button btn = (Button)findViewById(R.id.btn_save);
		btn.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		EditText title = (EditText)findViewById(R.id.title);
		EditText body = (EditText)findViewById(R.id.body);
		String id = getIntent().getStringExtra(DB.EntryTable._ID);
		if (id == null) {
			title.setText("");
			body.setText("");
		} else {
			Cursor c = null;
			try {
				String[] projection = {DB.EntryTable._ID, DB.EntryTable.TITLE,
									   DB.EntryTable.BODY, DB.EntryTable.DATE};

				c = getContentResolver().query(ContentUris.withAppendedId(
																		  DB.EntryTable.CONTENT_URI,
																		  Long.parseLong(id)),
											   projection, null, null, null);

				c.moveToFirst();
				title.setText(c.getString(1));
				body.setText(c.getString(2));
			} finally {
				c.close();
			}
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		int result = RESULT_CANCELED;
		String id = getIntent().getStringExtra(DB.EntryTable._ID);

		ContentValues values = new ContentValues();
		long now = System.currentTimeMillis();
		values.put(DB.EntryTable.TITLE, ((EditText)findViewById(R.id.title)).getText().toString());
		values.put(DB.EntryTable.BODY, ((EditText)findViewById(R.id.body)).getText().toString());
		values.put(DB.EntryTable.DATE, now);
		try {
			if (id == null) {
				this.getContentResolver()
                    .insert(DB.EntryTable.CONTENT_URI, values);
			} else {
				this.getContentResolver()
                    .update(ContentUris
                            .withAppendedId(DB.EntryTable.CONTENT_URI, Long.parseLong(id)), values, null, null);
			}
			result = RESULT_OK;
		} catch (SQLException e) {
			android.util.Log.e(this.getClass().getSimpleName(), e.getLocalizedMessage());
		}
		setResult(result, intent);
		finish();
	}

}
