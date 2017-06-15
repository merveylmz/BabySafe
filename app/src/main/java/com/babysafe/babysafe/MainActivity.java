package com.babysafe.babysafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.babysafe.babysafe.R;

import Fragments.HomePage;
import Fragments.Settings;
import Fragments.Tokens;

import static com.babysafe.babysafe.SharedPreferencesUtil.PRE_EMAIL;


public class MainActivity extends ActionBarActivity {

    private DrawerLayout drawerLayout ;
    NavigationView navigationView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(getApplicationContext(), MyService.class)); //Servis çalıştırılıyor.

        MyService.numMessages = 0;

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        try {
            View header = navigationView.getHeaderView(0);
            TextView txtFullName = (TextView) header.findViewById(R.id.txt_name);
            TextView txtEmail = (TextView) header.findViewById(R.id.txt_email);

            txtEmail.setText(LoginActivity.user.getEmail());
            txtFullName.setText(LoginActivity.user.getAdsoyad());
        } catch (Exception e) {
        }

        setupToolbar();

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        menuItem.setChecked(true);
                        int id = menuItem.getItemId();
                        if (R.id.item_navigation_drawer_homepage == id) {
                            HomePage();
                        } else if (R.id.item_navigation_drawer_tokens == id) {
                            Tokens();
                        } else if (R.id.item_navigation_drawer_settings == id) {
                            Settings();
                        } else if (R.id.item_navigation_drawer_signout == id) {
                            if (SharedPreferencesUtil.deleteFromSharedPrefs(MainActivity.this, PRE_EMAIL)) {
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                        }

                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
        HomePage();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);


            alertdialog.setMessage("Uygulamayı kapatmak istediğinizden emin misiniz?");
            alertdialog.setCancelable(false).setPositiveButton("Evet", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alert=alertdialog.create();
            alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar(){
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbars);
            setSupportActionBar(toolbar);
            //NavigationView ekranda açmak için kullancagimiz iconu ActionBar'da gösterilmesini sagladik
            final ActionBar ab = getSupportActionBar();
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            ab.setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void HomePage () {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomePage()).commit();
    }

    public void Settings () {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Settings()).commit();
    }

    public void Tokens() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Tokens()).commit();
    }
}
