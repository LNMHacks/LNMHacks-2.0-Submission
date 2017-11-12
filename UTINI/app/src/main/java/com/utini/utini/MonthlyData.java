package com.utini.utini;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by garga on 11/12/2017.
 */

public class MonthlyData {
    int month;
    private ArrayList<transaction> transactions;
    transaction[] sorted;

    public void sort(){
        sorted = transactions.toArray(new transaction[transactions.size()]);
        Arrays.sort(sorted);
        Log.i("SORTED", sorted.toString());
    }

    double getSavings(){
        double Csum =0;
        double Dsum =0;
        if(transactions != null){
            Log.i("TRANSACTION DATA", transactions.toString());
            for(int i=0; i<transactions.size(); i++){
                transaction trans =  transactions.get(i);
                if(trans.type=true){
                    Csum += trans.amount;
                }else{
                    Dsum += trans.amount;
                }
            }
            return Csum-Dsum;
        }else{
            Log.i("in else", "bhag");
            return 0;
        }
    }

    double getExpenditure(){
        double Dsum =0;
        if(transactions != null){
            for(int i=0; i<transactions.size(); i++){
                transaction trans =  transactions.get(i);
                if(trans.type == false || trans.amount<0){
                    Dsum += trans.amount;
                }
            }
        }
        return -Dsum;
    }

    public double getMonthEstimate(){
        double Dsum = getExpenditure(),count = 0;
        final Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DAY_OF_MONTH);
        if(month == 4 || month == 6 || month == 9 || month == 11  ) {
            double toReturn = Dsum / date * 30;
            toReturn = Math.round(toReturn*100);
            toReturn = toReturn/100;
            return toReturn;
        }
        else if (month == 2){
            double toReturn = Dsum / date * 28;
            toReturn = Math.round(toReturn*100);
            toReturn = toReturn/100;
            return toReturn;
        }
        double toReturn = Dsum / date * 31;
        toReturn = Math.round(toReturn*100);
        toReturn = toReturn/100;
        return toReturn;
    }

    public MonthlyData(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void newTransaction(String source,Double amount){
        if(transactions != null){
            for(int i = 0; i < transactions.size() ; i++){
                transaction t = transactions.get(i);
                if(t.source.equalsIgnoreCase(source)){
                    t.amount += amount;
                    if(t.amount<0){
                        t.type = false;
                        t.amount = -t.amount;
                    }
                }
            }
        }
        transaction t = new transaction(source,amount);
        if(transactions != null){
            transactions.add(transactions.size(), t);
            return;
        }else{
            transactions = new ArrayList<transaction>();
            transactions.add(0,t);
        }
    }
    public double getMaxExpenditure(){
        double dsum = 0.0;
        int i,j;
        if(transactions != null){
            for(i = 0;i < transactions.size();i++){
                if(transactions.get(i).type == false || transactions.get(i).amount<0){
                    dsum = transactions.get(i).amount;
                }
            }

            for(j = i;i < transactions.size();j++){
                if(transactions.get(i).type == false || transactions.get(i).amount<0){
                    if (transactions.get(i).amount >= dsum){
                        dsum = transactions.get(i).amount;
                    }
                }
            }

        }
        return dsum;
    }
}
