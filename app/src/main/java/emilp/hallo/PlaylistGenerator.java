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
        global.clearPlaylist();

        if(global.getPlaylistID() == null) {

            global.resetPlaylist();

            global.createPlaylist();
            System.out.println("Hej skapar spellista var är den");

            ApiGetSongs api = new ApiGetSongs(AMOUNT_OF_SONGS_FOR_INITIAL_PLAYLIST);
            for (Content c : api.getSongs()) {
                global.addToPlaylist((Song) c);
            }

        } else {
            ArrayList<String> ids = global.getPlaylistFromDb();
            StringBuilder builder = new StringBuilder();

            boolean hej = false;
            for(String id : ids) {
                builder.append(id+",");
                hej = true;
            }

            boolean match = false;
            for(int i=0; i<ids.size(); i++) {
                for(int k=i+1; k<ids.size(); k++) {
                    if(ids.get(i).equals(ids.get(k))) {
                        match = true;
                    }
                }
            }
            System.out.println( match ? "Found duplicates" : "No duplicates" );

            if(match) {
                builder = new StringBuilder();
                for(int i=0; i<ids.size(); i++) {
                    boolean dupe = false;
                    for(int k=i+1; k<ids.size(); k++) {
                        if(ids.get(i).equals(ids.get(k))) {
                            dupe = true;
                        }
                    }
                    if(!dupe) {
                        builder.append(ids.get(i)+",");
                        hej = true;
                    }
                }
            }

            if(hej)
                builder.deleteCharAt(builder.length()-1);

            System.out.println("Playlist exsists, fetching: " + builder.toString());

            ApiGetSongs api = new ApiGetSongs( builder.toString() );
            for (Content c : api.getSongs())
                global.addToPlaylist((Song) c);

        }
        return null;
    }
}
