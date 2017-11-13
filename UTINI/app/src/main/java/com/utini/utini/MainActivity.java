package com.utini.utini;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    TextView item1, item2, item3;
    TextView cost1, cost2, cost3;

    private BarChart mChart;
    List < MonthlyData > DataList;
    TextView expecditure, saving, forecastExpenditure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        item1 = findViewById(R.id.name1);
        item2 = findViewById(R.id.name2);
        item3 = findViewById(R.id.name3);
        cost1 = findViewById(R.id.amnt1);
        cost2 = findViewById(R.id.amnt2);
        cost3 = findViewById(R.id.amnt3);

        expecditure = findViewById(R.id.exp);
        saving = findViewById(R.id.saving);
        forecastExpenditure = findViewById(R.id.exp_forcast);

        ImageView newtrans = findViewById(R.id.newtrans);
        newtrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, form.class);
                startActivity(intent);
                finish();
            }
        });

        String dataString = getApplicationContext().getSharedPreferences("MyPref", 0).getString("Data", null);
        if(dataString == null){
            Log.i("PREFRENCES EMPTY", "NO DATA");
        }else{
            Log.i("Le BC", dataString);
            Type type = new TypeToken<List< MonthlyData >>() {}.getType();
            DataList = new Gson().fromJson(dataString, type);
            GraphView graph = (GraphView) findViewById(R.id.graph);
            DataPoint[] dataArray = new DataPoint[DataList.size()];
            int j=0;
            for(int i=0; i<DataList.size(); i++){
                dataArray[j] = new DataPoint(DataList.get(i).getMonth(), DataList.get(i).getSavings());
                j++;
            }
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataArray);
            graph.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.NONE );
            graph.getViewport().setDrawBorder(true);
            graph.addSeries(series);
            String expe = Double.toString(DataList.get(DataList.size()-1).getExpenditure());
            Log.i("Expenditure", expe);
            String savi = Double.toString(DataList.get(DataList.size()-1).getSavings());
            Log.i("Saving", savi);
            String est = Double.toString(DataList.get(DataList.size()-1).getMonthEstimate());
            Log.i("Estimate", est);
            String  max = Double.toString(DataList.get(DataList.size()-1).getMaxExpenditure());
            Log.i("MAX Expenditure BITCHES", max);
            expecditure.setText(expe);
            saving.setText(savi);
            forecastExpenditure.setText(est);
            DataList.get(DataList.size()-1).sort();
            cost1.setText(Double.toString(DataList.get(DataList.size()-1).sorted[0].amount));

            if(DataList.size()>0) {
                cost2.setText(Double.toString(DataList.get(DataList.size() - 1).sorted[1].amount));
                item2.setText(DataList.get(DataList.size() - 1).sorted[1].source);
                if (DataList.size() > 1) {
                    cost3.setText(Double.toString(DataList.get(DataList.size() - 1).sorted[2].amount));
                    item3.setText(DataList.get(DataList.size()-1).sorted[2].source);
                }

            }
            item1.setText(DataList.get(DataList.size()-1).sorted[0].source);


        }
    }
}
