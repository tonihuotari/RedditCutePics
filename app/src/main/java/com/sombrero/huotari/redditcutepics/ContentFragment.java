package com.sombrero.huotari.redditcutepics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sombrero.huotari.redditcutepics.common.ImageLoader;
import com.sombrero.huotari.redditcutepics.models.RedditItem;

import java.util.ArrayList;
import java.util.List;

public class ContentFragment extends Fragment {

	private static final String TAG = ContentFragment.class.getSimpleName();

	private static final String ARGUMENTS_ITEMS = "ARGUMENTS_ITEMS";

	private int mCurrentPosition = 0;
	private List<RedditItem> mItems;
	private FloatingActionButton mFloatingActionButton;

	private Listener mListener;

	public interface Listener {
		void saveItem(RedditItem item);

		void deleteSavedItem(RedditItem item);

		boolean itemIsSaved(RedditItem item);
	}

	public void setListener(Listener listener) {
		mListener = listener;
	}

	public static ContentFragment newInstance(@NonNull ArrayList<RedditItem> items) {
		Bundle args = new Bundle();
		args.putParcelableArrayList(ARGUMENTS_ITEMS, items);

		ContentFragment fragment = new ContentFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_content, container, false);

		final ImageView imageView = (ImageView) view.findViewById(R.id.content_image);
		ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.content_progressbar);
		mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.content_fab);

		mItems = getArguments().getParcelableArrayList(ARGUMENTS_ITEMS);

		if (mItems.isEmpty()) {
			progressBar.setVisibility(View.GONE);
			// TODO: 2017-04-08 notify user, no items
			return view;
		}

		loadCurrentImage(imageView);
		imageView.setOnClickListener(v -> loadNextImage(imageView));


		mFloatingActionButton.setOnClickListener(v -> {
			RedditItem item = mItems.get(mCurrentPosition);
			saveImage(item);
		});
		return view;
	}

	private void saveImage(RedditItem item) {
		boolean isSaved = mListener.itemIsSaved(item);
		if (isSaved) {
			mListener.deleteSavedItem(item);
		} else {
			mListener.saveItem(item);
		}

		mFloatingActionButton.setImageResource(!isSaved ? R.drawable.ic_star_filled_24dp : R.drawable.ic_star_24dp);
	}

	private void loadNextImage(ImageView imageView) {
		if (mItems.isEmpty()) {
			// early exit: no images, nothing to do here.
			return;
		}

		mCurrentPosition++;
		if (mCurrentPosition >= mItems.size()) {
			mCurrentPosition = 0;
		}

		loadCurrentImage(imageView);
	}

	private void loadCurrentImage(ImageView imageView) {
		RedditItem item = mItems.get(mCurrentPosition);
		ImageLoader.loadImage(getContext(), item.getImageUrl(), imageView);

		boolean isSaved = mListener.itemIsSaved(item);
		mFloatingActionButton.setImageResource(isSaved ? R.drawable.ic_star_filled_24dp : R.drawable.ic_star_24dp);
	}
}
