package com.hackslash.x_smartmart;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.speech.tts.TextToSpeech;
import java.util.Locale;
import android.widget.Toast;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    TextToSpeech t1;
    // Constructor
    public FingerprintHandler(Context mContext) {
        context = mContext;
        t1 = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.");
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
    speak("Authentication success");
        ((Activity) context).finish();
        Intent intent = new Intent(context, ShopActivity.class);
        context.startActivity(intent);
    }

    private void update(String e){
        speak(e.toString());
        Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
    }
    void speak(String str) {
        t1.speak(str, TextToSpeech.QUEUE_FLUSH, null, null);
    }

}