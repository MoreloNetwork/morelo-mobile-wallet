/*
 * Copyright (c) 2018 m2049r
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.m2049r.xmrwallet.data;

import android.bluetooth.BluetoothDevice;

import java.util.Comparator;

import lombok.Data;
import lombok.Getter;

@Data
public class BluetoothInfo {
    @Getter
    final private String name;
    @Getter
    final private String address;
    @Getter
    private boolean bonded;

    public BluetoothInfo(BluetoothDevice device) {
        String deviceName;
        String deviceAddress;
        boolean isBonded;
        try {
            deviceName = device.getName();
            deviceAddress = device.getAddress();
            isBonded = device.getBondState() == BluetoothDevice.BOND_BONDED;
        } catch (SecurityException ignored) {
            deviceName = null;
            deviceAddress = null;
            isBonded = false;
        }
        name = deviceName == null ? "" : deviceName.trim();
        address = deviceAddress;
        bonded = isBonded;
    }

    static public Comparator<BluetoothInfo> NameComparator = (o1, o2) -> o1.name.compareTo(o2.name);
}
