package emilp.hallo;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import emilp.hallo.view.ContentList;

public class ApiRecommendedArtists {

    private ContentList contentList;
    private GlobalApplication global;
    private ArrayList<Content> songs = new ArrayList<>();


    public ApiRecommendedArtists(ContentList contentList, GlobalApplication global) {
        this.contentList = contentList;
        this.global = global;

        contentList.init(songs);
        getSongHistory();
    }

    private void addSongToResult(Content song) {
        songs.add(song);
    }

    private void getSongHistory() {
        URL url = NetworkUtils.buildRandom("artist", 3);
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
            JSONArray arr = obj.getJSONObject("artists").getJSONArray("items");
            for(int i=0; i<arr.length(); i++) {
                obj = arr.getJSONObject(i);
                String aName = obj.getString("name");
                String aId = obj.getString("id");
                String aUrl = null;
                if(obj.getJSONArray("images").length() > 0)
                    aUrl = obj.getJSONArray("images").getJSONObject(0).getString("url");

                Artist artist = new Artist(aName, aId, aUrl);
                artist.downloadImage();

                addSongToResult(artist);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
