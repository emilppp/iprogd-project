package emilp.hallo;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import java.util.ArrayList;

import emilp.hallo.view.ContentList;

/**
 * Created by kenneth on 4/5/17.
 */

public class ArtistPage extends AppCompatActivity {

    String testId = "0OdUWJ0sBjDrqHygGUXeCF";
    Artist currentArtist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_page);

        currentArtist = (Artist) ((GlobalApplication) getApplication()).getCurrentContent();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.artist_page_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            //getSupportActionBar().setDisplayShowTitleEnabled(true);
            //getSupportActionBar().setLogo(R.drawable.icon_naked2);
        }
        ArrayList<Content> data = new ArrayList<>();
        data.add(new Song());
        data.add(new Song());
        data.add(new Song());
        data.add(new Song());
        data.add(new Song());

        ContentList c = new ContentList(this, R.id.popular_songs, LinearLayoutManager.VERTICAL){
            @Override
            protected void onItemClick(View view, Content content) {
                super.onItemClick(view, content);
            }
        };
        c.init(data);
        c.setTitle(R.string.popular_songs);

        ArrayList<Content> data2 = new ArrayList<>();
        data2.add(new Album());
        data2.add(new Album());
        data2.add(new Album());

        ContentList c2 = new ContentList(this, R.id.album_list, LinearLayoutManager.VERTICAL, R.layout.list_item_album){
            @Override
            protected void onItemClick(View view, Content content) {
                super.onItemClick(view, content);
                Intent intent = new Intent(getApplicationContext(), AlbumPage.class);
                startActivity(intent);
            }
        };
        c2.init(data2);
        c2.setTitle(R.string.albums);
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
        URL url =  NetworkUtils.buildUrlArtist(testId);
        new SpotifyQueryTask(this){
            @Override
            protected void onPostExecute(JSONObject githubSearchResults) {
                TextView tvArtistName = (TextView) findViewById(R.id.artist_name);
                ImageView ivArtistBanner = (ImageView) findViewById(R.id.artist_banner);

                tvArtistName.setText(currentArtist.getName());
                if(currentArtist.getImage() != null)
                    ivArtistBanner.setImageBitmap(currentArtist.getImage());
                else
                    ivArtistBanner.setImageResource(currentArtist.fallbackImage());
            }
        }.execute(url);
        return true;
    }
}