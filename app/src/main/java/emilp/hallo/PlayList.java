package emilp.hallo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;

import emilp.hallo.view.ContentList;

/**
 * Created by jonas on 2017-04-04.
 */

public class PlayList extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);

        ArrayList<Content> data = ((GlobalApplication) getApplication()).getRecommendedAlbums();
        ContentList contentList = new ContentList(this, R.id.playlist, LinearLayoutManager.VERTICAL);
        contentList.setTitle(R.string.recommendations_songs);
        contentList.init(data);
    }
}
