package emilp.hallo;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import emilp.hallo.view.ContentList;
import emilp.hallo.view.CurrentlyPlaying;
import emilp.hallo.view.Loader;
import emilp.hallo.view.MoreOptions;

public class MainActivity extends AppCompatActivity {
    ActionBarDrawerToggle mDrawerToggle;

    ApiGetSongs songRec;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GlobalApplication global = (GlobalApplication) getApplication();

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
            if(drawerLayout != null)
                drawerLayout.addDrawerListener(mDrawerToggle);
            setDrawerInfo();
            mDrawerToggle.syncState();
        }

        loadSongHistory();

        loadRecommended();

        Button btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        if (btnSignOut != null) {
            btnSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // It does not appear as if it's possible to sign out unless using the
                    // webservice to sign in, which we are not using.
                    // So this will do Donkey, this will do.
                    System.exit(0);

                    /*global.logOut();
                    Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    startActivity(intent);
                    MainActivity.this.finish();*/
                }
            });
        }

        Button btnCreatePlaylist = (Button) findViewById(R.id.btn_create_playlist);
        if(global.getPlaylistID() != null && btnCreatePlaylist != null) {
            btnCreatePlaylist.setText(R.string.view_playlist);
        }
        if (btnCreatePlaylist != null) {
            btnCreatePlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
        }

        new CurrentlyPlaying(this);

        final ScrollView scrollView = (ScrollView) findViewById(R.id.main_scroll);
        if (scrollView != null) {
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    if (scrollView.getChildAt(0).getBottom() <= (scrollView.getHeight() + scrollView.getScrollY())) {
                        if(songRec.getSize() < 50) {
                            songRec.addSongs(5);
                        }
                    }
                }
            });
        }

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        if (srl != null) {
            srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
                @Override
                public void onRefresh(){
                      srl.setRefreshing(true);
                      loadRecommended();
                      srl.setRefreshing(false);
                }
            });
        }
        global.initPlayer(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button btnCreatePlaylist = (Button) findViewById(R.id.btn_create_playlist);
        if(((GlobalApplication) getApplication()).getPlaylistID() != null && btnCreatePlaylist != null)
            btnCreatePlaylist.setText(R.string.view_playlist);
        else if(btnCreatePlaylist != null)
            btnCreatePlaylist.setText(R.string.create_playlist);
    }

    /**
     * Loads all the recommended views with data
     */
    private void loadRecommended() {
        loadRecommendedArtists();
        loadRecommendedAlbums();
        loadRecommendedSongs();
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

    /**
     * Loads recommended songs
     */
    private void loadRecommendedSongs() {
        final GlobalApplication global = (GlobalApplication) getApplication();
        final ContentList contentList = new ContentList(this, R.id.song_recommendations, LinearLayoutManager.VERTICAL) {
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
        songRec = new ApiGetSongs(contentList, 20);
        contentList.setTitle(R.string.recommendations_songs);
    }

    /**
     * Loads recommended artists
     */
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
    }

    /**
     * Loads recommended albums
     */
    private void loadRecommendedAlbums() {
        ContentList contentList = new ContentList(this, R.id.album_recommendations, LinearLayoutManager.HORIZONTAL) {
            @Override
            protected void onItemClick(View view, Content content) {
                ((GlobalApplication) getApplication()).setCurrentContent(content);
                Intent intent = new Intent(getApplicationContext(), AlbumPage.class);
                startActivity(intent);
            }
        };
        contentList.setTitle(R.string.recommendations_albums);
        new ApiGetAlbums(contentList, 10);
    }

    /**
     * Loads the last played track from the users spotify account
     */
    private void loadSongHistory() {
        ContentList contentList = new ContentList(this, R.id.song_history, LinearLayoutManager.HORIZONTAL) {
            @Override
            protected void onItemClick(View view, Content content) {
                new MoreOptions(MainActivity.this, content);
            }
        };
        ((GlobalApplication) getApplication()).getSongHistory(contentList);
        contentList.setTitle(R.string.song_history);
    }

    /**
     * Set up the drawer which appears when you click the hamburger icon in the top left
     * It contains information about the currently logged in user.
     */
    private void setDrawerInfo() {
        String userName = ((GlobalApplication) getApplication()).getDisplayName();
        String realName = ((GlobalApplication) getApplication()).getClientID();
        Bitmap pic = ((GlobalApplication) getApplication()).getProfilePicture();
        TextView mDrawerUserName = (TextView) findViewById(R.id.user_name);
        TextView mDrawerRealName = (TextView) findViewById(R.id.real_name);
        ImageView mDrawerProfilePic = (ImageView) findViewById(R.id.profile_pic);
        if(userName != null && mDrawerUserName != null && !userName.equals("null")) {
            mDrawerUserName.setText(userName);
        } else if(mDrawerUserName != null) {
            mDrawerUserName.setVisibility(View.GONE);
        }
        if(realName != null && mDrawerRealName != null && !realName.equals("null")) {
            mDrawerRealName.setText(realName);
        } else if(mDrawerRealName != null) {
            mDrawerRealName.setVisibility(View.GONE);
        }
        if(pic != null && mDrawerProfilePic != null) {
            mDrawerProfilePic.setImageBitmap(pic);
        }
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

    /**
     * Might be unnecessary but we had to override this and the function above to use androids search service
     */
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

        return id == R.id.logo || super.onOptionsItemSelected(item);

    }
}