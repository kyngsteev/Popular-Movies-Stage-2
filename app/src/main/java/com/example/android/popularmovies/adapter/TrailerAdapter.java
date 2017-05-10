package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.Trailers;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Omoarukhe on 09/05/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.trailerHolder>{
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    public Trailers[] trailerData;

    public TrailerAdapter(Context context, Trailers[] trailer) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        trailerData = trailer;
    }

    @Override
    public trailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        view.setBackgroundResource(mBackground);

        return new trailerHolder(view);
    }

    @Override
    public void onBindViewHolder(trailerHolder holder, int position) {
        holder.textView.setText(trailerData[position].getName());
    }

    @Override
    public int getItemCount() {
        if (trailerData == null) return 0;
        return trailerData.length;
    }

    public class trailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_trailer_name)
        TextView textView;

        public trailerHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            String videoUrl = trailerData[getAdapterPosition()].getVideoUrl();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
            context.startActivity(intent);
        }
    }

    public void swapTrailerData(Trailers[] trailers) {
        trailerData = trailers;
        notifyDataSetChanged();
    }
}
