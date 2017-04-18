package emilp.hallo;

import android.graphics.Bitmap;

/**
 * Created by jonas on 2017-04-04.
 */

/**
 * An interface for "content" which in our case can either be an album, song or artist. These three
 * classes all inherit from content
 */
public interface Content {

    String getTitle();
    String getBread();
    Bitmap getImage();
    int fallbackImage();
    void downloadImage();
}
