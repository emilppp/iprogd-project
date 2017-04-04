package  emilp.hallo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import emilp.hallo.NetworkUtils;

public class SpotifyQueryTask extends AsyncTask<URL, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(URL... params) {
        URL searchUrl = params[0];
        JSONObject githubSearchResults = null;
        try {
            githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return githubSearchResults;
    }

    @Override
    protected void onPostExecute(JSONObject githubSearchResults) {
        if (githubSearchResults != null && !githubSearchResults.equals("")) {
            Log.d("SpotifyQueryTask", "Fann" + githubSearchResults);
        }


    }
}