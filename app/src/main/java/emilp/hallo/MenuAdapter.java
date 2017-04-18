package emilp.hallo;

import emilp.hallo.MenuAdapter.ViewHolder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<ViewHolder> {

    private int layout;
    private ArrayList<Content> data;

    protected MenuAdapter(int layout, ArrayList<Content> data) {
        this.layout = layout;
        this.data = data;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Content content = data.get(position);

        if(holder.getArtistTextView() != null) {
            if(content.getBread() == null && holder.getArtistTextView() != null) {
                holder.getArtistTextView().setVisibility(View.GONE);
            } else if(holder.getArtistTextView() == null){
                //do nothing
            } else {
                holder.getArtistTextView().setText(content.getBread());
            }
        }
        if(holder.getTitleTextView() != null) {
            holder.getTitleTextView().setText(content.getTitle());
        }
        if(content.getImage() != null && holder.getCoverImageView() != null) {
            holder.getCoverImageView().setImageBitmap(content.getImage());
        }
        else if(holder.getCoverImageView() == null){
            //do nothing
        }
        else
            holder.getCoverImageView().setImageResource(content.fallbackImage());

        holder.getMainClick().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener(view, content);
            }
        });
        if(holder.hasSecondClick()) {
            holder.getSecondClick().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSecondClickListener(view, content);
                }
            });
        }
    }

    protected void onClickListener(View view, Content content) {
    }
    protected void onSecondClickListener(View view, Content content) {
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item;
        ViewHolder(LinearLayout v) {
            super(v);
            item = v;
        }

        boolean hasSecondClick() {
            return item.findViewById(R.id.item_more_options) != null;
        }

        View getSecondClick() {
            return item.findViewById(R.id.item_more_options);
        }

        View getMainClick() {
            return item.findViewById(R.id.main_click);
        }

        TextView getArtistTextView() {
            return (TextView) item.findViewById(R.id.history_item_artist);
        }
        TextView getTitleTextView() {
            return (TextView) item.findViewById(R.id.history_item_title);
        }
        ImageView getCoverImageView() {
            return (ImageView) item.findViewById(R.id.history_item_cover);
        }
    }

}
