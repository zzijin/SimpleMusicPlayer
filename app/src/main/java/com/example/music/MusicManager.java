package com.example.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MusicManager {
    static private Conn conn;
    static private MusicService.MusicBinder musicBinderl;
    static private List<MusicInf> musicList;
    static private int index=0;
    static private Context c;
    static private int musicSize=0;
    static private boolean playMusicStatus=false;
    static private int playmodel=0;//1.播放资源音乐  2.播放本地音乐


    /**
     * 开启播放服务
     * @param context
     */
    public static void openService(Context context){
        if (context!=null) {
            c=context;

            Intent intent = new Intent(context, MusicService.class);
            conn = new Conn();
            context.startService(intent);
            context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
            musicList=new ArrayList<>();
        }
        else {

        }
    }

    public static List<MusicInf> getMusicList() {
        return musicList;
    }
    public static int getPlaymodel() {
        return playmodel;
    }
    public static void setPlayMusicStatus(boolean playMusicStatus) {
        MusicManager.playMusicStatus = playMusicStatus;
    }
    public static boolean getPlayMusicStatus(){
        return playMusicStatus;
    }
    public static Context getC(){
        return c;
    }

    /**
     * 搜索获取本地音乐
     * @param context
     */
    public static void getLocalMusic(Context context){
        musicList.clear();
        musicList.addAll(GetLocalMusic.getmusic(context));
    }

    /**
     * 检查是否获取到新的本地音乐（用于线程更新列表）
     * @return
     */
    public static boolean LoadMusicStatus(){
        if(musicSize!=musicList.size()){
            musicSize=musicList.size();
            return true;
        }
        else
            return false;
    }
    public static int getMusicSize(){
        return musicSize;
    }

    //设置播放歌曲(资源内歌曲/测试用)
    public static void setMusic(){
        playmodel=1;
        musicBinderl.setMusic(c);
    }

    /**
     * 设置地址以播放手机内部音乐
     * @param i
     */
    public static void setLocalMusic(int i){

        if(playmodel==1||playmodel==0) {
            musicBinderl.iniLocalMusic();
            playmodel=2;
        }

        index=i;
        musicBinderl.setMusic(musicList.get(index).getMusicpath());

        startPauseMusic();
    }

    /**
     * 播放列表中下一首歌曲
     */
    public static void playNextMusic(){
        if(musicBinderl.getMusicStatu()) {
            startPauseMusic();
        }

        if(index<musicList.size()) {
            setLocalMusic(++index);
        }
        else if(index>=musicList.size()){
            setLocalMusic(0);
        }

        Log.i("按钮","播放上一首"+musicBinderl.getMusicStatu());
    }

    /**
     * 播放列表中上一曲歌曲
     */
    public static void playLastMusic(){
        if(musicBinderl.getMusicStatu()) {
            startPauseMusic();
        }

        if(index>0) {
            setLocalMusic(--index);
        }
        else if(index<=0){
            setLocalMusic(musicList.size()-1);
        }
        Log.i("按钮","播放下一首"+musicBinderl.getMusicStatu());
    }

    /**
     * 开始或暂停播放
     */
    public static void startPauseMusic(){
        if(musicBinderl.getMusicStatu()) {
            musicBinderl.pausemusic();
        }
        else {
            musicBinderl.playmusic();
        }
        playMusicStatus=musicBinderl.getMusicStatu();
    }

    /**
     * 获取当前播放歌曲的长度
     * @return
     */
    public static String getStringDuration(){
        return time(musicBinderl.getDuration());
    }
    public static int getIntDuration(){
        return musicBinderl.getDuration();
    }

    /**
     * 获取歌曲目前播放的进度
     */
    //String类型
    public static String getStringCurrenPostion(){
        return time(musicBinderl.getCurrenPostion());
    }
    //int类型（单位毫秒）
    public static int getIntCurrenPostion(){
        return musicBinderl.getCurrenPostion();
    }

    /**
     * 设置歌曲播放起始位置
     * @param t 单位毫秒
     */
    public static void setCurrenPostion(int t){
        musicBinderl.seekTo(t);
    }

    /**
     *int毫秒时间转String工具方法
     * @param t
     * @return 返回String时间
     */
    public static String time(int t){
        int s=t/1000;
        int m=s/60;
        int ss=s%60;

        if(s<60){
            if(s>9) {
                return "00:" + s;
            }
            else {
                return "00:0"+s;
            }
        }
        else {
            if(m>9){
                if(ss>9){
                    return m+":"+ss;
                }
                else {
                    return m+":0"+ss;
                }
            }
            else {
                if(ss>9){
                    return "0"+m+":"+ss;
                }
                else {
                    return "0"+m+":0"+ss;
                }
            }
        }
    }

    /**
     * 用于开启服务类
     */
    private static class Conn implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinderl=(MusicService.MusicBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

}
