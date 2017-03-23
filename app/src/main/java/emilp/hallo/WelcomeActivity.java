package emilp.hallo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jonas on 2017-03-23.
 */

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome_screen);

        LinearLayout btnSignInWithSpotify = (LinearLayout) findViewById(R.id.btn_with_spotify);
        final TextView btnContinueWithoutSpotify = (TextView) findViewById(R.id.btn_without_spotify);

        btnSignInWithSpotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithSpotify();
            }
        });
        btnContinueWithoutSpotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueWithoutSpotify();
            }
        });
    }

    private void loginWithSpotify() {
        Toast.makeText(getApplicationContext(), "Login with Spotify", Toast.LENGTH_SHORT).show();
        SpotifyService spotifyService = ((GlobalApplication) getApplication()).getSpotifyService();
        spotifyService.authSpotify(this);
    }

    private void continueWithoutSpotify() {
        Toast.makeText(getApplicationContext(), "Continue without Spotify", Toast.LENGTH_SHORT).show();
        startMainActivityAndReturn();
    }

    private void startMainActivityAndReturn() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
        this.finish();
    }
}