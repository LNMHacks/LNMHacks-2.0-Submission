package com.hackslash.x_smartmart;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShopActivity extends Activity {

    public static final String ROOT_URL = "http://192.168.43.228/";
    String id = "1";
    TextToSpeech t1;
    private TextView wallet, cart, transac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //App with fullscreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shop);
        wallet = (TextView) findViewById(R.id.wallet);
        cart = (TextView) findViewById(R.id.cart);
        transac = (TextView) findViewById(R.id.purchase);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        makedisplay();
    }

    void makedisplay() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        CartAPI api = adapter.create(CartAPI.class);

        //Defining the method insertuser of our interface
        api.sendData(
                id,
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        BufferedReader reader = null;

                        //An string to store output from the server
                        String output = "";

                        try {
                            //Initializing buffered reader
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                            //Reading the output in the string
                            output = reader.readLine();
                        } catch (IOException e) {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ShopActivity.this, "Something went wrong. Unable to connect to the server right now. Check server address or your network connection again!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                       final String[] parts = output.split("#");
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                speak("You have been assigned the cart number ".concat(parts[1]));
                            }
                        }, 5000);

                        wallet.setText("WALLET: Rs ".concat(parts[0]));
                        cart.setText("Cart No. : ".concat(parts[1]));
                        transac.setText("Current Purchase: ".concat(parts[2]));

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occur displaying the error as toast
                        Toast.makeText(ShopActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

    }

    void speak(String str) {
        t1.speak(str, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}