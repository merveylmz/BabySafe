package com.babysafe.babysafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.babysafe.babysafe.R;
import com.ubidots.ApiClient;
import com.ubidots.DataSource;

import java.util.ArrayList;
import java.util.HashMap;

import Connect.ApiService;
import Connect.RetroClient;
import RetrofitModel.User;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.babysafe.babysafe.SharedPreferencesUtil.PRE_EMAIL;
import static com.babysafe.babysafe.SharedPreferencesUtil.PRE_FULLNAME;
import static com.babysafe.babysafe.SharedPreferencesUtil.PRE_ID;
import static com.babysafe.babysafe.SharedPreferencesUtil.PRE_TOKEN;
import static com.babysafe.babysafe.SharedPreferencesUtil.setToSharedPreferences;


public class LoginActivity extends AppCompatActivity {
    /*** Genel olarak heryerden ulaşılabilecek kullanıcı : user ***/
    public static User user;
    public static ArrayList<String> list = new ArrayList<String>();
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static String TOKENID = "";
    public static HashMap<String, DataSource> ubiDataSources = new HashMap<>();
    public static String API_KEY = "296acf2680c7af72533bf940487768c9b52adaac";
    public static DataSource[] dataSources;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;

    ProgressDialog progressDialog;

    private MyDBHandler dbHandler;
    ApiService api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        dbHandler = new MyDBHandler(this, null, null, 1);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isNetworkAvailable()) {
                    login();
                } else {
                    Snackbar.make(v, "Sorry!, No Network Connection", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        if (SharedPreferencesUtil.getFromSharedPrefs(LoginActivity.this, PRE_EMAIL) != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        //new UbiDataSources().execute();
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }


        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        getLogin(email, password);
    }

    public void getLogin(String email, String parola) {

        api = RetroClient.getApiService();

        try {
            Call<User> call = api.getUserLogin(email, parola);
            call.enqueue(new Callback<User>() {

                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    user = response.body();
                    LoginActivity.TOKENID = user.getToken();
                    int success = response.body().getSuccess();
                    String messeage = response.body().getMessage();

                    if (success == 0) {
                        progressDialog.dismiss();
                        Snackbar.make(getCurrentFocus(), messeage, Snackbar.LENGTH_SHORT).show();
                    }

                    if (success == 1) {
                        dbHandler.deleteToken();
                        dbHandler.addToken(Integer.parseInt(user.getId()), user.getToken());

                        setToSharedPreferences(LoginActivity.this, PRE_EMAIL, user.getEmail());
                        setToSharedPreferences(LoginActivity.this, PRE_TOKEN, user.getToken());
                        setToSharedPreferences(LoginActivity.this, PRE_ID, user.getId());
                        setToSharedPreferences(LoginActivity.this, PRE_FULLNAME, user.getAdsoyad());

                        _loginButton.setEnabled(false);
                        progressDialog.dismiss();

                        onLoginSuccess();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private class UbiDataSources extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                ApiClient apiClient = new ApiClient(API_KEY);
                dataSources= apiClient.getDataSources();      //Device "id" leri burada alınıyor.

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
