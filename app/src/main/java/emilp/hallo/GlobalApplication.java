package emilp.hallo;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;

import emilp.hallo.view.ContentList;

/**
 * Created by jonas on 2017-03-23.
 */

public class GlobalApplication extends Application {

    private ArrayList<Content> artist = new ArrayList<>();
    private ArrayList<Content> searchRes = new ArrayList<>();
    private ArrayList<Content> songHistory = new ArrayList<>();
    private ContentList historyAdapter;

    private Artist currentArtist;

    private SpotifyService spotifyService = new SpotifyService();

    public SpotifyService getSpotifyService() {
        return spotifyService;
    }

    public ArrayList<Content> getSongHistory(ContentList contentList) {
        historyAdapter = contentList;
        return getSongHistory();
    }

    public ArrayList<Content> getSongHistory() {
        if(songHistory.size() == 0)
            songHistory = spotifyService.getSongHistory(this);
        return songHistory;
    }

    public void addSongHistory(Song song) {
        songHistory.add(song);
        historyAdapter.notifyDataSetChanged();
    }

    public ArrayList<Content> getRecommendedAlbums() {
        ArrayList<Content> arr = new ArrayList<>();
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
        return arr;
    }

    public ArrayList<Content> getRecommendedArtists() {
        ArrayList<Content> data = new ArrayList<>();
        data.add(new Artist());
        data.add(new Artist());
        data.add(new Artist());
        return data;
    }

    public void addArtist(Artist artist) {
        this.artist.add(artist);
    }

    public boolean containsArtist(String id) {
        return getArtistFromId(id) != null;
    }

    public Artist getArtistFromId(String id) {
        for(Content c : artist) {
            Artist a = (Artist) c;
            if(a.getSpotifyID().equals(id))
                return a;
        }
        return null;
    }

    public ArrayList<Content> getSearchRes() {
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
        for(Content c : searchRes) {
            Artist a = (Artist) c;
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
