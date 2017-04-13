package emilp.hallo;

import android.graphics.Bitmap;

/**
 * Created by emil on 2017-03-28.
 */

public class Artist implements Content {

    private String name;
    private String[] genres;
    private String imageUrl;
    private Bitmap image;
    private String spotifyID;
    private int popularity;

    public Artist(String name, String[] genres, String spotifyID, int popularity) {
        this.name = name;
        this.genres = genres;
        this.spotifyID = spotifyID;
        this.popularity = popularity;
    }

    public Artist(String name, String[] genres, String spotifyID, int popularity, String imageUrl) {
        this.name = name;
        this.genres = genres;
        this.spotifyID = spotifyID;
        this.popularity = popularity;
        this.imageUrl = imageUrl;
    }

    public Artist(String name, String id, String url) {
        this.name = name;
        this.spotifyID = id;
        this.imageUrl = url;
    }

    public Artist(String name, String id) {
        this.name = name;
        this.spotifyID = id;
    }

    public Artist() {
        name = "Artist";
        spotifyID = "opsasdasdpiahsdopiahsd12";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String[] getGenres() {
        return this.genres;
    }

    public void setPopularity(int pop) {
        popularity = pop;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setSpotifyID(String id) {
        spotifyID = id;
    }

    public String getSpotifyID() {
        return spotifyID;
    }

    public void downloadImage() {
        if(image == null && imageUrl != null && !imageUrl.equals(""))
            image = NetworkUtils.getBitmapFromUrl(imageUrl);
    }

    @Override
    public String getTitle() {
        return getName();
    }

    @Override
    public String getBread() {
        return null;
    }

    @Override
    public Bitmap getImage() {
        return image;
    }

    @Override
    public int fallbackImage() {
        return R.drawable.placeholder_pink;
    }
}
