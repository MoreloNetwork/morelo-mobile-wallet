package com.m2049r.xmrwallet;

import static org.junit.Assert.assertNotNull;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class WalletLaunchSmokeTest {
	@Test
	public void loginScreenLaunchesWithoutCrashing() {
		try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class)) {
			scenario.onActivity(activity -> {
				assertNotNull(activity.getWindow());
			});
		}
	}
}
