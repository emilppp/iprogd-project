package emilp.hallo;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

/**
 * Created by kenneth on 4/5/17.
 */

public class ArtistPage extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_page);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.artist_page_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.drawable.icon_naked2);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

}
