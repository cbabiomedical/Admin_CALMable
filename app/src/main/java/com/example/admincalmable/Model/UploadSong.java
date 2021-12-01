package com.example.admincalmable.Model;

public class UploadSong {



    public String url;
    public String imageView;
    public String id;
    public String songName;



    public UploadSong() {
    }

    public UploadSong(String url, String imageView, String id, String songName) {
        this.imageView = imageView;
        this.id = id;
        this.songName = songName;
        this.url = url;
    }

    public UploadSong(String url, String imageView, String songName) {
        this.url = url;
        this.imageView = imageView;
        this.songName = songName;
    }
    //    public UploadSong(String songsCategory, String title1, String toString) {


    public String getImageView() {
        return imageView;
    }

    public void setImageView(String imageView) {
        this.imageView = imageView;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    @Override
    public String toString() {
        return "UploadSong{" +
                "url='" + url + '\'' +
                ", imageUrl='" + imageView + '\'' +
                ", id='" + id + '\'' +
                ", name='" + songName + '\'' +
                '}';
    }
}

