package se.finalwork.nicetrip.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.finalwork.nicetrip.domain.Spending;
import se.finalwork.nicetrip.domain.Trip;
import se.finalwork.nicetrip.handle.DatabaseHelper;

public class NiceTripDAO {

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    // Constructor receive the context and initiate the DatabaseHelper..
    public NiceTripDAO(Context context) {
        helper = new DatabaseHelper(context);
    }

    // Returns an instance of SQLiteDatabase, creating it if necessary..
    private SQLiteDatabase getDb() {
        if (db == null) {
            db = helper.getWritableDatabase();
        }
        return db;
    }

    public void close() {
        helper.close();
    }


    public List<Trip> tripList() {
        Cursor cursor = getDb().query(Trip.TABLE, Trip.COLUMNS, null, null, null, null, null);

        List<Trip> trips = new ArrayList<Trip>();
        while (cursor.moveToNext()) {
            Trip trip = createTrip(cursor);
            trips.add(trip);
        }
        cursor.close();
        return trips;
    }

    public Trip bringTripById(Integer id) {
        Cursor cursor = getDb().query(Trip.TABLE, Trip.COLUMNS, Trip._ID + " =?", new String[]{id.toString()}, null, null, null);

        if (cursor.moveToNext()) {
            Trip trip = createTrip(cursor);
            cursor.close();
            return trip;
        }
        return null;
    }

    /*Trip(Long id, String destiny, Integer typeTrip, Date arrivalDate, Date exitDate, Double budget, Integer numberPeoples)*/
    private Trip createTrip(Cursor cursor) {

        // From the Constructor
        Trip trip = new Trip(
                cursor.getInt(cursor.getColumnIndex(Trip._ID)),
                cursor.getString(cursor.getColumnIndex(Trip.DESTINY)),
                cursor.getInt(cursor.getColumnIndex(Trip.TYPE_TRIP)),
                cursor.getString(cursor.getColumnIndex(Trip.ARRIVAL_DATE)),
                cursor.getString(cursor.getColumnIndex(Trip.EXIT_DATE)),
                cursor.getDouble(cursor.getColumnIndex(Trip.BUDGET)),
                cursor.getInt(cursor.getColumnIndex(Trip.NUMBER_PEOPLES)));

        return trip;

    }


    public long Insert(Trip trip) {
        ContentValues values = new ContentValues();

        values.put(Trip.DESTINY, trip.getDestiny());
        values.put(Trip.TYPE_TRIP, trip.getTypeTrip());
        values.put(Trip.ARRIVAL_DATE, trip.getArrivalDate());
        values.put(Trip.EXIT_DATE, trip.getExitDate());
        values.put(Trip.BUDGET, trip.getBudget());
        values.put(Trip.NUMBER_PEOPLES, trip.getNumberPeoples());

        return getDb().insert(Trip.TABLE, null, values);

        //long result = db.insert("trip", null, values);


    }


    /* Methods to implement Spending functionality*/
    public boolean removeSpending(Long id) {
        String whereClause = Spending._ID + " = ?";
        String[] whereArgs = new String[]{id.toString()};
        int removed = getDb().delete(Spending.TABLE, whereClause, whereArgs);
        return removed > 0;
    }


    public double calcTotalSpending(Trip trip) {
        Cursor cursor = getDb().rawQuery("SELECT SUM (" + Spending.VALUE + ") FROM " +
                Spending.TABLE + " WHERE " +
                Spending.TRIP_ID + " = ?", new String[]{trip.getId().toString()});
        cursor.moveToFirst();
        double total = cursor.getDouble(0);
        cursor.close();
        return total;

    }


    private Spending createSpending(Cursor cursor) {

        // From the Constructor
        Spending spending = new Spending(
                cursor.getLong(cursor.getColumnIndex(Spending._ID)),
                new Date(cursor.getLong(cursor.getColumnIndex(Spending.DATE))),
                cursor.getString(cursor.getColumnIndex(Spending.CATEGORY)),
                cursor.getString(cursor.getColumnIndex(Spending.DESCRIPTION)),
                cursor.getDouble(cursor.getColumnIndex(Spending.VALUE)),
                cursor.getString(cursor.getColumnIndex(Spending.PLACE)),
                cursor.getInt(cursor.getColumnIndex(Spending.TRIP_ID)));

        return spending;

    }


}
