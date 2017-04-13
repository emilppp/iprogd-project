package emilp.hallo;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URL;

import emilp.hallo.view.ContentList;

/**
 * Created by kenneth on 4/7/17.
 */

public class AlbumPage extends AppCompatActivity {

    String testId = "0OdUWJ0sBjDrqHygGUXeCF";
    Album currentAlbum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_page);

        final GlobalApplication global = ((GlobalApplication) getApplication());

        currentAlbum = (Album) global.getCurrentContent();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.album_page_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        ContentList c = new ContentList(this, R.id.album_songs, LinearLayoutManager.VERTICAL) {
            @Override
            protected void onItemClick(View view, Content content) {
                global.getSpotifyService().playSong(global, (Song) content);
            }
        };
        c.hideTitle();
        new ApiGetSongs(c, currentAlbum);
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
        URL url = NetworkUtils.buildUrlArtist(testId);
        new SpotifyQueryTask(this) {
            @Override
            protected void onPostExecute(JSONObject spotifySearchresults) {
                TextView tvAlbumName = (TextView) findViewById(R.id.album_name);
                ImageView ivAlbumCover = (ImageView) findViewById(R.id.album_cover);

                tvAlbumName.setText(currentAlbum.getName());
                if (currentAlbum.getImage() != null)
                    ivAlbumCover.setImageBitmap(currentAlbum.getImage());
                else
                    ivAlbumCover.setImageResource(currentAlbum.fallbackImage());
            }
        }.execute(url);
        return true;
    }
}
