package com.example.music;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service {
    private MediaPlayer player;


    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //player = new MediaPlayer();
    }

    public class MusicBinder extends Binder {

        //判断是否处于播放状态
        public boolean isPlaying() {
            return player.isPlaying();
        }

        public void setMusic(Context context){
            //播放资源文件夹内音乐
            player=MediaPlayer.create(context,R.raw.originalmix);

        }

        public void setMusic(String path){

            Log.i("播放本地音乐","暂停正在播放的音乐");
            iniLocalMusic();

            //播放文件夹音乐
            try {
                Log.i("播放本地音乐","路径："+path);
                player.setDataSource(path);
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 初始化播放器以用于播放本地音乐
         */
        public void iniLocalMusic(){
            if(player!=null) {
                player.stop();
                player.release();
                player=null;
            }

            player=new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Log.i("播放本地音乐","创建新播放器");
        }

        //播放
        public void playmusic() {
            Log.i("播放or暂停","播放");
            player.start();
        }

        public void pausemusic(){
            Log.i("播放or暂停","暂停");
            player.pause();
        }

        //返回歌曲的长度，单位为毫秒
        public int getDuration() {
            if(player!=null) {
                return player.getDuration();
            }
            else {
                return 0;
            }
        }

        //返回歌曲目前的进度，单位为毫秒
        public int getCurrenPostion() {
            if(player!=null) {
                return player.getCurrentPosition();
            }
            else {
                return 0;
            }
        }

        //设置歌曲播放的进度，单位为毫秒
        public void seekTo(int mesc) {
            if(mesc>=0&&mesc<getDuration()) {
                player.seekTo(mesc);
            }
            else {

            }
        }

        public boolean getMusicStatu(){
            return player.isPlaying();
        }



    }
}
