package emilp.hallo;

import android.graphics.Bitmap;
import android.text.Html;

import java.util.ArrayList;

public class Song implements Content {
    private String title;
    private Album album;
    private ArrayList<Artist> artists = new ArrayList<>();
    private long duration_ms;
    private String id;

    public Song(String title, String id, long duration_ms) {
        this.title = title;
        this.id = id;
        this.duration_ms = duration_ms;
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

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    void addArtist(Artist artists) {
        this.artists.add(artists);
    }

    private int getDuration() {
        return (int) (duration_ms / 1000);
    }

    public long getDurationMs() { return duration_ms; }

    private String getDurationString() {
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

    void setAlbum(Album alb) {
        album = alb;
    }

    private Album getAlbum() {
        return album;
    }

    private String getInformation() {
        return getArtistsName() + " - " + getAlbum().getName() + " " + Html.fromHtml("&#8226;").toString() + " " + getDurationString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
