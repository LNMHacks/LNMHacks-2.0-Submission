package com.example.vishwesh.optio;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;


public class MainActivity extends Activity {
    TextView textv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textv = (TextView) findViewById(R.id.text);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openApp(MainActivity.this, "net.one97.paytm");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(
                        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //extra class tomo

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            Bundle bundle = intent.getExtras();
            Set<String> keyset = bundle.keySet();
            String result = "";
            final Iterator<String> iterator = keyset.iterator();


            while (iterator.hasNext()) {
                final String key = iterator.next();
                final Object o = bundle.get(key);
                result = result + o;


            }
            Toast.makeText(context, "body" + result, Toast.LENGTH_SHORT).show();
            textv.setText(result);

            //int id = intent.getIntExtra("icon",0);

            Context remotePackageContext = null;
            try {
//                remotePackageContext = getApplicationContext().createPackageContext(pack, 0);
//                Drawable icon = remotePackageContext.getResources().getDrawable(id);
//                if(icon !=null) {
//                    ((ImageView) findViewById(R.id.imageView)).setBackground(icon);
//                }
                byte[] byteArray = intent.getByteArrayExtra("icon");
                Bitmap bmp = null;
                if (byteArray != null) {
                    bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                }
                Toast.makeText(context, "Title:- " + title + " Text:-" + text, Toast.LENGTH_LONG).show();
                findCase(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    public void findCase(String result) {
        String[] resultarray = result.split(" ");
        for (int i = 0; i < resultarray.length; i++) {
            if (resultarray[i].equals("pay") || resultarray[i].equals("udhaar")) {
                payMe(result);
                return;
            } else if (resultarray[i].equals("tomorrow")
                    || resultarray[i].equals("date")
                    || resultarray[i].equals("time")) {
                if (resultarray[i].equals("tomorrow")) {
                    addInCalendar(result, resultarray, 1, i);
                } else if (resultarray[i].equals("date")) {
                    addInCalendar(result, resultarray, 2, i);
                }
                return;
            }
        }

    }

    private void addInCalendar(String result, String[] resultarray, int type, int keywordIndex) {
        if (type == 1) {
            //tomorrow case
            //find today's date
            //find time
            int timeIndex = -1;
            for (int i = 0; i < resultarray.length; i++) {
                if (resultarray[i].contains(":")) {
                    timeIndex = i;
                    break;
                }
            }
            //send calendar intent
            long startTime = getStartTimeForTomorrow(resultarray[timeIndex],
                    timeIndex + 1 < resultarray.length ? resultarray[timeIndex + 1] : "");
            long endTime = startTime + 1000;
            sendCalendarIntent(startTime, endTime, result);
        }
    }

    private void sendCalendarIntent(long startTime, long endTime, String result) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        startTime)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                        endTime)
                .putExtra(CalendarContract.Events.TITLE, result)
                //.putExtra(CalendarContract.Events.DESCRIPTION, result)
                //.putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                //.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                // .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com")
                ;
        startActivity(intent);
    }

    private long getStartTimeForTomorrow(String s, String s1) {
        //contains am/pm
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        Calendar c = Calendar.getInstance();
        c.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        String[] itemArray = s.split(":");
        int hour = Integer.parseInt(itemArray[0]);
        int minute = Integer.parseInt(itemArray[1]);
        c.set(Calendar.MINUTE, minute);
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
        return c.getTimeInMillis();
    }

    //pay me cash function
    public void payMe(String result) {
        String[] resultarray = result.split(" ");
        for (int i = 0; i < resultarray.length; i++) {
            try {
                Float.parseFloat(resultarray[i]);

                return;
            } catch (Exception e) {
                continue;
            }
        }

    }

    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                return false;
                //throw new ActivityNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }
}

