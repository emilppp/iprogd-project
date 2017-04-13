package emilp.hallo;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import emilp.hallo.view.ContentList;
import emilp.hallo.view.CurrentlyPlaying;
import emilp.hallo.view.Loader;
import emilp.hallo.view.MoreOptions;


public class MainActivity extends AppCompatActivity {
    ActionBarDrawerToggle mDrawerToggle;

    private Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        GlobalApplication global = (GlobalApplication) getApplication();
        global.fetchClientID();

        System.out.println("Playlist ID: " + global.getPlaylistID());

        setDrawerInfo();

        // initActionbar();
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            //actionBar.setDisplayHomeAsUpEnabled(true);
            mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.open_content_desc, R.string.close_content_desc)
            {

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
            drawerLayout.addDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();
        }

        loadSongHistory();

        loadRecommended();

        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setLogo(R.drawable.icon_naked2);

        Button btnCreatePlaylist = (Button) findViewById(R.id.btn_create_playlist);
        if(global.getPlaylistID() != null) {
            btnCreatePlaylist.setText(R.string.view_playlist);
        }
        btnCreatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Show loading here.

                final Loader loader = new Loader(MainActivity.this);

                new PlaylistGenerator(MainActivity.this) {
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        loader.hide();
                        Intent intent = new Intent(getApplicationContext(), PlayList.class);
                        startActivity(intent);
                    }
                }.execute();
            }
        });

        new CurrentlyPlaying(this);
    }

    private void loadRecommended() {
        loadRecommendedAlbums();
        loadRecommendedSongs();
        loadRecommendedArtists();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void loadRecommendedSongs() {
        final GlobalApplication global = (GlobalApplication) getApplication();
        ContentList contentList = new ContentList(this, R.id.song_recommendations, LinearLayoutManager.VERTICAL) {
            @Override
            protected void onItemClick(View view, Content content) {
                global.getSpotifyService().playSong(global, (Song) content);
            }

            @Override
            protected void onSecondItemClick(View view, Content content) {
                Song song = (Song) content;
                new MoreOptions(MainActivity.this, song);
            }
        };
        new ApiGetSongs(contentList, 20);
        contentList.setTitle(R.string.recommendations_songs);
        contentList.hideAnimation();
    }

    private void loadRecommendedArtists() {
        ContentList contentList = new ContentList(this, R.id.recommended_artists_list, LinearLayoutManager.VERTICAL, R.layout.list_item_big) {
            @Override
            protected void onItemClick(View view, Content content) {
                ((GlobalApplication) getApplication()).setCurrentContent(content);
                Intent intent = new Intent(getApplicationContext(), ArtistPage.class);
                startActivity(intent);
            }
        };
        ((GlobalApplication) getApplication()).getRecommendedArtists(contentList);
        contentList.setTitle(R.string.recommendations_artists);
        contentList.hideAnimation();
    }

    private void loadRecommendedAlbums() {
        ContentList contentList = new ContentList(this, R.id.album_recommendations, LinearLayoutManager.HORIZONTAL) {
            @Override
            protected void onItemClick(View view, Content content) {
                ((GlobalApplication) getApplication()).setCurrentContent(content);
                Intent intent = new Intent(getApplicationContext(), AlbumPage.class);
                startActivity(intent);
            }
        };
        new ApiGetAlbums(contentList, 10);
        contentList.setTitle(R.string.recommendations_albums);
    }

    private void loadSongHistory() {
        ContentList contentList = new ContentList(this, R.id.song_history, LinearLayoutManager.HORIZONTAL) {
            @Override
            protected void onItemClick(View view, Content content) {
                new MoreOptions(MainActivity.this, content);
            }
        };
        ((GlobalApplication) getApplication()).getSongHistory(contentList);
    }

    private void setDrawerInfo() {
        TextView mDrawerUserName = (TextView) findViewById(R.id.user_name);
        TextView mDrawerRealName = (TextView) findViewById(R.id.real_name);
        mDrawerRealName.setText(((GlobalApplication) getApplication()).getDisplayName());
        mDrawerUserName.setText(((GlobalApplication) getApplication()).getClientID());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        if(id == R.id.logo) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}