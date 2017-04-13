package emilp.hallo;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by jonas on 2017-04-13.
 */

public class PlaylistGenerator extends AsyncTask<Void, Void, Void> {

    public static final int AMOUNT_OF_SONGS_FOR_INITIAL_PLAYLIST = 25;

    private Activity activity;

    public PlaylistGenerator(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        GlobalApplication global = (GlobalApplication) activity.getApplication();

        global.createPlaylist();
        System.out.println("Hej skapar spellista var är den");

        ApiGetSongs api = new ApiGetSongs(AMOUNT_OF_SONGS_FOR_INITIAL_PLAYLIST);
        for(Content c : api.getSongs())
            global.addToPlaylist((Song) c);

        global.postPlaylist();

        return null;
    }
}
