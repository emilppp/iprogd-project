package emilp.hallo;

import android.graphics.Bitmap;

public class Artist implements Content {

    private String name;
    private String imageUrl;
    private Bitmap image;
    private String spotifyID;

    public Artist(String name, String id, String url) {
        this.name = name;
        this.spotifyID = id;
        this.imageUrl = url;
    }

    public Artist(String name, String id) {
        this.name = name;
        this.spotifyID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getSpotifyID() {
        return spotifyID;
    }

    public void downloadImage() {
        if(image == null && imageUrl != null && !imageUrl.equals(""))
            image = NetworkUtils.getBitmapFromUrl(imageUrl);
    }

    @Override
    public String getTitle() {
        return getName();
    }

    @Override
    public String getBread() {
        return null;
    }

    @Override
    public Bitmap getImage() {
        return image == null ? null : Bitmap.createScaledBitmap(this.image, 250, 250, false);
    }

    @Override
    public int fallbackImage() {
        return R.drawable.fallback_album;
    }
}
