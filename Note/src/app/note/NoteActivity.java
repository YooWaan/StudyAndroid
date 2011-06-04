package app.note;

import java.util.Date;
import java.text.SimpleDateFormat;

import android.app.ListActivity;
import android.content.Intent;
import android.content.ContentUris;
import android.os.Bundle;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.SQLException;
import android.view.View;
import android.view.MenuItem;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.widget.AdapterView.AdapterContextMenuInfo;

import app.note.DB.EntryTable;

public class NoteActivity extends ListActivity {

	private static final int DIALOG_INSERT = 100;
	private static final int DIALOG_UPDATE = 101;

	private static final int ENTRY_EDIT = 1;
	private static final int ENTRY_DELETE = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		// EntryTable Column Names
		String[] projection = {EntryTable.TITLE, EntryTable.DATE};
		// layout/row.xml id
		int[] ids = {R.id.title, R.id.date};

		Cursor c = managedQuery(EntryTable.CONTENT_URI,
								new String[]{EntryTable._ID,
											 EntryTable.TITLE,
											 EntryTable.BODY,
											 EntryTable.DATE},
								null, null, EntryTable.DEFAULT_SORT_ORDER);

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
															  R.layout.row,
															  c,
															  projection,
															  ids);

		adapter.setViewBinder(new DateViewBinder());
		setListAdapter(adapter);
		registerForContextMenu(getListView());

		Button btn = (Button)findViewById(R.id.btn_add);
		btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_INSERT);
					intent.setClass(NoteActivity.this, EditActivity.class);
					startActivityForResult(intent, ENTRY_EDIT);
				}
			});

		btn = (Button)findViewById(R.id.btn_refresh);
		btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					refreshList();
				}
			});
    }

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		// android.util.Log.i( "&&& onCreateContextMenu &&&" , "menu[" + menu + "]" );
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo aminfo = (AdapterContextMenuInfo)menuInfo;
		CursorWrapper cw = (CursorWrapper)getListView().getAdapter().getItem(aminfo.position);
		// EntryTable ID
		String id = cw.getString(0);
		Intent menuIntent = new Intent();
		menuIntent.putExtra(EntryTable._ID, id);

		menu.add(0, ENTRY_EDIT, 0, "Edit")
			.setIcon(android.R.drawable.ic_menu_edit)
			.setIntent(menuIntent);
		menu.add(0, ENTRY_DELETE, 1, "Delete")
			.setIcon(android.R.drawable.ic_menu_delete)
			.setIntent(menuIntent);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Intent intent = item.getIntent();
		String id = intent.getStringExtra(EntryTable._ID);
		if (ENTRY_EDIT == item.getItemId()) {
			if (id != null) {
				intent.setClass(NoteActivity.this, EditActivity.class);
				intent.setAction(Intent.ACTION_EDIT);
				intent.putExtra(EntryTable._ID, id);
				startActivityForResult(intent, ENTRY_EDIT);
				return true;
			}
		} else if (ENTRY_DELETE == item.getItemId()) {
			if (deleteEntry(Long.valueOf(id))) {
				refreshList();
				return true;
			}
		}
		return super.onContextItemSelected(item);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ENTRY_EDIT) {
			if (resultCode == RESULT_OK) {
				refreshList();
			}
		}
	}

	protected void refreshList() {
		((SimpleCursorAdapter)this.getListAdapter()).notifyDataSetChanged();
	}


	protected boolean deleteEntry(long id) {
		try {
			this.getContentResolver().delete(
											 ContentUris.withAppendedId(EntryTable.CONTENT_URI, id),
											 null, null);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	class DateViewBinder implements SimpleCursorAdapter.ViewBinder {

		private SimpleDateFormat mFormat = new SimpleDateFormat("MM.dd yy");

		@Override
		public boolean setViewValue(View view, Cursor cursor, int column) {
			if (view.getId() == R.id.date) {
				long time = cursor.getLong(column);
				TextView txt = (TextView)view;
				txt.setText(mFormat.format(new Date(time)));
				return true;
			}
			return false;
		}
	}
}
