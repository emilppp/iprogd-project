package emilp.hallo;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import org.json.JSONObject;

import java.net.URL;

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

        currentArtist = ((GlobalApplication) getApplication()).getCurrentArtist();


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
                     Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                     startActivity(intent);
                 }
             });
             //getSupportActionBar().setDisplayShowTitleEnabled(true);
             //getSupportActionBar().setLogo(R.drawable.icon_naked2);
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
        URL url =  NetworkUtils.buildUrlArtist(testId);
        new SpotifyQueryTask(this){
            @Override
            protected void onPostExecute(JSONObject githubSearchResults) {
                TextView tvArtistName = (TextView) findViewById(R.id.artist_name);
                ImageView ivArtistBanner = (ImageView) findViewById(R.id.artist_banner);

                tvArtistName.setText(currentArtist.getName());
                ivArtistBanner.setImageBitmap(currentArtist.getImage());

            }
        }.execute(url);
        return true;
    }

}
