package com.sombrero.huotari.redditcutepics.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.BaseColumns;

import com.sombrero.huotari.redditcutepics.common.L;
import com.sombrero.huotari.redditcutepics.models.RedditItem;

import java.util.ArrayList;

public class RedditItemContract {
	private static final String TAG = RedditItemContract.class.getSimpleName();

	public interface Callback {
		void onResult(ArrayList<RedditItem> items);
	}

	private RedditItemContract() {
		//Do nothing
	}

	public static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + RedditItemEntry.TABLE_NAME + " (" +
					RedditItemEntry.COLUMN_NAME_ITEM_ID + " TEXT PRIMARY KEY," +
					RedditItemEntry.COLUMN_NAME_IMAGE_URL + " TEXT)";

	public static final String SQL_DELETE_ENTRIES =
			"DROP TABLE IF EXISTS " + RedditItemEntry.TABLE_NAME;

	/* Inner class that defines the table contents */
	public static class RedditItemEntry implements BaseColumns {
		public static final String TABLE_NAME = "redditItem";
		public static final String COLUMN_NAME_ITEM_ID = "itemId";
		public static final String COLUMN_NAME_IMAGE_URL = "imageUrl";
	}

	private static ContentValues toContentValues(RedditItem item) {
		ContentValues values = new ContentValues();
		values.put(RedditItemEntry.COLUMN_NAME_ITEM_ID, item.getId());
		values.put(RedditItemEntry.COLUMN_NAME_IMAGE_URL, item.getImageUrl());

		return values;
	}

	private static RedditItem fromCursor(Cursor cursor) {
		String itemId = cursor.getString(
				cursor.getColumnIndexOrThrow(RedditItemEntry.COLUMN_NAME_ITEM_ID));
		String imageUrl = cursor.getString(
				cursor.getColumnIndexOrThrow(RedditItemEntry.COLUMN_NAME_IMAGE_URL));
		return new RedditItem(itemId, imageUrl);
	}

	public static void insertItem(final DataBaseHelper dataBaseHelper, final RedditItem item) {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				ContentValues values = toContentValues(item);

				SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
				long rowId = db.insert(RedditItemEntry.TABLE_NAME, null, values);

				return rowId != -1 ? item.getId() : null;
			}

			@Override
			protected void onPostExecute(String insertedId) {
				super.onPostExecute(insertedId);
				L.d(TAG, "inserted item with id: " + insertedId);
			}
		}.execute();
	}

	public static void deleteItem(final DataBaseHelper dataBaseHelper, final String id) {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

				String selection = RedditItemEntry.COLUMN_NAME_ITEM_ID + " LIKE ?";

				String[] selectionArgs = {id};

				int rowsDeleted = db.delete(RedditItemEntry.TABLE_NAME, selection, selectionArgs);

				return rowsDeleted > 0 ? id : null;
			}

			@Override
			protected void onPostExecute(String deletedId) {
				super.onPostExecute(deletedId);
				L.d(TAG, "deleted item with id: " + deletedId);
			}
		}.execute();
	}

	public static void getItems(final DataBaseHelper dataBaseHelper, final Callback callback) {
		new AsyncTask<Void, Void, ArrayList<RedditItem>>() {

			@Override
			protected ArrayList<RedditItem> doInBackground(Void... params) {

				SQLiteDatabase db = dataBaseHelper.getReadableDatabase();

				String[] projection = {
						RedditItemEntry.COLUMN_NAME_ITEM_ID,
						RedditItemEntry.COLUMN_NAME_IMAGE_URL,
				};

				Cursor cursor = db.query(
						RedditItemEntry.TABLE_NAME,
						projection,
						null,
						null,
						null,
						null,
						null
				);

				ArrayList<RedditItem> items = new ArrayList<>();
				while (cursor.moveToNext()) {
					items.add(fromCursor(cursor));
				}
				cursor.close();

				return items;
			}

			@Override
			protected void onPostExecute(ArrayList<RedditItem> items) {
				callback.onResult(items);
			}
		}.execute();
	}
}

