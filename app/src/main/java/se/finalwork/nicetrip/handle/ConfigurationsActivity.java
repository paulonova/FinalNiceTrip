package se.finalwork.nicetrip.handle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.os.Bundle;



public class ConfigurationsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment()).commit();



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

                    //LoginActivity.beConnected.setChecked(false);
//                    SharedPreferences prefDefault = getSharedPreferences(LoginActivity.DEFAULT_CONNECTED, Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();

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
        }


    }


}
