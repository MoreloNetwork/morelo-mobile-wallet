package com.m2049r.xmrwallet.data;

import com.m2049r.xmrwallet.model.NetworkType;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DefaultNodesTest {
	@Test
	public void devnetSeedIsAvailableOnlyForTestnetFlavor() {
		assertArrayEquals(new String[]{"138.3.255.138:38302/testnet"},
				DefaultNodes.getNodeStrings(NetworkType.NetworkType_Testnet));
		assertTrue(DefaultNodes.getNodeStrings(NetworkType.NetworkType_Mainnet).length > 0);
		assertTrue(DefaultNodes.getNodeStrings(NetworkType.NetworkType_Stagenet).length == 0);
	}

	@Test
	public void trustedEndpointIsLimitedToExactDevnetHostAndRpcPort() {
		assertTrue(DefaultNodes.isTrustedDevnetEndpoint("138.3.255.138", 38302,
				NetworkType.NetworkType_Testnet));
		assertFalse(DefaultNodes.isTrustedDevnetEndpoint("138.3.255.139", 38302,
				NetworkType.NetworkType_Testnet));
		assertFalse(DefaultNodes.isTrustedDevnetEndpoint("138.3.255.138", 38312,
				NetworkType.NetworkType_Testnet));
		assertFalse(DefaultNodes.isTrustedDevnetEndpoint("138.3.255.138", 38302,
				NetworkType.NetworkType_Mainnet));
	}
}
