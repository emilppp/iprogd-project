package emilp.hallo;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by jonas on 2017-03-23.
 */

public class GlobalService extends IntentService {

    public GlobalService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
