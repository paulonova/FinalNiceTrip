package se.finalwork.nicetrip.handle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.widget.Toast;


public class ConfigurationsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment()).commit();



    }

    // Method to read preferences(String)
    public static String Read(Context context, final String key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(key, "");
    }


    // Methods to write preferences(String)
    public static void Write(Context context, final String key, final String value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }



    @SuppressLint("ValidFragment")
    public class SettingFragment extends PreferenceFragment{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preference);



            Preference showDefaultLogin = findPreference("default_mode");
            showDefaultLogin.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {

                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(LoginActivity.DEFAULT_CONNECTED, false);
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();

                    return false;
                }
            });

//            Preference showValueLimit = findPreference("value_limit");
//            showValueLimit.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//
////                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
////                    String valueLimit = prefs.getString("value_limit", null);
////
////
////
////                    Toast.makeText(getApplicationContext(), "ValueLimit: " + valueLimit, Toast.LENGTH_LONG).show();
//
//
//                    return false;
//                }
//            });





        }


    }


}
