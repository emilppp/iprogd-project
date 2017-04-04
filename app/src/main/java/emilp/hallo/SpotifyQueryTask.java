package  emilp.hallo;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import emilp.hallo.NetworkUtils;

public class SpotifyQueryTask extends AsyncTask<URL, Void, JSONObject> {
    Activity act;
    public SpotifyQueryTask(Activity activity) {
        act = activity;
    }

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

        TextView test = (TextView) act.findViewById(R.id.test_text_view);
        test.setText(githubSearchResults.toString());

    }
}