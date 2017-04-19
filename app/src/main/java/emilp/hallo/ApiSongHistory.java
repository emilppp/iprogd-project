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

class ApiSongHistory {

    private ContentList contentList;
    private GlobalApplication global;
    private ArrayList<Content> songs = new ArrayList<>();

    ApiSongHistory(ContentList contentList, GlobalApplication global) {
        this.contentList = contentList;
        this.global = global;

        contentList.init(songs);
        getSongHistory();
    }

    private void addSongToResult(Song song) {
        songs.add(song);
    }

    private void getSongHistory() {
        URL url = NetworkUtils.buildUrlHistory();
        final String token = global.getSpotifyService().getAccessToken();
        new AsyncTask<URL, Void, Void>(){
            @Override
            protected void onPreExecute(){
                contentList.getSpinner().setVisibility(View.VISIBLE);
            }
            @Override
            protected Void doInBackground(URL... params) {
                URL searchUrl = params[0];
                try {
                    JSONObject res = NetworkUtils.getResponseFromHttpUrl(searchUrl, token);
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
                contentList.getSpinner().setVisibility(View.GONE);
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
                String aUrl = a.getJSONArray("images").getJSONObject(a.getJSONArray("images").length()-1).getString("url");
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
                String id = obj.getString("id");

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
