package com.sombrero.huotari.redditcutepics.net.models;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataChild {

	@SerializedName("data")
	private Data mData;

	public String getId() {
		if (mData != null) {
			return mData.mId;
		}
		return null;
	}

	public Image getFirstImage() {
		if (mData != null && mData.mPreview != null && mData.mPreview.mImages != null && !mData.mPreview.mImages.isEmpty()) {
			return mData.mPreview.mImages.get(0);
		}
		return null;
	}

	public boolean isImage() {
		return mData != null && TextUtils.equals(mData.mPostHint, "image");
	}

	public boolean isNotFaulty() {
		return isImage()
				&& !TextUtils.isEmpty(getId())
				&& getFirstImage() != null
				&& !getFirstImage().isGif() // ignoring gifs for now
				&& !TextUtils.isEmpty(getFirstImage().getImageUrl());
	}

	private static class Data {
		@SerializedName("id")
		private String mId;

		@SerializedName("preview")
		private Preview mPreview;

		@SerializedName("post_hint")
		private String mPostHint;
	}

	private static class Preview {
		@SerializedName("images")
		private List<Image> mImages;
	}
}
