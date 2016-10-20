package dejwid_smoker.sprawunki_v2.edit_item;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import dejwid_smoker.sprawunki_v2.R;
import dejwid_smoker.sprawunki_v2.pojo.ItemProperties;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditItemFragment extends Fragment {

    public interface OnSelectUnitClicked {
        public void onSelectUnitClicked(int posOnUnitList);
    }

    private OnSelectUnitClicked listener;

    public EditItemFragment() { }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (EditItemFragment.OnSelectUnitClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSelectUnitClicked");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_item, container, false);

        Bundle args = getArguments();
        if (args != null) {
            ItemProperties itemProperties = args.getParcelable(EditItemActivity.FRAGMENT_PROP);

            Switch itemChecked = (Switch) view.findViewById(R.id.set_gotit);
            EditText itemPrice = (EditText) view.findViewById(R.id.set_price);
            EditText itemCount = (EditText) view.findViewById(R.id.set_count);
            EditText itemComment = (EditText) view.findViewById(R.id.set_comment);
            final Spinner itemUnit = (Spinner) view.findViewById(R.id.set_unit);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.units,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            itemUnit.setAdapter(adapter);
            itemUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position,
                                           long id) {
                    itemUnit.setSelection(position);
                    listener.onSelectUnitClicked(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            });

            if (itemProperties.getItemChecked() == 1) {
                itemChecked.setChecked(true);
            }
            itemPrice.setText(String.valueOf(itemProperties.getItemPrice()),
                    TextView.BufferType.EDITABLE);
            itemCount.setText(String.valueOf(itemProperties.getItemCount()),
                    TextView.BufferType.EDITABLE);
            itemComment.setText(itemProperties.getItemComment(), TextView.BufferType.EDITABLE);
        }

        return view;
    }

}
