package com.sombrero.huotari.redditcutepics.models;

import android.os.Parcel;

import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class RedditItemTest {

	@Test
	public void redditItemParcelable() {
		String title = "title";
		String imageUrl = "url";
		RedditItem item = new RedditItem(title, imageUrl);

		String itemJson = new Gson().toJson(item);

		Parcel parcel = Parcel.obtain();
		item.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);

		RedditItem item2 = RedditItem.CREATOR.createFromParcel(parcel);

		String itemJson2 = new Gson().toJson(item2);
		assertEquals(itemJson, itemJson2);

		parcel.recycle();
	}

	@Test
	public void redditItemParcelable_whenItemIsNull() {
		String title = null;
		String imageUrl = null;
		RedditItem item = new RedditItem(title, imageUrl);

		String itemJson = new Gson().toJson(item);

		Parcel parcel = Parcel.obtain();
		item.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);

		RedditItem item2 = RedditItem.CREATOR.createFromParcel(parcel);

		String itemJson2 = new Gson().toJson(item2);
		assertEquals(itemJson, itemJson2);

		parcel.recycle();
	}

}