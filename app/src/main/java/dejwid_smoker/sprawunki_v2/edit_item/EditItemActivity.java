package dejwid_smoker.sprawunki_v2.edit_item;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import dejwid_smoker.sprawunki_v2.R;
import dejwid_smoker.sprawunki_v2.add_items.AddItemsActivity;
import dejwid_smoker.sprawunki_v2.add_items.ShowItemsFragment;
import dejwid_smoker.sprawunki_v2.database.ListDatabaseHelper;
import dejwid_smoker.sprawunki_v2.fragments_main.AddListFragment;
import dejwid_smoker.sprawunki_v2.pojo.ItemProperties;

public class EditItemActivity extends AppCompatActivity
        implements EditItemFragment.OnSelectUnitClicked {

    public static final String FRAGMENT_PROP = "frag_prop";
    private static final int ITEM_INFO_FRAGMENT = 0;
    private static final int EDIT_ITEM_FRAGMENT = 1;
    private static final String VISIBLE_EDIT_FRAGMENT = "visible_edit_fragment";
    private static final String CURRENT_EDIT_FRAGMENT = "current_edit_fragment";
    private static final boolean SHOW_BACK_TO_PARENT = true;
    private static final boolean HIDE_BACK_TO_PARENT = false;

    private FloatingActionButton fabSaveEdited;
    private FloatingActionButton fabGoToEdit;
    private String listName;
    private String itemName;
    private int itemPosOnList;
    private int currentFragment;
    private int spinnersChoice;
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

        fabGoToEdit = (FloatingActionButton) findViewById(R.id.fab_edit_item);
        fabGoToEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workOnDb(listName, EDIT_ITEM_FRAGMENT, itemPosOnList);
            }
        });

        fabSaveEdited = (FloatingActionButton) findViewById(R.id.fab_save_edit_item);
        fabSaveEdited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDb();
                backToParentList();
//                workOnDb(listName, currentFragment, itemPosOnList);
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment = fm.findFragmentByTag(VISIBLE_EDIT_FRAGMENT);
                if (fragment instanceof ItemInfoFragment) {
                    currentFragment = ITEM_INFO_FRAGMENT;
                    setToolbarToEachFrag(itemName, SHOW_BACK_TO_PARENT);
                    setFab(currentFragment);
                }
                if (fragment instanceof EditItemFragment) {
                    currentFragment = EDIT_ITEM_FRAGMENT;
                    setToolbarToEachFrag(itemName, HIDE_BACK_TO_PARENT);
                    setFab(currentFragment);
                }
            }
        });


        workOnDb(listName, currentFragment, itemPosOnList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToParentList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (currentFragment == ITEM_INFO_FRAGMENT) {
            setFab(ITEM_INFO_FRAGMENT);
            backToParentList();
        }
        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(CURRENT_EDIT_FRAGMENT, currentFragment);
    }

    private void setFab(int currFrag) {
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fabSaveEdited.getLayoutParams();
        fabSaveEdited.setLayoutParams(p);
        fabGoToEdit.setLayoutParams(p);
        if (currFrag == ITEM_INFO_FRAGMENT) {
            fabGoToEdit.setVisibility(View.VISIBLE);
            fabSaveEdited.setVisibility(View.GONE);
        } else {
            fabGoToEdit.setVisibility(View.GONE);
            fabSaveEdited.setVisibility(View.VISIBLE);
        }
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
                    itemProperties = getItemProperties(itemProperties, lName, iPosition);
                    break;
                case EDIT_ITEM_FRAGMENT:
                    itemProperties = getItemProperties(itemProperties, lName, iPosition);
                    break;
            }

            showCurrFrag(itemProperties, currentFragment);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemProperties;
    }

    private void saveToDb() {
        Switch itemChecked = (Switch) findViewById(R.id.set_gotit);
        EditText itemPrice = (EditText) findViewById(R.id.set_price);
        EditText itemCount = (EditText) findViewById(R.id.set_count);
        EditText itemComment = (EditText) findViewById(R.id.set_comment);

        int checkSwitch = 0;
        if (itemChecked.isChecked()) { checkSwitch = 1; }

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("ITEM_CHECKED", checkSwitch);
            contentValues.put("ITEM_PRICE", Double.parseDouble(String.valueOf(itemPrice.getText())));
            contentValues.put("ITEM_COUNT", Double.parseDouble(String.valueOf(itemCount.getText())));
            contentValues.put("ITEM_UNIT", spinnersChoice);
            contentValues.put("ITEM_COMMENT", String.valueOf(itemComment.getText()));

            db.update(listName + AddItemsActivity.REST_OF_TABLE_NAME,
                    contentValues,
                    "_id = ?",
                    new String[] {String.valueOf(itemPosOnList + 1)});

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ItemProperties getItemProperties(ItemProperties properties,
                                                   String lName,
                                                   int iPos) {
        cursor = db.query(lName + AddItemsActivity.REST_OF_TABLE_NAME,
                new String[] {"ITEM_NAME", "ITEM_CHECKED", "ITEM_PRICE", "ITEM_COUNT", "ITEM_UNIT",
                        "ITEM_COMMENT"},
                "_id = ?",
                new String[] {String.valueOf(iPos + 1)}, null, null, null);

        if (cursor.moveToFirst()) {
            properties = new ItemProperties(cursor.getString(0), cursor.getInt(1),
                    cursor.getDouble(2), cursor.getDouble(3), cursor.getInt(4),
                    cursor.getString(5));
        } else {
            Toast.makeText(getApplication(), "nie pobrano danych przedmiotu", Toast.LENGTH_SHORT)
                    .show();
        }
        return properties;
    }

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

    @Override
    public void onSelectUnitClicked(int posOnUnitList) {
        spinnersChoice = posOnUnitList;
    }

    private void backToParentList() {
        Intent intent = new Intent(this, AddItemsActivity.class);
        intent.putExtra(AddListFragment.LIST_NAME, listName);
        intent.putExtra(AddListFragment.NEW_LIST_NAME, false);
        startActivity(intent);
    }
}
