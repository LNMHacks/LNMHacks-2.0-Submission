package com.example.android.mybusiness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;



public class account_summary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_summary);
        setTitle("Account Summary");
        display(1500);
        displ(2000);
        dis(500);
        play((float) ((float) (2000*.12)-(1500)+(1500/1.12)));

    }
    private void display(int num) {
        TextView quantityTextView = (TextView) findViewById(R.id.expen);
        quantityTextView.setText("Rs " + num);
    }
    private void displ(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.sales);
        quantityTextView.setText("Rs " + number);
    }
    private void dis(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.net);
        quantityTextView.setText("Rs " + number);
    }
    private void play(float number) {
        TextView quantityTextView = (TextView) findViewById(R.id.tax);
        quantityTextView.setText("Rs " + number);
    }



}
