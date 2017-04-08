package com.sombrero.huotari.redditcutepics.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RedditItem implements Parcelable {

	private String mId;
	private String mImageUrl;

	public RedditItem(String id, String imageUrl) {
		mId = id;
		mImageUrl = imageUrl;
	}

	protected RedditItem(Parcel in) {
		mId = in.readString();
		mImageUrl = in.readString();
	}

	public static final Creator<RedditItem> CREATOR = new Creator<RedditItem>() {
		@Override
		public RedditItem createFromParcel(Parcel in) {
			return new RedditItem(in);
		}

		@Override
		public RedditItem[] newArray(int size) {
			return new RedditItem[size];
		}
	};

	public String getId() {
		return mId;
	}

	public String getImageUrl() {
		return mImageUrl;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
		dest.writeString(mImageUrl);
	}
}
