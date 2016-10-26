package dejwid_smoker.sprawunki_v2.fragments_main;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import dejwid_smoker.sprawunki_v2.R;
import dejwid_smoker.sprawunki_v2.activities.AddItemsActivity;


public class AddListFragment extends DialogFragment {

    public static final String LIST_NAME = "list_name";
    public static final String NEW_LIST_NAME = "new_list_name";

    private SharedPreferences sharedPreferences;
    private AutoCompleteTextView autoCompleteTextView;
    private String inputTxt;
    private Button btnAdd;
    private Button btnClose;

    public AddListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_list, container);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.input);
        btnClose = (Button) view.findViewById(R.id.dialog_btn_close);
        btnAdd = (Button) view.findViewById(R.id.dialog_btn_add);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputTxt = autoCompleteTextView.getText().toString();
                Intent intent = new Intent(getActivity(), AddItemsActivity.class);
                intent.putExtra(LIST_NAME, inputTxt);
                intent.putExtra(NEW_LIST_NAME, true);
                startActivity(intent);
            }
        });

        loadDefaultValues();
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.dialog_add_list);
        return dialog;
    }

    private void loadDefaultValues() {
        boolean defaultValues = sharedPreferences.getBoolean("pref_default_values", false);
        if (defaultValues) {
            String defaultListName = sharedPreferences.getString("pref_default_list_name", "");
            autoCompleteTextView.setText(defaultListName);
        }
    }
}
