package se.finalwork.nicetrip.handle;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.spend_list);

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
    }

    /* db.execSQL("CREATE TABLE spending (_id INTEGER PRIMARY KEY," +
                " category TEXT, date DATE, value DOUBLE," +
                " description TEXT, place TEXT, trip_id INTEGER," +
                " FOREIGN KEY(trip_id) REFERENCES trip(_id));");*/



    private List<Map<String, Object>>spendList(){

        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT date, description, value, category, trip_id FROM spending";
        //Cursor cursor = db.rawQuery("SELECT date, description, value, category FROM spending WHERE trip_id = ?", new String[]{id});
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        spend = new ArrayList<Map<String, Object>>();

        Log.d("SpendListActivity", "Passing here..");

        for (int i = 0; i < cursor.getCount(); i++) {

            Log.d("SpendListActivity", "Passing here too..");
            String date = cursor.getString(0);
            String description = cursor.getString(1);
            double value = cursor.getDouble(2);
            String category = cursor.getString(3);
            int tripId = cursor.getInt(4);

            Log.d("Database Info", "BudgetTable: " + date + " - " + description + " - " + value + " - " + category + " - " + tripId);

            Map<String, Object> item = new HashMap<String, Object>();
            item.put("date", date);
            item.put("description", description);
            item.put("value", getString(R.string.money) + " " + value);

            if(category.equalsIgnoreCase("fuel")){
                item.put("category", R.color.category_fuel);
            }else if(category.equalsIgnoreCase("food")){
                item.put("category", R.color.category_feeding);
            }else if(category.equalsIgnoreCase("transportation")){
                item.put("category", R.color.category_transport);
            }else if(category.equalsIgnoreCase("accommodation")){
                item.put("category", R.color.category_accommodation);
            }else if(category.equalsIgnoreCase("others")){
                item.put("category", R.color.category_others);
            }

            spend.add(item);
            cursor.moveToNext();
        }

//        Map<String, Object> item = new HashMap<String, Object>();
//        item.put("date","05/08/2010");
//        item.put("description","Hotel");
//        item.put("value","Kr 350,00");
//        item.put("category",R.color.category_accommodation);
//        spend.add(item);

        return spend;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

       Map<String, Object> map= spend.get(position);
        String description = (String)map.get("description");
        String message = "Selected expense: " + description;
        Toast.makeText(this,message, Toast.LENGTH_SHORT ).show();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_spend_list, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getItemId() == R.id.remove_spending){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            spend.remove(info.position);
            getListView().invalidateViews();
            dateBefore = "";

            //Remove from Database
            Toast.makeText(getApplicationContext(), "Remove from Database..", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private String dateBefore = "";

    private class SpendViewBinder implements SimpleAdapter.ViewBinder{

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {

            if(view.getId() == R.id.spend_date){
                if(!dateBefore.equals(data)){
                    TextView textView = (TextView) view;
                    textView.setText(textRepresentation);
                    dateBefore = textRepresentation;
                    view.setVisibility(View.VISIBLE);
                }else{
                    view.setVisibility(View.GONE);
                }
                return true;
            }
            if(view.getId() == R.id.spend_category){
                Integer id = (Integer) data;
                view.setBackgroundColor(getResources().getColor(id));
                return true;
            }
            return false;
        }
    }

}
