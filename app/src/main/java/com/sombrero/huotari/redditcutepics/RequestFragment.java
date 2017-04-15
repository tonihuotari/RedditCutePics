package com.sombrero.huotari.redditcutepics;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.sombrero.huotari.redditcutepics.common.L;
import com.sombrero.huotari.redditcutepics.models.RedditItem;
import com.sombrero.huotari.redditcutepics.net.ApiClient;
import com.sombrero.huotari.redditcutepics.net.models.DataChild;
import com.sombrero.huotari.redditcutepics.net.models.Image;
import com.sombrero.huotari.redditcutepics.net.models.RedditResponse;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RequestFragment extends Fragment {

	public static final String TAG = RequestFragment.class.getSimpleName();

	private ApiClient mApiClient;
	private Listener mListener;

	private enum Type {
		CATS,
		DOGS,
		AWWS
	}

	private CompositeDisposable compositeDisposable = new CompositeDisposable();

	public interface Listener {
		void onCats(@NonNull ArrayList<RedditItem> cats);

		void onDogs(@NonNull ArrayList<RedditItem> dogs);

		void onAww(@NonNull ArrayList<RedditItem> awws);

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

		makeRequest(Type.CATS, mApiClient.getCats());
		makeRequest(Type.DOGS, mApiClient.getDogs());
		makeRequest(Type.AWWS, mApiClient.getAwws());
	}

	@Override
	public void onPause() {
		super.onPause();

		compositeDisposable.dispose();
	}

	private void makeRequest(final Type type, Observable<RedditResponse> observable) {
		DisposableObserver<ArrayList<RedditItem>> observer = new DisposableObserver<ArrayList<RedditItem>>() {
			@Override
			public void onNext(ArrayList<RedditItem> items) {
				L.d(TAG, "got request: " + type.name());
				switch (type) {
					case CATS:
						mListener.onCats(items);
						break;
					case DOGS:
						mListener.onDogs(items);
						break;
					case AWWS:
						mListener.onAww(items);
						break;
				}
			}

			@Override
			public void onError(Throwable e) {
				L.e(TAG, "Failed request: " + type.name(), e);
				mListener.onFailed();
			}

			@Override
			public void onComplete() {
				L.d(TAG, "onComplete: " + type.name());
				// Do nothing

			}
		};

		observable.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.map(RequestFragment::responseToList)
				.doOnDispose(() -> L.d(TAG, "disposing: " + type.name()))
				.subscribe(observer);

		compositeDisposable.add(observer);
	}

	private static ArrayList<RedditItem> responseToList(RedditResponse response) {
		ArrayList<RedditItem> items = new ArrayList<>();
		if (response.getChildren() != null) {
			response.getChildren().stream().filter(DataChild::isNotFaulty).forEach(child -> {
				Image image = child.getFirstImage();
				items.add(new RedditItem(child.getId(), image.getImageUrl()));
			});
		}
		return items;
	}
}
