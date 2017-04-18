package emilp.hallo;

import android.graphics.Bitmap;

import java.util.ArrayList;

class Album implements Content {

    private ArrayList<Artist> artists = new ArrayList<>();
    private String name;
    private String imageUrl;
    private Bitmap image;
    private String id;

    Album(String name, String id, String url) {
        this.name = name;
        this.id = id;
        this.imageUrl = url;
    }

    public void setName(String nam) {
        name = nam;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    private String getArtistsName() {
        if (getArtists().size() == 0)
            return null;
        String res = "";
        ArrayList<Artist> artists = getArtists();
        for(int i=0; i<artists.size(); i++) {
            res += artists.get(i).getName();
            if(i < artists.size() - 2)
                res += ", ";
            if(i == artists.size() - 2)
                res += " & ";
        }
        return res;
    }

    public String getId() {
        return id;
    }

    void addArtists(Artist art) {
        artists.add(art);
    }

    @Override
    public String getTitle() {
        return getName();
    }

    @Override
    public String getBread() {
        return getArtistsName();
    }

    @Override
    public Bitmap getImage() {
        return image == null ? null : Bitmap.createScaledBitmap(this.image, 250, 250, false);
    }

    @Override
    public int fallbackImage() {
        return R.drawable.fallback_album;
    }

    public void downloadImage() {
        if(image == null && imageUrl != null && !imageUrl.equals(""))
            image = NetworkUtils.getBitmapFromUrl(imageUrl);
    }
}
