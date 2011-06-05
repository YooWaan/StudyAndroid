package app.note;

import java.util.Map;
import java.util.HashMap;

import android.content.Context;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import app.note.DB.EntryTable;

public class EntryProvider extends ContentProvider {

    private static final String DATABASE_NAME = "entry.db";
    private static final int DATABASE_VERSION = 1;
    private static final String ENTRY_TABLE_NAME = "entries";

	private static final int ENTRIES = 1;
	private static final int ENTRY = 2;

	private static final UriMatcher sUriMatcher;
	private static Map<String, String> sEntryProjectionMap;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(DB.AUTHORITY, "entry", ENTRIES);
        sUriMatcher.addURI(DB.AUTHORITY, "entry/#", ENTRY);

		sEntryProjectionMap = new HashMap<String,String>();
		sEntryProjectionMap.put(EntryTable._ID, EntryTable._ID);
		sEntryProjectionMap.put(EntryTable.TITLE, EntryTable.TITLE);
		sEntryProjectionMap.put(EntryTable.BODY, EntryTable.BODY);
		sEntryProjectionMap.put(EntryTable.DATE, EntryTable.DATE);

	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL("CREATE TABLE " + ENTRY_TABLE_NAME + " ("
					   + EntryTable._ID   + " INTEGER PRIMARY KEY,"
					   + EntryTable.TITLE + " TEXT,"
					   + EntryTable.BODY  + " TEXT,"
					   + EntryTable.DATE  + " INTEGER"
					   + ");");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + ENTRY_TABLE_NAME);
			onCreate(db);
		}

	}

	private DatabaseHelper mDBHelper;

	@Override
	public boolean onCreate() {
		mDBHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case ENTRIES:
			return EntryTable.CONTENT_TYPE;
		case ENTRY:
			return EntryTable.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		int matchType = sUriMatcher.match(uri);
		String orderBy = sortOrder;
		switch (matchType) {
		case ENTRIES:
			builder.setTables(ENTRY_TABLE_NAME);
			builder.setProjectionMap(sEntryProjectionMap);
			break;
		case ENTRY:
			builder.setTables(ENTRY_TABLE_NAME);
			builder.setProjectionMap(sEntryProjectionMap);
			builder.appendWhere(EntryTable._ID + "=" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}

		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = EntryTable.DEFAULT_SORT_ORDER;
		}
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		Cursor c = builder.query(db, projection, selection, selectionArgs, null, null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}


	@Override
	public Uri insert(Uri uri, ContentValues aValues) {
		int matchType = sUriMatcher.match(uri);
		if (matchType != ENTRIES) {
			throw new IllegalArgumentException("Unknown URL " + uri);
		}

		ContentValues values;
		if (aValues == null) {
			values = new ContentValues();
		} else {
			values = new ContentValues(aValues);
		}

		if (!values.containsKey(EntryTable.TITLE) || !values.containsKey(EntryTable.BODY)) {
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
		Long now = Long.valueOf(System.currentTimeMillis());
		if (!values.containsKey(EntryTable.DATE)) {
			values.put(EntryTable.DATE, now);
		}

		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		long id = db.insert(ENTRY_TABLE_NAME, null, values);
		if (id > 0) {
			Uri entryUri = ContentUris.withAppendedId(EntryTable.CONTENT_URI, id);
			getContext().getContentResolver().notifyChange(entryUri, null);
			return entryUri;
		}
		throw new SQLException("Fail to insert row into : " + uri);
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		int count;
		String id;
		switch (sUriMatcher.match(uri)) {
		case ENTRIES:
			count = db.update(ENTRY_TABLE_NAME, values, where, whereArgs);
			break;
		case ENTRY:
			id = uri.getPathSegments().get(1);
			count = db.update(ENTRY_TABLE_NAME, values, EntryTable._ID + "=" + id + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		String id;
		int count;
		switch (sUriMatcher.match(uri)) {
		case ENTRIES:
			count = db.delete(ENTRY_TABLE_NAME, where, whereArgs);
			break;
		case ENTRY:
			id = uri.getPathSegments().get(1);
			count = db.delete(ENTRY_TABLE_NAME, EntryTable._ID + "=" + id + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}