package com.m2049r.xmrwallet.data;

import com.m2049r.xmrwallet.model.NetworkType;

import org.json.JSONObject;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NodeInfoTest {
	@Test
	public void probesNetworkDefaultRpcPortBeforeLegacyPort() {
		assertEquals(Arrays.asList(38312, 18089), Arrays.asList(NodeInfo.getRpcProbePorts(38312).toArray()));
	}

	@Test
	public void doesNotProbeLegacyPortTwice() {
		assertEquals(Arrays.asList(18089), Arrays.asList(NodeInfo.getRpcProbePorts(18089).toArray()));
	}

	@Test
	public void acceptsOnlyTheConfiguredNetwork() throws Exception {
		JSONObject info = new JSONObject("{\"mainnet\":false,\"testnet\":true,\"stagenet\":false}");

		assertTrue(NodeInfo.isExpectedNetwork(NetworkType.NetworkType_Testnet, info));
		assertFalse(NodeInfo.isExpectedNetwork(NetworkType.NetworkType_Mainnet, info));
		assertFalse(NodeInfo.isExpectedNetwork(NetworkType.NetworkType_Stagenet, info));
	}

	@Test
	public void rejectsMainnetNodeForTestnetWallet() throws Exception {
		JSONObject info = new JSONObject("{\"mainnet\":true,\"testnet\":false,\"stagenet\":false}");

		assertFalse(NodeInfo.isExpectedNetwork(NetworkType.NetworkType_Testnet, info));
	}
}
