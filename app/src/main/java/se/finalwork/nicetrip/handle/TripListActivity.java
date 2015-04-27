package se.finalwork.nicetrip.handle;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




    public class TripListActivity extends ListActivity
                                    implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener, SimpleAdapter.ViewBinder {


    private AlertDialog dialogConfirmation;
    private AlertDialog alertDialog;
    private int selectedTrip;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        trips = new ArrayList<Map<String, Object>>();

        Map<String, Object> item = new HashMap<String, Object>();
        item.put("image",R.drawable.business);
        item.put("destiny","Stockholm");
        item.put("date","02/02/2015 to 05/02/2015");
        item.put("total","Total Spend: Kr 314,00");
        item.put("progressBar", new Double[]{500.0, 450.0, 314.0});
        trips.add(item);

        item = new HashMap<String, Object>();
        item.put("image",R.drawable.vacations);
        item.put("destiny","Malmö");
        item.put("date","05/03/2015 to 05/04/2015");
        item.put("total","Total Spend: Kr 550,00");
        item.put("progressBar", new Double[]{650.0, 590.0, 550.0});
        trips.add(item);

        item = new HashMap<String, Object>();
        item.put("image",R.drawable.business);
        item.put("destiny","Göteborg");
        item.put("date","05/03/2015 to 05/04/2015");
        item.put("total","Total Spend: Kr 600,00");
        item.put("progressBar", new Double[]{700.0, 650.0, 600.0});
        trips.add(item);

        return trips;
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

        switch (item){

            case 0:
                startActivity(new Intent(this, NewTripActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, SpendingActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, SpendListActivity.class));
                break;
            case 3:
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



    /*
    Problemas com a ProgressBar...
    Alguma coisa esta errada e precisa ser refeita...
    parou pg. 107
     */

}
