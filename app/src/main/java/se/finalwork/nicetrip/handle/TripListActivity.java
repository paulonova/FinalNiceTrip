package se.finalwork.nicetrip.handle;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.finalwork.nicetrip.domain.Spending;
import se.finalwork.nicetrip.domain.Trip;


public class TripListActivity extends ListActivity
        implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener, SimpleAdapter.ViewBinder {


    public static final String LIMIT_VALUE = "limit_value";
    public static final String CAME_FROM_TRIP_LIST = "CameFromTripList";
    public static final String IS_FROM_TRIP_LIST = "isFromTripList";
    private AlertDialog dialogConfirmation;
    private AlertDialog alertDialog;
    private int selectedTrip;
    private DatabaseHelper helper;
    private SimpleDateFormat dateFormat;
    private Double limitValue;
    private AlertDialog alert;
    private List<Map<String, Object>> itemTrips;

    private long selectItemID;
    private String selectedTripDestination;
    private int selectedDestinationId;

    private double totalSpend;
    private double alertLimit;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new DatabaseHelper(this);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");


        // Instantiate SharedPreferences and retrieve the limit value of the budget..
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String value = preferences.getString("value_limit", "0");
        Log.d("Limit Value Saved", "The Value is: " + value);

        limitValue = Double.valueOf(value);
        setAlertLimit(limitValue);
        Log.d("limitValue","!!!!!" + getAlertLimit());

        String[] from = {"image", "destiny", "date", "total", "progressBar"};
        int[] to = {R.id.type_trip, R.id.destiny, R.id.date, R.id.total, R.id.progressBar};

        SimpleAdapter adapter = new SimpleAdapter(this, listTrips(), R.layout.trip_list, from, to);
        adapter.setViewBinder(this);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        this.alertDialog = buildAlertDialog();
        this.dialogConfirmation = buildDialogConfirmation();

    }


    private List<Map<String, Object>> listTrips() {

        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT _id, type_trip, destiny, arrive_date, exit_date, budget, number_peoples FROM trip";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        itemTrips = new ArrayList<Map<String, Object>>();


        for (int i = 0; i < cursor.getCount(); i++) {


            // Getting values from DB..
            String id = cursor.getString(0);
            String typeTrip = cursor.getString(1);
            String destiny = cursor.getString(2);
            String arrivalDate = cursor.getString(3);
            String exitDate = cursor.getString(4);
            double budget = cursor.getDouble(5);
            int numberPeoples = cursor.getInt(6);

            double alert = budget * limitValue / 100;
            double totalSpend = calcTotalSpend(db, id);
            setTotalSpend(totalSpend);


            Log.d("Database Info TRIP", "Info: " + "ID: " + id + " - " + "TypeTrip: " + typeTrip + " - " +
                    "Destiny: " + destiny + " - " + "ArrivalDate: " + arrivalDate + " - " +
                    "ExitDate: " + exitDate + " - " + "Budget: " + budget);

            Map<String, Object> item = new HashMap<String, Object>();

            if (typeTrip.contains(Constants.TRIP_VACATIONS)) {
                item.put("image", R.drawable.vacations);
            } else {
                item.put("image", R.drawable.business);
            }

            item.put("id", id);
            item.put("destiny", destiny);
            item.put("date", arrivalDate + " to " + exitDate);

            item.put("total", "Total Spend: " + totalSpend + "    Budget: " + budget);
            Log.d("TOTAL_SPEND", "TOTAL: " + totalSpend);

            Double[] values = new Double[]{budget, alert, totalSpend};

            Log.d("ProgressBar", "values: " + "Budget: " + budget + " Alert: " + alert + " Total: " + totalSpend);
            item.put("progressBar", values);
            itemTrips.add(item);
            cursor.moveToNext();

        }

        cursor.close();
        return itemTrips;
    }

    // Used to get the ID from trip according to destiny
    public int returnSelectedTripId(String destiny){

        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT _id FROM trip WHERE destiny=?";
        Cursor cursorID = db.rawQuery(sql, new String[]{destiny.toString()});
        cursorID.moveToFirst();

        int result = cursorID.getInt(0);
        Log.d("Actual Id", "ID: " + result);
        return result;
    }


    private double calcTotalSpend(SQLiteDatabase db, String id) {
        Cursor cursor = db.rawQuery("SELECT SUM(value) FROM SPENDING WHERE TRIP_ID = ?", new String[]{id});
        cursor.moveToFirst();
        double total = cursor.getDouble(0);
        Log.d("SUM(value)", "VALUE: " + cursor.getDouble(0));
        cursor.close();
        return total;
    }

//    public void alertOmLimitValue() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Limit Alert");
//        builder.setMessage(R.string.limit_alert);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        alert = builder.create();
//        alert.show();
//
//    }


    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {


        if (view.getId() == R.id.progressBar) {
            Double values[] = (Double[]) data;
            ProgressBar progressBar = (ProgressBar) view;
            progressBar.setMax(values[0].intValue());
            progressBar.setSecondaryProgress(values[1].intValue());
            progressBar.setProgress(values[2].intValue());
            return true;
        }

        return false;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        this.selectedTrip = position;
        alertDialog.show();

        Map<String, Object> map = itemTrips.get(position);
        String destiny = (String) map.get("destiny");

        setSelectedTripDestination(destiny);
        setSelectedDestinationId(returnSelectedTripId(destiny)); // get the selected trip id..

        // Set the selected Trip _id
        setSelectItemID(id + 1);
        Log.d("SelectedTripID", "Destination: " + getSelectedTripDestination() + " ID: " + returnSelectedTripId(destiny) + " ItemId: " + getSelectItemID());


    }

    private AlertDialog buildAlertDialog() {
        final CharSequence[] items = {
                getString(R.string.new_spend_dialog),
                getString(R.string.made_expenses),
                getString(R.string.remove_dialog)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.options);
        builder.setItems(items, this);
        return builder.create();
    }

    // AlertDialog to confirm the remove..
    private AlertDialog buildDialogConfirmation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.trip_confirmation);
        builder.setPositiveButton(R.string.yes, this);
        builder.setNegativeButton(R.string.no, this);
        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialog, int item) {
        Log.d("Options Dialog", "I am Here in OPTIONS ");


        switch (item) {

            case 0: // New Spend
                Intent spendIntent = new Intent(getApplicationContext(), SpendingActivity.class);
                spendIntent.putExtra(CAME_FROM_TRIP_LIST, getSelectedDestinationId());  // Sending the selected trip..t
                Log.d("Check Select Dest Id", "Value: " + getSelectedDestinationId());
                spendIntent.putExtra(IS_FROM_TRIP_LIST, true);
                startActivity(spendIntent);
                finish();
                break;

            case 1: //Expenses Made

                Intent intent = new Intent(getApplicationContext(),SpendListActivity.class );
                intent.putExtra("DestinationID", getSelectedDestinationId());  // Sending the selected trip..
                intent.putExtra("ItemId", getSelectItemID());
                startActivity(intent);
                finish();
                break;

            case 2: //Remove
                dialogConfirmation.show();
                break;

            case DialogInterface.BUTTON_POSITIVE:
                itemTrips.remove(selectedTrip);
                SQLiteDatabase db = helper.getReadableDatabase();
                db.delete("trip", "_id=?", new String[]{Long.toString(getSelectedDestinationId())});
                db.delete("spending", "trip_id=?", new String[]{Long.toString(getSelectedDestinationId())});
                getListView().invalidateViews();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmation.dismiss();
                break;
        }
    }

    public long getSelectItemID() {
        return selectItemID;
    }

    public void setSelectItemID(long selectTripID) {
        this.selectItemID = selectTripID;
    }

    public String getSelectedTripDestination() {
        return selectedTripDestination;
    }

    public void setSelectedTripDestination(String selectedTripDestination) {
        this.selectedTripDestination = selectedTripDestination;
    }

    public int getSelectedDestinationId() {
        return selectedDestinationId;
    }

    public void setSelectedDestinationId(int selectedDestinationId) {
        this.selectedDestinationId = selectedDestinationId;
    }

    public double getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(double totalSpend) {
        this.totalSpend = totalSpend;
    }

    public double getAlertLimit() {
        return alertLimit;
    }

    public void setAlertLimit(double alertLimit) {
        this.alertLimit = alertLimit;
    }
}
