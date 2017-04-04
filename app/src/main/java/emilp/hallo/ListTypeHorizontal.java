package emilp.hallo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by jonas on 2017-04-04.
 */

public class ListTypeHorizontal implements ListType {

    private Activity activity;
    private Object[] data;

    public ListTypeHorizontal(Activity activity, Object[] data) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.song_history_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Content content = (Content) data[position];

        holder.getArtistTextView().setText(content.getBread());
        holder.getTitleTextView().setText(content.getTitle());
        if(content.getImage() != null)
            holder.getCoverImageView().setImageBitmap(content.getImage());
        else
            holder.getCoverImageView().setImageResource(content.fallbackImage());
    }

    @Override
    public Object[] getData() {
        return data;
    }
}
