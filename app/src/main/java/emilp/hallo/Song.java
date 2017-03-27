package emilp.hallo;

/**
 * Created by jonas on 2017-03-27.
 */

class Song {
    private String title = "Title";
    private String artist = "Artist";

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public Song() {
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }
}
