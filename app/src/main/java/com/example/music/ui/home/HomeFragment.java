package com.example.music.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.music.MainActivity;
import com.example.music.MusicManager;
import com.example.music.R;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private SeekBar seekBar;
    private TextView starttime;
    private TextView stoptime;
    private ImageButton lastButton;
    private ImageButton nextButton;
    private ImageButton stpueButton;
    private MusicUpData upData;
    private boolean playStatus=false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //View view = View.inflate(getActivity(),R.layout.fragment_home,null);

        lastButton=(ImageButton)view.findViewById(R.id.button_lastmusic);
        nextButton=(ImageButton)view.findViewById(R.id.button_nextmusic);
        stpueButton=(ImageButton)view.findViewById(R.id.button_startpausemusic);
        starttime=(TextView)view.findViewById(R.id.starttime);
        stoptime=(TextView)view.findViewById(R.id.stoptime);

        lastButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        stpueButton.setOnClickListener(this);

        initThread();


        seekBar = (SeekBar) view.findViewById(R.id.musicSeekBar);
        seekBar.setMax(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                starttime.setText(MusicManager.time(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                upData.stopThred();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MusicManager.setCurrenPostion(seekBar.getProgress());
                upData.reStartThread();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        if(MusicManager.getPlaymodel()==0){
            MusicManager.setMusic();
        }
        switch (v.getId()){
            case R.id.button_lastmusic:{

                if(MusicManager.getPlaymodel()==0) {
                    Toast.makeText(getActivity(),"没有上一曲了",Toast.LENGTH_LONG).show();
                }

                Toast.makeText(getActivity(),"播放上一首",Toast.LENGTH_LONG).show();
                playStatus=true;
                MusicManager.playLastMusic();
            };
            break;
            case R.id.button_nextmusic:{
                if(MusicManager.getPlaymodel()==0) {
                    Toast.makeText(getActivity(),"没有下一曲了",Toast.LENGTH_LONG).show();
                }

                Toast.makeText(getActivity(),"播放下一首",Toast.LENGTH_LONG).show();
                playStatus=true;
                MusicManager.playNextMusic();
            };
            break;
            case R.id.button_startpausemusic:{
                if(MusicManager.getPlayMusicStatus())
                {
                    Toast.makeText(getActivity(), "暂停播放当前歌曲", Toast.LENGTH_LONG).show();
                    MusicManager.startPauseMusic();
                    playStatus=false;
                    stpueButton.setBackgroundResource(R.drawable.ic_start);
                }
                else {
                    Toast.makeText(getActivity(), "播放当前歌曲", Toast.LENGTH_LONG).show();
                    MusicManager.startPauseMusic();
                    playStatus=true;
                    stpueButton.setBackgroundResource(R.drawable.ic_pause);
                }
            };
            break;
            default:{

            };
            break;
        }
    }

//    private void upDataView(){
//        starttime.setText(MusicManager.getCurrenPostion());
//        stoptime.setText(MusicManager.getDuration());
//    }

    public void initThread(){
        upData=new MusicUpData();
        upData.start();
    }

    public void upDataView(final Context context) {
        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                starttime.setText(MusicManager.getStringCurrenPostion());
                stoptime.setText(MusicManager.getStringDuration());
                seekBar.setMax(MusicManager.getIntDuration());
                seekBar.setProgress(MusicManager.getIntCurrenPostion());
                if(playStatus!=MusicManager.getPlayMusicStatus()) {
                    Log.i("更新界面","播放图标更新");
                    if (MusicManager.getPlayMusicStatus()) {
                        stpueButton.setBackgroundResource(R.drawable.ic_pause);
                    } else {
                        stpueButton.setBackgroundResource(R.drawable.ic_start);
                    }
                    playStatus=MusicManager.getPlayMusicStatus();
                }
            }
        });
    }

    public void upDataStarttime(final Context context) {
        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public class MusicUpData extends Thread{
        private boolean stop=true;

        @Override
        public void run() {
            while (true) {
                if (MusicManager.getPlayMusicStatus()&&stop){
                    try {
                        upDataView(MusicManager.getC());
                        Thread.sleep(500);
                        continue;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void stopThred(){
            stop=false;
        }

        public void reStartThread(){
            stop=true;
        }
    }
}