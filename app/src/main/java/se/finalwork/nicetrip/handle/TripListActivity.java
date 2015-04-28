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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




    public class TripListActivity extends ListActivity
                                  implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener, SimpleAdapter.ViewBinder {


        private AlertDialog dialogConfirmation;
        private AlertDialog alertDialog;
        private int selectedTrip;

        public static final String LIMIT_VALUE = "limit_value";
        private DatabaseHelper helper;
        private SimpleDateFormat dateFormat;
        private Double limitValue;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new DatabaseHelper(this);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        // Instantiate database and retrieve the limit value of the budget..
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String value = preferences.getString(LIMIT_VALUE, "-1");
        limitValue = Double.valueOf(value);

        String[] from = {"image", "destiny", "date", "total", "progressBar"};
        int[] to = {R.id.type_trip, R.id.destiny, R.id.date, R.id.total, R.id.progressBar};

        SimpleAdapter adapter = new SimpleAdapter(this, listTrips(), R.layout.trip_list, from, to);
        adapter.setViewBinder(this);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        this.alertDialog = buildAlertDialog();
        this.dialogConfirmation = buildDialogConfirmation();
    }




    private List<Map<String, Object>> trips;

    private List <Map<String, Object>> listTrips(){

        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT _id, type_trip, destiny, arrive_date, exit_date, budget FROM trip";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        trips = new ArrayList<Map<String, Object>>();

        for (int i = 0; i <cursor.getCount() ; i++) {



            // Getting values from DB..
            String id = cursor.getString(0);
            int  typeTrip = cursor.getInt(1);
            String destiny = cursor.getString(2);
            String arrivalDate = cursor.getString(3);
            String exitDate = cursor.getString(4);
            double budget = cursor.getDouble(5);
            Log.d("Database Info", "Info: " + id + " - " + typeTrip + " - " + destiny + " - " + arrivalDate + " - " + exitDate + " - " + budget);

            Map<String, Object> item = new HashMap<String, Object>();

            if(typeTrip == Constants.TRIP_VACATIONS){
                item.put("image", R.drawable.vacations);
            }else{
                item.put("image",R.drawable.business);
            }

            item.put("id", id);
            item.put("destiny",destiny);
            item.put("date", arrivalDate + " to " + exitDate);

            double totalSpend = calcTotalSpend(db, id);
            item.put("total","Total Spend: "+ totalSpend);
            Log.d("TOTAL_SPEND", "TOTAL: " + totalSpend);

            double alert = budget * limitValue / 100;
            Double[] values = new Double[]{budget, alert, totalSpend};
            item.put("progressBar", values);
            trips.add(item);
            cursor.moveToNext();

        }

        // Old testing codes..
//        Map<String, Object> item = new HashMap<String, Object>();
//        item.put("image",R.drawable.business);
//        item.put("destiny","Stockholm");
//        item.put("date","02/02/2015 to 05/02/2015");
//        item.put("total","Total Spend: Kr 314,00");
//        item.put("progressBar", new Double[]{500.0, 450.0, 314.0});
//        trips.add(item);
        cursor.close();
        return trips;
    }

        private double calcTotalSpend(SQLiteDatabase db, String id) {
            Cursor cursor = db.rawQuery("SELECT SUM(value) FROM SPENDING WHERE TRIP_ID = ?", new String[]{ id });
            cursor.moveToFirst();
            double total = cursor.getDouble(0);
            Log.d("SUM(value)","VALUE: " + cursor.getDouble(0));
            cursor.close();
            return total;
        }


    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {

        if(view.getId() == R.id.progressBar){
            Double values[] = (Double[]) data;
            ProgressBar progressBar = (ProgressBar)view;
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

     /*   Map<String, Object> map = trips.get(position);
        String destiny = (String) map.get("destiny");
        String message = "Selected trip: " + destiny;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, SpendListActivity.class)); */

    }

    private AlertDialog buildAlertDialog(){
        final CharSequence[] items = {
                getString(R.string.edit),
                getString(R.string.new_spend_dialog),
                getString(R.string.made_expenses),
                getString(R.string.remove_dialog)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.options);
        builder.setItems(items, this);
        return builder.create();
    }

    // AlertDialog to confirm the remove..
    private AlertDialog buildDialogConfirmation(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.trip_confirmation);
        builder.setPositiveButton(R.string.yes, this);
        builder.setNegativeButton(R.string.no, this);
        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialog, int item) {
        Log.d("Options Dialog","I am Here in OPTIONS ");
        switch (item){

            case 0: // Edit
                startActivity(new Intent(this, NewTripActivity.class));
                break;
            case 1: //New Spend
                startActivity(new Intent(this, SpendingActivity.class));
                break;
            case 2: //Expenses Made
                getSelectedItemId();
                Log.d("getListView()","ID: "+ getSelectedItemId());
                startActivity(new Intent(this, SpendListActivity.class));
                break;
            case 3: //Remove
                dialogConfirmation.show();
                break;

            case DialogInterface.BUTTON_POSITIVE:
                trips.remove(selectedTrip);
                getListView().invalidateViews();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmation.dismiss();
                break;
        }
    }



}
