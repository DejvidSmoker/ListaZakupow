package dejwid_smoker.sprawunki_v2.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

import dejwid_smoker.sprawunki_v2.MainActivity;
import dejwid_smoker.sprawunki_v2.R;
import dejwid_smoker.sprawunki_v2.database.ListDatabaseHelper;
import dejwid_smoker.sprawunki_v2.fragments_add_items.AddItemFragment;
import dejwid_smoker.sprawunki_v2.fragments_add_items.CategoryFragment;
import dejwid_smoker.sprawunki_v2.fragments_add_items.ShowItemsFragment;
import dejwid_smoker.sprawunki_v2.fragments_main.AddListFragment;
import dejwid_smoker.sprawunki_v2.pojo.ItemProperties;

public class AddItemsActivity extends AppCompatActivity
        implements AddItemFragment.OnListCategoryClicked,
                    CategoryFragment.OnItemCategoryClicked,
                    AddItemFragment.OnConfirmButtonClicked {

    public static final String REST_OF_TABLE_NAME = "_table";
    public static final String VISIBLE_FRAGMENT = "visible_fragment";
    private static final String CURRENT_FRAGMENT = "current_fragment";
    private static final int SHOW_ITEMS_FRAGMENT = 0;
    private static final int ADD_ITEM_FRAGMENT = 1;
//    private static final int CATEGORY_FRAGMENT = 2;
    private static final boolean HOME_BUTTON_ENABLE = true;
    private static final boolean HOME_BUTTON_DISABLE = false;

    private InputMethodManager keyboardHide;
    private FloatingActionButton fabAdd;
    private SQLiteDatabase db;
    private SQLiteOpenHelper openHelper;
    private Cursor cursor;
    private Toolbar toolbar;
    private String listName;
    private int currentFrag;
    private boolean newList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        if (savedInstanceState != null) {
            currentFrag = savedInstanceState.getInt(CURRENT_FRAGMENT);
        } else {
            currentFrag = SHOW_ITEMS_FRAGMENT;
        }

        openHelper = new ListDatabaseHelper(this);
        listName = getIntent().getExtras().get(AddListFragment.LIST_NAME).toString();
        newList = (boolean) getIntent().getExtras().get(AddListFragment.NEW_LIST_NAME);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (newList) {
            listName = lookForFreeName(listName, "lists", "NAME");
            addNewRecord(listName);
        }

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment = fm.findFragmentByTag(VISIBLE_FRAGMENT);
                if (fragment instanceof ShowItemsFragment) {
                    currentFrag = SHOW_ITEMS_FRAGMENT;
                    fabAdd.show();
                    setToolbarToEachFrag(listName, HOME_BUTTON_ENABLE);
                }
                if (fragment instanceof AddItemFragment) {
                    currentFrag = ADD_ITEM_FRAGMENT;
                    fabAdd.hide();
                    setToolbarToEachFrag(getString(R.string.title_add_item), HOME_BUTTON_DISABLE);
                }
                if (fragment instanceof CategoryFragment) {
                    currentFrag = ADD_ITEM_FRAGMENT;
                    fabAdd.hide();
                    setToolbarToEachFrag("Kategoria", HOME_BUTTON_DISABLE);
                }
            }
        });
        keyboardHide= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        showCurrFrag(currentFrag);
        createFabs();
    }

    @Override
    public void onBackPressed() {
        if (currentFrag == SHOW_ITEMS_FRAGMENT) {
            backToParentList();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClick = item.getItemId();
        if (itemClick == R.id.share_list) {
            Intent intentSend = new Intent();
            intentSend.setAction(Intent.ACTION_SEND);
            intentSend.putExtra(Intent.EXTRA_TITLE, listName);
            intentSend.putExtra(Intent.EXTRA_TEXT, setListToImplicitIntent());
            intentSend.setType("text/plain");

            if (intentSend.resolveActivity(getPackageManager()) != null) {
                startActivity(intentSend);
            }
        } else if (itemClick == R.id.refresh_list) {
            showCurrFrag(SHOW_ITEMS_FRAGMENT);
        }
        return super.onOptionsItemSelected(item);
    }

    private String setListToImplicitIntent() {
        ArrayList<ItemProperties> items = getItemsFromTable();
        StringBuilder toBuy = new StringBuilder();
        StringBuilder bought = new StringBuilder();

        toBuy.append(getString(R.string.to_buy));

        String itemName = "";
        int itemsToBuy = 0;
        int itemsBought = 0;

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemChecked() != 1) {
                itemName = items.get(i).getItemName();
                toBuy.append(itemName + ", ");
                itemsToBuy++;
            }
        }

        if (itemsToBuy < 1) {
            toBuy.append(getString(R.string.no_items_to_buy));
        }

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemChecked() != 0) {
                itemName = items.get(i).getItemName();
                bought.append(itemName + ", ");
                itemsBought++;
            }
        }

        if (itemsBought > 0) {
            toBuy.append(getString(R.string.bought));
            toBuy.append(bought.toString());
        }

        return toBuy.toString();
    }

    private void setToolbarToEachFrag(String title, boolean homeEnabled) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeEnabled);
    }

    private void showCurrFrag(int cFrag) {
        Fragment fragment;
        switch (cFrag) {
            case SHOW_ITEMS_FRAGMENT:
                fragment = new ShowItemsFragment();
                showListItems(fragment);
                break;
            case ADD_ITEM_FRAGMENT:
                fragment = new AddItemFragment();
                runFragment(fragment);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_FRAGMENT, currentFrag);
    }

    private void createFabs() {
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_test);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddItemFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_add_item, fragment, AddItemsActivity.VISIBLE_FRAGMENT)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });
    }

    @Override
    public void onCategoryListClick(int position) {
        keyboardHide.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        Fragment fragment = new CategoryFragment();

        Bundle args = new Bundle();
        args.putInt(CategoryFragment.POSITION_CATEGORY, position);
        fragment.setArguments(args);
        runFragment(fragment);
    }

    @Override
    public void oItemCategoryListClick(String recivedFromCateg) {
        addNewItemToDb(recivedFromCateg);
        showCurrFrag(SHOW_ITEMS_FRAGMENT);
    }

    @Override
    public void onConfirmButtonClicked(String recivedFromEditTxt) {
        keyboardHide.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        addNewItemToDb(recivedFromEditTxt);
        showCurrFrag(SHOW_ITEMS_FRAGMENT);
    }

    private void showListItems(Fragment fragment) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(ShowItemsFragment.ITEMS_ARRAY, getItemsFromTable());
        args.putString(ShowItemsFragment.CURRENT_NAME_LIST, listName);
        fragment.setArguments(args);
        runFragment(fragment);
    }

    private ArrayList<ItemProperties> getItemsFromTable() {
        ArrayList<ItemProperties> items = null;
        try {
            db = openHelper.getReadableDatabase();
            Cursor cursor = db.query(listName + REST_OF_TABLE_NAME,
                    new String[] {"ITEM_NAME", "ITEM_CHECKED"},
                    null, null, null, null,
                    "ITEM_CHECKED ASC");

            int count = cursor.getCount();

            items = new ArrayList<>(count);
            if (count > 0) {

                if (count > 0) {
                    int listNr = 0;

                    if (cursor.moveToFirst()) {
                        items.add(listNr, new ItemProperties(cursor.getString(0),
                                cursor.getInt(1)));
                        listNr++;
                    }
                    if (count > 1) {
                        while (cursor.moveToNext()) {
                            items.add(listNr,  new ItemProperties(cursor.getString(0),
                                    cursor.getInt(1)));
                            listNr++;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    private void addNewRecord(String lName) {
        ContentValues contentValues = new ContentValues();
        try {
            db = openHelper.getWritableDatabase();
            contentValues.put("NAME", lName);
            db.insert("lists", null, contentValues);

            db.execSQL("CREATE TABLE " + lName + REST_OF_TABLE_NAME
                    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ITEM_NAME TEXT, "
                    + "ITEM_CHECKED INTEGER, "
                    + "ITEM_PRICE REAL, "
                    + "ITEM_COUNT REAL, "
                    + "ITEM_UNIT INTEGER, "
                    + "ITEM_COMMENT TEXT); ");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void runFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_add_item, fragment, VISIBLE_FRAGMENT)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void addNewItemToDb(String justAdded) {
        try {
            justAdded = lookForFreeName(justAdded, listName + REST_OF_TABLE_NAME, "ITEM_NAME");

//          ZMIENIC DOMYSLNE I USUNAC TO GOWNO
            ContentValues contentValues = new ContentValues();
            contentValues.put("ITEM_NAME", justAdded);
            contentValues.put("ITEM_CHECKED", 0);
            contentValues.put("ITEM_PRICE", 1.1);
            contentValues.put("ITEM_COUNT", 1.1);
            contentValues.put("ITEM_UNIT", "default");
            contentValues.put("ITEM_COMMENT", "comm");

            db.insert(listName + REST_OF_TABLE_NAME, null, contentValues);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String lookForFreeName (String name, String tableName, String columnName) {
        try {

            if (name.equals("")) {
                name = getString(R.string.no_name);
            }
            db = openHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT " + columnName + " FROM " + tableName, null);

            if (cursor != null) {
                cursor.moveToFirst();
                int howMany = 0;
                for (int i = 0; i < cursor.getCount(); i++) {
                    String eachItem = cursor.getString(0);
                    String eachItemWithoutCount = "";

                    if (eachItem.length() >= 3) {
                        eachItemWithoutCount = eachItem.substring(0, eachItem.length() - 3);
                    }

                    if (eachItem.equals(name) || eachItemWithoutCount.equals(name)) {
                        howMany++;
                    }

                    cursor.moveToNext();
                }

                if (howMany > 0) {
                    name = name + "_" + (howMany);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;
    }

    private void backToParentList() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}