package se.finalwork.nicetrip.handle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import se.finalwork.nicetrip.handle.R;


public class DashboardActivity extends Activity {

    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
    }


    public void selectOption(View v){

        switch(v.getId()){
            case R.id.new_trips:
                startActivity(new Intent(getApplicationContext(), NewTripActivity.class));
                break;
            case R.id.new_spending:
                startActivity(new Intent(getApplicationContext(), SpendingActivity.class));
                break;
            case R.id.my_trips:
                startActivity(new Intent(getApplicationContext(), TripListActivity.class));
                break;
            case R.id.configurations:
                startActivity(new Intent(getApplicationContext(), ConfigurationsActivity.class));
                break;
        }

        //  Shows a Message Toast with the selected item..
     /* TextView textView = (TextView) v;
        String option = "Option: " + textView.getText().toString();
        Toast.makeText(this, option, Toast.LENGTH_LONG).show(); */
    }

    // Method to show the menu with exit button..
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashbord_menu, menu);
        //getMenuInflater().inflate(R.menu.menu_first_page, menu);
        return true;
    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        finish();
        return true;
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


    /**
     * Method to use when onBackPressed() is used.
     */
    public void alertBeforeClose(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage(R.string.close_alert);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do Nothing
            }
        });

        alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        alertBeforeClose();

    }


}
