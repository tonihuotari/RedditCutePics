package com.sombrero.huotari.redditcutepics.net.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RedditResponse {
	@SerializedName("data")
	private DataWrapper mData;

	public List<DataChild> getChildren() {
		return mData != null ? mData.mChildren : null;
	}

	private static class DataWrapper {
		@SerializedName("children")
		private List<DataChild> mChildren;
	}
}
