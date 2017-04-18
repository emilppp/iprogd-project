package emilp.hallo.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import emilp.hallo.Content;
import emilp.hallo.GlobalApplication;
import emilp.hallo.R;
import emilp.hallo.Song;

public class MoreOptions {
    public MoreOptions(final Activity activity, final Content content) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.more_options_menu);

        final GlobalApplication global = (GlobalApplication) activity.getApplication();

        ImageView cover = ((ImageView) dialog.findViewById(R.id.cover));

        if(content.getImage() == null)
            cover.setImageResource(content.fallbackImage());
        else
            cover.setImageBitmap(content.getImage());

        TextView title = ((TextView) dialog.findViewById(R.id.title));
        title.setText(content.getTitle());
        TextView bread = ((TextView) dialog.findViewById(R.id.bread));
        bread.setText(content.getBread());

        TextView like = (TextView) dialog.findViewById(R.id.like);
        TextView dislike = (TextView) dialog.findViewById(R.id.dislike);
        TextView addToPlaylist = (TextView) dialog.findViewById(R.id.add_to_playlist);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Like", Toast.LENGTH_SHORT).show();
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Dislike", Toast.LENGTH_SHORT).show();
            }
        });
        like.setVisibility(View.GONE);
        dislike.setVisibility(View.GONE);
        final Song song = (Song) content;

        if(global.getPlaylistID() == null) {
            addToPlaylist.setVisibility(View.GONE);
        }
        else if(global.isInPlaylist(song)) {
            addToPlaylist.setText(R.string.remove_from_playlist);
            addToPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    global.removeFromPlaylist(song);
                    Toast.makeText(activity, "Removed " + content.getTitle() + " from playlist.", Toast.LENGTH_SHORT).show();
                    onRemovedFromPlaylist();
                    dialog.dismiss();
                }
            });
        }
        else {
            addToPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    global.addToPlaylist(song);
                    Toast.makeText(activity, "Added " + content.getTitle() + " to playlist.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    protected void onRemovedFromPlaylist() {
    }
}
