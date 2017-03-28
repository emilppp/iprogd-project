package emilp.hallo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // initActionbar();

        loadSongHistory();

        loadRecommended();

        //((GlobalApplication) getApplication()).getSpotifyService().playSong("spotify:track:2MxcYeVuWGzFP7ybXcMRJH");

    }

    private void loadRecommended() {
        loadRecommendedAlbums();
        loadRecommenedeSongs();
    }

    private void loadRecommenedeSongs() {
        TextView textView = (TextView) findViewById(R.id.song_recommendations).findViewById(R.id.list_horizontal_title);
        textView.setText(R.string.recommendations_songs);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.song_recommendations).findViewById(R.id.song_history_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        final MenuAdapter adapter = new MenuAdapter(this, ((GlobalApplication) getApplication()).getRecommendedAlbums());
        recyclerView.setAdapter(adapter);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        return true;
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