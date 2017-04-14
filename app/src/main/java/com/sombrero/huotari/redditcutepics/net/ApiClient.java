package com.sombrero.huotari.redditcutepics.net;

import com.sombrero.huotari.redditcutepics.net.models.RedditResponse;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

	private static final String TAG = ApiClient.class.getSimpleName();

	private ApiService mApiService;

	private static ApiService buildApiService() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://www.reddit.com/")
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.build();

		return retrofit.create(ApiService.class);
	}

	public ApiClient() {
		mApiService = buildApiService();
	}

	public Observable<RedditResponse> getCats() {
		return mApiService.getCats();
	}

	public Observable<RedditResponse> getDogs() {
		return mApiService.getDogs();
	}

	public Observable<RedditResponse> getAwws() {
		return mApiService.getAwws();
	}
}
