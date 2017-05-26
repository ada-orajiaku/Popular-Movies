package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.helpers.MetaDataUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by adaobifrank on 4/12/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String IMAGE_BASE_URL = "image_base_url";
    private final Context context;
    private final List<Movie> movieList;
    private OnItemClickListener onItemClickListener;

    public MovieAdapter(Context context, List<Movie> movies){
        this.context = context;
        this.movieList = movies;
    }


    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: ");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        holder.movieTitle.setText(movieList.get(position).getTitle());
        Picasso.with(context).load(MetaDataUtil.getMetaData(context,IMAGE_BASE_URL)+movieList.get(position).getPosterPath()).into(holder.movieImage);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(movieList.get(position));
            }
        };
        holder.movieImage.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
    
    class MovieViewHolder extends RecyclerView.ViewHolder{
        public Movie movie;
        public TextView movieTitle;
        public ImageView movieImage;

        public MovieViewHolder(View itemView) {
            super(itemView);

            movieTitle = (TextView) itemView.findViewById(R.id.movie_title);
            movieImage = (ImageView) itemView.findViewById(R.id.movie_poster);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }
}
