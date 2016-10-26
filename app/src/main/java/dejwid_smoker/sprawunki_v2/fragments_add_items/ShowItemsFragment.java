package dejwid_smoker.sprawunki_v2.fragments_add_items;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import dejwid_smoker.sprawunki_v2.R;
import dejwid_smoker.sprawunki_v2.activities.AddItemsActivity;
import dejwid_smoker.sprawunki_v2.activities.EditItemActivity;
import dejwid_smoker.sprawunki_v2.adapters.CaptionedAddItemsAdapter;
import dejwid_smoker.sprawunki_v2.database.ListDatabaseHelper;
import dejwid_smoker.sprawunki_v2.pojo.ItemProperties;

/**
 * A placeholder fragment containing a simple view.
 */
public class ShowItemsFragment extends Fragment {

    public static final String ITEMS_ARRAY = "items_array";
    public static final String CURRENT_NAME_LIST = "current_list_name";
    public static final String ITEM_POSITION = "item_position";
    public static final String ITEM_NAME = "item_name";
    private static final int DELETE_ITEM = 0;
    private static final int UPDATE_ITEM_CHECK = 1;
    private static final boolean JUST_FOR_CHECK = false;

    private ArrayList<ItemProperties> items;
    private String currentListName;

    public ShowItemsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final RecyclerView recyclerView = (RecyclerView) inflater
                .inflate(R.layout.fragment_show_items, container, false);


        Bundle args = getArguments();
        if (args != null) {
            items = args.getParcelableArrayList(ITEMS_ARRAY);
            currentListName = args.getString(CURRENT_NAME_LIST);

            final CaptionedAddItemsAdapter adapter = new CaptionedAddItemsAdapter(items);
            recyclerView.setAdapter(adapter);

            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(manager);

            adapter.setListener(new CaptionedAddItemsAdapter.Listener() {
                @Override
                public void onClick(int position, String name) {
                    Intent intent = new Intent(getActivity(), EditItemActivity.class);
                    intent.putExtra(CURRENT_NAME_LIST, currentListName);
                    intent.putExtra(ITEM_NAME, name);
                    intent.putExtra(ITEM_POSITION, position);
                    startActivity(intent);
                }

                @Override
                public void onClickDelete(int position, String name) {
                    items.remove(position);
                    workOnDb(name, DELETE_ITEM, JUST_FOR_CHECK);

                    Fragment currentFragment = getActivity().getSupportFragmentManager()
                            .findFragmentById(R.id.content_add_item);
                    if (currentFragment instanceof ShowItemsFragment) {
                        FragmentTransaction fragTransaction = (getActivity())
                                .getSupportFragmentManager()
                                .beginTransaction();
                        fragTransaction.detach(currentFragment);
                        fragTransaction.attach(currentFragment);
                        fragTransaction.commit();
                    }
                }

                @Override
                public void onCheckClicked(int position,
                                           String name,
                                           int checked,
                                           boolean isChecked) {
                    workOnDb(name, UPDATE_ITEM_CHECK, isChecked);
                    items.set(position, new ItemProperties(name, checked));
                }
            });

        }
        return recyclerView;
    }

    private void workOnDb(String itemName, int whichAction, boolean justForCheck) {
        try {
            SQLiteOpenHelper openHelper = new ListDatabaseHelper(getActivity());
            SQLiteDatabase db = openHelper.getWritableDatabase();

            if (whichAction == DELETE_ITEM) {
                db.delete(currentListName + "_table",
                        "ITEM_NAME = ?",
                        new String[]{itemName});
            } else if (whichAction == UPDATE_ITEM_CHECK) {
                int putToDb;
                if (justForCheck) {
                    putToDb = 1;
                } else {
                    putToDb = 0;
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put("ITEM_CHECKED", putToDb);
                db.update(currentListName + AddItemsActivity.REST_OF_TABLE_NAME,
                        contentValues,
                        "ITEM_NAME = ?",
                        new String[]{itemName});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
