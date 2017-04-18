package emilp.hallo;

import android.graphics.Bitmap;

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
