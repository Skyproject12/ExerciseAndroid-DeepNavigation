package com.example.deepnavigation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AsyncCallback {

    Button buttonDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonDetail= findViewById(R.id.button_open_detail);
        buttonDetail.setOnClickListener(this);
        DelayAsync delayAsync= new DelayAsync(this);
        delayAsync.execute();
    }

    // to tell all android onclick
    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.button_open_detail){
            // klik button open detail
            Intent  intent= new Intent(MainActivity.this, DetailActivity.class);
            // intent extra
            intent.putExtra(DetailActivity.EXTRA_TITLE, "Hola, Good News");
            intent.putExtra(DetailActivity.EXTRA_MESSAGE, "Now you can learn android in Dicoding");
            startActivity(intent);
        }
    }

    // make interface android
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void postAsync() {
        // get notification
        showNotification(MainActivity.this, "Hi how are you ?", "Do you have any plan this weekend",110);
    }

    // use asyntask
    private static class DelayAsync extends AsyncTask<Void, Void, Void>{

        // menghubungkan activity asyntask
        WeakReference<AsyncCallback> callback;

        DelayAsync(AsyncCallback callback){
            this.callback= new WeakReference<>(callback);
        }

        // xecution asyntask
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(3000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            return null;
        }

        // setelah selesai eksekusi
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // get asyntask
            callback.get().postAsync();
        }
    }
    // make notification
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(Context context, String title, String message, int notifId){
        String CHANNEL_ID= "Channel_1";
        String CHANNEL_NAME="Navigation channel";
        Intent intent= new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_TITLE, title);
        intent.putExtra(DetailActivity.EXTRA_MESSAGE, message);
        // membuat task agar button up saat diklik akan kembali ke parent activity
        PendingIntent pendingIntent= TaskStackBuilder.create(this)
                .addParentStack(DetailActivity.class)
                .addNextIntent(intent)
                .getPendingIntent(110, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.black))
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setSound(alarmSound)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        // membuat notification ketika versio diatas sdk 21
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel= new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT) ;
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000,1000,1000,1000,1000});
            builder.setChannelId(CHANNEL_ID);
            if(notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
            Notification notification= builder.build();
            if(notificationManager != null){
                notificationManager.notify(notifId, notification);
            }
        }
    }
}
