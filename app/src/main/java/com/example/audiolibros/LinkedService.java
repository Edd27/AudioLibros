package com.example.audiolibros;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.MediaController;

import androidx.annotation.Nullable;

import java.io.IOException;

public class LinkedService extends Service implements MediaController.MediaPlayerControl {
    private final IBinder binder = new MyBinder();

    MediaPlayer player;

    Libro book;

    int position = 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    void prepareMediaPlayer(MediaPlayer.OnPreparedListener onPreparedListener, Libro book) {
        this.book = book;
        Uri uri = Uri.parse(book.getUrl());
        if (player != null) {
            player.reset();
            player.release();
        }

        player = new MediaPlayer();

        try {
            player.setOnPreparedListener((onPreparedListener));
            Intent intent = new Intent(LinkedService.this, LinkedService.class);
            player.setOnCompletionListener(player -> LinkedService.this.stopService(intent));
            player.setDataSource(getBaseContext(), uri);
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < Libro.ejemplosLibros().size(); i++) {
            if (book.getTitulo().equals(Libro.ejemplosLibros().elementAt(i).getTitulo())) {
                position = i;
                break;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "1000";
            String name = "Audio";
            String description = "Music Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            return;
        }
        String CHANNEL_ID = "1000";
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra("flag_service", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2000,
                notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(book.getTitulo())
                .setContentText(book.getAutor())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(2000, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
        stopForeground(true);
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public class MyBinder extends Binder {
        public LinkedService getService(){
            return LinkedService.this;
        }
    }
}
