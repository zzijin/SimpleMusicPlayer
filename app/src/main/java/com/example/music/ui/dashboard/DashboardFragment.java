package com.example.music.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.music.MainActivity;
import com.example.music.MusicManager;
import com.example.music.R;

public class DashboardFragment extends Fragment {
    public MusicListViewAdapter adapter;
    public ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        getLocalMusic(root.getContext());
        //firstRun(root.getContext());

        waitGetLocalMusic waitGetLocalMusic=new waitGetLocalMusic();
        waitGetLocalMusic.start();

        return root;
    }

    private void firstRun(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("share",Context.MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFirstRun)
        {
            getLocalMusic(context);
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        } else
        {

        }
    }

    public void getLocalMusic(Context context){
         MusicManager.getLocalMusic(context);
    }

    public void initAdapter(final Context context) {
        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("加载本地歌曲","初始化适配器");
                adapter = new MusicListViewAdapter(getActivity(), R.layout.listview_music);
                listView = (ListView)getActivity().findViewById(R.id.listview_music);
                listView.setAdapter(adapter);
            }
        });
    }

    public void upDataAdapter(final Context context) {
        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("加载本地歌曲","搜索到新歌");
                adapter.notifyDataSetChanged();
            }
        });
    }

    class waitGetLocalMusic extends Thread{
        private int times=0;
        private boolean iniAdapterStatu=true;

        @Override
        public void run() {
            while (true){
                try {
                    if(getActivity()!=null) {

                        if (MusicManager.getMusicSize() > 0&&iniAdapterStatu) {
                            initAdapter(getActivity());
                            iniAdapterStatu=false;
                        }
                        if (MusicManager.LoadMusicStatus()&&!iniAdapterStatu) {
                            upDataAdapter(getActivity());
                            times=0;
                        }
                        if(times>20){
                            break;
                        }
                        times++;
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}