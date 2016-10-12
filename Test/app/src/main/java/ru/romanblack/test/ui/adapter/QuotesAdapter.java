package ru.romanblack.test.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.romanblack.test.R;
import ru.romanblack.test.data.entities.Quote;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.ViewHolder> {

    public QuotesAdapter(List<Quote> quotes) {
        this.quotes = quotes;
    }

    private List<Quote> quotes;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.item_quote, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Quote quote = getItem(position);

        holder.textTextView.setText(quote.getText());
        holder.dateTextView.setText(quote.getDate());
    }

    @Override
    public int getItemCount() {
        return quotes == null ? 0 : quotes.size();
    }

    private Quote getItem(int position) {
        if (quotes == null) {
            return null;
        }

        if (quotes.size() - 1 < position) {
            return null;
        }

        return quotes.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);

            textTextView = (TextView) itemView.findViewById(R.id.text);
            dateTextView = (TextView) itemView.findViewById(R.id.date);
        }

        private TextView textTextView;
        private TextView dateTextView;
    }
}
