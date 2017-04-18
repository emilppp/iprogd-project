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

import emilp.hallo.view.ContentList;
import emilp.hallo.view.MoreOptions;

public class ArtistPage extends AppCompatActivity {

    Artist currentArtist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_page);

        final GlobalApplication global = (GlobalApplication) getApplication();

        currentArtist = (Artist) global.getCurrentContent();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.artist_page_toolbar);
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
        ContentList c = new ContentList(this, R.id.popular_songs, LinearLayoutManager.VERTICAL){
            @Override
            protected void onItemClick(View view, Content content) {
                super.onItemClick(view, content);
                global.getSpotifyService().playSong(global, (Song) content);
            }
            @Override
            protected void onSecondItemClick(View view, Content content) {
                Song song = (Song) content;
                new MoreOptions(ArtistPage.this, song);
            }
        };
        TextView tvArtistName = (TextView) findViewById(R.id.artist_name);
        ImageView ivArtistBanner = (ImageView) findViewById(R.id.artist_banner);

        if(tvArtistName != null)
            tvArtistName.setText(currentArtist.getName());
        if(currentArtist.getImage() != null && ivArtistBanner != null)
            ivArtistBanner.setImageBitmap(currentArtist.getImage());
        else if(ivArtistBanner != null)
            ivArtistBanner.setImageResource(currentArtist.fallbackImage());

        new ApiGetSongs(c, currentArtist);
        c.setTitle(R.string.popular_songs);

        ContentList c2 = new ContentList(this, R.id.album_list, LinearLayoutManager.VERTICAL, R.layout.list_item_album){
            @Override
            protected void onItemClick(View view, Content content) {
                super.onItemClick(view, content);
                ((GlobalApplication) getApplication()).setCurrentContent(content);
                Intent intent = new Intent(getApplicationContext(), AlbumPage.class);
                startActivity(intent);
            }
        };
        new ApiGetAlbums(c2, currentArtist);
        c2.setTitle(R.string.albums);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        return true;
    }
}