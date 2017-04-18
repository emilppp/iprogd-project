package  emilp.hallo;
import android.app.Activity;
import android.os.AsyncTask;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SpotifyQueryTask extends AsyncTask<URL, Void, JSONObject> {
    private Activity act;
    private String token = null;
    private String type = "get";
    private ArrayList<Song> tracks;
    String track = null;

    public SpotifyQueryTask(Activity activity) {
        act = activity;
    }

    public SpotifyQueryTask(Activity activity, String token) {
        act = activity;
        this.token = token;
        this.type = "get";
    }

    public SpotifyQueryTask(Activity activity, String token, String type, String track) {
        act = activity;
        this.token = token;
        this.type = type;
        this.track = track;
    }

    public SpotifyQueryTask(Activity activity, String token, ArrayList<Song> arr) {
        act = activity;
        this.token = token;
        this.type = "playlist";
        this.tracks = arr;
    }



    @Override
    protected JSONObject doInBackground(URL... params) {
        URL searchUrl = params[0];
        JSONObject spotifyResults = null;

        try {

            switch (type) {
                case "get":
                    spotifyResults = NetworkUtils.getResponseFromHttpUrl(searchUrl, token);
                    break;
                case "post":
                    spotifyResults = NetworkUtils.getResponseFromPostHttpUrl(searchUrl, token);
                    break;
                case "playlist":
                    spotifyResults = NetworkUtils.getResponseFromAddToPlaylist(searchUrl, token, tracks);
                    break;
                case "delete":
                    spotifyResults = NetworkUtils.getResponseFromDeleteFromPlaylist(searchUrl, token, track);
                default:
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        type = "";
        return spotifyResults;
    }

    @Override
    protected void onPostExecute(JSONObject res) {
    }
}