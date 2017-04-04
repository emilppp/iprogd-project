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

import emilp.hallo.view.ContentList;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // initActionbar();

        loadSongHistory();

        loadRecommended();


    }

    private void loadRecommended() {
        loadRecommendedAlbums();
        loadRecommendedSongs();

    }

    private void loadRecommendedSongs() {
        Object[] data = ((GlobalApplication) getApplication()).getRecommendedAlbums();
        ContentList contentList = new ContentList(this, R.id.song_recommendations, LinearLayoutManager.VERTICAL, data);
        contentList.setTitle(R.string.recommendations_songs);
    }

    private void loadRecommendedAlbums() {
        Object[] data = ((GlobalApplication) getApplication()).getRecommendedAlbums();
        ContentList contentList = new ContentList(this, R.id.album_recommendations, LinearLayoutManager.HORIZONTAL, data);
        contentList.setTitle(R.string.recommendations_albums);
    }

    private void loadSongHistory() {
        Object[] data = ((GlobalApplication) getApplication()).getSongHistory();
        ContentList contentList = new ContentList(this, R.id.song_history, LinearLayoutManager.HORIZONTAL, data);
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
}