package emilp.hallo;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // initActionbar();

        loadSongHistory();

        loadRecommended();

        ((GlobalApplication) getApplication()).getSpotifyService().makeSearchQuery("lean", "artist");


    }

    private void loadRecommended() {
        loadRecommendedAlbums();
        loadRecommendedSongs();

    }

    private void loadRecommendedSongs() {
        TextView textView = (TextView) findViewById(R.id.song_recommendations).findViewById(R.id.list_horizontal_title);
        textView.setText(R.string.recommendations_songs);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.song_recommendations).findViewById(R.id.song_history_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        final MenuAdapter adapter = new MenuAdapter(this, ((GlobalApplication) getApplication()).getRecommendedAlbums()) {
            @Override
            protected int getLayoutId() {
                return R.layout.list_item_song;
            }
            @Override
            protected String getArtistText() {
                return getInformationText();
            }
        };
        recyclerView.setAdapter(adapter);

        // ((GlobalApplication) getApplication()).getSpotifyService().playSong("spotify:track:3WTz4svCL6ouAD7E9AEWXL");

      /*  try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        ((GlobalApplication) getApplication()).getSpotifyService().pauseSong();

        try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        ((GlobalApplication) getApplication()).getSpotifyService().resumeSong();
        try {
            Thread.sleep(2000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
*/

    }

    private void loadRecommendedAlbums() {
        TextView textView = (TextView) findViewById(R.id.album_recommendations).findViewById(R.id.list_horizontal_title);
        textView.setText(R.string.recommendations_albums);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.album_recommendations).findViewById(R.id.song_history_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        final MenuAdapter adapter = new MenuAdapter(this, ((GlobalApplication) getApplication()).getRecommendedAlbums());
        recyclerView.setAdapter(adapter);
    }

    private void loadSongHistory() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.song_history).findViewById(R.id.song_history_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        final MenuAdapter adapter = new MenuAdapter(this, ((GlobalApplication) getApplication()).getSongHistory());
        recyclerView.setAdapter(adapter);


    }

    /* SEARCH SHIT ICON SHIT STUFF */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        return true;



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        if(menuItemThatWasSelected == R.id.action_search) {
            Context context = MainActivity.this;
            String message = "Search clicked";
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
/*
    private void initActionbar() {
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {

            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);

            View mCustomView = mInflater.inflate(R.layout.actionbar, null);

            mActionBar.setDisplayHomeAsUpEnabled(false);
            Toolbar toolbar = (Toolbar) mCustomView.findViewById(R.id.toolbar);
            SearchView searchView = (SearchView) mCustomView.findViewById(R.id.toolbar_search);

            DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
            ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_content_desc, R.string.close_content_desc) {

                public void onDrawerClosed(View view)
                {
                    supportInvalidateOptionsMenu();
                    //drawerOpened = false;
                }

                public void onDrawerOpened(View drawerView)
                {
                    supportInvalidateOptionsMenu();
                    //drawerOpened = true;
                }
            };

            mDrawerToggle.setDrawerIndicatorEnabled(true);
            drawerLayout.setDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();

            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
        }
    }
    */
}