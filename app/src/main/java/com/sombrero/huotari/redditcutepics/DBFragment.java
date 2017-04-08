package com.sombrero.huotari.redditcutepics;

import android.support.v4.app.Fragment;

import com.sombrero.huotari.redditcutepics.common.L;
import com.sombrero.huotari.redditcutepics.db.DataBaseHelper;
import com.sombrero.huotari.redditcutepics.db.RedditItemContract;
import com.sombrero.huotari.redditcutepics.models.RedditItem;

import java.util.ArrayList;
import java.util.HashSet;

public class DBFragment extends Fragment {

	public static final String TAG = DBFragment.class.getSimpleName();

	private Listener mListener;
	private DataBaseHelper mDataBaseHelper;

	private HashSet<String> mSavedIds;
	private ArrayList<RedditItem> mSavedItems;

	private boolean mFragmentWasPaused;

	public interface Listener {
		void onSavedItems(ArrayList<RedditItem> savedItems);
	}

	public static DBFragment newInstance() {
		return new DBFragment();
	}

	public void setListener(Listener listener) {
		mListener = listener;
	}

	public void setDataBaseHelper(DataBaseHelper dataBaseHelper) {
		mDataBaseHelper = dataBaseHelper;
	}

	@Override
	public void onResume() {
		super.onResume();
		mFragmentWasPaused = false;
		loadSavedItems();
	}

	@Override
	public void onPause() {
		super.onPause();
		mFragmentWasPaused = true;
	}

	public void loadSavedItems() {
		RedditItemContract.getItems(mDataBaseHelper, new RedditItemContract.Callback() {
			@Override
			public void onResult(ArrayList<RedditItem> items) {
				if (mFragmentWasPaused) return;

				L.d(TAG, "loaded saved items: " + items.size());

				mSavedItems = items;

				mSavedIds = new HashSet<>();
				for (RedditItem item : items) {
					mSavedIds.add(item.getId());
				}
				mListener.onSavedItems(mSavedItems);
			}
		});
	}

	public void saveItem(RedditItem item) {
		RedditItemContract.insertItem(mDataBaseHelper, item);
	}

	public void deleteSavedItem(RedditItem item) {
		RedditItemContract.deleteItem(mDataBaseHelper, item.getId());
	}

	public boolean itemIsSaved(String id) {
		return mSavedIds != null && mSavedIds.contains(id);
	}
}
