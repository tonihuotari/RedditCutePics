package com.sombrero.huotari.redditcutepics.net;

import com.sombrero.huotari.redditcutepics.net.models.RedditResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiService {

	@GET("r/cats.json")
	Observable<RedditResponse> getCats();

	@GET("r/dogpictures.json")
	Observable<RedditResponse> getDogs();

	@GET("r/aww.json")
	Observable<RedditResponse> getAwws();

}
