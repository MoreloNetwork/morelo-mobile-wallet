# External lib build

## Requirements:

1. Docker

2. `make` (sudo apt install make, little tricky to get it on Windows, https://stackoverflow.com/questions/32127524/how-to-install-and-use-make-in-windows)

3. Huge amount of RAM and free disk space

## Building:

1. Clone the Morelo source repository and update its submodules: `git submodule update --init --force`.

2. In `external-libs`, create a directory link named `morelo` to the Morelo source checkout:

Linux: `ln -s ~/morelo ~/xmrwallet/external-libs/morelo`

Windows: `mklink /J "C:\Users\<USERNAME>\Documents\GitHub\xmrwallet\external-libs\morelo" "C:\Users\<USERNAME>\Documents\GitHub\morelo"`

3. Start Docker and run `make` in `external-libs`. This builds the Morelo native libraries for `armeabi-v7a` and `arm64-v8a`; `wallet2_api.h` must also come from Morelo. Configure the app to connect to a reachable Morelo daemon; the default mainnet RPC port is `38302`.
