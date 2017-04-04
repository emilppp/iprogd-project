package emilp.hallo;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

interface ListType {

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

    Object[] getData();

    ListType.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);
    void onBindViewHolder(final ViewHolder holder, final int position);
}
