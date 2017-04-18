package emilp.hallo;

import android.os.AsyncTask;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import emilp.hallo.view.ContentList;

class ApiGetSongs {

    private ContentList contentList;
    private ArrayList<Content> songs = new ArrayList<>();
    private String query = null;
    private int limit = 3;

    /**
     * This constructor will generate <code>limit</code> random results
     * @param limit The amount of results returned
     */
    ApiGetSongs(ContentList contentList, int limit) {
        this.contentList = contentList;
        this.limit = limit;

        contentList.init(songs);
        getSongHistory();
    }

    /**
     * WARNING! This will not be thread-safe and will halt execution.
     * This constructor will generate <code>limit</code> random results.
     * The results can then be fetched with the <code>getSongs()</code> method.
     * @param limit The amount of results returned
     */
    ApiGetSongs(int limit) {
        this.limit = limit;
        getSongHistoryUnsafe();
    }

    /**
     * WARNING! This will not be thread-safe and will halt execution.
     * This constructor will get all songs from the provided list of ids
     * The results can then be fetched with the <code>getSongs()</code> method.
     * @param ids
     *      Should be of the form "id1,id2,...,idn". All ids must be separated by a comma.
     */
    ApiGetSongs(String ids) {
        getSongHistoryIdsUnsafe(ids);
    }

    /**
     * Using this constructor, results will be based on the query
     */
    ApiGetSongs(ContentList contentList, String query) {
        this.contentList = contentList;
        this.query = query;

        contentList.init(songs);
        getSongHistory();
    }

    ApiGetSongs(ContentList contentList, Album album) {
        this.contentList = contentList;

        contentList.init(songs);
        getSongsFromAlbum(album);
    }

    /**
     * Get's the most popular songs from the supplied artist
     */
    ApiGetSongs(ContentList contentList, Artist artist) {
        this.contentList = contentList;

        contentList.init(songs);
        getSongsFromArtist(artist);
    }

    private void getSongsFromArtist(final Artist artist) {
        URL url = NetworkUtils.buildAristsPopularTracksURL(artist.getSpotifyID());
        new AsyncTask<URL, Void, Void>() {
            @Override
            protected Void doInBackground(URL... urls) {
                URL searchUrl = urls[0];
                try {
                    JSONObject res = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                    parseArtistsTracksJSON(res);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(url);
    }

    private void getSongsFromAlbum(final Album album) {
        URL url = NetworkUtils.buildAlbumTracksURL(album.getId());
        new AsyncTask<URL, Void, Void>() {
            @Override
            protected Void doInBackground(URL... urls) {
                URL searchUrl = urls[0];
                try {
                    JSONObject res = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                    parseAlbumTracksJSON(res, album);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                contentList.notifyDataSetChanged();
            }
        }.execute(url);
    }

    private void addSongToResult(Song song) {
        songs.add(song);
    }

    private void getSongHistoryUnsafe() {
        URL url = NetworkUtils.buildRandom("track", this.limit);
        try {
            JSONObject res = NetworkUtils.getResponseFromHttpUrl(url);
            parseTracksJSON(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getSongHistoryIdsUnsafe(String ids) {
        JSONObject res = NetworkUtils.getTracks( ids );
        parseTracksIdsJSON(res);
    }

    private void getSongHistory() {
        URL url;
        if(query == null)
            url = NetworkUtils.buildRandom("track", this.limit);
        else
            url = NetworkUtils.buildUrlSearch(query, "track");
        new AsyncTask<URL, Void, Void>(){
            @Override
            protected void onPreExecute(){
                contentList.getSpinner().setVisibility(View.VISIBLE);
            }
            @Override
            protected Void doInBackground(URL... params) {
                URL searchUrl = params[0];
                try {
                    JSONObject res = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                    parseTracksJSON(res);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                contentList.notifyDataSetChanged();
                contentList.getSpinner().setVisibility(View.GONE);
            }
        }.execute(url);
    }

    private void parseArtistsTracksJSON(JSONObject obj) {
        try {
            JSONArray arr = obj.getJSONArray("tracks");
            for(int i=0; i<arr.length(); i++) {
                obj = arr.getJSONObject(i);

                Song song = extractSongInformation(obj);
                Album album = extractAlbumInformation(obj);
                addArtists(obj, song);
                song.setAlbum(album);

                addSongToResult(song);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseAlbumTracksJSON(JSONObject obj, Album album) {
        try {
            JSONArray arr = obj.getJSONArray("items");
            for(int i=0; i<arr.length(); i++) {
                obj = arr.getJSONObject(i);

                Song song = extractSongInformation(obj);
                addArtists(obj, song);
                song.setAlbum(album);

                addSongToResult(song);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addArtists(JSONObject obj, Song song) throws JSONException {
        JSONArray artists = obj.getJSONArray("artists");
        for(int k=0; k<artists.length(); k++) {
            JSONObject bb = artists.getJSONObject(k);
            String bbName = bb.getString("name");
            String bbId = bb.getString("id");
            Artist artist = new Artist(bbName, bbId);
            song.addArtist(artist);
        }
    }

    private Song extractSongInformation(JSONObject obj) throws JSONException {
        long duration = (obj.getLong("duration_ms"));
        String songName = obj.getString("name");
        String songId = obj.getString("id");
        return new Song(songName, songId, duration);
    }

    private void parseTracksJSON(JSONObject obj) {
        try {
            JSONArray arr = obj.getJSONObject("tracks").getJSONArray("items");
            for(int i=0; i<arr.length(); i++) {
                obj = arr.getJSONObject(i);

                Album album = extractAlbumInformation(obj);
                Song song = extractSongInformation(obj);

                addArtists(obj, song);

                song.setAlbum(album);
                album.downloadImage();
                addSongToResult(song);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void parseTracksIdsJSON(JSONObject obj) {
        try {
            JSONArray arr = obj.getJSONArray("tracks");
            for(int i=0; i<arr.length(); i++) {
                obj = arr.getJSONObject(i);

                Album album = extractAlbumInformation(obj);
                Song song = extractSongInformation(obj);

                addArtists(obj, song);

                song.setAlbum(album);
                album.downloadImage();
                addSongToResult(song);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Album extractAlbumInformation(JSONObject obj) throws JSONException {
        JSONObject a = obj.getJSONObject("album");
        String aName = a.getString("name");
        String aId = a.getString("id");
        String aUrl = a.getJSONArray("images").getJSONObject(0).getString("url");
        return new Album(aName, aId, aUrl);
    }

    ArrayList<Content> getSongs() {
        return songs;
    }

    void addSongs(int amount){
        this.limit = amount;
        getSongHistory();
    }

    int getSize(){
        return songs.size();
    }
}
