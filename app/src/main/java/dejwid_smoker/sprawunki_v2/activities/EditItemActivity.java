package dejwid_smoker.sprawunki_v2.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import dejwid_smoker.sprawunki_v2.R;
import dejwid_smoker.sprawunki_v2.add_items.AddItemsActivity;
import dejwid_smoker.sprawunki_v2.add_items.ShowItemsFragment;
import dejwid_smoker.sprawunki_v2.database.ListDatabaseHelper;

public class EditItemActivity extends AppCompatActivity {

    private static final int SET_ITEM_PROPERTIES = 0;
    private String listName;
    private int itemPosOnList;
    private SQLiteDatabase db;
    private SQLiteOpenHelper openHelper;
    private Cursor cursor;
    private EditText price;
    private EditText count;
    private EditText comment;
    private AppCompatSpinner unit;
    private Switch gotIt;
    private int whichAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        listName = intent.getExtras().getString(ShowItemsFragment.CURRENT_NAME_LIST);
        itemPosOnList = intent.getExtras().getInt(ShowItemsFragment.ITEM_POSITION);

        price = (EditText) findViewById(R.id.set_price);
        count = (EditText) findViewById(R.id.set_count);
        comment = (EditText) findViewById(R.id.set_comment);
        unit = (AppCompatSpinner) findViewById(R.id.set_unit);
        gotIt = (Switch) findViewById(R.id.set_gotit);

        price.setText("2.99", TextView.BufferType.EDITABLE);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, listName + " pos: " + itemPosOnList, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    private void workOnDb(int whichAction) {
        try {
            openHelper = new ListDatabaseHelper(this);
            db = openHelper.getWritableDatabase();

            switch (whichAction) {
                case SET_ITEM_PROPERTIES:
                    setItemProperties();
                    break;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setItemProperties() {
        cursor = db.query(listName + AddItemsActivity.REST_OF_TABLE_NAME,
                new String[] {"_id", "ITEM_NAME", "ITEM_CHECKED", "ITEM_PRICE", "ITEM_COUNT",
                "ITEM_UNIT", "ITEM_COMMENT"}, null, null, null, null, null);
        int count = cursor.getCount();

        if (count == 0) {
            if (cursor.moveToFirst()) {
                price.setText(cursor.getString(3));

            }
        } else {

        }
    }
}
