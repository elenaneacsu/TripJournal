package com.elenaneacsu.tripjournal.trips.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.elenaneacsu.tripjournal.R;
import com.elenaneacsu.tripjournal.trips.entities.Trip;
import com.elenaneacsu.tripjournal.trips.fragments.TripListFragment;
import com.elenaneacsu.tripjournal.utils.Constants;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    FloatingActionButton fab;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ManageTripActivity.class);
                intent.putExtra(Constants.FLAG, "add");
                startActivityForResult(intent, 1);
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        item.setChecked(true);

        if (id == R.id.nav_home) {
            initFragment(new TripListFragment());
        } else if (id == R.id.nav_favourite) {
            Toast.makeText(this, "hani", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_info) {

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {
            logout();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        TripListFragment fragment = new TripListFragment();
        Bundle bundle = new Bundle();
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Trip trip = data.getParcelableExtra(Constants.TRIP_OBJECT);
                String flag = data.getStringExtra(Constants.FLAG);
                if (flag.equalsIgnoreCase("add")) {
                    bundle.putParcelable(Constants.TRIP_OBJECT, trip);
                    bundle.putString(Constants.FLAG, "add");
                    fragment.setArguments(bundle);
                    initFragment(fragment);
                }
            }
        }
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                Trip trip = data.getParcelableExtra(Constants.TRIP_OBJECT);
                String flag = data.getStringExtra(Constants.FLAG);
                int position = data.getIntExtra(Constants.POSITION, -1);
                if (flag.equalsIgnoreCase("update")) {
                    if (position > -1) {
                        bundle.putParcelable(Constants.TRIP_OBJECT, trip);
                        bundle.putString(Constants.FLAG, "update");
                        bundle.putInt(Constants.POSITION, position);
                        fragment.setArguments(bundle);
                        initFragment(fragment);
                    }
                }
            }
        }
    }


    private void initFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.log_out));
        builder.setMessage(R.string.logout_message);
        builder.setPositiveButton(getString(R.string.logout_caps), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                sp.edit().putBoolean("logged", false).apply();
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
