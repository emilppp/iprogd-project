package emilp.hallo.view;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import emilp.hallo.Content;
import emilp.hallo.MenuAdapter;
import emilp.hallo.R;

public class ContentList {

    private RecyclerView recyclerView;
    private MenuAdapter adapter;
    private View view;
    private int layout;
    private int spinner;

    /**
     * Creates a list in the given direction with the provided data
     * @param activity
     *  Activity that contains the list
     * @param container
     * The view that is the correct layout
     * @param direction
     * The direction of the list
     */
    public ContentList(Activity activity, int container, int direction) {
        constructorHelp(activity, container, direction);
        layout = R.layout.list_item_song;
        if(direction == LinearLayoutManager.HORIZONTAL)
            layout = R.layout.song_history_item;
    }

    public ContentList(Activity activity, int container, int direction, int customLayout) {
        constructorHelp(activity, container, direction);
        layout = customLayout;
    }

    private void constructorHelp(Activity activity, int container, final int direction) {
        spinner = R.id.progress_bar;
        this.view = activity.findViewById(container);
        recyclerView = (RecyclerView) view.findViewById(R.id.song_history_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, direction, false) {
            @Override
            public boolean canScrollVertically() {
                return direction != LinearLayoutManager.VERTICAL;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void init(ArrayList<Content> data) {

        adapter = new MenuAdapter(layout, data) {
            @Override
            public void onClickListener(View view, Content content) {
                super.onClickListener(view, content);
                onItemClick(view, content);
            }

            @Override
            protected void onSecondClickListener(View view, Content content) {
                super.onSecondClickListener(view, content);
                onSecondItemClick(view, content);
            }
        };

        recyclerView.setAdapter(adapter);
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    protected void onItemClick(View view, Content content) {
    }
    protected void onSecondItemClick(View view, Content content) {
    }

    public void setTitle(int res) {
        TextView textView = (TextView) view.findViewById(R.id.list_horizontal_title);
        textView.setText(res);
    }

    public void hideTitle(){
        TextView textView = (TextView) view.findViewById(R.id.list_horizontal_title);
        textView.setVisibility(View.GONE);
    }

    public View getSpinner(){
        return view.findViewById(spinner);
    }
}
