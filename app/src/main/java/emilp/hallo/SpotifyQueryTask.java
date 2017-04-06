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
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import emilp.hallo.NetworkUtils;
import emilp.hallo.view.ContentList;

public class SpotifyQueryTask extends AsyncTask<URL, Void, JSONObject> {
    private Activity act;
    private String token = null;

    public SpotifyQueryTask(Activity activity) {
        act = activity;
    }

    public SpotifyQueryTask(Activity activity, String token) {
        act = activity;
        this.token = token;
    }

    @Override
    protected JSONObject doInBackground(URL... params) {
        URL searchUrl = params[0];
        JSONObject githubSearchResults = null;
        try {
            githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl, token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return githubSearchResults;
    }

    @Override
    protected void onPostExecute(JSONObject res) {
    }
}