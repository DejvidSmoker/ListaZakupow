package dejwid_smoker.sprawunki_v2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import dejwid_smoker.sprawunki_v2.activities.PreferencesActivity;
import dejwid_smoker.sprawunki_v2.database.ListDatabaseHelper;
import dejwid_smoker.sprawunki_v2.fragments_main.AddListFragment;
import dejwid_smoker.sprawunki_v2.fragments_main.ListsFragment;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final int DRAWER_CLOSE_DELAY = 300;
    private static final String VISIBLE_MAIN_FRAGMENT = "visible_main_fragment";
    private static final String NAV_ITEM_ID = "nav_item_id";

    private final Handler drawerActionHandler = new Handler();

    private int navItemId;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            navItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        } else {
            navItemId = R.id.nav_lists;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(navItemId).setChecked(true);

        drawerNavigation(navItemId);

        FloatingActionButton fabAddList = (FloatingActionButton) findViewById(R.id.fab_main_add);
        fabAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(NAV_ITEM_ID, navItemId);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(VISIBLE_MAIN_FRAGMENT);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragment instanceof ListsFragment) {
            this.finishAffinity();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        item.setChecked(true);
        navItemId = item.getItemId();

        drawerLayout.closeDrawer(GravityCompat.START);
        drawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawerNavigation(item.getItemId());
            }
        }, DRAWER_CLOSE_DELAY);
        return true;
    }

    private void drawerNavigation(final int id) {
        switch (id) {
            case R.id.nav_lists:
                showListsFragment();
                break;
            case R.id.nav_add_list:
                showAddDialog();
                break;
            case R.id.nav_properties:
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void runFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment, VISIBLE_MAIN_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    private void showAddDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog_add");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment newFragment = new AddListFragment();
        newFragment.show(ft, getResources().getString(R.string.dialog_add));
    }

    private void showListsFragment() {
        Fragment fragment = new ListsFragment();

        try {
            SQLiteOpenHelper openHelper = new ListDatabaseHelper(this);

            db = openHelper.getReadableDatabase();
            cursor = db.query("lists",
                    new String[] {"NAME"},
                    null, null, null, null, null);

            int count = cursor.getCount();

            if (count > 0) {
                Bundle args = new Bundle();
                ArrayList<String> lists = new ArrayList<>(count);
                int listNr = 0;

                if (cursor.moveToFirst()) {
                    lists.add(listNr, cursor.getString(0));
                    listNr++;
                }
                if (count > 1) {
                    while (cursor.moveToNext()) {
                        lists.add(listNr, cursor.getString(0));
                        listNr++;
                    }
                }
                args.putStringArrayList(ListsFragment.LISTS_MAIN, lists);
                fragment.setArguments(args);
            }

        } catch (SQLiteException e) {
            Toast.makeText(this, "DB niedostepna", Toast.LENGTH_LONG).show();
        }

        runFragment(fragment);
    }

}
