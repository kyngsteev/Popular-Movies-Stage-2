package com.example.android.popularmovies.data;

/**
 * Created by Omoarukhe on 09/05/2017.
 */

public class Trailers {
    private final String videoUrl = "https://www.youtube.com/watch?v=";
    private String key;
    private String name;

    public String getVideoUrl() {
        return videoUrl + getKey();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
