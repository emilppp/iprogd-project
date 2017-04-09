package emilp.hallo;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import emilp.hallo.view.ContentList;

public class ApiRecommendedAlbums {

    private ContentList contentList;
    private GlobalApplication global;
    private ArrayList<Content> songs = new ArrayList<>();


    public ApiRecommendedAlbums(ContentList contentList, GlobalApplication global) {
        this.contentList = contentList;
        this.global = global;

        contentList.init(songs);
        getSongHistory();
    }

    private void addSongToResult(Content song) {
        songs.add(song);
    }

    private void getSongHistory() {
        URL url = NetworkUtils.buildRandom("album", 10);
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
            JSONArray arr = obj.getJSONObject("albums").getJSONArray("items");
            for(int i=0; i<arr.length(); i++) {
                obj = arr.getJSONObject(i);
                String aName = obj.getString("name");
                String aId = obj.getString("id");
                String aUrl = obj.getJSONArray("images").getJSONObject(0).getString("url");
                Album album = new Album(aName, aId, aUrl);
                album.downloadImage();

                JSONArray artists = obj.getJSONArray("artists");
                for(int k=0; k<artists.length(); k++) {
                    JSONObject b = artists.getJSONObject(k);
                    String bName = b.getString("name");
                    String bId = b.getString("id");
                    Artist artist = new Artist(bName, bId);
                    album.addArtists(artist);
                }

                addSongToResult(album);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
