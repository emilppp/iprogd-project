package emilp.hallo.view;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.spotify.sdk.android.*;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

import emilp.hallo.GlobalApplication;
import emilp.hallo.R;

/**
 * Created by jonas on 2017-04-11.
 */

public class CurrentlyPlaying {

    private Activity activity;
    private ProgressBar progressBar;
    private Timer timer = new Timer();

    public CurrentlyPlaying(Activity activity) {
        this.activity = activity;

        this.progressBar = (ProgressBar) activity.findViewById(R.id.current_progress);
        styleProgressBar();

        progressBar.setMax(100);
        progressBar.setProgress(50);

        final GlobalApplication global = ((GlobalApplication) activity.getApplication());
        final Player player = global.getSpotifyService().getmPlayer();

        final TextView pausePlay = (TextView) activity.findViewById(R.id.current_action);
        pausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(global.getSpotifyService().isPlaying()) {
                    handlePaused(global, pausePlay);
                }
                else {
                    handlePlayed(global, pausePlay);
                }
            }
        });

        player.addNotificationCallback(new Player.NotificationCallback() {
            @Override
            public void onPlaybackEvent(PlayerEvent playerEvent) {
                // Duration in set of quarter-seconds
                int duration = (int) (player.getMetadata().currentTrack.durationMs);
                if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyMetadataChanged)) {
                    progressBar.setMax(duration);
                    progressBar.setProgress(0);
                }
                if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyPlay)) {
                    handlePlayed(global, pausePlay);
                    // Started playing
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressBar.getProgress() + 1);
                        }
                    }, 0, 1);
                }
                if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyPause)) {
                    handlePaused(global, pausePlay);
                    timer.cancel();
                    timer.purge();
                    timer = new Timer();
                }
            }

            @Override
            public void onPlaybackError(Error error) {
            }
        });
        global.getSpotifyService().playSong("spotify:track:2zvot9pY2FNl1E94kc4K8M");
    }

    private void handlePaused(GlobalApplication global, TextView pausePlay) {
        global.getSpotifyService().pauseSong();
        pausePlay.setText(activity.getResources().getString(R.string.play));
    }

    private void handlePlayed(GlobalApplication global, TextView pausePlay) {
        global.getSpotifyService().resumeSong();
        pausePlay.setText(activity.getResources().getString(R.string.pause));
    }

    private void styleProgressBar() {
        progressBar.setBackgroundColor(ResourcesCompat.getColor(activity.getResources(), R.color.colorPrimary, null));
        progressBar.setProgressTintList(ColorStateList.valueOf(ResourcesCompat.getColor(activity.getResources(), R.color.colorWhiteish, null)));
    }
}
