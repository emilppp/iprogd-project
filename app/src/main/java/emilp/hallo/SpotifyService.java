package emilp.hallo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthActivity;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;

public class SpotifyService extends Activity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "0daad18357ce4eab851f60728f1c5ac1";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "emilp://callback";

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
        builder.setScopes(new String[]{"user-read-private", "streaming"});
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
    public ArrayList<Song> getSongHistory() {
        ArrayList<Song> arr = new ArrayList<>();
        arr.add(new Song("Kebab", new Album(), new Artist(), arre , 13));
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
        return arr;
    }
}