package dejwid_smoker.sprawunki_v2.fragments_main;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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
import dejwid_smoker.sprawunki_v2.add_items.AddItemsActivity;
import dejwid_smoker.sprawunki_v2.database.ListDatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListsFragment extends Fragment {

    public static final String LISTS_MAIN = "lists_main";

    private SQLiteDatabase db;
    private ArrayList<String> names;

    public ListsFragment() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) inflater
                .inflate(R.layout.fragment_lists, container, false);


        Bundle args = getArguments();
        if (args != null) {
            names = args.getStringArrayList(LISTS_MAIN);

            CaptionedMainAdapter adapter = new CaptionedMainAdapter(names);
            recyclerView.setAdapter(adapter);

            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(manager);

            adapter.setListener(new CaptionedMainAdapter.Listener() {
                @Override
                public void onClick(int position, String name) {
                    Intent intent = new Intent(getActivity(), AddItemsActivity.class);
                    intent.putExtra(AddListFragment.LIST_NAME, name);
                    intent.putExtra(AddListFragment.NEW_LIST_NAME, false);
                    startActivity(intent);
                }

                @Override
                public void onClickDelete(final int position, final String name) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Czy chcesz usunąć listę " + name + "?");
                    builder.setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteName(name);
                            names.remove(position);

                            Fragment currentFragment = getActivity().getSupportFragmentManager()
                                    .findFragmentById(R.id.content_main);
                            if (currentFragment instanceof ListsFragment) {
                                FragmentTransaction fragTransaction = (getActivity())
                                        .getSupportFragmentManager()
                                        .beginTransaction();
                                fragTransaction.detach(currentFragment);
                                fragTransaction.attach(currentFragment);
                                fragTransaction.commit();
                            }
                        }
                    });
                    builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();

                }
            });
        }
        return recyclerView;
    }

    private void deleteName(String name) {
        try {
            SQLiteOpenHelper openHelper = new ListDatabaseHelper(getActivity());

            db = openHelper.getWritableDatabase();
            db.delete("lists",
                    "NAME = ?",
                    new String[]{name});

            db.execSQL("DROP TABLE " + ListDatabaseHelper.DB_NAME + "." + name + "_table;");

            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

}
