package com.m2049r.xmrwallet.data;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class CryptoTest {
	@Test
	public void xmrSymbolResolvesToMoreloAsset() {
		assertSame(Crypto.XMR, Crypto.withSymbol("XMR"));
		assertSame(Crypto.XMR, Crypto.withSymbol("xmr"));
		assertSame(Crypto.XMR, Crypto.withSymbol("MORELO"));
	}
}
