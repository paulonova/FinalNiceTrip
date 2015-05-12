package se.finalwork.nicetrip.handle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import se.finalwork.nicetrip.dao.NiceTripDB;


public class ConfigurationsActivity extends Activity {

    NiceTripDB dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment()).commit();

    }

    @SuppressLint("ValidFragment")
    public class SettingFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preference);



            Preference showActualTrip = findPreference("actual_trip_mode");
            showActualTrip.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Toast.makeText(getApplicationContext(), R.string.trip_mode_text, Toast.LENGTH_LONG).show();
                    return false;
                }
            });


            Preference showDefaultLogin = findPreference("default_mode");
            showDefaultLogin.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {

                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(LoginActivity.DEFAULT_CONNECTED, false);
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();

                    return false;
                }
            });




            Preference showValueLimit = findPreference("value_limit");
            showValueLimit.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //codes in EditLimitValuePreference..
                    return false;
                }
            });


            Preference showDeleteDatabase = findPreference("delete_database");
            showDeleteDatabase.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
//                   // Codes in ResetDataBase class..
                    return false;
                }
            });
        }
    }

}
