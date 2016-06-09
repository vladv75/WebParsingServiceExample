package ru.allfound.webparsingserviceexample;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/*
 * WebParsingService.java    v.1.0 06.06.2016
 *
 * Copyright (c) 2015-2016 Vladislav Laptev,
 * All rights reserved. Used by permission.
 */

public class WebParsingService extends Service {

    private final String LOG = "WebParsingService";
    private final int ID_NOTIFICATION = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG, "onStartCommand");

        String uri = intent.getStringExtra(MainActivity.PARAM_URI);
        PendingIntent pendingIntent = intent.getParcelableExtra(MainActivity.PARAM_PINTENT);

        Thread thread = new Thread(new ParsingRun(uri, pendingIntent));
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    class ParsingRun implements Runnable {

        String uri;
        PendingIntent pendingIntent;

        public ParsingRun(String uri, PendingIntent pendingIntent) {
            this.pendingIntent = pendingIntent;
            this.uri = uri;
            Log.d(LOG, "Parsing Run");
        }

        @Override
        public void run() {
            try {
                //TimeUnit.SECONDS.sleep(1);
                WebParsing webParsing = new WebParsing();
                String result = webParsing.parsing(uri);

                Intent intent = new Intent().putExtra(MainActivity.PARAM_RESULT, result);
                pendingIntent.send(WebParsingService.this, 0, intent);
                sendNotification();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }

        private void sendNotification()
                throws PendingIntent.CanceledException {

            Context context = getApplicationContext();

            NotificationCompat.Builder mBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setAutoCancel(true)
                            .setTicker("HTML page loaded!")
                            .setContentTitle("Parsing Service")
                            .setContentText("HTML page loaded...");

            Intent resultIntent = new Intent(context, MainActivity.class);
            resultIntent.setAction(Intent.ACTION_MAIN);
            resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    resultIntent, 0);

            mBuilder.setContentIntent(pendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(ID_NOTIFICATION, mBuilder.build());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG, "onBind");
        //return new Binder();
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(LOG, "onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(LOG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(LOG, "onDestroy");
        super.onDestroy();
    }
}
