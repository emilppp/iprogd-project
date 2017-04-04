package emilp.hallo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by jonas on 2017-04-04.
 */

public class ListTypeVertical implements ListType {

    private Object[] data;
    private Activity activity;

    public ListTypeVertical(Activity activity, Object[] data) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public Object[] getData() {
        return data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_song, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = (Song) data[position];
        holder.getArtistTextView().setText(song.getInformation());
        holder.getTitleTextView().setText(song.getTitle());
        holder.getCoverImageView().setImageResource(R.drawable.fallback_album);
    }
}
