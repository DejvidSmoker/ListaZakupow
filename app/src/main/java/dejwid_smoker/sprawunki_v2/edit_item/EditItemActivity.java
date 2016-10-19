package dejwid_smoker.sprawunki_v2.edit_item;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import dejwid_smoker.sprawunki_v2.R;
import dejwid_smoker.sprawunki_v2.add_items.AddItemsActivity;
import dejwid_smoker.sprawunki_v2.add_items.ShowItemsFragment;
import dejwid_smoker.sprawunki_v2.database.ListDatabaseHelper;
import dejwid_smoker.sprawunki_v2.pojo.ItemProperties;

public class EditItemActivity extends AppCompatActivity {

    public static final String ITEM_PROPERTIES = "item_properties";
    public static final String FRAGMENT_PROP = "frag_prop";
    private static final int ITEM_INFO_FRAGMENT = 0;
    private static final int EDIT_ITEM_FRAGMENT = 1;
    private static final String VISIBLE_EDIT_FRAGMENT = "visible_edit_fragment";
    private static final String CURRENT_EDIT_FRAGMENT = "current_edit_fragment";

    private String listName;
    private String itemName;
    private int itemPosOnList;
    private int currentFragment;
    private Toolbar toolbar;
    private SQLiteDatabase db;
    private SQLiteOpenHelper openHelper;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            currentFragment = savedInstanceState.getInt(CURRENT_EDIT_FRAGMENT);
        } else {
            currentFragment = ITEM_INFO_FRAGMENT;
        }

        Intent intent = getIntent();
        listName = intent.getExtras().getString(ShowItemsFragment.CURRENT_NAME_LIST);
        itemName = intent.getExtras().getString(ShowItemsFragment.ITEM_NAME);
        itemPosOnList = intent.getExtras().getInt(ShowItemsFragment.ITEM_POSITION);

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment = fm.findFragmentByTag(VISIBLE_EDIT_FRAGMENT);
                if (fragment instanceof ItemInfoFragment) {
                    currentFragment = ITEM_INFO_FRAGMENT;
                    setToolbarToEachFrag(itemName, true);
                }
                if (fragment instanceof EditItemFragment) {
                    currentFragment = EDIT_ITEM_FRAGMENT;
                    setToolbarToEachFrag(itemName, false);
                }
            }
        });

//        price = (EditText) findViewById(R.id.set_price);
//        count = (EditText) findViewById(R.id.set_count);
//        comment = (EditText) findViewById(R.id.set_comment);
//        unit = (AppCompatSpinner) findViewById(R.id.set_unit);
//        gotIt = (Switch) findViewById(R.id.set_gotit);
//
//        price.setText("2.99", TextView.BufferType.EDITABLE);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, listName + " pos: " + itemPosOnList, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                workOnDb(listName, 1, itemPosOnList);
            }
        });

        workOnDb(listName, currentFragment, itemPosOnList);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(CURRENT_EDIT_FRAGMENT, currentFragment);
    }

    private void setToolbarToEachFrag(String title, boolean homeEnabled) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeEnabled);
    }

    private ItemProperties workOnDb(String lName, int currentFragment, int iPosition) {
        ItemProperties itemProperties = null;
        try {
            openHelper = new ListDatabaseHelper(this);
            db = openHelper.getWritableDatabase();

            switch (currentFragment) {
                case ITEM_INFO_FRAGMENT:
                    itemProperties = getItemPropertiesToEdit(itemProperties, lName, iPosition);
                    break;
                case EDIT_ITEM_FRAGMENT:
                    itemProperties = getItemPropertiesToEdit(itemProperties, lName, iPosition);
                    break;
            }

            showCurrFrag(itemProperties, currentFragment);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemProperties;
    }

    private ItemProperties getItemPropertiesToEdit(ItemProperties properties,
                                                   String lName,
                                                   int iPos) {
        cursor = db.query(lName + AddItemsActivity.REST_OF_TABLE_NAME,
                new String[] {"ITEM_NAME", "ITEM_CHECKED", "ITEM_PRICE", "ITEM_COUNT", "ITEM_UNIT",
                        "ITEM_COMMENT"},
                "_id = ?",
                new String[] {String.valueOf(iPos + 1)}, null, null, null);

        if (cursor.moveToFirst()) {
            properties = new ItemProperties(cursor.getString(0), cursor.getInt(1),
                    cursor.getDouble(2), cursor.getDouble(3), cursor.getString(4),
                    cursor.getString(5));

            Toast.makeText(getApplication(), "pobrano dane przedmiotu", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getApplication(), "nie pobrano danych przedmiotu", Toast.LENGTH_SHORT)
                    .show();
        }
        return properties;
    }
//
//    private ArrayList<String> getItemPropertiesToShow(ArrayList<String> list, String lName, int iPos) {
//        cursor = db.query(lName + AddItemsActivity.REST_OF_TABLE_NAME,
//                new String[] {"ITEM_PRICE", "ITEM_COUNT", "ITEM_UNIT", "ITEM_COMMENT"},
//                "_id = ?",
//                new String[] {String.valueOf(iPos)}, null, null, null);
//        if (cursor.moveToFirst()) {
//            list = new ArrayList<String>();
//            list.add(0, String.valueOf(cursor.getDouble(0)));
//            list.add(1, String.valueOf(cursor.getDouble(1)));
//            list.add(2, cursor.getString(2));
//            list.add(3, cursor.getString(3));
//
//            Toast.makeText(getApplication(), "pobrano dane przedmiotu", Toast.LENGTH_SHORT)
//                    .show();
//        } else {
//            Toast.makeText(getApplication(), "nie pobrano danych przedmiotu", Toast.LENGTH_SHORT)
//                    .show();
//        }
//        return list;
//    }

    private void showCurrFrag(ItemProperties properties, int cFrag) {
        Fragment fragment;
        Bundle args = new Bundle();
        args.putParcelable(FRAGMENT_PROP, properties);
        switch (cFrag) {
            case ITEM_INFO_FRAGMENT:
                fragment = new ItemInfoFragment();
                fragment.setArguments(args);
                runFragment(fragment);
                break;
            case EDIT_ITEM_FRAGMENT:
                fragment = new EditItemFragment();
                fragment.setArguments(args);
                runFragment(fragment);
                break;
        }
    }

    private void runFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_edit_item, fragment, VISIBLE_EDIT_FRAGMENT)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

}
