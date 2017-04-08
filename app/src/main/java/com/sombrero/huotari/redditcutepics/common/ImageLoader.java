package com.sombrero.huotari.redditcutepics.common;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageLoader {

	public static void loadImage(Context context, String imageUrl, ImageView imageView) {
		Glide.with(context).load(imageUrl).crossFade().into(imageView);
	}

}
