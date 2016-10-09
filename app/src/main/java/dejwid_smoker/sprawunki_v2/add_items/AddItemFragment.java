package dejwid_smoker.sprawunki_v2.add_items;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import dejwid_smoker.sprawunki_v2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddItemFragment extends Fragment {

    private static final String TEXT_EDIT = "text_edit";
    public EditText editText;

    public interface OnListCategoryClicked {
        public void onCategoryListClick(int position);
    }

    public interface OnConfirmButtonClicked {
        public void onConfirmButtonClicked(String s);
    }

    private OnListCategoryClicked listenerCategory;
    private OnConfirmButtonClicked listenerConfirm;

    public AddItemFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listenerCategory = (OnListCategoryClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnListCategoryClicked");
        }
        try {
            listenerConfirm = (OnConfirmButtonClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnConfirmButtonClicked");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        ListViewCompat listView = (ListViewCompat) view.findViewById(R.id.category_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.categories));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listenerCategory.onCategoryListClick(position);
            }
        });

        editText = (EditText) view.findViewById(R.id.list_item);

        ImageView imageView = (ImageView) view.findViewById(R.id.confirm_item);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getTxt = editText.getText().toString();

                if (getTxt != "") {
                    listenerConfirm.onConfirmButtonClicked(getTxt);
                }
            }
        });
        return view;
    }

}
