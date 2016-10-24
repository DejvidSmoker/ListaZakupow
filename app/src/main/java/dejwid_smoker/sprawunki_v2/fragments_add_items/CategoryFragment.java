package dejwid_smoker.sprawunki_v2.fragments_add_items;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import dejwid_smoker.sprawunki_v2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends ListFragment {

    public interface OnItemCategoryClicked {
        public void oItemCategoryListClick(String string);
    }

    private OnItemCategoryClicked listener;

    public static final String POSITION_CATEGORY = "position_category";
    public static final String ITEM_NAME = "item_name";

    private int arrayId;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (CategoryFragment.OnItemCategoryClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemCategoryClicked");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle args = this.getArguments();
        int whichCategory = args.getInt(POSITION_CATEGORY, 0);
        Log.v("position", String.valueOf(whichCategory));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                getArray(whichCategory));

        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        listener.oItemCategoryListClick(String.valueOf(l.getItemAtPosition(position)));
    }

    private String[] getArray(int whichCat) {
        switch (whichCat) {
            case 0:
                arrayId = R.array.diary;
                break;
            case 1:
                arrayId = R.array.fruits;
                break;
            case 2:
                arrayId = R.array.hygiene;
                break;
            case 3:
                arrayId = R.array.vegetables;
                break;
            case 4:
                arrayId = R.array.drinks;
                break;
            case 5:
                arrayId = R.array.bread;
                break;
            case 6:
                arrayId = R.array.frozen;
                break;
            default:
                Toast.makeText(getActivity(),
                        "Brak danych dla kategorii",
                                Toast.LENGTH_SHORT).show();
        }
        return getResources().getStringArray(arrayId);
    }
}
