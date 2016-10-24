package dejwid_smoker.sprawunki_v2.fragments_edit_item;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dejwid_smoker.sprawunki_v2.R;
import dejwid_smoker.sprawunki_v2.activities.EditItemActivity;
import dejwid_smoker.sprawunki_v2.pojo.ItemProperties;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemInfoFragment extends Fragment {

    public ItemInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_info, container, false);

        Bundle args = getArguments();
        if (args != null) {
            ItemProperties itemProperties = args.getParcelable(EditItemActivity.FRAGMENT_PROP);

            Log.i("ITEM_NAME", itemProperties.getItemName());

            TextView price = (TextView) view.findViewById(R.id.check_price);
            TextView count = (TextView) view.findViewById(R.id.check_count);
            TextView unit = (TextView) view.findViewById(R.id.check_unit);
            TextView comment = (TextView) view.findViewById(R.id.check_comment);

            price.setText(String.valueOf(itemProperties.getItemPrice()));
            count.setText(String.valueOf(itemProperties.getItemCount()));
            String[] unitArray = getResources().getStringArray(R.array.units);
            unit.setText(unitArray[itemProperties.getItemUnit()]);
            comment.setText(itemProperties.getItemComment());
        }

        return view;
    }

}
