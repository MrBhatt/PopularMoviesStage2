package com.mrbhatt.popularmoviesstage2.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrbhatt.popularmoviesstage2.R;
import com.mrbhatt.popularmoviesstage2.dto.movieDetail.ReviewResult;

import java.util.List;

/**
 * Created by anupambhatt on 14/05/16.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.CardViewHolder> {

    private final List<ReviewResult> reviewList;
    private final Context context;

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public CardView reviewCardView;
        public CardViewHolder(CardView cardView) {
            super(cardView);
            reviewCardView = cardView;
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_review_item, parent, false);
        return new CardViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int position) {
        CardView reviewCardView = cardViewHolder.reviewCardView;
        TextView authorTextView = (TextView) reviewCardView.findViewById(R.id.review_author);
        TextView contentTextView = (TextView) reviewCardView.findViewById(R.id.review_content);
        ReviewResult reviewResult = reviewList.get(position);
        authorTextView.setText(reviewResult.getAuthor());
        contentTextView.setText(reviewResult.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public ReviewAdapter(Context context, List<ReviewResult> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }
}