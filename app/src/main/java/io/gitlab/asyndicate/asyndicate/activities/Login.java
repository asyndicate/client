package io.gitlab.asyndicate.asyndicate.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

import io.gitlab.asyndicate.asyndicate.R;
import io.gitlab.asyndicate.asyndicate.helpers.Settings;

public class Login extends AppCompatActivity {

    public static final String TAG = "Login";
    public static int remember_count;

    private EditText usernameEditText;
    private EditText passwordEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        File me = new File(Settings.getInstance().getContext().getFilesDir() + "/me.json");
        remember_count = 0;
        Log.d("Login", "Attempting to resume session from: " + me.getAbsolutePath());

        if (me.exists()) {
            Log.d("Login", "Attempting to resume session from: " + me.getAbsolutePath());
        }
        Log.d("Home", "Starting login");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.pass);
        TextView titleTextView = (TextView) findViewById(R.id.textView_title);
        Button loginButton = (Button) findViewById(R.id.btn_login);

        titleTextView.setText(R.string.details);

        loginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (!username.equals("")) {
                    if (!password.equals("")) {
//                        login(username, password);
                    } else {
                        passwordEditText.setError("Required");
                    }
                } else {
                    usernameEditText.setError("Required");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        new Utils(this).quit();
    }

}
