package com.example.android.mybusiness;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void expenditure (View view) {
        Intent nextpage = new Intent(this, Expenditure.class);
        startActivity(nextpage);

    }
    public void sales (View view) {
        Intent nextpage = new Intent(this, sales.class);
        startActivity(nextpage);

    }
    public void account_summary (View view) {
        Intent nextpage = new Intent(this, account_summary.class);
        startActivity(nextpage);

    }


}
