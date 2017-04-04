package emilp.hallo;

import emilp.hallo.MenuAdapter.ViewHolder;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Activity activity;
    private int layout;
    private Object[] data;

    public MenuAdapter(Activity activity, int layout, Object[] data) {
        this.activity = activity;
        this.layout = layout;
        this.data = data;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Content content = (Content) data[position];

        holder.getArtistTextView().setText(content.getBread());
        holder.getTitleTextView().setText(content.getTitle());
        if(content.getImage() != null)
            holder.getCoverImageView().setImageBitmap(content.getImage());
        else
            holder.getCoverImageView().setImageResource(content.fallbackImage());
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
}
