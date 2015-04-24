package finalwork.nicetrip.se.nicetrip;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


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

                    SharedPreferences prefDefault = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefDefault.edit();
                    editor.putBoolean(LoginActivity.DEFAULT_CONNECTED, false);
                    editor.apply();

                    Toast.makeText(getApplicationContext(), "Login Page is now available..", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }


    }


}
