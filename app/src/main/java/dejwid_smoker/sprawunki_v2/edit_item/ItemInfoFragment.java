package dejwid_smoker.sprawunki_v2.edit_item;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import dejwid_smoker.sprawunki_v2.R;

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
        View view = inflater.inflate(R.layout.fragment_item_info, container);

        Bundle args = getArguments();
        if (args != null) {
            ArrayList<String> list = args.getStringArrayList(EditItemActivity.ITEM_PROPERTIES);

            TextView price = (TextView) view.findViewById(R.id.check_price);
            TextView count = (TextView) view.findViewById(R.id.check_count);
            TextView unit = (TextView) view.findViewById(R.id.check_unit);
            TextView comment = (TextView) view.findViewById(R.id.check_comment);

//            price.setText(list.get(0));
//            count.setText(list.get(1));
//            unit.setText(String.valueOf(list.get(2)));
//            comment.setText(list.get(3).toString());
        }

        return view;
    }


}
