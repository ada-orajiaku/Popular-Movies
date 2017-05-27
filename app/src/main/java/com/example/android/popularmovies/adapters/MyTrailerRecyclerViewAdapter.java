package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.fragments.TrailerFragment;
import com.example.android.popularmovies.models.Trailer;

import java.util.List;

public class MyTrailerRecyclerViewAdapter extends RecyclerView.Adapter<MyTrailerRecyclerViewAdapter.ViewHolder> {

    private final List<Trailer> mValues;
    private final TrailerFragment.OnListFragmentInteractionListener mListener;

    public MyTrailerRecyclerViewAdapter(List<Trailer> items, TrailerFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_trailer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.trailer = mValues.get(position);
        holder.nameText.setText(mValues.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.trailer);
                }
            }
        });

        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: launch intent to play video here
                Context context = holder.mView.getContext();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.youtube_base_url) + holder.trailer.getKey()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameText;
        public final ImageButton playButton;
        public Trailer trailer;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameText = (TextView) view.findViewById(R.id.name);
            playButton = (ImageButton) view.findViewById(R.id.play_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameText.getText() + "'";
        }
    }

}
