package com.example.android.mybusiness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import static android.widget.Toast.LENGTH_SHORT;




public class sales extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        setTitle("Sales");
    }
    public void sub_sal(View view){
        Toast.makeText(sales.this, "Data Saved", LENGTH_SHORT).show();

    }
}
