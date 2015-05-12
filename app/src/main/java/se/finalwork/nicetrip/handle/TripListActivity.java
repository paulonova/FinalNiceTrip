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


public class TripListActivity extends ListActivity
        implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener, SimpleAdapter.ViewBinder {


    public static final String LIMIT_VALUE = "limit_value";
    Date date;
    private AlertDialog dialogConfirmation;
    private AlertDialog alertDialog;
    private int selectedTrip;
    private DatabaseHelper helper;
    private SimpleDateFormat dateFormat;
    private Double limitValue;
    private AlertDialog alert;
    private List<Map<String, Object>> itemTrips;



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

            double totalSpend = calcTotalSpend(db, id);
            item.put("total", "Total Spend: " + totalSpend + "   Total peoples: " + numberPeoples);
            Log.d("TOTAL_SPEND", "TOTAL: " + totalSpend);


            double alert = budget * limitValue / 100;
            Double[] values = new Double[]{budget, alert, 500d};  // Value not REAL !
            Log.d("ProgressBar", "values: " + "Budget: " + budget + " Alert: " + alert + " Total: " + totalSpend);
            item.put("progressBar", values);
            itemTrips.add(item);
            cursor.moveToNext();

        }

        // Old testing codes..
//        Map<String, Object> item = new HashMap<String, Object>();
//        item.put("image", R.drawable.business);
//        item.put("destiny", "Stockholm");
//        item.put("date", "02/02/2015 to 05/02/2015");
//        item.put("total", "Total Spend: Kr 314,00");
//        item.put("progressBar", new Double[]{500.0, 450.0, 314.0});
//        itemTrips.add(item);

        cursor.close();
        return itemTrips;
    }


    private double calcTotalSpend(SQLiteDatabase db, String id) {
        Cursor cursor = db.rawQuery("SELECT SUM(value) FROM SPENDING WHERE TRIP_ID = ?", new String[]{id});
        cursor.moveToFirst();
        double total = cursor.getDouble(0);
        Log.d("SUM(value)", "VALUE: " + cursor.getDouble(0));
        cursor.close();
        return total;
    }

    public void alertOmLimitValue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage(R.string.limit_alert);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });
        alert = builder.create();
        alert.show();

    }


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

        String message = "Selected trip: " + destiny;
        Toast.makeText(this, message + " - " + id, Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(this, SpendListActivity.class));

        /*Aqui quando o Intent se direcionar para SpendingListActivity, eu envio como Extra o ID do item selecionado..*/

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

//        if(limitValue>=budget){
//
//        }
        switch (item) {

            case 0: // New Spend
                startActivity(new Intent(this, SpendingActivity.class));
                break;

            case 1: //Expenses Made
                getSelectedItemId();
                Log.d("getListView()", "ID: " + getSelectedItemId());
                startActivity(new Intent(this, SpendListActivity.class));
                break;

            case 2: //Remove
                dialogConfirmation.show();
                break;

            case DialogInterface.BUTTON_POSITIVE:
                itemTrips.remove(selectedTrip);
                getListView().invalidateViews();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmation.dismiss();
                break;
        }
    }


}
