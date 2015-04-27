package finalwork.nicetrip.se.nicetrip;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity {

    public static final String DEFAULT_CONNECTED = "be_connected";
    private EditText username;
    private EditText password;
    public static CheckBox beConnected;

    private Button confirm;
    private TextView goToRegister;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        beConnected = (CheckBox)findViewById(R.id.cbDefault);

       SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean checked = preferences.getBoolean(DEFAULT_CONNECTED, false);
        Log.d("DEFAULT_CONNECTED", "Value: " + checked);

        if(!checked){
            //Do nothing
        }else{
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }
            confirm = (Button)findViewById(R.id.confirmBtn);
            goToRegister = (TextView)findViewById(R.id.registerLinkTxt);
      }

    // Confirm Button and fields validation...
    public void confirmOnClick(View v){
        //To insert text in EditText...
        String usernameInfo = username.getText().toString();
        String passwordInfo = password.getText().toString();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString(RegisterPageActivity.USER_NAME, "");
        String password = preferences.getString(RegisterPageActivity.PASSWORD, "");

        if(usernameInfo.equals("") && passwordInfo.equals("")){
            String msg = getString(R.string.error_empty);
            Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_SHORT).show();

        }else if(usernameInfo.equals(username) && passwordInfo.equals(password)){
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(DEFAULT_CONNECTED, beConnected.isChecked());
            Log.d("CheckBox Default","Value: " + beConnected.isChecked());
            editor.commit();

            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }else {
            String msg = getString(R.string.error_autentication);
            Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_SHORT).show();
        }

    }

    // Text link to Registration DialogBox..
    public void goToRegistration(View v){

        Intent intent = new Intent(getApplicationContext(), RegisterPageActivity.class);
        startActivity(intent);
    }


    /**
     * Method to use when onBackPressed() is used.
     */
    public void alertBeforeClose(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage(R.string.close_alert);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do Nothing
            }
        });

        alert = builder.create();
        alert.show();

    }


    @Override
    public void onBackPressed() {
        alertBeforeClose();

    }
}
