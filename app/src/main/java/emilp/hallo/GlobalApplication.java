package emilp.hallo;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by jonas on 2017-03-23.
 */

public class GlobalApplication extends Application {

    private ArrayList<Artist> artist = new ArrayList<>();
    private ArrayList<Artist> searchRes = new ArrayList<>();

    private Artist currentArtist;

    private SpotifyService spotifyService = new SpotifyService();

    public SpotifyService getSpotifyService() {
        return spotifyService;
    }

    public Object[] getSongHistory() {
        return spotifyService.getSongHistory().toArray();
    }

    public Object[] getRecommendedAlbums() {
        ArrayList<Song> arr = new ArrayList<>();
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        return arr.toArray();
    }

    public void addArtist(Artist artist) {
        this.artist.add(artist);
    }

    public boolean containsArtist(String id) {
        return getArtistFromId(id) != null;
    }

    public Artist getArtistFromId(String id) {
        for(Artist a : artist) {
            if(a.getSpotifyID().equals(id))
                return a;
        }
        return null;
    }

    public ArrayList<Artist> getSearchRes() {
        return searchRes;
    }

    public void clearResList() {
        this.searchRes.clear();
    }

    public void addSearchRes(Artist artist) {
        if(!searchContainsArtist(artist.getSpotifyID()))
            this.searchRes.add(artist);
    }

    public boolean searchContainsArtist(String id) {
        return getSearchArtistFromId(id) != null;
    }

    public Artist getSearchArtistFromId(String id) {
        for(Artist a : searchRes) {
            if(a.getSpotifyID().equals(id))
                return a;
        }
        return null;
    }

    public Artist getCurrentArtist() {
        return currentArtist;
    }

    public void setCurrentArtist(Artist currentArtist) {
        this.currentArtist = currentArtist;
    }
}
