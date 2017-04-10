package emilp.hallo;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import emilp.hallo.view.ContentList;

public class ApiGetSongs {

    private ContentList contentList;
    private ArrayList<Content> songs = new ArrayList<>();
    private String query = null;

    /**
     * Using this constructor, random results will be returned
     * @param contentList
     */
    public ApiGetSongs(ContentList contentList) {
        this.contentList = contentList;

        contentList.init(songs);
        getSongHistory();
    }

    /**
     * Using this constructor, results will be based on the query
     * @param contentList
     */
    public ApiGetSongs(ContentList contentList, String query) {
        this.contentList = contentList;
        this.query = query;

        contentList.init(songs);
        getSongHistory();
    }

    public ApiGetSongs(ContentList contentList, Album album) {
        this.contentList = contentList;

        contentList.init(songs);
        getSongsFromAlbum(album);
    }

    /**
     * Get's the most popular songs from the supplied artist
     * @param contentList
     * @param artist
     */
    public ApiGetSongs(ContentList contentList, Artist artist) {
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
                    parseAristsTracksJSON(res, artist);
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

    private void getSongHistory() {
        URL url;
        if(query == null)
            url = NetworkUtils.buildRandom("track", 3);
        else
            url = NetworkUtils.buildUrlSearch(query, "track");
        new AsyncTask<URL, Void, Void>(){
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
            }
        }.execute(url);
    }

    private void parseAristsTracksJSON(JSONObject obj, Artist artist) {
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
        int duration = (int) (obj.getLong("duration_ms") / 1000);
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

    private Album extractAlbumInformation(JSONObject obj) throws JSONException {
        JSONObject a = obj.getJSONObject("album");
        String aName = a.getString("name");
        String aId = a.getString("id");
        String aUrl = a.getJSONArray("images").getJSONObject(0).getString("url");
        return new Album(aName, aId, aUrl);
    }
}
