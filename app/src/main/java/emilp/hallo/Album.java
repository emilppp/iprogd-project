package emilp.hallo;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by emil on 2017-03-28.
 */

public class Album implements Content {
    private String type;
    private ArrayList<Artist> artists = new ArrayList<>();
    private Song[] songs;
    private String name;
    private String[] genres;
    private String imageUrl;
    private Bitmap image;
    private String id;

    public Album(String name, String type, Artist artists, Song[] songs, String[] genres) {
        this.name = name;
        this.type = type;
        this.artists.add(artists);
        this.songs = songs;
        this.genres = genres;
    }

    public Album(String name, String id, String url) {
        this.name = name;
        this.id = id;
        this.imageUrl = url;
    }

    public Album() {
        this.name = "Album";
    }

    public void setType(String typ) {
        type = typ;
    }

    public String getType() {
        return type;
    }

    public void setName(String nam) {
        name = nam;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    private String getArtistsName() {
        if (getArtists().size() == 0)
            return null;
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

    public void addArtists(Artist art) {
        artists.add(art);
    }

    public void setSongs(Song[] sang) {
        songs = sang;
    }

    public Song[] getSongs() {
        return songs;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    @Override
    public String getTitle() {
        return getName();
    }

    @Override
    public String getBread() {
        return getArtistsName();
    }

    @Override
    public Bitmap getImage() {
        return image;
    }

    @Override
    public int fallbackImage() {
        return R.drawable.fallback_album;
    }

    public void downloadImage() {
        if(image == null && imageUrl != null && !imageUrl.equals(""))
            image = NetworkUtils.getBitmapFromUrl(imageUrl);
    }
}
