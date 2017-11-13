package com.utini.utini;

import android.support.annotation.NonNull;

/**
 * Created by garga on 11/12/2017.
 */

public class transaction implements Comparable<transaction> {
    String source;
    boolean type=true;
    double amount=0;

    public transaction(String source, double amount) {
        this.source = source;
        this.amount = amount;
        if(amount<0){
            type=false;
        }
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public int compareTo(@NonNull transaction comtransaction) {
        double compareQuantity = ((transaction)comtransaction).getAmount();
        return (int) (this.amount - compareQuantity);
    }
}
