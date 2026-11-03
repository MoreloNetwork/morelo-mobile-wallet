/*
 * Copyright (c) 2017 m2049r
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

import android.os.Parcel;
import android.os.Parcelable;

import com.m2049r.xmrwallet.model.PendingTransaction;
import com.m2049r.xmrwallet.model.Wallet;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// https://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
@ToString
public class TxData implements Parcelable {
    private String destination;
    private long amount;
    @Getter
    @Setter
    private int mixin;
    @Getter
    @Setter
    private PendingTransaction.Priority priority;
    @Getter
    @Setter
    private UserNotes userNotes;

    public TxData() {
    }

    public String getDestination() {
        return destination;
    }

    public long getAmount() {
        return amount;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setAmount(double amount) {
        setAmount(Wallet.getAmountFromDouble(amount));
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(destination);
        out.writeLong(amount);
        out.writeInt(mixin);
        out.writeInt(priority.getValue());
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TxData> CREATOR = new Parcelable.Creator<TxData>() {
        public TxData createFromParcel(Parcel in) {
            return new TxData(in);
        }

        public TxData[] newArray(int size) {
            return new TxData[size];
        }
    };

    protected TxData(Parcel in) {
        destination = in.readString();
        amount = in.readLong();
        mixin = in.readInt();
        priority = PendingTransaction.Priority.fromInteger(in.readInt());
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
