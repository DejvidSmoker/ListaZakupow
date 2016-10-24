package dejwid_smoker.sprawunki_v2.fragments_add_items;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dejwid_smoker.sprawunki_v2.R;

public class CaptionedAddItemsAdapter extends
        RecyclerView.Adapter<CaptionedAddItemsAdapter.ViewHolder> {

    private Listener listener;
    private ArrayList<String> items;

    public static interface Listener {
        public void onClick(int position, String name);
        public void onClickDelete(int position, String name);
    }


    public static class ViewHolder extends  RecyclerView.ViewHolder {

        private CardView cardView;
        public ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }

    }

    public CaptionedAddItemsAdapter(ArrayList<String> name) {
        this.items = name;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_captioned_add_items, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;

        final ImageView imageView = (ImageView) cardView.findViewById(R.id.delete_card_view_add_items);
        final TextView textView = (TextView) cardView.findViewById(R.id.text_card_add_items);

        textView.setText(items.get(position));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setBackgroundResource(R.color.item_clicked);
                imageView.setBackgroundResource(R.color.item_clicked);
                listener.onClick(position, items.get(position));
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickDelete(position, items.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
