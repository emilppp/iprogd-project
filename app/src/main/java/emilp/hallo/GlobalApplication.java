package emilp.hallo;

import android.app.Application;
import android.content.Context;

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
}
