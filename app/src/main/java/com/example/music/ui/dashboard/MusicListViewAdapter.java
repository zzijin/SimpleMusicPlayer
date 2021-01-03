package com.example.music.ui.dashboard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.music.MusicInf;
import com.example.music.MusicManager;
import com.example.music.R;

public class MusicListViewAdapter extends ArrayAdapter<MusicInf> {
    private int resourceId;

    public MusicListViewAdapter(Context context,int textViewResourceId){
        super(context,textViewResourceId, MusicManager.getMusicList());
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view  = LayoutInflater.from(getContext()).inflate(resourceId, null);
        MusicInf song=getItem(position);

        TextView name=(TextView)view.findViewById(R.id.text_musicname);
        TextView size=(TextView)view.findViewById(R.id.text_musicsize);
        TextView artist=(TextView)view.findViewById(R.id.text_artist);
        final TextView index=(TextView)view.findViewById(R.id.text_musicindex);
        TextView leng=(TextView)view.findViewById(R.id.text_leng);


        //if(song.getMusicName().length()>15) {
        //    name.setText(song.getMusicName().substring(0, 15)+"...");
        //}
        //else {
            name.setText(song.getMusicName());
        //}
        if(song.getMusicArtist().length()>10){
            artist.setText(song.getMusicArtist().substring(0,10)+"...");
        }
        else {
            artist.setText(song.getMusicArtist());
        }

        size.setText(song.getSize()/1024/1024+"M");
        index.setText(String.valueOf(position));
        leng.setText(MusicManager.time(song.getLeng()));

        view.findViewById(R.id.button_musicmore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //音乐更多菜单
                Toast.makeText(getContext(),"点击了更多信息",Toast.LENGTH_LONG).show();
            }
        });

        view.findViewById(R.id.line_music).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"选择播放音乐:"+MusicManager.getMusicList().get(position).getMusicName(),Toast.LENGTH_LONG).show();
                MusicManager.setLocalMusic(position);
            }
        });
        return view;
    }
}
