package emilp.hallo;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import emilp.hallo.view.ContentList;
import emilp.hallo.view.Loader;

public class ApiSongHistory {

    private ContentList contentList;
    private GlobalApplication global;
    private ArrayList<Content> songs = new ArrayList<>();
    private Loader spinner;

    public ApiSongHistory(ContentList contentList, GlobalApplication global) {
        this.contentList = contentList;
        this.global = global;
        //this.spinner = new Loader(new Activity());

        contentList.init(songs);
        getSongHistory();
    }

    private void addSongToResult(Song song) {
        songs.add(song);
        // Has to be done on main thread, lol
        //contentList.notifyDataSetChanged();
    }

    private void getSongHistory() {
        URL url = NetworkUtils.buildUrlHistory();
        final String token = global.getSpotifyService().getAccessToken();
        new AsyncTask<URL, Void, Void>(){
            @Override
            protected void onPreExecute(){
                LinearLayout spinner = contentList.getSpinner();
                spinner.setVisibility(View.VISIBLE);
            }
            @Override
            protected Void doInBackground(URL... params) {
                URL searchUrl = params[0];
                JSONObject res = null;
                try {
                    res = NetworkUtils.getResponseFromHttpUrl(searchUrl, token);
                    if(res != null) {
                        String tracks = parseHistoryJSON(res);
                        res = NetworkUtils.getTracks(tracks);
                        parseTracksJSON(res);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                contentList.notifyDataSetChanged();
                LinearLayout spinner = contentList.getSpinner();
                spinner.setVisibility(View.GONE);
            }
        }.execute(url);
    }

    private void parseTracksJSON(JSONObject obj) {
        try {
            JSONArray arr = obj.getJSONArray("tracks");
            for(int i=0; i<arr.length(); i++) {
                obj = arr.getJSONObject(i);
                JSONObject a = obj.getJSONObject("album");
                String aName = a.getString("name");
                String aId = a.getString("id");
                String aUrl = a.getJSONArray("images").getJSONObject(0).getString("url");
                Album album = new Album(aName, aId, aUrl);

                long duration = (obj.getLong("duration_ms"));
                String songName = obj.getString("name");
                String songId = obj.getString("id");
                Song song = new Song(songName, songId, duration);
                
                JSONArray artists = obj.getJSONArray("artists");
                for(int k=0; k<artists.length(); k++) {
                    JSONObject bb = artists.getJSONObject(k);
                    String bbName = bb.getString("name");
                    String bbId = bb.getString("id");
                    Artist artist = new Artist(bbName, bbId);
                    song.addArtist(artist);
                }

                song.setAlbum(album);
                album.downloadImage();
                addSongToResult(song);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String parseHistoryJSON(JSONObject res) {
        String songUrl = "";
        try {
            JSONArray arr = res.getJSONArray("items");
            for(int i=0; i<arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i).getJSONObject("track");
                String name = obj.getString("name");
                String id = obj.getString("id");
                long duration = obj.getLong("duration_ms");

                if(i < arr.length()-1)
                    songUrl += id + ",";
                else
                    songUrl += id;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return songUrl;
    }
}
