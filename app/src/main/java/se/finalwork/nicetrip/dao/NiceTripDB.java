package se.finalwork.nicetrip.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import se.finalwork.nicetrip.domain.Spending;
import se.finalwork.nicetrip.domain.Trip;
import se.finalwork.nicetrip.handle.DatabaseHelper;

public class NiceTripDB  {


    private SQLiteDatabase db;
    Trip trip;
    Spending spend;
    DatabaseHelper dbHelper;


    public NiceTripDB(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    // Using in ResetDatabase..
    public void dropAllDataBase() {

        try {
            db.execSQL("DROP TABLE IF EXISTS trip");
            db.execSQL("DROP TABLE IF EXISTS spending");

            db.execSQL("CREATE TABLE trip (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "destiny TEXT, type_trip TEXT, arrive_date DATE," +
                    "exit_date DATE, budget DOUBLE," +
                    "number_peoples INTEGER);");

            db.execSQL("CREATE TABLE spending (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " category TEXT, date DATE, value DOUBLE," +
                    " description TEXT, place TEXT, trip_id INTEGER," +
                    " FOREIGN KEY(trip_id) REFERENCES trip(_id));");

            Log.d("Drop and Re-create"," Was successfully!!");

        }catch (SQLiteException e){
            Log.e("dropAllDataBase", "ERROR: dropAllDataBase" + e.getMessage());
        }

    }

    // Get data
    public Cursor getData(int id){
        SQLiteDatabase db = this. dbHelper.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id, null );
        return res;
    }

    public int getActualTripId(){

        // Get the actual id..
        SQLiteDatabase db = this. dbHelper.getReadableDatabase();
        String sql = "SELECT _id FROM trip";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToLast();

        int id = cursor.getInt(0);
        Log.d("Test ID","ID: " + id);
        return id;
    }



    public void deleteTrip  (Trip trip){

        ContentValues values = new ContentValues();

        db.delete(Trip.TABLE, "_id =" + trip.getId(), null);
    }

//    public Integer deleteContact (Integer id)  {
//        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
//        return db.delete("contacts", "id = ? ", new String[] { Integer.toString(id) }); // to convert to String..
//    }






//    // id value connect Spending with Trip..
//    public void insertSpending (Spending spend, int id){
//
//
//        ContentValues values = new ContentValues();
//
//        values.put(Spending.CATEGORY, spend.getCategory());
//        values.put(Spending.DATE, spend.getDate());
//        values.put(Spending.VALUE, spend.getValue());
//        values.put(Spending.DESCRIPTION, spend.getDescription());
//        values.put(Spending.PLACE, spend.getPlace());
//        values.put(Spending.TRIP_ID, id);
//
//        db.insert(Spending.TABLE, null, values);
//
//    }

}
