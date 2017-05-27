package com.example.android.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.fragments.ReviewFragment;
import com.example.android.popularmovies.models.Review;

import java.util.List;

public class MyMovieReviewRecyclerViewAdapter extends RecyclerView.Adapter<MyMovieReviewRecyclerViewAdapter.ViewHolder> {

    private final List<Review> mValues;
    private final ReviewFragment.OnListFragmentInteractionListener mListener;

    public MyMovieReviewRecyclerViewAdapter(List<Review> items, ReviewFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.review = mValues.get(position);
        holder.commentText.setText(mValues.get(position).getContent());
       // holder.readMoreText.setText(mValues.get(position).getAuthor());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.review);
                }
            }
        });

//        holder.readMoreText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TODO: go to webpage url here
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView commentText;
       // public final TextView readMoreText;
        public Review review;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            commentText = (TextView) view.findViewById(R.id.comment);
           // readMoreText = (TextView) view.findViewById(R.id.read_more);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + commentText.getText() + "'";
        }
    }
}
