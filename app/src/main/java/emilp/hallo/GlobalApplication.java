package emilp.hallo;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import emilp.hallo.SQL.FeedReaderContract;
import emilp.hallo.view.ContentList;
import emilp.hallo.view.CurrentlyPlaying;

public class GlobalApplication extends Application {

    private ArrayList<Song> songsToBeAdded = new ArrayList<>();
    private String playlistID;
    private String clientID;
    private String displayName;
    private Content currentContent;
    private SpotifyService spotifyService = new SpotifyService();
    private FeedReaderContract.FeedReaderDbHelper mDbHelper;
    private SharedPreferences sharedPreferences;
    private Song currentlyPlayingSong = null;
    private CurrentlyPlaying currentlyPlayingPlayer = null;
    private boolean isBroadcasting = false;
    private String imageUrl;
    private Bitmap image;

    private static String SHARED_PREFERENCES_PLAYLIST_ID = "playlistId";

    @Override
    public void onCreate() {
        super.onCreate();

        mDbHelper = new FeedReaderContract.FeedReaderDbHelper( getApplicationContext() );

        String SHARED_PREFERENCES = "spotgenprefs";
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
        e.apply(); // this saves to disk and notifies observers
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

    /**
     * Add a single song to the database
     */
    public void addSongToDatabase(Song song) {
        if(song != null)
           putData(mDbHelper, song.getId());
    }

    public void printDataBase() {
        ArrayList<String> itemIds = getPlaylistFromDb(mDbHelper);
        System.out.println(java.util.Arrays.toString(itemIds.toArray()));
    }

    // Remove a single song from the database
    private void removeSongFromPlaylistDb(String id) {
        removeSongFromPlaylistDb( new String[]{ id } );
    }

    // Remove multiple songs from the database
    public void removeSongFromPlaylistDb(String[] ids) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define 'where' part of query.
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, ids);
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

    /**
     * Gets the last played tracks from the users spotify account
     */
    public void getSongHistory(ContentList contentList) {
        new ApiSongHistory(contentList, this);
    }

    public void getRecommendedArtists(ContentList contentList) {
        new ApiGetArtists(contentList);
    }

    public Content getCurrentContent() {
        return currentContent;
    }

    public void setCurrentContent(Content currentArtist) {
        this.currentContent = currentArtist;
    }

    // Gets the current users ID if they're properly logged in
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

    public void setImageUrl(String url) {this.imageUrl = url;}

    public void downloadImage() {
        if(image == null && imageUrl != null && !imageUrl.equals(""))
            image = NetworkUtils.getBitmapFromUrl(imageUrl);
    }

    public Bitmap getProfilePicture() {
        return image == null ? null : Bitmap.createScaledBitmap(this.image, 250, 250, false);
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

    public ArrayList<Content> getSongsToBeAddedAsContent() {
        ArrayList<Content> res = new ArrayList<>();
        for(Song song : getSongsToBeAdded())
            res.add(song);
        return res;
    }

    /**
     *     Clears the 'local' playlist
     */
    public void clearPlaylist() {
        songsToBeAdded.clear();
    }

    /**
     * Reset the playlist locally & in the database
     */
    public void resetPlaylist() {
        clearPlaylist();
        resetDataBase();
        removeSavedPlaylistId();
    }

    /**
     * Will add the content to the playlist an update both the local database as well as
     * the Spotify playlist.
     */
    public void addToPlaylist(Song content) {
        if(content != null) {
            if(!isInPlaylist(content)) {
                songsToBeAdded.add(content);
                addSongToDatabase(content);
                ArrayList<Song> a = new ArrayList<>();
                a.add(content);
                spotifyService.postPlaylist(this, a);
            }
        }
    }

    /**
     * Will only add the content to the playlist and won't update the database nor Spotify.
     */
    public void addToPlaylistPure(Song content) {
        if(content != null)
            if(!isInPlaylist(content))
                songsToBeAdded.add(content);
    }

    /**
     * Will post all contents in the playlist to both the database as well as Spotify.
     * This can cause duplicates.
     */
    public void postPlaylist() {
        ArrayList<Song> songs = getSongsToBeAdded();
        for(Song s : songs)
            addSongToDatabase(s);
        spotifyService.postPlaylist(this, songs);
    }

    /**
     * Looks for a track in the local playlist and tries to remove it.
     */
    public void removeFromLocalPlaylist(Song track) {
        for(Iterator<Song> it = songsToBeAdded.iterator(); it.hasNext();) {
            Song c = it.next();
            if(c.getId().equals(track.getId()))
                it.remove();
        }
    }

    /**
     * Checks if the given song is in the playlist
     */
    public boolean isInPlaylist(Song song) {
        for(int i=0; i<songsToBeAdded.size(); i++) {
            System.out.println("Is " + song.getId() + " == ");


            if (Objects.equals(songsToBeAdded.get(i).getId(), song.getId()))
                return true;
        }
        return false;
    }

    /**
     * @return the currently playing song
     */
    public Song getCurrentlyPlayingSong() {
        return currentlyPlayingSong;
    }

    public void setCurrentlyPlayingSong(Song currentlyPlayingSong) {
        this.currentlyPlayingSong = currentlyPlayingSong;
        isBroadcasting = false;
    }

    public void initPlayer(Activity activity) {
        currentlyPlayingPlayer = new CurrentlyPlaying(activity);
    }

    public void showCurrentlyPlayingBroadcastedSong(Song song, int progress) {
        isBroadcasting = true;
        if(song != null)
            this.currentlyPlayingSong = song;
        currentlyPlayingPlayer.showBroadcasted(progress);
    }

    public void hideCurrentlyPlayingBroadcastedSong() {
        currentlyPlayingPlayer.hideBroadcasted();
    }

    public boolean isBroadcasting() {
        return isBroadcasting;
    }

    public CurrentlyPlaying getCurrentlyPlayingPlayer() {
        return currentlyPlayingPlayer;
    }
}
