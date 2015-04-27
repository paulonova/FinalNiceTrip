package se.finalwork.nicetrip.handle;

import android.app.Activity;
import android.app.DatePickerDialog;
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



public class SpendingActivity extends Activity implements View.OnClickListener {

    private int year, month, day;
    private int actualYear, actualMonth, actualDay;
    private int selectedYear, selectedMonth, selectedDay;
    private Button spendDate;
    private Button spendingBtn;
    private String selectedDate;
    private String actualDate;
    private EditText value, description, place;
    Calendar calendar = Calendar.getInstance();
    private Spinner category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spending);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_spend, android.R.layout.simple_spinner_item);
        category = (Spinner)findViewById(R.id.category);
        category.setAdapter(adapter);

        value = (EditText)findViewById(R.id.value);
        description = (EditText)findViewById(R.id.descriptionTxt);
        place = (EditText)findViewById(R.id.placeTxt);


        actualYear = calendar.get(Calendar.YEAR);
        actualMonth = calendar.get(Calendar.MONTH);
        actualDay = calendar.get(Calendar.DAY_OF_MONTH);
        actualDate = actualYear + "/" + (actualMonth+1) + "/" + actualDay;
        selectedDate = selectedYear + "/" + (selectedMonth+1) + "/" + selectedDay;

        spendDate = (Button)findViewById(R.id.date);
        spendDate.setOnClickListener(this);
        updateDate();

        spendingBtn = (Button)findViewById(R.id.spending);

    }

    public void updateDate(){

        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        selectedDate = selectedYear + "/" + (selectedMonth+1) + "/" + selectedDay;

        if(selectedYear > actualYear || selectedMonth > actualMonth || selectedDay > actualDay){
            Toast.makeText(getApplicationContext(),"Impossible to spend money at this date! Try again..",Toast.LENGTH_LONG).show();
            setDate();

        }else{
            spendDate.setText(selectedYear + "/" + (selectedMonth+1) + "/" + selectedDay);
        }


        Log.d("Button Date","selected Date: " + selectedDate); // Choosen Date!
        Log.d("DATE", "Actual Date: " + actualDate);
    }

    public void setDate(){
        new DatePickerDialog(SpendingActivity.this,d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
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

        setDate();
    }


    // Method to show the menu with exit button..
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_spending, menu);
        //getMenuInflater().inflate(R.menu.menu_first_page, menu);
        return true;
    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        switch (item.getItemId()){
            case R.id.remove_spending:
                // remove from DataBase
                Toast.makeText(getApplicationContext(), "Remove from Database!", Toast.LENGTH_SHORT).show();
                return true;
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
