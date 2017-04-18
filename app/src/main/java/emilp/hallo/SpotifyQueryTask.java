package  emilp.hallo;

import android.os.AsyncTask;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;

class SpotifyQueryTask extends AsyncTask<URL, Void, JSONObject> {

    private String token = null;
    private String type = "get";
    private String track = null;

    SpotifyQueryTask(String token, String type, String track) {
        this.token = token;
        this.type = type;
        this.track = track;
    }

    SpotifyQueryTask(String token) {
        this.token = token;
        this.type = "playlist";
    }

    @Override
    protected JSONObject doInBackground(URL... params) {
        URL searchUrl = params[0];
        JSONObject spotifyResults = null;

        try {

            switch (type) {
                case "get":
                    spotifyResults = NetworkUtils.getResponseFromHttpUrl(searchUrl, token);
                    break;
                case "post":
                    spotifyResults = NetworkUtils.getResponseFromPostHttpUrl(searchUrl, token);
                    break;
                case "playlist":
                    spotifyResults = NetworkUtils.getResponseFromAddToPlaylist(searchUrl, token);
                    break;
                case "delete":
                    spotifyResults = NetworkUtils.getResponseFromDeleteFromPlaylist(searchUrl, token, track);
                default:
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        type = "";
        return spotifyResults;
    }

    @Override
    protected void onPostExecute(JSONObject res) {
    }
}