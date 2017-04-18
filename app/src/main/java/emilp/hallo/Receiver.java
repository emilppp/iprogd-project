package emilp.hallo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class Receiver extends BroadcastReceiver {

    private AsyncTask<Void, Song, Song> bgTask;
    private static Song curSong;

    private static final class BroadcastTypes {
        static final String SPOTIFY_PACKAGE = "com.spotify.music";
        static final String PLAYBACK_STATE_CHANGED = SPOTIFY_PACKAGE + ".playbackstatechanged";
        static final String METADATA_CHANGED = SPOTIFY_PACKAGE + ".metadatachanged";
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        final GlobalApplication global = (GlobalApplication) context.getApplicationContext();

        if(global.getCurrentlyPlayingPlayer() == null) return;

        String action = intent.getAction();

        System.out.println("Received broadcast message.");

        boolean playing = intent.getBooleanExtra("playing", false);
        final String trackId = intent.getStringExtra("id").substring(14);
        final int positionInMs = intent.getIntExtra("playbackPosition", 0);

        if (action.equals(BroadcastTypes.METADATA_CHANGED)) {

            if(bgTask != null) {
                bgTask.cancel(true);
                bgTask = null;
            }

            bgTask = new AsyncTask<Void, Song, Song>() {
                @Override
                protected Song doInBackground(Void... voids) {
                    ApiGetSongs apiGetSongs = new ApiGetSongs(trackId);
                    Song song = null;
                    if (apiGetSongs.getSongs().size() > 0) {
                        song = (Song) apiGetSongs.getSongs().get(0);
                    }
                    return song;
                }

                @Override
                protected void onPostExecute(Song song) {
                    super.onPostExecute(song);
                    curSong = song;
                    // TODO: Could, and possibly should, increment positionInMs to better match reality.
                    global.showCurrentlyPlayingBroadcastedSong(song, positionInMs);
                }
            }.execute();

        } else if (action.equals(BroadcastTypes.PLAYBACK_STATE_CHANGED)) {
            System.out.println(playing + ", " + global.isBroadcasting());
            if(playing && global.isBroadcasting()) {
                System.out.println(2);
                global.showCurrentlyPlayingBroadcastedSong(global.getCurrentlyPlayingSong(), positionInMs);
            }
            else if(!playing && global.isBroadcasting()) {
                System.out.println(3);
                global.hideCurrentlyPlayingBroadcastedSong();
            }
            else if(playing && curSong != null) {
                System.out.println(4);
                global.showCurrentlyPlayingBroadcastedSong(curSong, positionInMs);
            }
        }
    }
}
