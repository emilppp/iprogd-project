package emilp.hallo;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;

import emilp.hallo.view.ContentList;


public class SearchResultsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("SearchResultsActivity", query);

            URL searchUrl = NetworkUtils.buildUrl(query, "artist");
            new SpotifyQueryTask(this){
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
                                for (Object o : params)
                                    ((Artist) o).downloadImage();
                                System.out.println(((Artist) params[0]).getImage());
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);

                                ContentList contentList = new ContentList(act, R.id.search_results, LinearLayoutManager.VERTICAL, global.getSearchRes().toArray());
                                contentList.setTitle(R.string.artists);
                            }
                        }.execute(artists);
                    }
                }
            }.execute(searchUrl);
        }
    }
}
