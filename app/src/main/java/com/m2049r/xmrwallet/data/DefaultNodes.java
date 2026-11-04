package com.m2049r.xmrwallet.data;

import com.m2049r.xmrwallet.model.NetworkType;
import com.m2049r.xmrwallet.model.WalletManager;

import java.util.HashSet;
import java.util.Set;

public final class DefaultNodes {
	static final String DEVNET_HOST = "138.3.255.138";
	static final int DEVNET_RPC_PORT = 38302;
	static final int DEVNET_LEVIN_PORT = 38301;
	private static final String[] DEVNET_NODES = {
			DEVNET_HOST + ":" + DEVNET_RPC_PORT + "/testnet"
	};
	private static final String[] MAINNET_NODES = {
			"0.seed.morelonetwork.pl:38302/mainnet",
			"1.seed.morelonetwork.pl:38302/mainnet"
	};

	private DefaultNodes() {
	}

	static String[] getNodeStrings(NetworkType networkType) {
		if (networkType == NetworkType.NetworkType_Mainnet) {
			return MAINNET_NODES.clone();
		}
		if (networkType == NetworkType.NetworkType_Testnet) {
			return DEVNET_NODES.clone();
		}
		return new String[0];
	}

	public static Set<NodeInfo> getNodes() {
		Set<NodeInfo> nodes = new HashSet<>();
		for (String nodeString : getNodeStrings(WalletManager.getInstance().getNetworkType())) {
			NodeInfo node = NodeInfo.fromString(nodeString);
			if (node != null) {
				if (isTrustedDevnetEndpoint(node.getHost(), node.getRpcPort(), WalletManager.getInstance().getNetworkType())) {
					node.setLevinPort(DEVNET_LEVIN_PORT);
				}
				node.setFavourite(true);
				nodes.add(node);
			}
		}
		return nodes;
	}

	static boolean isTrustedDevnetEndpoint(String host, int rpcPort, NetworkType networkType) {
		return networkType == NetworkType.NetworkType_Testnet
				&& DEVNET_HOST.equals(host)
				&& rpcPort == DEVNET_RPC_PORT;
	}
}
