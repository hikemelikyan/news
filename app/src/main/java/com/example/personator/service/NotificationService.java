package com.example.personator.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.personator.R;
import com.example.personator.activity.MainActivity;
import com.example.personator.network.GetConnect;
import com.example.personator.network.NewsAPI;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.personator.common.Common.API_KEY;
import static com.example.personator.common.Common.FIELDS_TO_SHOW;
import static com.example.personator.common.Common.JSON;

public class NotificationService extends Service {
    public static final int NOTIFICATION_ID = 100;
    public static final String CHANNEL_ID = "newNews";
    private int page = 1;
    private int oldCount = 1;
    private int addedNewsCount = 0;
    private boolean first_time = true;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NewsAPI newsAPI = GetConnect.getInstance().create(NewsAPI.class);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        Log.d("SOLOTASK", "sendRequest LatestNews");
        compositeDisposable.add(newsAPI.getContent(JSON, "newest", FIELDS_TO_SHOW, 10, page++, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(news -> {
                    if (first_time) {
                        oldCount = (int) news.getResponse().getTotal();
                        first_time = false;
                    } else {
                        addedNewsCount = (int) (news.getResponse().getTotal() - oldCount);
                    }
                }, throwable -> {
                    Log.d("TASK", "" + throwable.toString());
                }));
        if (addedNewsCount > 0) {
            NewNotification();
        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }


    private void NewNotification() {
        IsOreo();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle("News are waiting for you")
                .setContentText("See the newest economic news!")
                .setSmallIcon(R.drawable.ic_newspaper)
                .addAction(R.drawable.ic_newspaper, "Read", pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    private void IsOreo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "news";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
