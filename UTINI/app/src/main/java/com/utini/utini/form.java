package com.utini.utini;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class form extends AppCompatActivity {

    List < MonthlyData > DataList;
    RadioGroup radioGroup;
    RadioButton radiobutton;
    EditText sourceEdit, amountEdit;
    Spinner month;
    Button submit, camcel;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(form.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        radioGroup = findViewById(R.id.type);
        sourceEdit = findViewById(R.id.Source);
        amountEdit = findViewById(R.id.amount);
        month = findViewById(R.id.monthSpinner);
        submit = findViewById(R.id.submit);
        camcel = findViewById(R.id.cancel);

        String dataString = getApplicationContext().getSharedPreferences("MyPref", 0).getString("Data", null);
        if(dataString == null){
            Log.i("PREFRENCES EMPTY", "NO DATA");
        }else{
            Type type = new TypeToken<List< MonthlyData >>() {}.getType();
            DataList = new Gson().fromJson(dataString, type);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean wasUpdated = false;
                String source =  sourceEdit.getText().toString();
                double amount = Integer.parseInt(amountEdit.getText().toString());
                radioGroup.getCheckedRadioButtonId();
                int Month = month.getSelectedItemPosition()+1;
                int checkedId = radioGroup.getCheckedRadioButtonId();
                radiobutton = findViewById(checkedId);
                if(radiobutton.getText().equals("Debit")){
                    amount = -amount;
                }
                if(DataList != null){
                    for (int i=0; i<DataList.size(); i++){
                        if(DataList.get(i).month == Month){
                            DataList.get(i).newTransaction(source, amount);
                            Toast.makeText(form.this, "SUCESS", Toast.LENGTH_SHORT).show();
                            Log.i("SUCESS", "YAAAY");
                            wasUpdated = true;
                            break;
                        }
                    }
                }
                if(!wasUpdated){
                    if(DataList != null){
                        DataList.add(DataList.size(), new MonthlyData(Month));
                        DataList.get(DataList.size()-1).newTransaction(source, amount);
                    }else{
                        DataList = new ArrayList<MonthlyData>();
                        DataList.add(0, new MonthlyData(Month));
                        DataList.get(0).newTransaction(source, amount);
                    }
                    Toast.makeText(form.this, "SUCESS", Toast.LENGTH_SHORT).show();
                    Log.i("SUCESS2", "YAAAY");
                }
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("MyPref", 0).edit();
                String connectionsJSONString = new Gson().toJson(DataList);
                editor.putString("Data", connectionsJSONString);
                editor.commit();
                Log.i("SUCESS","BITCH!!!!");
            }
        });
        camcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(form.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
