package com.sombrero.huotari.redditcutepics.net;

import com.sombrero.huotari.redditcutepics.common.L;
import com.sombrero.huotari.redditcutepics.net.models.RedditResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

	private static final String TAG = ApiClient.class.getSimpleName();

	private ApiService mApiService;

	public interface ApiCallback<T> {
		void onSuccess(T response);

		void onError(ApiErrorException e);
	}

	private static ApiService buildApiService() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://www.reddit.com/")
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		return retrofit.create(ApiService.class);
	}

	public ApiClient() {
		mApiService = buildApiService();
	}

	public void getCats(final ApiCallback<RedditResponse> callback) {
		makeRequest(mApiService.getCats(), callback);
	}

	public void getDogs(final ApiCallback<RedditResponse> callback) {
		makeRequest(mApiService.getDogs(), callback);
	}

	public void getAwws(final ApiCallback<RedditResponse> callback) {
		makeRequest(mApiService.getAwws(), callback);
	}

	private void makeRequest(final Call<RedditResponse> call, final ApiCallback<RedditResponse> callback) {
		call.enqueue(new Callback<RedditResponse>() {
			@Override
			public void onResponse(Call<RedditResponse> call, Response<RedditResponse> response) {
				if (!response.isSuccessful()) {
					callback.onError(new ApiErrorException("Get call failed with", response.code()));
					return;
				}

				callback.onSuccess(response.body());
			}

			@Override
			public void onFailure(Call<RedditResponse> call, Throwable t) {
				L.d(TAG, "onFailure: " + t.getMessage());
				callback.onError(new ApiErrorException(t.getMessage()));
			}
		});
	}
}
