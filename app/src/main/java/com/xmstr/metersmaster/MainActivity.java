package com.xmstr.metersmaster;


import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.xmstr.metersmaster.db.MeterDatabase;
import com.xmstr.metersmaster.dialogs.NewMeterDialog;
import com.xmstr.metersmaster.fragments.ElectroFragment;
import com.xmstr.metersmaster.fragments.GasFragment;
import com.xmstr.metersmaster.fragments.WaterFragment;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_CURRENT_ID = "key:currentId";

    private TextView mTextMessage;
    BottomNavigationView navigation;
    ElectroFragment mElectroFragment;
    WaterFragment mWaterFragment;
    GasFragment mGasFragment;
    FragmentManager fragmentManager;
    Fragment fragment = null;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_electro:
                case R.id.navigation_water:
                case R.id.navigation_gas:
                    showRightFragment(item.getItemId());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        // ALL UI

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        mTextMessage = findViewById(R.id.message);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_electro);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_CURRENT_ID, navigation.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }

    private void showRightFragment(int itemId) {

        String tag = null;
        switch (itemId) {
            case R.id.navigation_electro:
                if (mElectroFragment == null) {
                    mElectroFragment = mElectroFragment.newInstance(getResources().getString(R.string.navigation_electro_text));
                }
                tag = mElectroFragment.TAG;
                fragment = mElectroFragment;
                break;
            case R.id.navigation_water:
                if (mWaterFragment == null) {
                    mWaterFragment = mWaterFragment.newInstance(getResources().getString(R.string.navigation_water_text));
                }
                tag = mWaterFragment.TAG;
                fragment = mWaterFragment;
                break;
            case R.id.navigation_gas:
                if (mGasFragment == null) {
                    mGasFragment = mGasFragment.newInstance(getResources().getString(R.string.navigation_gas_text));
                }
                tag = mGasFragment.TAG;
                fragment = mGasFragment;
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment, tag)
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_upload) {
            Toast.makeText(this, "UPLOAD DIALOG", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
