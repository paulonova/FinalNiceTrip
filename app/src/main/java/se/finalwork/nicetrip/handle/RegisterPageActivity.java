package se.finalwork.nicetrip.handle;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterPageActivity extends Activity implements View.OnClickListener {

    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";

    private EditText regUsername;
    private EditText regPassword;

    private Button saveButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**Method to take off the titleBar in register layout...
         * This need to be exactly here, before the setContentView.. */
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.new_register_layout);


        regUsername = (EditText) findViewById(R.id.username);
        regPassword = (EditText) findViewById(R.id.password);

        saveButton = (Button) findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(this);

        cancelButton = (Button) findViewById(R.id.cancelBtn);
        cancelButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.saveBtn:
                checkInputValues();
                break;

            case R.id.cancelBtn:
                finish();
                Toast.makeText(getApplicationContext(), "Register canceled", Toast.LENGTH_SHORT).show();
        }
    }


    public void showSharedInfo() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString(USER_NAME, "");
        String password = preferences.getString(PASSWORD, "");

        Log.d("ShowSharedInfo","Username: " + username + " Password: "+ password);

    }

    public void checkInputValues() {

        String username = regUsername.getText().toString();
        String password = regPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.register_error, Toast.LENGTH_SHORT).show();

        } else {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(USER_NAME, regUsername.getText().toString());
            editor.putString(PASSWORD, regPassword.getText().toString());
            editor.apply();
            Toast.makeText(getApplicationContext(), R.string.register_ok, Toast.LENGTH_SHORT).show();
            finish();

        }


    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
