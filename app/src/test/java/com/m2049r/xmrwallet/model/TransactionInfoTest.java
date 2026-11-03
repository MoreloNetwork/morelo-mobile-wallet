package com.m2049r.xmrwallet.model;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TransactionInfoTest {
	@Test
	public void identifiesIncomingBlockReward() {
		assertTrue(createTransaction(TransactionInfo.Direction.Direction_In, false, true, 100, 118).isBlockReward());
	}

	@Test
	public void doesNotIdentifyPendingIncomingTransactionAsBlockReward() {
		assertFalse(createTransaction(TransactionInfo.Direction.Direction_In, true, true, 100, 118).isBlockReward());
	}

	@Test
	public void doesNotIdentifyRegularIncomingTransactionAsBlockReward() {
		assertFalse(createTransaction(TransactionInfo.Direction.Direction_In, false, false, 100, 118).isBlockReward());
	}

	@Test
	public void doesNotIdentifyOutgoingTransactionAsBlockReward() {
		assertFalse(createTransaction(TransactionInfo.Direction.Direction_Out, false, true, 100, 118).isBlockReward());
	}

	private TransactionInfo createTransaction(TransactionInfo.Direction direction, boolean pending, boolean coinbase,
											  long blockHeight, long unlockTime) {
		return new TransactionInfo(
				direction.getValue(), pending, coinbase, false, 1, 0, blockHeight, "hash", 0,
				"", 0, 0, 0, unlockTime, "", Collections.emptyList());
	}
}