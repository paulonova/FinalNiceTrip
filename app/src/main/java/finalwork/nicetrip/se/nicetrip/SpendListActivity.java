package finalwork.nicetrip.se.nicetrip;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SpendListActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private List<Map<String, Object>> spend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.spend_list);

        String[] from = {"date", "description", "value", "category"};
        int[] to = {R.id.spend_date, R.id.spend_description, R.id.spend_value, R.id.spend_category};

        SimpleAdapter adapter = new SimpleAdapter(this, spendList(), R.layout.spend_list, from, to);
        adapter.setViewBinder(new SpendViewBinder()); // class SpendViewBinder
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        // register the new context menu..
        registerForContextMenu(getListView());
    }

    private List<Map<String, Object>>spendList(){
        spend = new ArrayList<Map<String, Object>>();

        Map<String, Object> item = new HashMap<String, Object>();
        item.put("date","05/03/2015");
        item.put("description","Hotel");
        item.put("value","Kr 350,00");
        item.put("category",R.color.category_accommodation);
        spend.add(item);

        item = new HashMap<String, Object>();
        item.put("date","05/03/2015");
        item.put("description","Car rent");
        item.put("value","Kr 290,00");
        item.put("category",R.color.category_transport);
        spend.add(item);

        item = new HashMap<String, Object>();
        item.put("date","01/01/2015");
        item.put("description","Kina food");
        item.put("value","Kr 150,00");
        item.put("category",R.color.category_feeding);
        spend.add(item);

        item = new HashMap<String, Object>();
        item.put("date","01/01/2015");
        item.put("description","IT Magazine");
        item.put("value","Kr 90,00");
        item.put("category",R.color.category_others);
        spend.add(item);

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
