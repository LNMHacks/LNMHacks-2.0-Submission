package com.example.plotit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        getSupportActionBar().setTitle("Here's your plot!");
        Intent intent = getIntent();
        String imageURl = String.valueOf(intent.getStringExtra(getString(R.string.image_url)));
        ImageView result = findViewById(R.id.result_graph_image_view);
        Glide.with(this)
                .load(imageURl)
                .into(result);
    }


}
