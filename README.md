# Morelo Wallet for Android

Android wallet forked from `m2049r/xmrwallet` and adapted for the Morelo network.

## Build

1. Link the Morelo source checkout into `external-libs/morelo` and build its Android native libraries as described in [the build guide](doc/BUILDING-external-libs.md).
2. Build the `app` module with Android Studio or Gradle.

The app uses Morelo's network configuration and address handling from those native libraries. Configure a reachable Morelo daemon; the default mainnet RPC port is 38302. The app package ID is separate from the Monero wallet so both can be installed without sharing wallet data.

## Important

Back up wallet keys and recovery seeds securely. Verify the daemon belongs to the Morelo network before sending transactions. The fork does not include default public RPC endpoints; use a trusted Morelo node or run your own.
