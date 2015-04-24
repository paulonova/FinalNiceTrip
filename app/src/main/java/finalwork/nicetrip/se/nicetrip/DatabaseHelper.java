package finalwork.nicetrip.se.nicetrip;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PAULO NOVA on 2015-04-04.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATA_BASE = "NiceTrip";
    private static int VERSION = 1;


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE trip (_id INTEGER PRIMARY KEY," +
                "destiny TEXT, type_trip INTEGER, arrive_date DATE," +
                "exit_date DATE, budget DOUBLE," +
                "number_peoples INTEGER);");

        db.execSQL("CREATE TABLE spending (_id INTEGER PRIMARY KEY," +
                " category TEXT, date DATE, value DOUBLE," +
                " description TEXT, place TEXT, trip_id INTEGER," +
                " FOREIGN KEY(trip_id) REFERENCES trip(_id));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("ALTER TABLE spending ADD COLUMN person TEXT");

    }

    public DatabaseHelper(Context context) {
        super(context, DATA_BASE, null, VERSION);
    }
}
