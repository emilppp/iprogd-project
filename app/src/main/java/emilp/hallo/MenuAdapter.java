package emilp.hallo;

import emilp.hallo.ListType.ViewHolder;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class MenuAdapter extends RecyclerView.Adapter<ViewHolder> {

    public Activity activity;
    private ListType list;

    public MenuAdapter(Activity activity, ListType list) {
        this.activity = activity;
        this.list = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return list.onCreateViewHolder(parent, viewType);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        list.onBindViewHolder(holder, position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.getData().length;
    }
}
