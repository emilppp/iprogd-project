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

            //TextView test = (TextView) act.findViewById(R.id.test_text_view);
            //test.setText(githubSearchResults.toString());

            final GlobalApplication global = (GlobalApplication) act.getApplication();
            SpotifyService.parseSearchJSON(githubSearchResults, global);

            Object[] artists = global.getSearchRes().toArray();

            new AsyncTask<Object, Void, Void>() {
                @Override
                protected Void doInBackground(Object[] params) {
                    for(Object o : params)
                        ((Artist) o).downloadImage();
                    System.out.println(((Artist) params[0]).getImage());
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    Toast.makeText(act, "Images done", Toast.LENGTH_SHORT).show();
                    ContentList contentList = new ContentList(act, R.id.search_results, LinearLayoutManager.VERTICAL, global.getSearchRes().toArray());
                }
            }.execute(artists);
            Toast.makeText(act, "Images going", Toast.LENGTH_SHORT).show();
        }
    }
}