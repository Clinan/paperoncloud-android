package com.quyue.paperoncloud;

import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;


public class VoiceService extends Service implements MediaPlayer.OnCompletionListener {
    public VoiceService() {
    }


    private MediaPlayer player;

    private final IBinder binder = new AudioBinder();

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return binder;
    }

    /**
     * 当播放完的时候触发该动作
     */
    @Override
    public void onCompletion(MediaPlayer player) {
        // TODO Auto-generated method stub
//        stopSelf();//结束了，则结束Service
    }

    // 实例化MediaPlayer对象
    public void onCreate() {
        super.onCreate();
        //我们从raw文件夹中获取一个应用自带的mp3文件
        player = MediaPlayer.create(this, R.raw.francis_lai);
        player.setOnCompletionListener(this);
//        player.prepareAsync();
    }

    /**
     * 该方法在SDK2.0才开始有的，替代原来的onStart方法
     */
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if(!player.isPlaying()){
//            playOrPause();
//        }
        return START_STICKY;
    }

    public void onDestroy() {
        //super.onDestroy();
        if (player!=null){
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
        }
    }

    //为了和Activity交互，定义一个Binder对象
    class AudioBinder extends Binder {

        //返回Service对象
        VoiceService getService() {
            return VoiceService.this;
        }
    }

    //后退播放进度
    public void haveFun() {
        if (player.isPlaying() && player.getCurrentPosition() > 2500) {
            player.seekTo(player.getCurrentPosition() - 2500);
        }
    }

    public void playOrPause() {
        if (player.isPlaying()) {
            player.pause();
        } else {
            player.start();
        }

    }

    public void stop() {
        if (player != null) {
            player.stop();
            try {
                player.prepare();
                player.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    public void setVoiceResource(int resId){
        player.release();
        player=MediaPlayer.create(this,resId);
        player.setOnCompletionListener(this);
        player.start();
    }
}