package se.finalwork.nicetrip.views;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Config;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import se.finalwork.nicetrip.handle.ConfigurationsActivity;
import se.finalwork.nicetrip.handle.LoginActivity;
import se.finalwork.nicetrip.handle.R;


public class EditLimitValuePreference extends DialogPreference {

    private EditText setLimit;
    private Button ok;
    private Button cancel;

    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
    SharedPreferences.Editor editor = pref.edit();
    String valueLimit = pref.getString("value_limit", null);


    public EditLimitValuePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.limit_value_layout);

    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        setLimit = (EditText)v.findViewById(R.id.limit_value);
        setLimit.setHint(valueLimit);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        String value = setLimit.getEditableText().toString();

            // OK Button
        if(positiveResult){

            if(!value.equals("")){
                editor.putString("value_limit", value);
                editor.commit();
                valueLimit = pref.getString("value_limit", null);
                Toast.makeText(getContext(), "Limit value saved = " + valueLimit, Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getContext(), "No values were entered!", Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), "Old value is: " + valueLimit , Toast.LENGTH_LONG).show();
            }

            // Cancel Button
        }else if(!positiveResult){
            // Do nothing
        }
    }
}
