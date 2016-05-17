package com.mrbhatt.popularmoviesstage2;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by anupambhatt on 04/05/16.
 */
public class MasterActivity extends Activity implements FragmentManager.OnBackStackChangedListener {
    private static final String MASTER_FRAGMENT_TAG = "MasterFragment1";
    private static final String SETTINGS_FRAGMENT_TAG = "SettingsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_activity);

        if (!isNetworkAvailable()) {
            displayErorToaster();
            return;
        }

        if (savedInstanceState != null) {
            return;
        }

        //Listen for changes in the back stack
        getFragmentManager().addOnBackStackChangedListener(this);
        //Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.masterFrame, new MasterFragment(), MASTER_FRAGMENT_TAG);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    private void shouldDisplayHomeUp(){
        // Enable Up button only  if there are entries in the back stack
        boolean canback = getFragmentManager().getBackStackEntryCount()>0;
        getActionBar().setDisplayHomeAsUpEnabled(canback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.preferences) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            SettingsFragment settingsFragment = new SettingsFragment();
            fragmentTransaction.replace(R.id.masterFrame , settingsFragment, SETTINGS_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void displayErorToaster() {
        Toast.makeText(getApplicationContext(), "This application requires internet connection!", Toast.LENGTH_LONG).show();
    }
}