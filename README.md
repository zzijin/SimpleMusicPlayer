# SimpleMusicPlayer  

#### 介绍  
简洁轻巧的本地音乐播放器   
SignInAPP:https://gitee.com/zzijin/SignIn  

#### Service(服务)   
Service是一个后台运行的组件，执行长时间运行且不需要用户交互的任务。即使应用被销毁也依然可以工作。服务基本上包含两种状态：   
Started（Android的应用程序组件，如活动，通过startService()启动了服务，则服务是Started状态。一旦启动，服务可以在后台无限期运行，即使启动它的组件已经被销毁）  
Bound（当Android的应用程序组件通过bindService()绑定了服务，则服务是Bound状态。Bound状态的服务提供了一个客户服务器接口来允许组件与服务进行交互，如发送请求，获取结果，甚至通过IPC来进行跨进程通信）   
![Server生命周期](https://images.gitee.com/uploads/images/2021/0103/195158_3af8cf05_8505810.png "屏幕截图.png")   
MusicServer构造:  

```
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
```

#### MediaStore(安卓多媒体库)   
MediaStore这个类是android系统提供的一个多媒体数据库，android中多媒体信息都可以从这里提取。这个MediaStore包括了多媒体数据库的所有信息，包括 音频，视频和图像,android把所有的多媒体数据库接口进行了封装，所有的数据库不用自己进行创建，直接调用利用ContentResolver去掉 用那些封装好的接口就可以进行数据库的操作了。   
获取本机音乐库: 

```
public static List<MusicInf> getmusic(Context context) {
        List<MusicInf> songs=new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        , null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MusicInf song = new MusicInf();
                String name;
                String singer;
                String path;
                int duration;
                long size;
                long albumId;
                long id;
                //歌名
                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                //歌曲id
                id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                //歌手
                singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                //歌曲路径
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                //歌曲长度
                duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                //歌曲大小
                size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                //歌曲封面
                albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                //list.add(song);
                //把歌曲名字和歌手切割开
                //song.setName(name);
                name=namereplace(name);
                song.setMusicpath(path);
                song.setLeng(duration);
                song.setSize(size);
                song.setAlbumId(albumId);
                if (size > 1000 * 800) {
                    if (name.contains("-")) {
                        String[] str = name.split("-");
                        singer = str[0];
                        song.setMusicArtist(singer);
                        name = str[1];
                        song.setMusicName(name);
                    } else {
                        song.setMusicArtist(singer);
                        song.setMusicName(name);
                    }
                    songs.add(song);
                }
                Log.i("加载本地歌曲","搜索到歌曲--名:"+song.getMusicName()+"歌手:"+song.getMusicArtist()+"地址："+song.getMusicpath());
            }
        }
        cursor.close();
        return songs;
    }
```

#### ArrayAdapter<MusicInf>(适配器)  
重写适配器的getView()方法
```
public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view  = LayoutInflater.from(getContext()).inflate(resourceId, null);
        MusicInf song=getItem(position);

        TextView name=(TextView)view.findViewById(R.id.text_musicname);
        TextView size=(TextView)view.findViewById(R.id.text_musicsize);
        TextView artist=(TextView)view.findViewById(R.id.text_artist);
        final TextView index=(TextView)view.findViewById(R.id.text_musicindex);
        TextView leng=(TextView)view.findViewById(R.id.text_leng);

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
```
#### 功能实现  
1.获取本地媒体库中的音乐   
2.点击列表播放该音乐   
3.自由拖动进度条转跳进度   
4.点击上下曲切换播放列表音乐   
5.播放完后自动切换下一曲   
6.暂停音乐稍后再播放   
7.后台播放音乐   
  
#### 截图   
![音乐播放器主页](https://images.gitee.com/uploads/images/2021/0103/200200_28e38e5b_8505810.jpeg "音乐播放器主页.jpg")  
![音乐播放器本地音乐列表](https://images.gitee.com/uploads/images/2021/0103/200207_447c599a_8505810.jpeg "音乐播放器本地音乐列表.jpg")  