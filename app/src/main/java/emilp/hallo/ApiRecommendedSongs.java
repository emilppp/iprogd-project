package emilp.hallo;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import emilp.hallo.view.ContentList;

public class ApiRecommendedSongs {

    private ContentList contentList;
    private GlobalApplication global;
    private ArrayList<Content> songs = new ArrayList<>();


    public ApiRecommendedSongs(ContentList contentList, GlobalApplication global) {
        this.contentList = contentList;
        this.global = global;

        contentList.init(songs);
        getSongHistory();
    }

    private void addSongToResult(Song song) {
        songs.add(song);
    }

    private void getSongHistory() {
        URL url = NetworkUtils.buildRandom("track", 10);
        new AsyncTask<URL, Void, Void>(){
            @Override
            protected Void doInBackground(URL... params) {
                URL searchUrl = params[0];
                JSONObject res = null;
                try {
                    res = NetworkUtils.getResponseFromHttpUrl(searchUrl);
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

    private void parseTracksJSON(JSONObject obj) {
        try {
            JSONArray arr = obj.getJSONObject("tracks").getJSONArray("items");
            for(int i=0; i<arr.length(); i++) {
                obj = arr.getJSONObject(i);
                JSONObject a = obj.getJSONObject("album");
                String aName = a.getString("name");
                String aId = a.getString("id");
                String aUrl = a.getJSONArray("images").getJSONObject(0).getString("url");
                Album album = new Album(aName, aId, aUrl);

                int duration = (int) (obj.getLong("duration_ms") / 1000);
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
}
