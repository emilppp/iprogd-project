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
        global.resetPlaylist();

        if(global.getPlaylistID() == null) {

            global.createPlaylist();
            System.out.println("Hej skapar spellista var är den");

            ApiGetSongs api = new ApiGetSongs(AMOUNT_OF_SONGS_FOR_INITIAL_PLAYLIST);
            for (Content c : api.getSongs())
                global.addToPlaylist((Song) c);

            global.postPlaylist();

        } else {
            ArrayList<String> ids = global.getPlaylistFromDb();
            StringBuilder builder = new StringBuilder();

            boolean hej = false;
            for(String id : ids) {
                builder.append(id+",");
                hej = true;
            }
            if(hej)
                builder.deleteCharAt(builder.length()-1);

            System.out.println("WASDASDASD " + builder.toString());

            ApiGetSongs api = new ApiGetSongs( builder.toString() );
            for (Content c : api.getSongs())
                global.addToPlaylist((Song) c);

            for( Content d : api.getSongs()) {
                Song s = (Song) d;
                global.removeTrackFromPlaylist("spotify:track:" + s.getId());
            }
        }
        return null;
    }
}
