package dejwid_smoker.sprawunki_v2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ListDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "db_name";
    private static final int DB_VERSION = 1;

    public ListDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        upgradeMyDatabase(db, 0, DB_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        upgradeMyDatabase(db, oldVersion, DB_VERSION);
    }

    private void upgradeMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE " + "lists" + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "ITEMS TEXT,"
                    + "CHECKBOX INTEGER);");

        }

    }

    public void insertData(String name) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("NAME", name);

            db.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertList(SQLiteDatabase db, String name, String items) {
        ContentValues listValues = new ContentValues();
        listValues.put("NAME", name);
        listValues.put("ITEMS", items);
        db.insert("lists", null, listValues);
    }
}
