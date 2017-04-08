package com.sombrero.huotari.redditcutepics.net;

import com.sombrero.huotari.redditcutepics.net.models.RedditResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

	@GET("r/cats.json")
	Call<RedditResponse> getCats();

	@GET("r/dogpictures.json")
	Call<RedditResponse> getDogs();

	@GET("r/aww.json")
	Call<RedditResponse> getAwws();

}
