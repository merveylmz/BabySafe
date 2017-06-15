package Fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.babysafe.babysafe.LoginActivity;
import com.babysafe.babysafe.R;
import com.babysafe.babysafe.SharedPreferencesUtil;

import Connect.ApiService;
import Connect.RetroClient;
import RetrofitModel.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.babysafe.babysafe.SharedPreferencesUtil.PRE_FULLNAME;
import static com.babysafe.babysafe.SharedPreferencesUtil.PRE_ID;

/**
 * Created by ss on 4.3.2017.
 */

public class Settings extends Fragment {

    private EditText editName, editSurName, editEmail, editParola;
    private Button btnGuncelle;
    ApiService api;
    String userid, name, surName, email, parola, fullName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_settings, container, false);

        editName = (EditText) view.findViewById(R.id.input_name);
        editSurName = (EditText) view.findViewById(R.id.input_surname);
        editEmail = (EditText) view.findViewById(R.id.input_email);
        editParola = (EditText) view.findViewById(R.id.input_password);

        btnGuncelle = (Button) view.findViewById(R.id.btn_guncelle);


        btnGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid = LoginActivity.user.getId();
                //userid = SharedPreferencesUtil.getFromSharedPrefs(getActivity(), PRE_ID);

                if (validate()) {
                    getUserUpdate(userid, fullName, parola, email);
                    return;
                }
            }
        });

        getUser();

        return view;
    }

    public void getUser() {
        String FullName = LoginActivity.user.getAdsoyad();
        //String FullName = SharedPreferencesUtil.getFromSharedPrefs(getActivity(), PRE_FULLNAME);
        int index = 0;
        for (int i = 0; i < FullName.length() - 1; i++) {
            if (FullName.charAt(i) == ' ') {
                index = i;
            }
        }

        editName.setText(FullName.substring(0, index));
        editSurName.setText(FullName.substring(index + 1, FullName.length()));
        editEmail.setText(LoginActivity.user.getEmail());
        editParola.setText("");
    }
    public void getUserUpdate (String userid, String adsoyad, String parola, String email) {

        try {

            api = RetroClient.getApiService();
            Call<User> call = api.getUserUpdate(userid, adsoyad, parola, email);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    LoginActivity.user = response.body();
                    getUser();
                    Snackbar.make(getView(), "Güncelleme İşlemi Başarılı",Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Snackbar.make(getView(), "Sunucuya Bağlantı Sağlanamadı",Snackbar.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean validate() {
        boolean valid = true;

        name = editName.getText().toString();
        surName = editSurName.getText().toString();
        fullName = name + " " + surName;
        email = editEmail.getText().toString();
        parola = editParola.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            editName.setError("at least 3 characters");
            valid = false;
        } else {
            editName.setError(null);
        }

        if (surName.isEmpty()) {
            editSurName.setError("Enter Valid Surname");
            valid = false;
        } else {
            editSurName.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("enter a valid email address");
            valid = false;
        } else {
            editEmail.setError(null);
        }

        if (parola.isEmpty() || parola.length() < 4 || parola.length() > 10) {
            editParola.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            editParola.setError(null);
        }

        return valid;
    }
}
