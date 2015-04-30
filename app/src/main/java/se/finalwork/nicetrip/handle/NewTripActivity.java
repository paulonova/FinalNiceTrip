package se.finalwork.nicetrip.handle;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import java.util.Calendar;



public class NewTripActivity extends Activity implements View.OnClickListener  {    //

    private int year;
    private int month;
    private int day;
    private Button arrivalBtn;
    private Button exitBtn;
    private Button saveBtn;

    private RadioGroup typeTrip;
    private String typeTripText;
    Calendar arrivalCalendar = Calendar.getInstance();
    Calendar exitCalendar = Calendar.getInstance();

    private DatabaseHelper helper;
    private EditText destiny, budget, numberPeople;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_trip);

        arrivalBtn = (Button)findViewById(R.id.arrivalBtn);
        arrivalBtn.setOnClickListener(this);
        exitBtn = (Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(this);
        updateArrivalDate();
        updateExitDate();

        destiny = (EditText) findViewById(R.id.destination);
        radioGroup = (RadioGroup)findViewById(R.id.typeTrip);
        budget = (EditText)findViewById(R.id.budget);
        numberPeople = (EditText)findViewById(R.id.number_people);

        saveBtn = (Button)findViewById(R.id.save_trip);



        // Prepare access to database..
        helper = new DatabaseHelper(this);

    }

    // Listener to the DatePicker btn..
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.arrivalBtn:
                setArriveDate();
                break;

            case R.id.exitBtn:
                setExitDate();
                break;
        }
    }

    DatePickerDialog.OnDateSetListener dArrival = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            arrivalCalendar.set(Calendar.YEAR,year);
            arrivalCalendar.set(Calendar.MONTH,monthOfYear);
            arrivalCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateArrivalDate();
        }
    };

    DatePickerDialog.OnDateSetListener dExit = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            exitCalendar.set(Calendar.YEAR,year);
            exitCalendar.set(Calendar.MONTH,monthOfYear);
            exitCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateExitDate();

        }
    };

    public void setArriveDate(){

        new DatePickerDialog(NewTripActivity.this, dArrival,arrivalCalendar.get(Calendar.YEAR),arrivalCalendar.get(Calendar.MONTH),arrivalCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void setExitDate(){

        new DatePickerDialog(NewTripActivity.this, dExit,exitCalendar.get(Calendar.YEAR),exitCalendar.get(Calendar.MONTH),exitCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void updateArrivalDate(){

        int year = arrivalCalendar.get(Calendar.YEAR);
        int month = arrivalCalendar.get(Calendar.MONTH);
        int day = arrivalCalendar.get(Calendar.DAY_OF_MONTH);

        arrivalBtn.setText(year + "/" + (month + 1) + "/" + day);
    }

    public void updateExitDate(){
        int year = exitCalendar.get(Calendar.YEAR);
        int month = exitCalendar.get(Calendar.MONTH);
        int day = exitCalendar.get(Calendar.DAY_OF_MONTH);

        exitBtn.setText(year + "/" + (month + 1) + "/" + day);

    }

    public void SetActualTrip(){

    }



    public void saveTrip(View view){
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("destiny", destiny.getText().toString());
        values.put("arrive_date", arrivalBtn.getText().toString());

        values.put("exit_date", exitBtn.getText().toString());
        values.put("budget", budget.getText().toString());
        values.put("number_peoples", numberPeople.getText().toString());



        int type = radioGroup.getCheckedRadioButtonId();
        if(type == R.id.vacationBtn){
            values.put("type_trip", Constants.TRIP_VACATIONS);
            setTypeTripText(getString(R.string.vacation));
        }else{
            values.put("type_trip", Constants.TRIP_BUSINESS);
            setTypeTripText(getString(R.string.business));
        }

        // Show all information to save in DB..
        Log.d("Saved Informations", "Info: " + destiny.getText().toString() + " - " + getTypeTripText() + " - " +
                                               arrivalBtn.getText().toString()+ " - " + exitBtn.getText().toString() + " - " +
                                               budget.getText().toString() + " - " + numberPeople.getText().toString());

        long result = db.insert("trip", null, values);

        if(result != -1){
            Toast.makeText(this, "Register saved successfully..", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(this, "Register NOT saved! ", Toast.LENGTH_SHORT).show();
        }


    }



    // Method to show the menu with exit button..
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trip_menu, menu);
        //getMenuInflater().inflate(R.menu.menu_first_page, menu);
        return true;
    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        switch (item.getItemId()){
            case R.id.new_spend:
                startActivity(new Intent(this,SpendingActivity.class));
                return true;
            case R.id.remove_trip:
                // remove from DataBase
                Toast.makeText(getApplicationContext(), "Remove from Database!", Toast.LENGTH_SHORT).show();
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


    // Getters and Setters
    public String getTypeTripText() {
        return typeTripText;
    }

    public void setTypeTripText(String typeTripText) {
        this.typeTripText = typeTripText;
    }
}
