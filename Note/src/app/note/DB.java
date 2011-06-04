package app.note;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DB {

	public static final String AUTHORITY = "app.note.DB";

	private DB() {}


	public static final class EntryTable implements BaseColumns {

		private EntryTable() {}

		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/entry");

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.app.note.entry";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.app.note.entry";

		public static final String DEFAULT_SORT_ORDER = "modified DESC";
		//
		// Column
		//
		public static final String TITLE = "title";
		public static final String BODY = "body";
		public static final String DATE = "modified";

	}

}