package se.finalwork.nicetrip.handle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import se.finalwork.nicetrip.domain.Spending;
import se.finalwork.nicetrip.domain.Trip;


public class SpendingActivity extends Activity { //implements View.OnClickListener


    //private int year, month, day;
    private int actualYear, actualMonth, actualDay;
    private int selectedYear, selectedMonth, selectedDay;
    private Button spendDate;
    private Button spendingBtn;
    private String selectedDate;
    private String actualDate;
    private EditText value, description, place;
    private Spinner category;
    private DatabaseHelper helper;
    private Spending spend;

    private AlertDialog alert;

    private Trip trip;
    private TripListActivity tripListActivity;

    private String valueLimit;



    Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spending);

        category = (Spinner) findViewById(R.id.category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_spend, R.layout.spinner_layout);
        category.setAdapter(adapter);

        value = (EditText) findViewById(R.id.value);
        description = (EditText) findViewById(R.id.descriptionTxt);
        place = (EditText) findViewById(R.id.placeTxt);


        actualYear = calendar.get(Calendar.YEAR);
        actualMonth = calendar.get(Calendar.MONTH);
        actualDay = calendar.get(Calendar.DAY_OF_MONTH);
        actualDate = actualYear + "/" + (actualMonth + 1) + "/" + actualDay;
        selectedDate = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;

        spendDate = (Button) findViewById(R.id.date);
        spendDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });

        updateDate();
        spendingBtn = (Button) findViewById(R.id.spending);

        // Prepare access to database..
        helper = new DatabaseHelper(this);
        trip = new Trip();

        checkEmptyData();
        retrieveSelectedTripID();
        isFromTripList();


        // Instantiate SharedPreferences and retrieve the limit value of the budget..
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        valueLimit = preferences.getString("value_limit", "null");
        Log.d("Limit Value Saved", "from SpendingActivity: " + valueLimit);


        tripListActivity = new TripListActivity();
        Log.d("CheckLimitBudget", "OnCreate: " + tripListActivity.getTotalSpend() + " " + tripListActivity.getAlertLimit());

    }

    // Check if we have a trip registered..
    public boolean checkEmptyData(){
        // Get the actual id..
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT _id FROM trip";
        Cursor cursor = db.rawQuery(sql, null);
        boolean s = cursor.moveToLast();

        Log.d("checkEmptyData"," Value: " + s);
        return s;
    }

    // Controls the limit of budget..
    public void checkLimitBudget(double totalSpend, double alert){
        if(totalSpend >= alert){
            alertOmLimitValue();
        }else{
            // Do nothing
        }
    }


    public void saveSpendingOnSpendDomain() {

             // Get the actual id..
            SQLiteDatabase db = helper.getReadableDatabase();
            String sql = "SELECT _id FROM trip";
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToLast();
            int tripId = cursor.getInt(0);
            Log.d("TripId from spending", "tripId: " + tripId);


            try {
                spend = new Spending();
                spend.setCategory(category.getSelectedItem().toString());
                spend.setDate(selectedDate);

                if(value.getText().toString().equals("")){
                    spend.setValue(0d);
                }else{
                    spend.setValue(Double.parseDouble(value.getText().toString()));
                }

                spend.setDescription(description.getText().toString());
                spend.setPlace(place.getText().toString());
                spend.setTripId(tripId);

                Log.d("Saved on Spending", "values: " + spend.getCategory() + " - " + spend.getDate() + " - " +
                        spend.getValue() + " - " + spend.getDescription() + " - " + spend.getPlace() + " actualID: " + spend.getTripId());
//                finish();
            }catch (NumberFormatException e){

                Toast.makeText(getApplicationContext(), "Spending was not saved: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Spending not saved: ", ">>: " + e.getMessage());
            }

    }

    // Get from Bundle Extra the selected trip _id..
    public int retrieveSelectedTripID(){

        Intent i = getIntent();
        Bundle b = i.getExtras();
        if(b!=null){
            int result = b.getInt("CameFromTripList");
            Log.d("DestinationID", "Bundle Extra: " + result);
            return result;
        }else {
            return 0;
        }
    }


    // Get from Bundle Extra the selected trip _id..
    public boolean isFromTripList(){

        Intent i = getIntent();
        Bundle b = i.getExtras();
        if(b!=null){
            boolean result = b.getBoolean("isFromTripList");
            Log.d("isFromTripList", "Bundle Extra: " + result);
            return result;
        }else {
            Log.d("isFromTripList", "Bundle Extra: " + "FALSE");
            return false;
        }
    }

    // Used to save spendings in the selected trip from TripList Activity..
    public void insertInSelectedTrip(String category, String date, double value,String description, String place, int itemId){


        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("category", category);
        values.put("date", date);
        values.put("value", value);
        values.put("description", description);
        values.put("place", place);
        values.put("trip_id", itemId);

        long result = db.insert("spending", null, values);

        if (result != -1) {
            Toast.makeText(this, "Register saved successfully..", Toast.LENGTH_SHORT).show();
            //finish();
        } else {
            Toast.makeText(this, "Register NOT saved! ", Toast.LENGTH_SHORT).show();

        }

    }




    public void saveSpending(View view) {

        tripListActivity = new TripListActivity();

        if(checkEmptyData()){

            saveSpendingOnSpendDomain();
            SQLiteDatabase db = helper.getWritableDatabase();

            if(isFromTripList()){
                insertInSelectedTrip(spend.getCategory(), spend.getDate(), spend.getValue(), spend.getDescription(), spend.getPlace(), retrieveSelectedTripID());
                //checkLimitBudget(tripListActivity.getTotalSpend(), tripListActivity.getAlertLimit());
                Log.d("CheckLimitBudget", "From isFromTripList");
                Log.d(">>>>>>>>>>", "" + spend.getCategory() + spend.getDate() + spend.getValue() +spend.getDescription() + spend.getPlace() + spend.getTripId() + retrieveSelectedTripID() );
                finish();
            }else{
                ContentValues values = new ContentValues();
                values.put("category", spend.getCategory());
                values.put("date", spend.getDate());
                values.put("value", spend.getValue());
                values.put("description", spend.getDescription());
                values.put("place", spend.getPlace());
                values.put("trip_id", spend.getTripId());

                // Show all information to save in DB..
                Log.d("Saved Informations", "Info: " + category.getSelectedItem().toString() + " - " + selectedDate + " - " +
                        value.getText().toString() + " - " + description.getText().toString() + " - " +
                        place.getText().toString() + " - " + spend.getTripId());


                long result = db.insert("spending", null, values);

                if (result != -1) {
                    Toast.makeText(this, "Register saved successfully..", Toast.LENGTH_SHORT).show();
                    //checkLimitBudget(tripListActivity.getTotalSpend(), tripListActivity.getAlertLimit());
                    Log.d("CheckLimitBudget", "From common place " + tripListActivity.getTotalSpend() + " " + tripListActivity.getAlertLimit());
                    finish();

                } else {
                    Toast.makeText(this, "Register NOT saved! ", Toast.LENGTH_SHORT).show();

                }
            }

        }else {
            Toast.makeText(getApplicationContext(), R.string.no_trip_msg, Toast.LENGTH_LONG).show();
            finish();
        }


    }



    public void updateDate() {

        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectedDate = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;

        // Condition to set Actual date or date before..
        if (selectedYear > actualYear || selectedMonth > actualMonth || selectedDay > actualDay) {
            Toast.makeText(getApplicationContext(), "Impossible to spend money at this date! Try again..", Toast.LENGTH_LONG).show();
            setDate();

        } else {
            spendDate.setText(selectedDate);
        }

        Log.d("Button Date", "selected Date: " + selectedDate); // Choosen Date!
        Log.d("DATE", "Actual Date: " + actualDate);
    }

    public void setDate() {
        new DatePickerDialog(SpendingActivity.this, d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    // Method to show the menu with exit button..
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_spending, menu);
        return true;
    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        switch (item.getItemId()) {

            case R.id.exit_spending:
                finish();
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void alertOmLimitValue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Limit Alert");
        builder.setMessage(R.string.limit_alert);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("ON CLICK"," GONE!");
            }
        });
        alert = builder.create();
        alert.show();

    }


}
