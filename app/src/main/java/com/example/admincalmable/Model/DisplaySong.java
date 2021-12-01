package com.example.admincalmable.Model;

public class DisplaySong {
    private String id;
    private String imageView;
    private String songTitle;
    private String songUrl;

    public DisplaySong() {
    }

    public DisplaySong(String id, String imageView, String songTitle, String songUrl) {
        this.id = id;
        this.imageView = imageView;
        this.songTitle = songTitle;
        this.songUrl = songUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageView() {
        return imageView;
    }

    public void setImageView(String imageView) {
        this.imageView = imageView;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }
}
