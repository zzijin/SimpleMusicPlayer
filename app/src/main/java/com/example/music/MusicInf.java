package com.example.music;

public class MusicInf {
    private String musicName;//歌曲名字
    private String musicArtist;//歌手
    private String musicpath;//歌曲地址
    public long size;//歌曲大小
    public long  albumId;//图片id
    public int leng;//歌曲长度


    public String getMusicName() {
        return musicName;
    }
    public String getMusicpath() {
        return musicpath;
    }
    public String getMusicArtist() {
        return musicArtist;
    }
    public long getSize() {
        return size;
    }
    public long getAlbumId() {
        return albumId;
    }
    public int getLeng() {
        return leng;
    }

    public void setMusicName(String musiceName) {
        this.musicName = musiceName;
    }
    public void setMusicpath(String musicpath) {
        this.musicpath = musicpath;
    }
    public void setMusicArtist(String musicArtist) {
        this.musicArtist = musicArtist;
    }
    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }
    public void setSize(long size) {
        this.size = size;
    }
    public void setLeng(int leng) {
        this.leng = leng;
    }

    public MusicInf(){
    }
}
