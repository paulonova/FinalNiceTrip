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

/**
 * Created by PauloRogério on 2015-02-18.
 */
public class LoginActivity extends Activity {

    public static final String DEFAULT_CONNECTED = "be_connected";
    private EditText username;
    private EditText password;
    public static CheckBox beConnected;

    private Button confirm;
    private TextView goToRegister;
    private DBRegistration db;
    private AlertDialog alert;
    private boolean checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        beConnected = (CheckBox)findViewById(R.id.cbDefault);
        checkDefaultBox();

        // Check if default checkBox is checked...
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean connected = preferences.getBoolean(DEFAULT_CONNECTED, false);

        if(connected){
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }

        confirm = (Button)findViewById(R.id.confirmBtn);
        goToRegister = (TextView)findViewById(R.id.registerLinkTxt);

    }

    public boolean checkDefaultBox(){

       SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
       checked = preferences.getBoolean(DEFAULT_CONNECTED, false);

        if(checked){
            beConnected.setChecked(true);
        }else{
            beConnected.setChecked(false);
        }
        return checked;
    }



    public void confirmOnClick(View v){
        //To insert text in EditText...
        //String usernameInfo = username.getText().toString();
        //String passwordInfo = password.getText().toString();

        String usernameInfo = "paulo";
        String passwordInfo = "0144";

        if(usernameInfo.equals("paulo") && passwordInfo.equals("0144")){

            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putBoolean(DEFAULT_CONNECTED, beConnected.isChecked());
            editor.commit();

            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();

        }else {
            String msgError = getString(R.string.error_autentication);
            Toast toast = Toast.makeText(this, msgError, Toast.LENGTH_SHORT);
            toast.show();
        }


    }




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
