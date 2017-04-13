package emilp.hallo.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;

import emilp.hallo.R;

/**
 * Created by jonas on 2017-04-13.
 */

public class Loader {
    final Dialog dialog;

    public Loader(Activity activity) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading);

        dialog.show();
    }

    public void hide() {
        dialog.hide();
        dialog.dismiss();
    }
}
