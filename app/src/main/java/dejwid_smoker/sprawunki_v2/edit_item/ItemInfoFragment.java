package dejwid_smoker.sprawunki_v2.edit_item;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_info, container, false);
    }

}
