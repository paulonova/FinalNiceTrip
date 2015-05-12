package se.finalwork.nicetrip.handle;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

    private Trip trip;



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

    }


    public void saveSpendingOnSpendDomain() {

        // Get the actual id..
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT _id FROM trip";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToLast();
        int tripId = cursor.getInt(0);
        Log.d("TripId from spending","tripId: " + tripId);

        try {
            spend = new Spending();
            spend.setCategory(category.getSelectedItem().toString());
            spend.setDate(selectedDate);
            spend.setValue(Double.parseDouble(value.getText().toString()));
            spend.setDescription(description.getText().toString());
            spend.setPlace(place.getText().toString());
            spend.setTripId(tripId);
            Toast.makeText(getApplicationContext(), "Saved in Spending", Toast.LENGTH_SHORT).show();


            Log.d("Saved on Spending", "values: " + spend.getCategory() + " - " + spend.getDate() + " - " +
                    spend.getValue() + " - " + spend.getDescription() + " - " + spend.getPlace() + " actualID: " + spend.getTripId());
            finish();
        }catch (NumberFormatException e){

            Toast.makeText(getApplicationContext(), "Spending was not saved: " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }


    public void saveSpending(View view) {

        saveSpendingOnSpendDomain();

        SQLiteDatabase db = helper.getWritableDatabase();

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
            finish();
        } else {
            Toast.makeText(this, "Register NOT saved! ", Toast.LENGTH_SHORT).show();
        }

    }


    // Saving spending in DAO..
    public void setSpendDomainInfo(){
        spend = new Spending();

        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT _id, category, date, value, description, place, trip_id FROM spending";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {

            spend.setId(cursor.getInt(0));
            spend.setCategory(cursor.getString(1));
            spend.setDate(cursor.getString(2));
            spend.setValue(cursor.getDouble(3));
            spend.setDescription(cursor.getString(4));
            spend.setPlace(cursor.getString(5));
            spend.setTripId(cursor.getInt(6));
            cursor.moveToNext();


            Log.d("SetSpendingDomainInfo", " INFO: SPENDING_DOMAIN " + spend.getId() + " - " + spend.getCategory() + " - " + spend.getDate() + " - " +
                                                                       spend.getValue() + " - " + spend.getDescription() + " - " + spend.getPlace() + " - " +
                                                                       spend.getTripId());
        }

        cursor.close();
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


}
