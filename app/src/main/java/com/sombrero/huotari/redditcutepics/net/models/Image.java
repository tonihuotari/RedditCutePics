package com.sombrero.huotari.redditcutepics.net.models;

import com.google.gson.annotations.SerializedName;

public class Image {
	@SerializedName("source")
	private ImageSource mSource;

	public String getImageUrl() {
		return mSource != null ? mSource.getUrl() : null;
	}

	public boolean isGif() {
		return getImageUrl() != null && getImageUrl().contains(".gif");
	}

	private static class ImageSource {
		@SerializedName("url")
		private String mUrl;

		public String getUrl() {
			return mUrl;
		}
	}
}
