package org.estrada.tp1;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.estrada.tp1.databinding.ActivityBaseBinding;

public class BaseActivity extends AppCompatActivity {
    private static String NomDeUsager;
    private ActionBarDrawerToggle abToggle;
    String currentActivity; // Évite la double ouverture d'une activité
    ActivityBaseBinding bindingBase;
    @Override
    public void setContentView(View view) {
        bindingBase = ActivityBaseBinding.inflate(getLayoutInflater());
        bindingBase.frameLayoutID.addView (view);
        super.setContentView(bindingBase.drawerLayoutID);
        getIncomingIntent();
        bindingBase.navigationViewID.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()){
                    case (R.id.menuAccueil):
                        if (!currentActivity.equals("Accueil")){
                            i = new Intent(BaseActivity.this, Accueil.class);
                            startActivity(i);
                            break;
                        }

                    case (R.id.menuAjoutDeTache):
                        if (!currentActivity.equals("Creation")) {
                            i = new Intent(BaseActivity.this, Creation.class);
                            startActivity(i);
                            break;
                        }

                    case (R.id.menuConnexion):
                            i = new Intent(BaseActivity.this, Connexion.class);
                            startActivity(i);
                            break;
                }
                return false;
            }
        });


        DrawerLayout nv1 = bindingBase.drawerLayoutID;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        abToggle = new ActionBarDrawerToggle(this, nv1, R.string.drawer_open, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
                NavigationView navigationView = (NavigationView) findViewById(R.id.navigationViewID);
                View headerView = navigationView.getHeaderView(0);
                TextView navUsager = (TextView) findViewById(R.id.txtViewNavHeader);
                //// juste un string
                navUsager.setText(NomDeUsager);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                NavigationView navigationView = (NavigationView) findViewById(R.id.navigationViewID);
                View headerView = navigationView.getHeaderView(0);
                TextView navUsager = (TextView) findViewById(R.id.txtViewNavHeader);
                navUsager.setText(NomDeUsager);
            }
        };

        nv1.addDrawerListener(abToggle);
        abToggle.syncState();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(abToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        abToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        abToggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }
    private void getIncomingIntent(){
        if(getIntent().hasExtra("Nom Usager")){
            NomDeUsager = getIntent().getStringExtra("Nom Usager");
        }
    }

}


