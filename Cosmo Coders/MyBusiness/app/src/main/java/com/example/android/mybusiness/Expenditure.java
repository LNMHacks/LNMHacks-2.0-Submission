package com.example.android.mybusiness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;



public class Expenditure extends AppCompatActivity {
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure);
        setTitle("Expenditure");
       // Spinner mySpinner=(Spinner) findViewById(R.id.spinner1);
        //text = mySpinner.getSelectedItem().toString();
        EditText eText = (EditText) findViewById(R.id.plain_text_input);
        text = eText.getText().toString();



    }
    public void sub_exp(View view){
        Toast.makeText(Expenditure.this, "Data Saved", LENGTH_SHORT).show();

    }


}
