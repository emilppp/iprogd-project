package emilp.hallo;

import android.text.Html;

class Song {
    private String title;
    private Artist artist;
    private Album album;
    private Artist[] artists;
    private int duration;

    public Song(String title, Album album, Artist artist, Artist[] artists, int duration) {
        this.title = title;
        this.artist = artist;
        this.artists = artists;
        this.duration = duration;
        this.album = album;
    }

    public Song() {
        this.title = "Title";
        this.artist = new Artist();
        this.album = new Album();
        this.duration = 10;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist art) {
        artist = art;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String tit) {
        title = tit;
    }

    public Artist[] getArtists() {
        return artists;
    }

    public void setArtists(Artist[] artists) {
        this.artists = artists;
    }

    public void setDuration(int d) {
        duration = d;
    }

    public int getDuration() {
        return duration;
    }

    public void setAlbum(Album alb) {
        album = alb;
    }

    public Album getAlbum() {
        return album;
    }

    public String getInformation() {
        return getArtist().getName() + " - " + getAlbum().getName() + " " + Html.fromHtml("&#8226;").toString() + " " + getDuration();
    }
}
