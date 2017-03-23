package emilp.hallo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((GlobalApplication) getApplication()).getSpotifyService().playSong("spotify:track:2MxcYeVuWGzFP7ybXcMRJH");
    }
}