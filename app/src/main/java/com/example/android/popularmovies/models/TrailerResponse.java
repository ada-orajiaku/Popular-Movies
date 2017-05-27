package com.example.android.popularmovies.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adaobifrank on 5/19/17.
 */

public class TrailerResponse {

    private int id;

    private List<Trailer> results = new ArrayList<Trailer>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
