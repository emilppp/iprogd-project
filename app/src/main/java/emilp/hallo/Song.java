package emilp.hallo;

import android.graphics.Bitmap;
import android.text.Html;

import java.util.ArrayList;

public class Song implements Content {
    private String title;
    private Artist artist;
    private Album album;
    private ArrayList<Artist> artists = new ArrayList<>();
    private int duration;
    private String id;

    public Song(String title, Album album, Artist artist, Artist[] artists, int duration) {
        this.title = title;
        this.artist = artist;
        this.artists.add(artist);
        this.duration = duration;
        this.album = album;
    }

    public Song(String title, String id, int duration) {
        this.title = title;
        this.id = id;
        this.duration = duration;
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

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getBread() {
        return getInformation();
    }

    @Override
    public Bitmap getImage() {
        return getAlbum().getImage();
    }

    @Override
    public int fallbackImage() {
        return getAlbum().fallbackImage();
    }

    @Override
    public void downloadImage() {
        if(getAlbum() != null)
            getAlbum().downloadImage();
    }

    public void setTitle(String tit) {
        title = tit;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public void addArtist(Artist artists) {
        this.artists.add(artists);
    }

    public void setDuration(int d) {
        duration = d;
    }

    public int getDuration() {
        return duration;
    }

    public String getDurationString() {
        int h = 0, m = 0, s = getDuration();
        while(s > 59) {
            m++;
            if(m == 60) {
                h++;
                m = 0;
            }
            s -= 60;
        }
        if(h != 0) return h + ":" + m + ":" + s;
        else if(m != 0) return m + ":" + s;
        return ""+s;
    }

    private String getArtistsName() {
        String res = "";
        ArrayList<Artist> artists = getArtists();
        for(int i=0; i<artists.size(); i++) {
            res += artists.get(i).getName();
            if(i < artists.size() - 2)
                res += ", ";
            if(i == artists.size() - 2)
                res += " & ";
        }
        return res;
    }

    public void setAlbum(Album alb) {
        album = alb;
    }

    public Album getAlbum() {
        return album;
    }

    public String getInformation() {
        return getArtistsName() + " - " + getAlbum().getName() + " " + Html.fromHtml("&#8226;").toString() + " " + getDurationString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
