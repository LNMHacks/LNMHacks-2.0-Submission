package com.example.vishwesh.optio.OCR;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vishwesh.optio.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class OCRActivity extends AppCompatActivity {

    private SurfaceView cameraView;
    private TextView textBlockContent;
    private CameraSource cameraSource;
    private StringBuilder value;
    // Pattern for recognizing a URL, based off RFC 3986
    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);


        cameraView = (SurfaceView) findViewById(R.id.surface_view);
        textBlockContent = (TextView) findViewById(R.id.text_value);

        textBlockContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = getIntent().getIntExtra("type", 0);
                ArrayList<String> site=new ArrayList<String>();
                ArrayList<String> email=new ArrayList<String>();
                ArrayList<String> mobile=new ArrayList<String>();
                String name="", jobTitle="";

                String wholeText = textBlockContent.getText().toString();
                String [] splittedText = wholeText.split("\n");
                for(int i=0; i<splittedText.length; i++)
                {
                    String [] split = splittedText[i].split(" ");
                    for(int j=0; j<split.length; j++)
                    {
                        if(urlPattern.matcher(split[j]).matches()){
                            site.add(split[j]);
                        }
                        else if(split[j].startsWith("+91")){
                            mobile.add(split[j]);
                        }
                        else if(pattern.matcher(split[j]).matches()){
                            email.add(split[j]);
                        }
                    }
                }
                if(type==1)
                {
                    //extract text
                }else if(type==2)
                {
                    //scan contact
                    //find phone number

                }else if (type==3)
                {
                    //poster
                }
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("data", wholeText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Text copied!!" , Toast.LENGTH_SHORT).show();
            }
        });


        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();


        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector dependencies are not yet available.");
        }

        cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(2.0f)
                .setAutoFocusEnabled(true)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    //noinspection MissingPermission
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {
                Log.d("Main", "receiveDetections");
                final SparseArray<TextBlock> items = detections.getDetectedItems();
                if (items.size() != 0) {

                    textBlockContent.post(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < items.size(); ++i) {
                                value = new StringBuilder();
                                TextBlock item = items.valueAt(i);
                                value.append(item.getValue());
                                value.append("\n");
                            }
                            textBlockContent.setText(value.toString());
                            try {
                                sleep(150);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        }
                    });


                }

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraSource.release();
    }

    public static void addAsContactConfirmed(final Context context, String name, String mobile, String email) {

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, mobile);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);

        context.startActivity(intent);

    }

    private void showDialogWithItems(ArrayList<String> data, final int type){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make your selection");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                if(type==1)
                {
                    //open broser with url
                }else if(type==2)
                {
                    //open email apps
                }else if(type==3)
                {
                    //open phone number in dialer
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}


