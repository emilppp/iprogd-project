package emilp.hallo;

import android.app.Activity;
import android.content.Intent;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class SpotifyService extends Activity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "0daad18357ce4eab851f60728f1c5ac1";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "emilp://callback";

    private String accessToken;

    private Player mPlayer;

    public Player getmPlayer() {
        return mPlayer;
    }

    public static final int REQUEST_CODE = 1337;

    private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
            Log.d("SpotidyService", "OK!");
        }

        @Override
        public void onError(Error error) {
            Log.d("SpotifyService", "ERROR:" + error);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void authSpotify(Activity activity) {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming", "user-read-recently-played", "playlist-modify-private"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(activity, REQUEST_CODE, request);
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    public void initiate(int requestCode, int resultCode, Intent intent, final WelcomeActivity activity) {
        // Check if result comes from the correct activity
        if (requestCode == SpotifyService.REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                // Logged in
                accessToken = response.getAccessToken();
                Config playerConfig = new Config(activity.getApplicationContext(), response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, activity.getApplicationContext(), new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(activity);
                        mPlayer.addNotificationCallback(SpotifyService.this);
                        Toast.makeText(activity.getApplicationContext(), "Initiated everything and we're logged in(?)", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    public void playSong(String spotifyUri) {
        mPlayer.playUri(mOperationCallback, spotifyUri, 0, 0);
    }

    public void queueSong(String spotifyUri) {
        mPlayer.queue(mOperationCallback, spotifyUri);
        Toast toast = Toast.makeText(this, "Queued track", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void pauseSong() {
        mPlayer.pause(mOperationCallback);
    }

    public void resumeSong() {
        mPlayer.resume(mOperationCallback);
    }

    public void stop() {
        mPlayer.destroy();
    }

    public Metadata.Track getCurrentTrack() {
        return mPlayer.getMetadata().currentTrack;
    }

    public void logOut() {
        mPlayer.logout();
    }




    @Override
    public void onLoggedIn() {

   }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("MainActivity", "Login failed");

    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    Artist[] arre;
    public ArrayList<Content> getSongHistory(final GlobalApplication global) {
        ArrayList<Content> arr = new ArrayList<>();

        URL url = NetworkUtils.buildUrlHistory();
        new SpotifyQueryTask(this, getAccessToken()){
            @Override
            protected void onPostExecute(JSONObject res) {
                if(res != null) {
                    parseHistoryJSON(res, global);
                }
            }
        }.execute(url);

        return arr;
    }

    public void getClientId(final GlobalApplication global) {

        URL url = NetworkUtils.buildUrlGetSpotifyProfile();
        new SpotifyQueryTask(this, getAccessToken()) {
            @Override
            protected void onPostExecute(JSONObject res) {
                if(res != null) {
                    parseProfileJSON(res, global);

                    System.out.println(global.getClientID());
                    System.out.println(global.getDisplayName());
                }
            }
        }.execute(url);

    }

    public static void parseProfileJSON(JSONObject res, GlobalApplication global) {
        // TODO fixa.
        try {
            global.setClientID(res.getString("id"));
            global.setDisplayName(res.getString("display_name"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void parseHistoryJSON(JSONObject res, GlobalApplication global) {
        try {
            JSONArray arr = res.getJSONArray("items");
            for(int i=0; i<arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i).getJSONObject("track");
                String name = obj.getString("name");
                String id = obj.getString("id");
                int duration = obj.getInt("duration_ms") / 1000;
                Song song = new Song(name, new Album(), new Artist(), new Artist[0], duration);

                global.addSongHistory(song);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Type kan vara album, track, artist, playlist

    public static void parseSearchJSON(JSONObject obj, GlobalApplication global) {
        try {
            obj = obj.getJSONObject("artists");
            JSONArray arr = obj.getJSONArray("items");

            for(int i=0; i<arr.length(); i++) {
                obj = arr.getJSONObject(i);
                String id = obj.getString("id");

                Artist artist = global.getArtistFromId(id);
                if(artist == null) {
                    String name = obj.getString("name");
                    JSONArray genreArr = obj.getJSONArray("genres");
                    String[] genres = new String[genreArr.length()];
                    for(int k=0; k<genreArr.length(); k++)
                        genres[k] = genreArr.getString(k);

                    int popularity = obj.getInt("popularity");

                    if(obj.has("images") && obj.getJSONArray("images").length() > 0) {
                        JSONArray img = obj.getJSONArray("images");
                        String imageUrl = img.getJSONObject(0).getString("url");
                        artist = new Artist(name, genres, id, popularity, imageUrl);
                    }
                    else
                        artist = new Artist(name, genres, id, popularity);

                    global.addArtist(artist);
                }
                global.addSearchRes(artist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAccessToken() {
        return accessToken;
    }
}
