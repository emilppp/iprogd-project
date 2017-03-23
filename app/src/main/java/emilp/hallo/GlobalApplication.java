package emilp.hallo;

import android.app.Application;
import android.content.Context;

/**
 * Created by jonas on 2017-03-23.
 */

public class GlobalApplication extends Application {

    SpotifyService spotifyService = new SpotifyService();

    public SpotifyService getSpotifyService() {
        return spotifyService;
    }
}
