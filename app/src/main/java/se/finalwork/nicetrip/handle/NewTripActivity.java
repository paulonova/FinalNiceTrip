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
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.finalwork.nicetrip.dao.NiceTripDB;
import se.finalwork.nicetrip.domain.Trip;


public class NewTripActivity extends Activity implements View.OnClickListener {


    private int year;
    private int month;
    private int day;
    private Button arrivalBtn;
    private Button exitBtn;
    private Button saveBtn;
    private RadioGroup typeTrip;
    private String typeTripText;
    private DatabaseHelper helper;
    private EditText destiny, budget, numberPeople;
    private RadioGroup radioGroup;
    private Trip trip;
    private int id_Actual;

    Calendar arrivalCalendar = Calendar.getInstance();
    Calendar exitCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dArrival = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            arrivalCalendar.set(Calendar.YEAR, year);
            arrivalCalendar.set(Calendar.MONTH, monthOfYear);
            arrivalCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateArrivalDate();
        }
    };
    DatePickerDialog.OnDateSetListener dExit = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            exitCalendar.set(Calendar.YEAR, year);
            exitCalendar.set(Calendar.MONTH, monthOfYear);
            exitCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateExitDate();

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_trip);

        arrivalBtn = (Button) findViewById(R.id.arrivalBtn);
        arrivalBtn.setOnClickListener(this);
        exitBtn = (Button) findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(this);
        updateArrivalDate();
        updateExitDate();

        destiny = (EditText) findViewById(R.id.destination);
        radioGroup = (RadioGroup) findViewById(R.id.typeTrip);
        budget = (EditText) findViewById(R.id.budget);
        numberPeople = (EditText) findViewById(R.id.number_people);

        saveBtn = (Button) findViewById(R.id.save_trip);


        // Prepare access to database..
        helper = new DatabaseHelper(this);

    }

    // Listener to the DatePicker btn..
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.arrivalBtn:
                setArriveDate();
                break;

            case R.id.exitBtn:
                setExitDate();
                break;
        }
    }

    public void setArriveDate() {

        new DatePickerDialog(NewTripActivity.this, dArrival, arrivalCalendar.get(Calendar.YEAR),
                                                             arrivalCalendar.get(Calendar.MONTH),
                                                             arrivalCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void setExitDate() {

        new DatePickerDialog(NewTripActivity.this, dExit, exitCalendar.get(Calendar.YEAR),
                                                          exitCalendar.get(Calendar.MONTH),
                                                          exitCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void updateArrivalDate() {

        int year = arrivalCalendar.get(Calendar.YEAR);
        int month = arrivalCalendar.get(Calendar.MONTH);
        int day = arrivalCalendar.get(Calendar.DAY_OF_MONTH);

        arrivalBtn.setText(year + "/" + (month + 1) + "/" + day);
    }

    public void updateExitDate() {
        int year = exitCalendar.get(Calendar.YEAR);
        int month = exitCalendar.get(Calendar.MONTH);
        int day = exitCalendar.get(Calendar.DAY_OF_MONTH);

        exitBtn.setText(year + "/" + (month + 1) + "/" + day);

    }



    // Take saved trip from Database and save it again att the Trip object..
    public void setTripDomainInfo(){
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT _id, type_trip, destiny, arrive_date, exit_date, budget, number_peoples FROM trip";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();


        trip = new Trip();

        for (int i = 0; i < cursor.getCount(); i++) {

            trip.setId(cursor.getInt(0));
            trip.setTypeTrip(cursor.getString(1));
            trip.setDestiny(cursor.getString(2));
            trip.setArrivalDate(cursor.getString(3));
            trip.setExitDate(cursor.getString(4));
            trip.setBudget(cursor.getDouble(5));
            trip.setNumberPeoples(cursor.getInt(6));
            trip.setActualTrip(trip.getId());

            cursor.moveToNext();

        }

        cursor.close();
        Log.d("SetTripDomainInfo", " INFO: TRIP_DOMAIN " + trip.getId() + " - " + trip.getTypeTrip() + " - " + trip.getDestiny() + " - " + trip.getArrivalDate()
                + " - " + trip.getExitDate() + " - " + trip.getBudget() + " - " + trip.getNumberPeoples() + "  Actual trip: " + trip.getActualTrip());
    }



    // Button SAVE TRIP..
    public void saveTrip(View view) {

        SQLiteDatabase db = helper.getReadableDatabase();
        trip = new Trip();

        try {
            ContentValues values = new ContentValues();
            values.put("destiny", destiny.getText().toString());
            values.put("arrive_date", arrivalBtn.getText().toString());

            values.put("exit_date", exitBtn.getText().toString());
            values.put("budget", budget.getText().toString());
            values.put("number_peoples", numberPeople.getText().toString());

            int type = radioGroup.getCheckedRadioButtonId();
            if (type == R.id.vacationBtn) {
                values.put("type_trip", Constants.TRIP_VACATIONS);
                setTypeTripText(getString(R.string.vacation));
            } else {
                values.put("type_trip", Constants.TRIP_BUSINESS);
                setTypeTripText(getString(R.string.business));
            }

            // Show all information to save in DB..
            Log.d("Saved Informations", "Info: " + destiny.getText().toString() + " - " + getTypeTripText() + " - " +
                    arrivalBtn.getText().toString() + " - " + exitBtn.getText().toString() + " - " +
                    budget.getText().toString() + " - " + numberPeople.getText().toString());

            long result = db.insert("trip", null, values);

            if (result != -1) {
                Toast.makeText(this, "Register saved successfully..", Toast.LENGTH_SHORT).show();
                finish();
            }

        }catch (SQLiteException e){

            Toast.makeText(this, "Register NOT saved! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        // Get the current trip ID
        String sql = "SELECT _id FROM trip";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToLast();
        trip.setActualTrip(cursor.getInt(0));
        Log.d("setActualTrip"," TESTE ATUAL " + cursor.getInt(0));
        setTripDomainInfo();

    }



    // Method to show the menu with exit button..
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trip_menu, menu);
        return true;
    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        switch (item.getItemId()) {
            case R.id.exit_trip:
                finish();
                return true;

            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Getters and Setters
    public String getTypeTripText() {
        return typeTripText;
    }

    public void setTypeTripText(String typeTripText) {
        this.typeTripText = typeTripText;
    }





}
