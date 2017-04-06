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
import android.view.View;
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

        ((GlobalApplication) getApplication()).clearResList();

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("SearchResultsActivity", query);

            URL searchUrl = NetworkUtils.buildUrlSearch(query, "artist");
            new SpotifyQueryTask(this){
                @Override
                protected void onPostExecute(JSONObject githubSearchResults) {
                    if (githubSearchResults != null && !githubSearchResults.equals("")) {
                        Log.d("SpotifyQueryTask", "Fann" + githubSearchResults);

                        final GlobalApplication global = (GlobalApplication) getApplication();
                        SpotifyService.parseSearchJSON(githubSearchResults, global);

                        Object[] artists = global.getSearchRes().toArray();

                        new AsyncTask<Object, Void, Void>() {
                            @Override
                            protected Void doInBackground(Object[] params) {
                                for (Object o : params)
                                    ((Artist) o).downloadImage();
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);

                                ContentList contentList = new ContentList(SearchResultsActivity.this, R.id.search_results, LinearLayoutManager.VERTICAL) {
                                    @Override
                                    protected void onItemClick(View view, Content content) {
                                        Artist art = (Artist) content;
                                        ((GlobalApplication) getApplication()).setCurrentArtist(art);
                                        Intent intent = new Intent(SearchResultsActivity.this, ArtistPage.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    protected void onSecondItemClick(View view, Content content) {

                                    }
                                };
                                contentList.setTitle(R.string.artists);
                                contentList.init(global.getSearchRes());
                            }
                        }.execute(artists);
                    }
                }
            }.execute(searchUrl);
        }
    }
}
