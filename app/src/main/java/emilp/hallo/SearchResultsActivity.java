package emilp.hallo;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;

import emilp.hallo.view.ContentList;
import emilp.hallo.view.MoreOptions;

/**
 * This activity displays the search results
 */
public class SearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();
        handleIntent(intent);
    }

    private ContentList getContentList(int id, int direction, final Class<?> c) {
        return new ContentList(SearchResultsActivity.this, id, direction) {
            @Override
            protected void onItemClick(View view, Content content) {
                ((GlobalApplication) getApplication()).setCurrentContent(content);
                Intent intent = new Intent(SearchResultsActivity.this, c);
                startActivity(intent);
            }
        };
    }

    /**
     * Searches songs based on the given query
     */
    private void searchSongs(String query) {
        ContentList contentList = new ContentList(SearchResultsActivity.this, R.id.search_results_songs, LinearLayoutManager.VERTICAL) {
            @Override
            protected void onItemClick(View view, Content content) {
                super.onItemClick(view, content);
                GlobalApplication global = (GlobalApplication) getApplication();
                global.getSpotifyService().playSong(global, (Song) content);
            }

            @Override
            protected void onSecondItemClick(View view, Content content) {
                super.onSecondItemClick(view, content);
                new MoreOptions(SearchResultsActivity.this, content);
            }
        };
        contentList.setTitle(R.string.songs);
        // TODO: Song view?
        new ApiGetSongs(contentList, query);
    }

    /**
     * Searches artists based on the given query
     */
    private void searchArtists(String query) {
        ContentList contentList = getContentList(R.id.search_results_artists, LinearLayoutManager.HORIZONTAL, ArtistPage.class);
        contentList.setTitle(R.string.artists);
        new ApiGetArtists(contentList, query);
    }

    /**
     * Searches albums based on the given query
     */
    private void searchAlbums(String query) {
        ContentList contentList = getContentList(R.id.search_results_albums, LinearLayoutManager.HORIZONTAL, AlbumPage.class);
        contentList.setTitle(R.string.albums);
        new ApiGetAlbums(contentList, query);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("SearchResultsActivity", query);

            searchArtists(query);
            searchAlbums(query);
            searchSongs(query);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            if(myToolbar != null)
                myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        }
    }

    @Override
    protected  void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        return true;
    }
}
