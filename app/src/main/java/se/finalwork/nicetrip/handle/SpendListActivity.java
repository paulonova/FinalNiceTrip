package se.finalwork.nicetrip.handle;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SpendListActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private DatabaseHelper helper;
    private SimpleDateFormat dateFormat;
    private List<Map<String, Object>> spend;
    private String dateBefore = "";

    private String selectedDescription;
    private int selectedIdSpend;
    private int ItemId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new DatabaseHelper(this);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        String[] from = {"date", "description", "value", "category"};
        int[] to = {R.id.spend_date, R.id.spend_description, R.id.spend_value, R.id.spend_category};

        SimpleAdapter adapter = new SimpleAdapter(this, spendList(), R.layout.spend_list, from, to);
        adapter.setViewBinder(new SpendViewBinder()); // class SpendViewBinder
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        // register the new context menu..
        registerForContextMenu(getListView());

        retrieveActualTripID();
    }

    // Get from Bundle Extra the selected trip _id..
    public long retrieveActualTripID(){

        Intent i = getIntent();
        Bundle b = i.getExtras();
        if(b!=null){
            long result = b.getLong("DestinationID");
            //setItemId(b.getInt("ItemId"));
            Log.d("DestinationID", "Bundle Extra: " + result);
            //Log.d("Item Id","Item id: " + getItemId());
            return result;
        }else {
            return 0;
        }
    }

    private List<Map<String, Object>> spendList() {


        // Get the actual trip _id to save into him..
//        SQLiteDatabase db = helper.getReadableDatabase();
//        String sql = "SELECT _id FROM trip";
//        Cursor c = db.rawQuery(sql, null);
//        c.moveToLast();
//        int id = c.getInt(0);
//        Log.d("Test ID","ID: " + id);



        SQLiteDatabase db = helper.getReadableDatabase();
        String sql1 = "SELECT _id, date, description, value, category, trip_id FROM spending WHERE trip_id=?";
        Cursor cursor = db.rawQuery(sql1, new String[]{Long.toString(retrieveActualTripID())});
        cursor.moveToFirst();

        spend = new ArrayList<Map<String, Object>>();

        Log.d("SpendListActivity", "Passing here..");


        for (int i = 0; i < cursor.getCount(); i++) {

            Log.d("SpendListActivity", "Passing here too..");
            int id = cursor.getInt(0);
            String date = cursor.getString(1);
            Log.d("Date", "date: " + date);
            String description = cursor.getString(2);
            double value = cursor.getDouble(3);
            String category = cursor.getString(4);
            int tripId = cursor.getInt(5);

            Log.d("Database Info", "BudgetTable: " + " id: "+ id + " - " + date + " - " + description + " - " + value + " - " + category + " - " + tripId);

            Map<String, Object> item = new HashMap<String, Object>();
            item.put("date", date);
            item.put("description", description);
            item.put("value", getString(R.string.money) + " " + value);

            if (category.equalsIgnoreCase("fuel")) {
                item.put("category", R.color.category_fuel);
            } else if (category.equalsIgnoreCase("food")) {
                item.put("category", R.color.category_feeding);
            } else if (category.equalsIgnoreCase("transportation")) {
                item.put("category", R.color.category_transport);
            } else if (category.equalsIgnoreCase("accommodation")) {
                item.put("category", R.color.category_accommodation);
            } else if (category.equalsIgnoreCase("others")) {
                item.put("category", R.color.category_others);
            }

            spend.add(item);
            cursor.moveToNext();
        }

        return spend;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Map<String, Object> map = spend.get(position);
        setSelectedDescription((String) map.get("description"));

        String message = "Selected expense: " + getSelectedDescription() + " expenses: " + id ;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_spend_list, menu);



    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.remove_spending) {
//            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//            spend.remove(info.position);
//            getListView().invalidateViews();
//            dateBefore = "";
//
            //Remove from Database
//            Toast.makeText(getApplicationContext(), "Remove from Database..", Toast.LENGTH_SHORT).show();
//            return true;
//        }

        //Remove from Database
        SQLiteDatabase db = helper.getReadableDatabase();
        db.delete("spending", "description=?", new String[]{getSelectedDescription()});
        getListView().invalidateViews();
        Toast.makeText(getApplicationContext(), "Spend Removed..", Toast.LENGTH_SHORT).show();
        return true;

        //return super.onContextItemSelected(item);
    }

    private class SpendViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {

            Log.d("TEst Spend","View :" + view + " data: " + data + " textRepresentation: " + textRepresentation);

            if (view.getId() == R.id.spend_date) {
                if (!dateBefore.equals(data)) {
                    TextView textView = (TextView) view;
                    textView.setText(textRepresentation);
                    dateBefore = textRepresentation;
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
                return true;
            }
            if (view.getId() == R.id.spend_category) {
                Integer id = (Integer) data;
                view.setBackgroundColor(getResources().getColor(id));
                return true;
            }
            return false;
        }
    }

    public String getSelectedDescription() {
        return selectedDescription;
    }

    public void setSelectedDescription(String selectedDescription) {
        this.selectedDescription = selectedDescription;
    }

    public int getSelectedIdSpend() {
        return selectedIdSpend;
    }

    public void setSelectedIdSpend(int selectedIdSpend) {
        this.selectedIdSpend = selectedIdSpend;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }
}
