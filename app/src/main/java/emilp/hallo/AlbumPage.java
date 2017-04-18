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

import emilp.hallo.view.ContentList;
import emilp.hallo.view.MoreOptions;

public class AlbumPage extends AppCompatActivity {

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
            if(myToolbar != null)
                myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        TextView tvAlbumName = (TextView) findViewById(R.id.album_name);
        ImageView ivAlbumCover = (ImageView) findViewById(R.id.album_cover);

        if(tvAlbumName != null)
            tvAlbumName.setText(currentAlbum.getName());
        if (currentAlbum.getImage() != null && ivAlbumCover != null)
            ivAlbumCover.setImageBitmap(currentAlbum.getImage());
        else if(ivAlbumCover != null)
            ivAlbumCover.setImageResource(currentAlbum.fallbackImage());

        ContentList c = new ContentList(this, R.id.album_songs, LinearLayoutManager.VERTICAL, R.layout.album_song_item) {
            @Override
            protected void onItemClick(View view, Content content) {
                global.getSpotifyService().playSong(global, (Song) content);
            }
            @Override
            protected void onSecondItemClick(View view, Content content) {
                Song song = (Song) content;
                new MoreOptions(AlbumPage.this, song);
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
        return true;
    }
}
