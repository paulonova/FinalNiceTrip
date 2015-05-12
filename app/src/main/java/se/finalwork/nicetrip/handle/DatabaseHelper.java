package se.finalwork.nicetrip.handle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATA_BASE = "NiceTrip";
    private static int VERSION = 1;


    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATA_BASE, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {



        db.execSQL("CREATE TABLE trip (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "destiny TEXT, type_trip TEXT, arrive_date DATE," +
                "exit_date DATE, budget DOUBLE," +
                "number_peoples INTEGER);");

        db.execSQL("CREATE TABLE spending (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " category TEXT, date DATE, value DOUBLE," +
                " description TEXT, place TEXT, trip_id INTEGER," +
                " FOREIGN KEY(trip_id) REFERENCES trip(_id));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE trip");
        db.execSQL("DROP TABLE spending");
        onCreate(db);

    }


}
