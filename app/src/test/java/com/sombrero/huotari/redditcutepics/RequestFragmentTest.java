package com.sombrero.huotari.redditcutepics;

import com.sombrero.huotari.redditcutepics.net.ApiClient;
import com.sombrero.huotari.redditcutepics.net.ApiErrorException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class RequestFragmentTest {

	@Captor
	private ArgumentCaptor<ApiClient.ApiCallback> mGetCatsCallback = ArgumentCaptor.forClass(ApiClient.ApiCallback.class);

	@Test
	public void whenRequestFails(){
		RequestFragment fragment = RequestFragment.newInstance();

		RequestFragment.Listener listener = mock(RequestFragment.Listener.class);
		ApiClient apiClient = mock(ApiClient.class);

		fragment.setListener(listener);
		fragment.setApiClient(apiClient);

		SupportFragmentTestUtil.startFragment(fragment);

		verify(apiClient).getCats(mGetCatsCallback.capture());

		mGetCatsCallback.getValue().onError(new ApiErrorException("Something happened"));

		verify(listener, times(1)).onFailed();

	}

}