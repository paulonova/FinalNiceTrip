package finalwork.nicetrip.se.nicetrip;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;


public class NewTripActivity extends Activity implements View.OnClickListener {

    private int year;
    private int month;
    private int day;
    private Button arrivalBtn;
    private Button exitBtn;
    private Button saveBtn;

    private RadioGroup typeTrip;
    Calendar calendar = Calendar.getInstance();

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
        updateDate();

        saveBtn = (Button)findViewById(R.id.save_trip);

        destiny = (EditText) findViewById(R.id.destination);
        numberPeople = (EditText)findViewById(R.id.number_people);
        budget = (EditText)findViewById(R.id.budget);
        radioGroup = (RadioGroup)findViewById(R.id.typeTrip);

        // Prepare access to database..
        helper = new DatabaseHelper(this);

    }

    // class with the constants values...
    public class Constants{
        public static final int TRIP_VACATIONS = 1;
        public static final int TRIP_BUSINESS = 2;
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
        }else{
            values.put("type_trip", Constants.TRIP_BUSINESS);
        }

        long result = db.insert("trip", null, values);

        if(result != -1){
            Toast.makeText(this, "Register saved successfully..", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Register NOT saved! ", Toast.LENGTH_SHORT).show();
        }


    }




    public void updateDate(){
        arrivalBtn.setText(calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH));
        exitBtn.setText(calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH));

       /* arrivalBtn.setText(year + "/" + (month+1) + "/" + day);
        exitBtn.setText(year + "/" + (month+1) + "/" + day);*/
    }

    public void setDate(){
        new DatePickerDialog(NewTripActivity.this,d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }



    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateDate();
        }
    };



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.arrivalBtn:
                setDate();
                break;

            case R.id.exitBtn:
                setDate();
                break;
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


}
