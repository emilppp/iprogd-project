/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package emilp.hallo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.renderscript.ScriptGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    final static String SPOTIFY_BASE_URL =
            "https://api.spotify.com/v1/search";

    final static String SPOTIFY_GET_USER_URL = "https://api.spotify.com/v1/me";
    final static String SPOTIFY_ARTIST_URL = "https://api.spotify.com/v1/artists/";

    final static String SPOTIFY_HISTORY_URL = "https://api.spotify.com/v1/me/player/recently-played";

    final static String SPOTIFY_CREATE_PLAYLIST_URL = "https://api.spotify.com/v1/users/";

    final static String SPOTIFY_TRACKS_URL = "https://api.spotify.com/v1/tracks/?ids=";

    final static String PARAM_QUERY = "q";
    final static String PARAM_TYPE = "type";

    public static URL buildAristsPopularTracksURL(String id) {
        URL url = null;
        try {
            url = new URL("https://api.spotify.com/v1/artists/" + id + "/top-tracks?country=SE");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildArtistAlbumsURL(String id) {
        URL url = null;
        try {
            url = new URL("https://api.spotify.com/v1/artists/" + id + "/albums");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildAlbumTracksURL(String album) {
        URL url = null;
        try {
            url = new URL("https://api.spotify.com/v1/albums/" + album + "/tracks");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildRandom(String type, int limit) {
        String[] randomArray = new String[]{"%25a%25", "a%25", "%25e%25", "e%25", "%25i%25", "i%25", "%25o%25", "o%25"};

        Random rand = new Random();
        //This will get a random result out of the array above
        String rString = randomArray[ rand.nextInt(randomArray.length) ];

        //This will get a random offset number between 1 and 1000. So you get a random track. (you can change the numbers btw)
        int rOffset = rand.nextInt(1000) + 1;

        //This is the url that gets the results out of the Spotify API. You have to put in the variables you created above.
        String urlString = "https://api.spotify.com/v1/search?query="+rString+"&offset="+rOffset+"&limit="+limit+"&type="+type;
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static JSONObject getTracks(String tracks) {
        try {
            URL url = new URL(SPOTIFY_TRACKS_URL + tracks);
            return NetworkUtils.getResponseFromHttpUrl(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL buildUrlSearch(String spotifySearchQuery, String type) {
        Uri builtUri = Uri.parse(SPOTIFY_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, spotifySearchQuery)
                .appendQueryParameter(PARAM_TYPE, type).build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrlHistory() {
        try {
            return new URL(SPOTIFY_HISTORY_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL buildUrlCreatePlaylist(String userid) {
        if(userid != null && userid != "") {
            Uri builtUri = Uri.parse(SPOTIFY_CREATE_PLAYLIST_URL).buildUpon().appendPath(userid).appendPath("playlists").build();
            System.out.println(builtUri.toString());
            try {
                return new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static URL buildUrlRemoveFromPlaylist(String userID, String playlistID, String track) {
        if(userID != null && userID != "") {
            Uri builtUri = Uri.parse(SPOTIFY_CREATE_PLAYLIST_URL).buildUpon().appendPath(userID).appendPath("playlists").appendPath(playlistID).appendPath("tracks").build();
            System.out.println(builtUri.toString());
            try {
                return new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static URL buildUrlAddTracksToPlaylist(String userID, String playlistID, ArrayList<Song> tracks) {
        if(userID != null && playlistID != null && tracks != null) {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i<tracks.size(); i++) {
                sb.append("spotify:track:"+tracks.get(i).getId());
                if(i != tracks.size() -1)
                    sb.append(",");
            }
            Uri builtUri = Uri.parse(SPOTIFY_CREATE_PLAYLIST_URL).buildUpon().appendPath(userID).appendPath("playlists").appendPath(playlistID).appendPath("tracks").appendQueryParameter("uris", sb.toString()).build();
            System.out.println("kebab "  + builtUri.toString());
            try {
                return new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static URL buildUrlArtist(String artistId) {
        Uri builtUri = Uri.parse(SPOTIFY_ARTIST_URL).buildUpon().appendPath(artistId).build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static URL buildUrlGetSpotifyProfile() {
        try {
            return new URL(SPOTIFY_GET_USER_URL);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getResponseFromHttpUrl(URL url) throws IOException {
        return getResponseFromHttpUrl(url, null);
    }


    public static JSONObject getResponseFromAddToPlaylist(URL url, String token, ArrayList<Song> tracks) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");

        if(token != null) {
            urlConnection.setRequestProperty("Authorization", "Bearer " + token);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            System.out.println("Vad har vi byggt " + urlConnection.toString());
        } try {
            InputStream in;
            in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                try {
                    String res = scanner.next();
                    return new JSONObject(res);
                } catch (JSONException e) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }

    public static JSONObject getResponseFromDeleteFromPlaylist(URL url, String token, String track) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("DELETE");

        StringBuilder sb = new StringBuilder();
        sb.append("{ \"tracks\": [{ \"uri\": ");
        sb.append("\"");
        sb.append(track);
        sb.append("\"");
        sb.append("}]}");
        String data = sb.toString();
        System.out.println(data);

        if(token != null) {
            urlConnection.setRequestProperty("Authorization", "Bearer " + token);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.setDoOutput(true);
            byte[] outputInBytes = data.getBytes("UTF-8");

            OutputStream os = urlConnection.getOutputStream();
            os.write(outputInBytes);
            os.close();

            System.out.println("Vad har vi tagit bort " + urlConnection.toString());
        } try {
            InputStream in;
            in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                try {
                    String res = scanner.next();
                    return new JSONObject(res);
                } catch (JSONException e) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }


    public static JSONObject getResponseFromPostHttpUrl(URL url, String token) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");

        if(token != null) {
            urlConnection.setRequestProperty("Authorization", "Bearer " + token);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            String data = "{ \"name\": \"A new playlist\", \"public\": false}";
            urlConnection.setDoOutput(true);
            byte[] outputInBytes = data.getBytes("UTF-8");
            OutputStream os = urlConnection.getOutputStream();
            os.write(outputInBytes);
            os.close();
            System.out.println(token);
            System.out.println(urlConnection.toString());
        } try {
            InputStream in;
            in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                try {
                    String res = scanner.next();
                    return new JSONObject(res);
                } catch (JSONException e) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static JSONObject getResponseFromHttpUrl(URL url, String token) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        if(token != null)
           urlConnection.setRequestProperty("Authorization", "Bearer " + token);
        try {
            InputStream in;
            if(urlConnection.getResponseCode() == 401) {
                //in = urlConnection.getErrorStream();
            }
            //else
            in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                try {
                    String res = scanner.next();
                    return new JSONObject(res);
                } catch (JSONException e) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }

    /**
     *
     * @param url
     * @return The bitmap of the url's image
     */
    public static Bitmap getBitmapFromUrl(String url) {
        Bitmap image = null;
        try {
            URL u = new URL(url);
            image = BitmapFactory.decodeStream(u.openStream());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}