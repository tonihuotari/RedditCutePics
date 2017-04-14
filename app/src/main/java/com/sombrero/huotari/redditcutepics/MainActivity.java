package com.sombrero.huotari.redditcutepics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.sombrero.huotari.redditcutepics.common.L;
import com.sombrero.huotari.redditcutepics.db.DataBaseHelper;
import com.sombrero.huotari.redditcutepics.models.RedditItem;
import com.sombrero.huotari.redditcutepics.net.ApiClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DBFragment.Listener, RequestFragment.Listener, ContentFragment.Listener {

	private static final String TAG = MainActivity.class.getSimpleName();

	private RequestFragment mRequestFragment;
	private DBFragment mDBFragment;

	private ContentFragment mCatsFragment;
	private ContentFragment mDogsFragment;
	private ContentFragment mAwwFragment;

	public State mCurrentState = State.CATS;

	enum State {
		CATS(R.string.title_cats),
		DOGS(R.string.title_dogs),
		AWW(R.string.title_aww),
		SAVED(R.string.title_saved);

		private int titleId;

		State(int titleId) {
			this.titleId = titleId;
		}

		public int getTitleId() {
			return titleId;
		}
	}

	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
			= new BottomNavigationView.OnNavigationItemSelectedListener() {

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			switch (item.getItemId()) {
				case R.id.navigation_cats:
					setCurrentState(State.CATS);
					return true;
				case R.id.navigation_dogs:
					setCurrentState(State.DOGS);
					return true;
				case R.id.navigation_awws:
					setCurrentState(State.AWW);
					return true;
				case R.id.navigation_saved:
					setCurrentState(State.SAVED);
					return true;
			}
			return false;
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

		if (mDBFragment == null) {
			mDBFragment = DBFragment.newInstance();

			getSupportFragmentManager().beginTransaction()
					.add(mDBFragment, DBFragment.TAG)
					.commit();
		}

		if (mRequestFragment == null) {
			mRequestFragment = RequestFragment.newInstance();

			getSupportFragmentManager()
					.beginTransaction()
					.add(mRequestFragment, RequestFragment.TAG)
					.commit();
		}
	}

	@Override
	protected void onDestroy() {
		DataBaseHelper.closeInstance();
		super.onDestroy();
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);

		if (fragment instanceof RequestFragment) {
			mRequestFragment = (RequestFragment) fragment;
			mRequestFragment.setApiClient(new ApiClient());
			mRequestFragment.setListener(this);
		}

		if (fragment instanceof DBFragment) {
			mDBFragment = (DBFragment) fragment;
			mDBFragment.setDataBaseHelper(DataBaseHelper.getInstance(this));
			mDBFragment.setListener(this);
		}

		if (fragment instanceof ContentFragment) {
			((ContentFragment) fragment).setListener(this);
		}
	}

	private void setCurrentState(State state) {
		if (mCurrentState == state) return;

		mCurrentState = state;

		switch (mCurrentState) {

			case CATS:
				setContentFragment(mCatsFragment);
				break;
			case DOGS:
				setContentFragment(mDogsFragment);
				break;
			case AWW:
				setContentFragment(mAwwFragment);
				break;
			case SAVED:
				mDBFragment.loadSavedItems();
				break;
		}
	}

	private void setContentFragment(Fragment fragment) {
		if (fragment == null) {
			// response has not been acquired, fragment transaction will be done later
			return;
		}
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.content_container, fragment)
				.commit();
	}

	@Override
	public void onCats(@NonNull ArrayList<RedditItem> cats) {
		mCatsFragment = ContentFragment.newInstance(cats);

		if (mCurrentState.equals(State.CATS)) {
			setContentFragment(mCatsFragment);
		}

	}

	@Override
	public void onDogs(@NonNull ArrayList<RedditItem> dogs) {
		mDogsFragment = ContentFragment.newInstance(dogs);

		if (mCurrentState.equals(State.DOGS)) {
			setContentFragment(mDogsFragment);
		}
	}

	@Override
	public void onAww(@NonNull ArrayList<RedditItem> awws) {
		mAwwFragment = ContentFragment.newInstance(awws);
		if (mCurrentState.equals(State.AWW)) {
			setContentFragment(mAwwFragment);
		}
	}

	@Override
	public void onSavedItems(ArrayList<RedditItem> savedItems) {
		ContentFragment savedItemsFragment = ContentFragment.newInstance(savedItems);
		if (mCurrentState.equals(State.SAVED)) {
			setContentFragment(savedItemsFragment);
		}
	}

	@Override
	public void onFailed() {
		// TODO: 2017-04-08 show error message
	}


	@Override
	public void saveItem(RedditItem item) {
		mDBFragment.saveItem(item);
	}

	@Override
	public void deleteSavedItem(RedditItem item) {
		mDBFragment.deleteSavedItem(item);
	}

	@Override
	public boolean itemIsSaved(RedditItem item) {
		return mDBFragment.itemIsSaved(item.getId());
	}
}
