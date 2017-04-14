package emilp.hallo;

import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

import emilp.hallo.SQL.FeedReaderContract;
import emilp.hallo.view.ContentList;

import static java.security.AccessController.getContext;

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
    private FeedReaderContract.FeedReaderDbHelper mDbHelper;
    private SharedPreferences sharedPreferences;
    private Song currentlyPlayingSong = null;

    private static String SHARED_PREFERENCES = "spotgenprefs";
    private static String SHARED_PREFERENCES_PLAYLIST_ID = "playlistId";

    @Override
    public void onCreate() {
        super.onCreate();

        mDbHelper = new FeedReaderContract.FeedReaderDbHelper( getApplicationContext() );
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);

        playlistID = getSavedPlaylistId();

        if(playlistID == null) {
            System.out.println("Deleted database (No matching id)");
            resetDataBase();
        }
        else if(getPlaylistFromDb().size() == 0) {
            System.out.println("Reset playlist (Empty database)");
            removeSavedPlaylistId();
            playlistID = null;
        }

        printDataBase();
    }

    private void setSavedPlaylistId(String id) {
        if(id == null)
            removeSavedPlaylistId();
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(SHARED_PREFERENCES_PLAYLIST_ID, id); // add or overwrite someValue
        e.commit(); // this saves to disk and notifies observers
    }

    private void removeSavedPlaylistId() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove( SHARED_PREFERENCES_PLAYLIST_ID );
        editor.apply();
    }

    private String getSavedPlaylistId() {
        return sharedPreferences.getString(SHARED_PREFERENCES_PLAYLIST_ID, null); // return null if doesn't exist
    }

    public void resetDataBase() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        mDbHelper.onUpgrade(db, 0, 0);
    }

    public void addSongToDatabase(ArrayList<Song> songs) {
        for(Song s : songs)
            addSongToDatabase( s );
    }

    public void addSongToDatabase(Song song) {
        if(song != null)
           putData(mDbHelper, song.getId());
    }

    public void printDataBase() {
        ArrayList<String> itemIds = getPlaylistFromDb(mDbHelper);
        System.out.println(java.util.Arrays.toString(itemIds.toArray()));
    }

    private void removeSongFromPlaylistDb(String id) {
        removeSongFromPlaylistDb( new String[]{ id } );
    }

    public void removeSongFromPlaylistDb(String[] ids) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define 'where' part of query.
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = ids;
        // Issue SQL statement.
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }

    public ArrayList<String> getPlaylistFromDb() {
        return getPlaylistFromDb(mDbHelper);
    }

    private ArrayList<String> getPlaylistFromDb(FeedReaderContract.FeedReaderDbHelper mDbHelper) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE
        };

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        ArrayList<String> itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            String item = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)
            );
            itemIds.add(item);
        }
        cursor.close();

        return itemIds;
    }

    private void putData(FeedReaderContract.FeedReaderDbHelper mDbHelper, String spotifyId) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, spotifyId);
        db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
    }

    public SpotifyService getSpotifyService() {
        return spotifyService;
    }

    public void getSongHistory(ContentList contentList) {
        new ApiSongHistory(contentList, this);
    }


    public void addSongHistory(Song song) {
        songHistory.add(song);
        historyAdapter.notifyDataSetChanged();
    }


    public void getRecommendedArtists(ContentList contentList) {
        new ApiGetArtists(contentList);
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
        setSavedPlaylistId(playlistID);
        this.playlistID = playlistID;
    }

    public ArrayList<Song> getSongsToBeAdded() {
        return songsToBeAdded;
    }

    public void clearSongsToBeAdded() {
        this.songsToBeAdded.clear();
    }

    public void setSongsToBeAdded(ArrayList<Song> songsToBeAdded) {
        this.songsToBeAdded = songsToBeAdded;
        resetDataBase();
        addSongToDatabase(this.songsToBeAdded);
    }

    public ArrayList<Content> getSongsToBeAddedAsContent() {
        ArrayList<Content> res = new ArrayList<>();
        for(Song song : getSongsToBeAdded())
            res.add(song);
        return res;
    }

    public void clearPlaylist() {
        songsToBeAdded.clear();
    }

    public void resetPlaylist() {
        clearPlaylist();
        resetDataBase();
        removeSavedPlaylistId();
    }

    public void addToPlaylist(Song content) {
        if(content != null) {
            if(!isInPlaylist(content)) {
                songsToBeAdded.add(content);
                addSongToDatabase(content);
                ArrayList<Song> a = new ArrayList<Song>();
                a.add(content);
                spotifyService.postPlaylist(this, a);
            }
        }
    }

    public void postPlaylist() {
        spotifyService.postPlaylist(this);
    }

    public void removeFromLocalPlaylist(Song track) {
        for(Iterator<Song> it = songsToBeAdded.iterator(); it.hasNext();) {
            Song c = it.next();
            if(c.getId().equals(track.getId()))
                it.remove();
        }
    }

    public void printPlaylist() {
        for(Song i : songsToBeAdded)
            System.out.println(i.getId());
    }

    public boolean isInPlaylist(Song song) {
        for(int i=0; i<songsToBeAdded.size(); i++) {
            System.out.println("Is " + song.getId() + " == ");


            if (Objects.equals(songsToBeAdded.get(i).getId(), song.getId()))
                return true;
        }
        return false;
    }

    public void removeTrackFromPlaylist(Song track) {
        removeSongFromPlaylistDb(track.getId());
        String removeSong = "spotify:track:" + track.getId();
        spotifyService.removeTrackFromPlaylist(this, removeSong);
        removeFromLocalPlaylist(track);
    }

    public Song getCurrentlyPlayingSong() {
        return currentlyPlayingSong;
    }

    public void setCurrentlyPlayingSong(Song currentlyPlayingSong) {
        this.currentlyPlayingSong = currentlyPlayingSong;
    }

    public void logOut() {
        spotifyService.getmPlayer().logout();
    }
}
