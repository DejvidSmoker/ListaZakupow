package dejwid_smoker.sprawunki_v2.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import dejwid_smoker.sprawunki_v2.R;
import dejwid_smoker.sprawunki_v2.pojo.ItemProperties;

public class CaptionedAddItemsAdapter extends
        RecyclerView.Adapter<CaptionedAddItemsAdapter.ViewHolder> {

    private Listener listener;
    private ArrayList<ItemProperties> items;

    public static interface Listener {
        public void onClick(int position, String name);
        public void onClickDelete(int position, String name);
        public void onCheckClicked(int position, String name, int checked, boolean isChecked);
    }


    public static class ViewHolder extends  RecyclerView.ViewHolder {

        private CardView cardView;
        public ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }

    }

    public CaptionedAddItemsAdapter(ArrayList<ItemProperties> name) {
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
        final CardView cardView = holder.cardView;

        final ImageView delete = (ImageView) cardView.findViewById(R.id.delete_card_view_add_items);
        final TextView itemName = (TextView) cardView.findViewById(R.id.text_card_add_items);
        final ToggleButton gotIt =
                (ToggleButton) cardView.findViewById(R.id.set_gotit_card_view_add_items);

        itemName.setText(items.get(position).getItemName());
        itemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setBackgroundResource(R.color.itemClicked);
                listener.onClick(position, items.get(position).getItemName());
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.setBackgroundResource(R.color.itemClicked);
                listener.onClickDelete(position, items.get(position).getItemName());
            }
        });

        final int check = items.get(position).getItemChecked();
        gotIt.setChecked(checkGotIt(check));
        gotIt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onCheckClicked(position,
                        items.get(position).getItemName(),
                        checkGotItToInt(isChecked),
                        isChecked);
                Log.i("position/name;", position + "/" + items.get(position).getItemName());
                Log.i("boolean:", String.valueOf(isChecked));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private boolean checkGotIt(int check) {
        if (check == 0) {
            return false;
        } else {
            return true;
        }
    }
    private int checkGotItToInt(boolean check) {
        if (check) {
            return 1;
        } else {
            return 0;
        }
    }
}
