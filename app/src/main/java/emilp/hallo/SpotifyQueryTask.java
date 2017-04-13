package  emilp.hallo;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import emilp.hallo.NetworkUtils;
import emilp.hallo.view.ContentList;

public class SpotifyQueryTask extends AsyncTask<URL, Void, JSONObject> {
    private Activity act;
    private String token = null;
    private String type = "get";
    private ArrayList<Song> tracks;

    public SpotifyQueryTask(Activity activity) {
        act = activity;
    }

    public SpotifyQueryTask(Activity activity, String token) {
        act = activity;
        this.token = token;
        this.type = "get";
    }
    public SpotifyQueryTask() {
    }
    public SpotifyQueryTask(Activity activity, String token, String type) {
        act = activity;
        this.token = token;
        this.type = type;
    }

    public SpotifyQueryTask(Activity activity, String token, ArrayList<Song> arr) {
        act = activity;
        this.token = token;
        this.type = "playlist";
        this.tracks = arr;
    }





    // superful lösning med POST ist för GET
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
                    spotifyResults = NetworkUtils.getResponseFromAddToPlaylist(searchUrl, token, tracks);
                    break;
                case "delete":
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