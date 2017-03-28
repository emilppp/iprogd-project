package emilp.hallo;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by jonas on 2017-03-23.
 */

public class GlobalApplication extends Application {

    private SpotifyService spotifyService = new SpotifyService();

    public SpotifyService getSpotifyService() {
        return spotifyService;
    }

    public Object[] getSongHistory() {
        return spotifyService.getSongHistory().toArray();
    }

    public Object[] getRecommendedAlbums() {
        ArrayList<Song> arr = new ArrayList<>();
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        arr.add(new Song());
        return arr.toArray();
    }
}
