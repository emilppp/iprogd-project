package emilp.hallo;

import emilp.hallo.MenuAdapter.ViewHolder;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Activity activity;
    private int layout;
    private ArrayList<Content> data;
    private int lastPosition = -1;
    private boolean animation = true;

    public MenuAdapter(Activity activity, int layout, ArrayList<Content> data) {
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
        final Content content = data.get(position);

        if(holder.getArtistTextView() != null) {
            if(content.getBread() == null) {
                holder.getArtistTextView().setVisibility(View.GONE);
            } else {
                holder.getArtistTextView().setText(content.getBread());
                //Context context = holder.getArtistTextView().getContext();
                //setAnimation(holder.getArtistTextView(), position, context);
                //holder.getArtistTextView().setSelected(true);
            }
        }
        if(holder.getTitleTextView() != null) {
            holder.getTitleTextView().setText(content.getTitle());
            //Context context = holder.getTitleTextView().getContext();
            //setAnimation(holder.getTitleTextView(), position, context);
            //holder.getTitleTextView().setSelected(true);
        }
        if(content.getImage() != null)
            holder.getCoverImageView().setImageBitmap(content.getImage());
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
        public LinearLayout item;
        public ViewHolder(LinearLayout v) {
            super(v);
            item = v;
        }

        public View getItem() {
            return item;
        }

        public boolean hasSecondClick() {
            return item.findViewById(R.id.item_more_options) != null;
        }

        public View getSecondClick() {
            return item.findViewById(R.id.item_more_options);
        }

        public View getMainClick() {
            return item.findViewById(R.id.main_click);
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

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(TextView text, int position, Context context)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition && animation)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.animation);
            text.startAnimation(animation);
            lastPosition = position;
            text.setSelected(true);
        }
    }

    public void noAnimation(){
        animation = false;
    }
}
