package com.hackslash.x_smartmart;

import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;
import android.content.pm.ActivityInfo;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

import android.widget.TextView;
import android.widget.Toast;
import retrofit.client.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MainActivity extends Activity{

    public static final String ROOT_URL = "http://192.168.43.228/";
    private TextView name, status, wallet, cart, transac;
    String id = "1";
    TextToSpeech t1;
    private KeyStore keyStore;
    private static final String KEY_NAME = "fingerprint";
    private Cipher cipher;
    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //App with fullscreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        name = (TextView) findViewById(R.id.name);
        status = (TextView) findViewById(R.id.status);
        wallet = (TextView) findViewById(R.id.wallet);
        cart = (TextView) findViewById(R.id.cart);
        transac = (TextView) findViewById(R.id.purchase);
        // Initializing both Android Keyguard Manager and Fingerprint Manager
        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        sendData();
    }

    public void sendData() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        DataAPI api = adapter.create(DataAPI.class);

        //Defining the method insertuser of our interface
        api.getData(
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
                                    Toast.makeText(MainActivity.this, "Something went wrong. Unable to connect to the server right now. Check server address or your network connection again!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        String[] parts = output.split("#");
                        name.setText("Hi ".concat(parts[0]).concat("!"));
                        wallet.setText("WALLET: Rs ".concat(parts[1]));
                        if (parts[2].equals("0")) {
                            status.setText("Status: Away from the store");
                        } else {
                            status.setText("Status: Inside the store");
                        }
                        if (parts[3].equals("0")) {
                            cart.setText("Cart no. : NA");
                        } else {
                            cart.setText("Cart No. : ".concat(parts[3]));
                        }
                        if (parts[2].equals("0")) {
                            transac.setText("Current Purchase: NA");
                        }

                        final String s = "Hi ".concat(parts[0]).concat(". Please visit a nearby smart store to start shopping.");
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                speak(s);
                            }
                        }, 5000);

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occur displaying the error as toast
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

      fingerprint();
    }

    void speak(String str) {
        t1.speak(str, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    void fingerprint() {

        // Check whether the device has a Fingerprint sensor.
        if (!fingerprintManager.isHardwareDetected()) {
            Toast.makeText(MainActivity.this, "Your Device does not have a Fingerprint Sensor", Toast.LENGTH_LONG).show();
        } else {
            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Your Device does not have a Fingerprint Sensor", Toast.LENGTH_LONG).show();
                ;
            } else {
                // Check whether at least one fingerprint is registered
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    Toast.makeText(MainActivity.this, "Register at least one fingerprint in Settings", Toast.LENGTH_LONG).show();
                } else {
                    // Checks whether lock screen security is enabled or not
                    if (!keyguardManager.isKeyguardSecure()) {
                        Toast.makeText(MainActivity.this, "Lock screen security not enabled in Settings", Toast.LENGTH_LONG).show();
                    } else {
                        generateKey();

                        if (cipherInit()) {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            FingerprintHandler helper = new FingerprintHandler(this);
                            helper.startAuth(fingerprintManager, cryptoObject);
                        }
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

}

