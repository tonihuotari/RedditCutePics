package com.sombrero.huotari.redditcutepics;

import android.support.v4.app.Fragment;

import com.sombrero.huotari.redditcutepics.common.L;
import com.sombrero.huotari.redditcutepics.models.RedditItem;
import com.sombrero.huotari.redditcutepics.net.ApiClient;
import com.sombrero.huotari.redditcutepics.net.ApiErrorException;
import com.sombrero.huotari.redditcutepics.net.models.DataChild;
import com.sombrero.huotari.redditcutepics.net.models.Image;
import com.sombrero.huotari.redditcutepics.net.models.RedditResponse;

import java.util.ArrayList;

public class RequestFragment extends Fragment {

	public static final String TAG = RequestFragment.class.getSimpleName();

	private ApiClient mApiClient;
	private Listener mListener;

	private boolean mFragmentWasPaused;

	private ArrayList<RedditItem> mCats;
	private ArrayList<RedditItem> mDogs;
	private ArrayList<RedditItem> mAwws;

	public interface Listener {
		void onCats(ArrayList<RedditItem> cats);

		void onDogs(ArrayList<RedditItem> dogs);

		void onAww(ArrayList<RedditItem> awws);

		void onFailed();
	}

	public static RequestFragment newInstance() {
		return new RequestFragment();
	}

	public void setApiClient(ApiClient apiClient) {
		mApiClient = apiClient;
	}

	public void setListener(Listener listener) {
		mListener = listener;
	}


	@Override
	public void onResume() {
		super.onResume();
		mFragmentWasPaused = false;
		loadCats();
		loadDogs();
		loadAwws();

	}

	@Override
	public void onPause() {
		super.onPause();
		mFragmentWasPaused = true;
	}

	private void loadCats() {
		mApiClient.getCats(new ApiClient.ApiCallback<RedditResponse>() {
			@Override
			public void onSuccess(RedditResponse response) {
				if (mFragmentWasPaused) return;

				mCats = responseToList(response);
				mListener.onCats(mCats);
			}

			@Override
			public void onError(ApiErrorException e) {
				if (mFragmentWasPaused) return;

				L.e(TAG, "Load cats failed", e);

				mListener.onFailed();
			}
		});
	}

	private void loadDogs() {
		mApiClient.getDogs(new ApiClient.ApiCallback<RedditResponse>() {
			@Override
			public void onSuccess(RedditResponse response) {
				if (mFragmentWasPaused) return;

				mDogs = responseToList(response);
				mListener.onDogs(mDogs);
			}

			@Override
			public void onError(ApiErrorException e) {
				if (mFragmentWasPaused) return;

				L.e(TAG, "Load dogs failed", e);

				mListener.onFailed();
			}
		});
	}

	private void loadAwws() {
		mApiClient.getAwws(new ApiClient.ApiCallback<RedditResponse>() {
			@Override
			public void onSuccess(RedditResponse response) {
				if (mFragmentWasPaused) return;

				mAwws = responseToList(response);
				mListener.onAww(mAwws);
			}

			@Override
			public void onError(ApiErrorException e) {
				if (mFragmentWasPaused) return;

				L.e(TAG, "Load awws failed", e);

				mListener.onFailed();
			}
		});
	}

	private static ArrayList<RedditItem> responseToList(RedditResponse response) {
		ArrayList<RedditItem> items = new ArrayList<>();
		if (response.getChildren() != null) {
			for (DataChild child : response.getChildren()) {
				if (child.isNotFaulty()) {
					Image image = child.getFirstImage();
					items.add(new RedditItem(child.getId(), image.getImageUrl()));
				}
			}
		}
		return items;
	}
}
