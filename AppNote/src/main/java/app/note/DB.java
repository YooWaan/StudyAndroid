/**
 * @(#)DB.java		2015/12/25
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

import android.net.Uri;
import android.provider.BaseColumns;

/*
 * DB
 *
 * @author YooWaan
 */
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
