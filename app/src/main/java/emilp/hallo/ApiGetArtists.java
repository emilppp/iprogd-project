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

class ApiGetArtists {

    private ContentList contentList;
    private ArrayList<Content> songs = new ArrayList<>();
    private String query = null;

    /**
     * Using this constructor, random results will be returned
     */
    ApiGetArtists(ContentList contentList) {
        this.contentList = contentList;

        contentList.init(songs);
        getSongHistory();
    }

    /**
     * Using this constructor, results will be based on the query
     */
    ApiGetArtists(ContentList contentList, String query) {
        this.contentList = contentList;
        this.query = query;

        contentList.init(songs);
        getSongHistory();
    }

    private void addSongToResult(Content song) {
        songs.add(song);
    }

    private void getSongHistory() {
        URL url;
        if(query == null)
            url = NetworkUtils.buildRandom("artist", 3);
        else
            url = NetworkUtils.buildUrlSearch(query, "artist");
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
                contentList.getSpinner().setVisibility(View.GONE);
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
