package emilp.hallo;

import android.graphics.Bitmap;

/**
 * Created by jonas on 2017-04-04.
 */

public interface Content {

    String getTitle();
    String getBread();
    Bitmap getImage();
    int fallbackImage();
    void downloadImage();
}
