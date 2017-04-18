package emilp.hallo.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;

import emilp.hallo.R;

public class Loader {
    private final Dialog dialog;

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
