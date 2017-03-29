package emilp.hallo;

/**
 * Created by emil on 2017-03-28.
 */

public class Artist {

    String name;
    String[] genres;
    String spotifyID;
    int popularity;

    public Artist(String name, String[] genres, String spotifyID, int popularity) {
        this.name = name;
        this.genres = genres;
        this.spotifyID = spotifyID;
        this.popularity = popularity;

    }

    public Artist() {

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

}
