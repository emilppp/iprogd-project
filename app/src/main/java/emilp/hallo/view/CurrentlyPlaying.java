package emilp.hallo.view;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;

import java.util.Timer;
import java.util.TimerTask;

import emilp.hallo.Album;
import emilp.hallo.Artist;
import emilp.hallo.GlobalApplication;
import emilp.hallo.R;
import emilp.hallo.Song;

/**
 * Created by jonas on 2017-04-11.
 */

public class CurrentlyPlaying {

    private Activity activity;
    private ProgressBar progressBar;
    private Timer timer = new Timer();

    public CurrentlyPlaying(final Activity activity) {
        this.activity = activity;

        this.progressBar = (ProgressBar) activity.findViewById(R.id.current_progress);
        styleProgressBar();

        activity.findViewById(R.id.currentlyplaying).setVisibility(View.GONE);

        progressBar.setMax(100);
        progressBar.setProgress(50);

        final GlobalApplication global = ((GlobalApplication) activity.getApplication());
        final Player player = global.getSpotifyService().getmPlayer();

        final TextView pausePlay = (TextView) activity.findViewById(R.id.current_action);
        final ImageView current_cover = (ImageView) activity.findViewById(R.id.current_cover);
        final TextView current_title = (TextView) activity.findViewById(R.id.current_title);
        final TextView current_information = (TextView) activity.findViewById(R.id.current_information);

        current_title.setSelected(true);
        current_information.setSelected(true);

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
                if(global.getCurrentlyPlayingSong() == null) return;

                int duration = (int) global.getCurrentlyPlayingSong().getDurationMs();

                if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyAudioDeliveryDone)) {
                    activity.findViewById(R.id.currentlyplaying).setVisibility(View.GONE);
                    global.setCurrentlyPlayingSong(null);
                }
                if(playerEvent.equals(PlayerEvent.kSpPlaybackNotifyMetadataChanged)) {
                    activity.findViewById(R.id.currentlyplaying).setVisibility(View.VISIBLE);

                    progressBar.setMax(duration);
                    progressBar.setProgress(0);
                    Song song = global.getCurrentlyPlayingSong();

                    current_information.setText(song.getBread());
                    current_title.setText(song.getTitle());
                    if(song.getImage() != null)
                        current_cover.setImageBitmap(song.getImage());
                    else
                        current_cover.setImageResource(song.fallbackImage());
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
