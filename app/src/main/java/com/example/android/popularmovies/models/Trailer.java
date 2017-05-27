package com.example.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by adaobifrank on 5/17/17.
 */

public class Trailer implements Parcelable {


    private String id;
    @SerializedName("iso_639_1")
    private String language;
    @SerializedName("iso_3166_1")
    private String country;
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;
    @SerializedName("site")
    private String site;
    @SerializedName("size")
    private Integer size;
    @SerializedName("type")
    private String type;

    public String getId() { return id; }

    public String getLanguage() { return language; }

    public String getCountry() { return country; }

    public String getKey() { return key; }

    public String getName() { return name; }

    public String getSite() { return site; }

    public Integer getSize() { return size; }

    public String getType() { return type; }

    public Trailer(String id, String language, String country, String key, String name, String site, Integer size, String type ) {
        this.id = id;
        this.language = language;
        this.country = country;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    protected Trailer(Parcel in) {
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
