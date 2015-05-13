package se.finalwork.nicetrip.views;


import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import se.finalwork.nicetrip.dao.NiceTripDB;
import se.finalwork.nicetrip.handle.R;

public class ResetDataBase extends DialogPreference {


    private TextView textView;
    NiceTripDB dbHelper;

    public ResetDataBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.delete_database);
    }



    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        textView = (TextView)view.findViewById(R.id.delete_database);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if(positiveResult){
            String msg = "Drop and re-create table was successfully!";
            try {
                dbHelper = new NiceTripDB(getContext());
                dbHelper.dropAllDataBase();
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }catch (SQLiteException e){
                Toast.makeText(getContext(), "Drop and re-create table did not worked.." + e.getMessage(), Toast.LENGTH_LONG).show();
            }


        }else {
            // Do nothing
        }
    }
}
