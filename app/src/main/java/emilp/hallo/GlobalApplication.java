package emilp.hallo;

import android.app.Application;

import java.util.ArrayList;

import emilp.hallo.view.ContentList;

/**
 * Created by jonas on 2017-03-23.
 */

public class GlobalApplication extends Application {

    private ArrayList<Content> artist = new ArrayList<>();
    private ArrayList<Content> searchRes = new ArrayList<>();
    private ArrayList<Content> songHistory = new ArrayList<>();
    private ArrayList<Song> songsToBeAdded = new ArrayList<>();
    private ContentList historyAdapter;

    private String playlistID;

    private String clientID;

    private String displayName;

    private Content currentContent;

    private SpotifyService spotifyService = new SpotifyService();

    public SpotifyService getSpotifyService() {
        return spotifyService;
    }

    public void getSongHistory(ContentList contentList) {
        new ApiSongHistory(contentList, this);
    }

    public void getRecommendedSongs(ContentList contentList) {
        new ApiGetSongs(contentList);
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

    public void getRecommendedAlbums(ContentList contentList) {
        new ApiGetAlbums(contentList);
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

    public void getRecommendedArtists(ContentList contentList) {
        new ApiGetArtists(contentList);
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

    public Content getCurrentContent() {
        return currentContent;
    }

    public void setCurrentContent(Content currentArtist) {
        this.currentContent = currentArtist;
    }

    public void fetchClientID() {
        if(clientID == null && displayName == null) {
            spotifyService.getClientId(this);
        }
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void createPlaylist() {
        spotifyService.createPlaylist(this);
    }

    public String getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(String playlistID) {
        this.playlistID = playlistID;
    }

    public ArrayList<Song> getSongsToBeAdded() {
        return songsToBeAdded;
    }

    public void setSongsToBeAdded(ArrayList<Song> songsToBeAdded) {
        this.songsToBeAdded = songsToBeAdded;
    }

    public ArrayList<Content> getSongsToBeAddedAsContent() {
        ArrayList<Content> res = new ArrayList<>();
        for(Song song : getSongsToBeAdded())
            res.add(song);
        return res;
    }

    public void addToPlaylist(Song content) {
        if(content != null)
            songsToBeAdded.add(content);
    }

    public void postPlaylist() {
        spotifyService.postPlaylist(this);
    }

    public void removeTrackFromPlaylist(String track) {
        spotifyService.removeTrackFromPlaylist(this, track);
    }
}
