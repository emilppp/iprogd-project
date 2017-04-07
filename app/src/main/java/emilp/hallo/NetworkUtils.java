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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    final static String PARAM_QUERY = "q";
    final static String PARAM_TYPE = "type";



    public static URL buildUrlSearch(String githubSearchQuery, String type) {
        Uri builtUri = Uri.parse(SPOTIFY_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, githubSearchQuery)
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

    public static URL buildUrlCreatePlaylist() {
        try {
            return new URL(SPOTIFY_CREATE_PLAYLIST_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
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

    public static JSONObject getResponseFromPostHttpUrl(URL url, String token) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        if(token != null) {
            urlConnection.setRequestProperty("Authorization", "Bearer " + token);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("name", "Playlist from apperino");
            urlConnection.setRequestProperty("public", "false");
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