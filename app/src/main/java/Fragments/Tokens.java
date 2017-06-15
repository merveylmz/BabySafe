package Fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.babysafe.babysafe.LoginActivity;
import com.babysafe.babysafe.MyDBHandler;
import com.babysafe.babysafe.R;
import com.babysafe.babysafe.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import Connect.ApiService;
import Connect.RetroClient;
import RetrofitModel.Message;
import RetrofitModel.Token;
import RetrofitModel.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.babysafe.babysafe.SharedPreferencesUtil.PRE_ID;

/**
 * Created by ss on 4.3.2017.
 */

public class Tokens extends Fragment {
    private ListView listView;
    private ArrayAdapter arrayAdapter;
    EditText tokenId;
    Button btnSil, btnGuncelle, btnKullan;
    String userid;

    private MyDBHandler dbHandler;
    ArrayList<String> arrlist = new ArrayList<String>();
    ApiService api;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_tokens, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        tokenId = (EditText) view.findViewById(R.id.input_tokenId);
        btnGuncelle = (Button) view.findViewById(R.id.btn_guncelle);
        btnSil = (Button) view.findViewById(R.id.btn_sil);
        btnKullan = (Button) view.findViewById(R.id.btn_kullan);

        userid = SharedPreferencesUtil.getFromSharedPrefs(getActivity(), PRE_ID);

        dbHandler = new MyDBHandler(getContext(), null, null, 1);

        arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.row_list, arrlist);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                tokenId.setText(arrlist.get(position));
            }
        });

        btnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tokens = tokenId.getText().toString();
                if (!tokenId.getText().toString().isEmpty()) {
                    getTokenDelete(userid, tokens);
                } else {
                    Snackbar.make(getView(), "Token Seçiniz",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        btnGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tokens = tokenId.getText().toString();

                if (!tokens.isEmpty()) {
                    getTokenInsert(userid, tokens);
                } else {
                    Snackbar.make(getView(), "Token Giriniz",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        btnKullan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tokens = tokenId.getText().toString();
                if(!tokens.isEmpty()){
                    getTokenUptade(userid, tokens);
                    dbHandler.deleteToken();
                    getUserDataToken(userid);
                    Snackbar.make(getView(), "Token Seçildi",Snackbar.LENGTH_SHORT).show();
                }
            }

        });
        getTokenAll(userid);
        return view;
    }

    public void getUserDataToken(String userid) {
        try {

            api = RetroClient.getApiService();
            Call<User> call = api.getUserTokenFirst(userid);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    LoginActivity.user = response.body();
                    dbHandler.addToken(Integer.parseInt(LoginActivity.user.getId()), LoginActivity.user.getToken());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void getTokenUptade(String userid, String token) {
        try {

            api = RetroClient.getApiService();
            Call<Message> call = api.getUserToken(userid, token);

            call.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();

                    if (success == 0) {
                        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
                    }

                    if (success == 1) {

                    }

                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getTokenInsert(String userid, String token) {
        try {
            api = RetroClient.getApiService();
            Call<List<Token>> call = api.getTokenInsert(userid, token);

            call.enqueue(new Callback<List<Token>>() {
                @Override
                public void onResponse(Call<List<Token>> call, Response<List<Token>> response) {
                    arrlist = new ArrayList<String>();
                    for (Token token : response.body()) {
                        arrlist.add(token.getToken());
                    }

                    tokenId.setText("");
                    arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.row_list, arrlist);
                    listView.setAdapter(arrayAdapter);
                }

                @Override
                public void onFailure(Call<List<Token>> call, Throwable t) {
                    Snackbar.make(getView(), "Sunucuya Bağlantı Sağlanamadı",Snackbar.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    public void getTokenUpdate(String userid, String tokenid, String token) {
        try {
            api = RetroClient.getApiService();

            Call<List<Token>> call = api.getTokenUpdate(userid, tokenid, token);
            call.enqueue(new Callback<List<Token>>() {
                @Override
                public void onResponse(Call<List<Token>> call, Response<List<Token>> response) {
                    arrlist = new ArrayList<String>();
                    for (Token token : response.body()) {
                        arrlist.add(token.getToken());
                    }
                    arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.row_list, arrlist);
                    listView.setAdapter(arrayAdapter);
                }

                @Override
                public void onFailure(Call<List<Token>> call, Throwable t) {
                    Snackbar.make(getView(), "Sunucuya Bağlantı Sağlanamadı",Snackbar.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    public void getTokenDelete(String userid, String tokenid) {
        try {
            api = RetroClient.getApiService();
            Call<List<Token>> call = api.getTokenDelete(userid, tokenid);

            call.enqueue(new Callback<List<Token>>() {
                @Override
                public void onResponse(Call<List<Token>> call, Response<List<Token>> response) {
                    arrlist = new ArrayList<String>();
                    for (Token token : response.body()) {
                        arrlist.add(token.getToken());
                    }

                    tokenId.setText("");

                    arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.row_list, arrlist);
                    listView.setAdapter(arrayAdapter);
                }

                @Override
                public void onFailure(Call<List<Token>> call, Throwable t) {
                    Snackbar.make(getView(), "Sunucuya Bağlantı Sağlanamadı",Snackbar.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTokenAll(String userid) {
        api = RetroClient.getApiService();
        try {
            Call<List<Token>> call = api.getTokenAlls(userid);
            call.enqueue(new Callback<List<Token>>() {
                @Override
                public void onResponse(Call<List<Token>> call, Response<List<Token>> response) {
                    arrlist = new ArrayList<String>();
                    for (Token token : response.body()) {
                        arrlist.add(token.getToken());
                    }

                    arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.row_list, arrlist);
                    listView.setAdapter(arrayAdapter);
                }

                @Override
                public void onFailure(Call<List<Token>> call, Throwable t) {
                    Snackbar.make(getView(), "Sunucuya Bağlantı Sağlanamadı",Snackbar.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
