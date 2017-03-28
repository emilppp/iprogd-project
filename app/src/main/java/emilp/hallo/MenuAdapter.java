package emilp.hallo;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import emilp.hallo.R;

/**
 * Created by Jonas on 2017-02-02.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private Object[] dataset;
    public Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout item;
        public ViewHolder(LinearLayout v) {
            super(v);
            item = v;
        }
        public TextView getArtistTextView() {
            return (TextView) item.findViewById(R.id.history_item_artist);
        }
        public TextView getTitleTextView() {
            return (TextView) item.findViewById(R.id.history_item_title);
        }
        public ImageView getCoverImageView() {
            return (ImageView) item.findViewById(R.id.history_item_cover);
        }
    }

    public MenuAdapter(Activity activity, Object[] dataset) {
        this.dataset = dataset;
        this.activity = activity;
    }

    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.song_history_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.getArtistTextView().setText(((Song) dataset[position]).getTitle());
        holder.getTitleTextView().setText("Title");
        holder.getCoverImageView().setImageResource(R.drawable.fallback_album);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.length;
    }
}
