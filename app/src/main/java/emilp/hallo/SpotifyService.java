package emilp.hallo;

import android.app.Activity;
import android.content.Intent;
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

import java.io.IOException;
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

    public void playSong(GlobalApplication global, Song song) {
        global.setCurrentlyPlayingSong(song);
        playSong("spotify:track:" + song.getId());
    }

    private void playSong(String spotifyUri) {
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

    public boolean isPlaying() {
        return mPlayer.getPlaybackState().isPlaying;
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
        AuthenticationClient.clearCookies(this);
        this.clearPlayerState();
        accessToken = null;
    }
    private void clearPlayerState() {
        if(mPlayer != null) {
            mPlayer.pause(mOperationCallback);
            mPlayer.logout();
        }

        mPlayer = null;
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

    public static void parsePlaylistJSON(JSONObject res, GlobalApplication global) {
        try {

            String url = res.getJSONObject("external_urls").getString("spotify");
            String name = res.getString("name");
            String uri = res.getString("uri");
            String id = res.getString("id");

            global.setPlaylistID(id);
            System.out.println(id);
            System.out.println(url);
            System.out.println(name);
            System.out.println(uri);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    /**
     * WARNING! This function is NOT thread-safe and will halt execution until it completes.
     * @param global
     */
    public void createPlaylist(final GlobalApplication global) {
        String userID = global.getClientID();
        URL url = NetworkUtils.buildUrlCreatePlaylist(userID);
        try {
            JSONObject result = NetworkUtils.getResponseFromPostHttpUrl(url, getAccessToken());
            parsePlaylistJSON(result, global);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void postPlaylist(final GlobalApplication global) {
        postPlaylist(global, global.getSongsToBeAdded());
    }

    public void postPlaylist(final GlobalApplication global, ArrayList<Song> content) {
        String userID = global.getClientID();
        String playlistID = global.getPlaylistID();
        URL url = NetworkUtils.buildUrlAddTracksToPlaylist(userID, playlistID, content);
        new SpotifyQueryTask(this, getAccessToken(), global.getSongsToBeAdded()) {
            @Override
            protected void onPostExecute(JSONObject res) {
                if(res!=null) {
                    System.out.println("posted " +  res.toString());

                }
            }
        }.execute(url);
    }

    public void removeTrackFromPlaylist(final GlobalApplication global, String track) {
        String userID = global.getClientID();
        String playlistID = global.getPlaylistID();
        URL url = NetworkUtils.buildUrlRemoveFromPlaylist(userID, playlistID, track);
        new SpotifyQueryTask(this, getAccessToken(), "delete", track) {
            @Override
            protected void onPostExecute(JSONObject res) {
                if(res!=null) {
                    System.out.println("deleted " + res.toString());
                }
            }
        }.execute(url);
    }

}
