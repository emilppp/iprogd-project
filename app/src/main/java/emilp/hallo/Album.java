package emilp.hallo;

/**
 * Created by emil on 2017-03-28.
 */

public class Album {
    private String type;
    private Artist[] artists;
    private Song[] songs;
    private String name;
    private String[] genres;

    public Album(String name, String type, Artist[] artists, Song[] songs, String[] genres) {
        this.name = name;
        this.type = type;
        this.artists = artists;
        this.songs = songs;
        this.genres = genres;
    }
    public Album() {

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

    public Artist[] getArtists() {
        return artists;
    }

    public void setArtists(Artist[] art) {
        artists = art;
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
}
