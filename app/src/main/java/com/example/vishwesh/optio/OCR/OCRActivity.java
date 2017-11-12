package com.example.vishwesh.optio.OCR;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
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
import java.util.Calendar;
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
                ArrayList<String> site = new ArrayList<String>();
                ArrayList<String> email = new ArrayList<String>();
                ArrayList<String> mobile = new ArrayList<String>();
                String name = "", jobTitle = "", company = "";
                String title = "", date = "", time = "", venue = "", twenty4 = "";

                String wholeText = textBlockContent.getText().toString();
                String[] splittedText = wholeText.split("\n");
                for (int i = 0; i < splittedText.length; i++) {
                    if (splittedText[i].contains(",")) {
                        String[] split = splittedText[i].split(",");
                        name = split[0];
                        jobTitle = split[1];
                    } else {
                        //for poster
                        if (splittedText[i].toLowerCase().startsWith("date")) {
                            date = splittedText[i].split(" ")[1];
                        } else if (splittedText[i].toLowerCase().startsWith("time")) {
                            String[] strings = splittedText[i].split(" ");
                            time = strings[1];
                            twenty4 = strings.length > 2 ? strings[2] : "";
                        } else if (splittedText[i].toLowerCase().startsWith("venue")) {
                            venue = splittedText[i].split(" ")[1];
                        } else {
                            title = splittedText[i];
                        }

                        String[] split = splittedText[i].split(" ");
                        boolean designationFlag = splittedText[i].toLowerCase().contains("designation");
                        boolean imFlag = splittedText[i].toLowerCase().contains("i am");
                        boolean companyFlag = splittedText[i].toLowerCase().contains("company");
                        for (int j = 0; j < split.length; j++) {
                            if (urlPattern.matcher(split[j]).matches()) {
                                site.add(split[j]);
                            } else if (split[j].startsWith("+91")) {
                                mobile.add(split[j]);
                            } else if (pattern.matcher(split[j]).matches()) {
                                email.add(split[j]);
                            } else if ((split[j].startsWith("\"") && split[j].endsWith("\""))
                                    || (split[j].startsWith("|") && split[j].endsWith("|"))
                                    || (split[j].startsWith("*") && split[j].endsWith("*"))
                                    ) {
                                company = split[j].substring(1, split[j].length() - 1);
                            } else if (j != 0 && (designationFlag && !split[j].contains(":"))
                                    || (imFlag && !split[j].toLowerCase().equals("i") && !split[j].toLowerCase().equals("am"))) {
                                jobTitle += split[j];
                            } else if (j != 0 && companyFlag && !split[j].contains(":")) {
                                company += split[j] + " ";
                            } else if (!split[j].toLowerCase().contains("company")
                                    && !split[j].toLowerCase().contains("designation")
                                    && !split[j].toLowerCase().equals("i")
                                    && !split[j].toLowerCase().equals("am")
                                    ) {
                                name += split[j] + " ";
                            }
                        }
                    }
                }
                if (type == 1) {
                    //extract text
                    if (site.size() > 0) {
                        if (site.size() == 1) {
                            //open directly
                            openBrowser(String.valueOf(site.get(0)));
                        } else {
                            //show dialog
                            showDialogWithItems(site, 1);
                        }
                    } else if (email.size() > 0) {
                        if (email.size() == 1) {
                            //open directly
                            openEmail(String.valueOf(email.get(0)));
                        } else {
                            //show dialog
                            showDialogWithItems(email, 2);
                        }
                    } else if (mobile.size() > 0) {
                        if (mobile.size() == 1) {
                            //open directly
                            openDialer(String.valueOf(mobile.get(0)));
                        } else {
                            //show dialog
                            showDialogWithItems(mobile, 3);
                        }
                    }
                } else if (type == 2) {
                    //scan contact
                    addAsContactConfirmed(OCRActivity.this,
                            name,
                            mobile.size() > 0 ? mobile.get(0) : "",
                            email.size() > 0 ? email.get(0) : "",
                            jobTitle + ", " + company
                    );

                } else if (type == 3) {
                    //poster
                    String[] dateArray = date.split("/");
                    String[] timeArray = time.split(":");
                    Calendar c = Calendar.getInstance();
                    c.set(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[1]) - 1, Integer.parseInt(dateArray[0]));
                    int hour = Integer.parseInt(timeArray[0]);
                    int minute = Integer.parseInt(timeArray[1]);
                    c.set(Calendar.MINUTE, minute);
                    String s1 = twenty4;
                    if (s1.toLowerCase().contains("am") || s1.toLowerCase().contains("pm")) {
                        c.set(Calendar.HOUR, hour);
                        if (s1.toLowerCase().contains("am")) {
                            c.set(Calendar.AM_PM, 0);
                        } else {
                            c.set(Calendar.AM_PM, 1);
                        }
                    } else {
                        c.set(Calendar.HOUR_OF_DAY, hour);
                    }

                    sendCalendarIntent(c.getTimeInMillis(), c.getTimeInMillis() + 100, title, venue);
                }
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("data", wholeText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Text copied!!", Toast.LENGTH_SHORT).show();
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

    public static void addAsContactConfirmed(final Context context,
                                             String name,
                                             String mobile,
                                             String email,
                                             String jobTitle) {

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, mobile);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, jobTitle);

        context.startActivity(intent);

    }

    private void showDialogWithItems(ArrayList<String> data, final int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make your selection");
        final CharSequence[] charSequences = toCharSeq(data);
        builder.setItems(charSequences, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                if (type == 1) {
                    //open browser with url
                    openBrowser(String.valueOf(charSequences[item]));
                } else if (type == 2) {
                    //open email apps
                    openEmail(String.valueOf(charSequences[item]));
                } else if (type == 3) {
                    //open phone number in dialer
                    openDialer(String.valueOf(charSequences[item]));
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public CharSequence[] toCharSeq(ArrayList<String> a) {
        CharSequence[] c = new CharSequence[a.size()];
        for (int i = 0; i < a.size(); i++) {
            c[i] = a.get(i);
        }

        return c;


    }

    public void openBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void openEmail(String email) {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("vnd.android.cursor.item/email");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
        //emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Email Subject");
        //emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "My email content");
        startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
    }

    public void openDialer(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    private void sendCalendarIntent(long startTime, long endTime, String result, String venue) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        startTime)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                        endTime)
                .putExtra(CalendarContract.Events.TITLE, result)
                //.putExtra(CalendarContract.Events.DESCRIPTION, result)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, venue)
                //.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                // .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com")
                ;
        startActivity(intent);
    }
}


