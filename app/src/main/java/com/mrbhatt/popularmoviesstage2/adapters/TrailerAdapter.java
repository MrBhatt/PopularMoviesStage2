package com.mrbhatt.popularmoviesstage2.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrbhatt.popularmoviesstage2.R;

import java.util.ArrayList;

/**
 * Created by anupambhatt on 14/05/16.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.CardViewHolder> {

    private final ArrayList<Spanned> trailerLinksList;
    private final Context context;

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public CardView trailerCardView;
        public CardViewHolder(CardView cardView) {
            super(cardView);
            trailerCardView = cardView;
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_trailer_item, parent, false);
        return new CardViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int position) {
        CardView trailerCardView = cardViewHolder.trailerCardView;
        TextView trailerLinkView = (TextView) trailerCardView.findViewById(R.id.trailer_item);
        trailerLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ((TextView) v).getUrls()[0].getURL();

                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                v.getContext().startActivity(i);
            }
        });
        trailerLinkView.setText(trailerLinksList.get(position));
    }

    @Override
    public int getItemCount() {
        return trailerLinksList.size();
    }

    public TrailerAdapter(Context context, ArrayList<Spanned> trailerLinksList) {
        this.context = context;
        this.trailerLinksList = trailerLinksList;
    }
}