package com.hilbing.banda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.hilbing.banda.fragment.ConcertFragment;
import com.hilbing.banda.fragment.PlaylistFragment;
import com.hilbing.banda.fragment.RehearsalFragment;
import com.hilbing.banda.fragment.SettingFragment;
import com.hilbing.banda.fragment.UserBandaFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserBandaFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_perfil);
        }


    }



    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_perfil:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserBandaFragment()).commit();
                break;
            case R.id.nav_playlist:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PlaylistFragment()).commit();
                break;
            case R.id.nav_rehearsal:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RehearsalFragment()).commit();
                break;
            case R.id.nav_concert:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConcertFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingFragment()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, getResources().getString(R.string.share), Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_send:
                Toast.makeText(this, getResources().getString(R.string.send), Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
