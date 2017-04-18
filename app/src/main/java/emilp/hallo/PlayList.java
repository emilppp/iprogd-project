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
import android.widget.SearchView;

import emilp.hallo.view.ContentList;
import emilp.hallo.view.MoreOptions;

public class PlayList extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);

        final GlobalApplication global = (GlobalApplication) getApplication();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.playlist_page_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            if (myToolbar != null) {
                myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
        }

        ContentList contentList = new ContentList(this, R.id.playlist, LinearLayoutManager.VERTICAL) {
            @Override
            protected void onItemClick(View view, Content content) {
                super.onItemClick(view, content);
                global.getSpotifyService().playSong(global, (Song) content);
            }

            @Override
            protected void onSecondItemClick(View view, Content content) {
                super.onSecondItemClick(view, content);
                new MoreOptions(PlayList.this, content);
            }
        };
        contentList.setTitle(R.string.playlist);
        contentList.init(global.getSongsToBeAddedAsContent());
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
