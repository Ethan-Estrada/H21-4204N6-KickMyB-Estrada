package org.estrada.tp1;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.estrada.tp1.databinding.ActivityBaseBinding;
import org.estrada.tp1.http.RetrofitCookie;
import org.estrada.tp1.http.ServiceCookie;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {
    private ActionBarDrawerToggle abToggle;
    String currentActivity; // Évite la double ouverture d'une activité
    ActivityBaseBinding bindingBase;
    @Override
    public void setContentView(View view) {
        bindingBase = ActivityBaseBinding.inflate(getLayoutInflater());
        bindingBase.frameLayoutID.addView (view);
        super.setContentView(bindingBase.drawerLayoutID);

        Singleton instance = Singleton.getInstance();

        bindingBase.navigationViewID.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final Intent[] in = new Intent[1];
                switch (item.getItemId()){
                    case (R.id.menuAccueil):
                        if (!currentActivity.equals(R.string.Accueil)){
                            in[0] = new Intent(BaseActivity.this, Accueil.class);
                            startActivity(in[0]);
                            break;
                        }
                    case (R.id.menuAjoutDeTache):
                        if (!currentActivity.equals(R.string.Creation)) {
                            in[0] = new Intent(BaseActivity.this, Creation.class);
                            startActivity(in[0]);
                            break;
                        }

                    case (R.id.menuConnexion):
                        // on below line we are executing our method.
                        ServiceCookie service = RetrofitCookie.get();
                        String r="";
                        service.signoutResponse(r).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if(response.isSuccessful()){
                                    Log.i("RETROFIT",response.code()+"");
                                    in[0] = new Intent(BaseActivity.this, Connexion.class);
                                    startActivity(in[0]);
                                }
                                else {
                                    // cas d'erreur http 400 404
                                    Log.i("RETROFIT",response.code()+"");
                                }
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.i("RETROFIT",t.getMessage());
                            }
                        });
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
                navUsager.setText(instance.getText());
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                NavigationView navigationView = (NavigationView) findViewById(R.id.navigationViewID);
                View headerView = navigationView.getHeaderView(0);
                TextView navUsager = (TextView) findViewById(R.id.txtViewNavHeader);
                navUsager.setText(instance.getText());
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
}


