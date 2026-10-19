package com.m2049r.xmrwallet.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class AmountHelper {
    static final DecimalFormat AmountFormatter_9;

    static {
        AmountFormatter_9 = new DecimalFormat();
        AmountFormatter_9.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        AmountFormatter_9.setMinimumIntegerDigits(1);
        AmountFormatter_9.setMaximumFractionDigits(9);
        AmountFormatter_9.setGroupingUsed(false);
    }

    public static String format(double amount) {
        return AmountFormatter_9.format(amount);
    }

    public static String format_6(double amount) {
        int n = (int) Math.ceil(Math.log10(amount));
        int d = Math.max(2, 6 - n);
        final String fmt = "%,." + d + "f";
        return String.format(Locale.getDefault(), fmt, amount);
    }
}
