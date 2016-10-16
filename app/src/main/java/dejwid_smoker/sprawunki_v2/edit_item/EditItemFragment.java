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
public class EditItemFragment extends Fragment {


    public EditItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_item, container, false);

        return view;
    }

}
