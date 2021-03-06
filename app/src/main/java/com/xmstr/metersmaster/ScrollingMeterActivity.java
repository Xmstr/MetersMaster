package com.xmstr.metersmaster;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xmstr.metersmaster.adapters.CountAdapter;
import com.xmstr.metersmaster.db.MeterDatabase;
import com.xmstr.metersmaster.dialogs.ChangeNameDialog;
import com.xmstr.metersmaster.dialogs.ChangeTariffDialog;
import com.xmstr.metersmaster.dialogs.NewCountDialog;
import com.xmstr.metersmaster.interfaces.FullMeterListener;
import com.xmstr.metersmaster.model.Count;
import com.xmstr.metersmaster.model.Meter;
import com.xmstr.metersmaster.utils.Utils;

import java.util.Calendar;
import java.util.List;

import petrov.kristiyan.colorpicker.ColorPicker;

import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;

public class ScrollingMeterActivity extends AppCompatActivity implements CountAdapter.CountListener, FullMeterListener {

    int meterId;
    private RecyclerView countsRecyclerView;
    private CountAdapter countAdapter;
    FloatingActionButton fabNewCount;
    TextView meterNameTextView;
    TextView meterCountTextView;
    TextView meterTariffTextView;
    TextView meterVolumeTypeTextView;
    TextView meterTariffCurrencyTextView;
    ImageView changeNameImageView;
    ImageView changeTariffImageView;
    List<Count> countsList;
    Meter currentMeter;
    MeterDatabase db;
    RelativeLayout layoutFullMeter;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collapsing_meter);
        Toolbar toolbar = findViewById(R.id.scroll_meter_toolbar);
        setSupportActionBar(toolbar);

        utils = new Utils(getApplicationContext());

        Intent intent = getIntent();
        meterId = intent.getIntExtra("meterId", 0);

        db = MeterDatabase.getInstance(getApplicationContext());
        currentMeter = db.meterDao().getMeterById(meterId).get(0);
        getSupportActionBar().setTitle("Показания:");

        meterNameTextView = findViewById(R.id.textView_full_meter_name);
        meterCountTextView = findViewById(R.id.textView_full_meter_count);
        meterTariffTextView = findViewById(R.id.textView_full_meter_tariff_content);
        meterVolumeTypeTextView = findViewById(R.id.textView_full_meter_volume_type);
        meterTariffCurrencyTextView = findViewById(R.id.textView_full_meter_tariff_currency);
        changeNameImageView = findViewById(R.id.imageView_edit_name);
        changeTariffImageView = findViewById(R.id.imageView_edit_tariff);


        updateCountsList();
        //newFakeCounts();

        meterNameTextView.setText(currentMeter.getName());
        meterCountTextView.setText(utils.prepareNumber(countsList.get(countsList.size() - 1).getNumber()));
        if (currentMeter.getTariff().equals("")) {
            meterTariffTextView.setText("нет данных");
            meterTariffCurrencyTextView.setVisibility(View.INVISIBLE);
        } else {
            meterTariffTextView.setText(currentMeter.getTariff());
            meterTariffCurrencyTextView.setVisibility(View.VISIBLE);
        }


        meterVolumeTypeTextView.setText(currentMeter.getVolumeType());
        meterTariffCurrencyTextView.setText(currentMeter.getCurrency());

        countAdapter = new CountAdapter(this, countsList, getApplicationContext());
        countsRecyclerView = findViewById(R.id.meter_recycler);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getApplicationContext(), HORIZONTAL);
        countsRecyclerView.addItemDecoration(itemDecor);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        countsRecyclerView.setLayoutManager(layoutManager);
        countsRecyclerView.setAdapter(countAdapter);

        changeNameImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                ChangeNameDialog changeNameDialog = ChangeNameDialog.newInstance(currentMeter.getName());
                changeNameDialog.show(fm, "change meter name");
            }
        });

        changeTariffImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                ChangeTariffDialog changeTariffDialog = ChangeTariffDialog.newInstance(currentMeter.getTariff(), currentMeter.getCurrency());
                changeTariffDialog.show(fm, "change meter tariff");
            }
        });


        fabNewCount = findViewById(R.id.fab_new_count);
        fabNewCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                NewCountDialog newCountDialog = NewCountDialog.newInstance(currentMeter.getId(), countsList.get(countsList.size() - 1).getNumber());
                newCountDialog.show(fm, "new count dialog");
            }
        });



/*
        meterNameTextView.setText("Счетчик: " + currentMeter.getName());
        meterCountTextView.setText(utils.prepareNumber(countsList.get(countsList.size() - 1).getNumber()));
        meterTariffTextView.setText(currentMeter.getTariff() + " рублей");
        layoutFullMeter.setBackgroundColor(currentMeter.getColor());

*/
        /*
        Drawable background = colorImageView.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(color);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(color);
        }*/


    }

    private void newFakeCounts() {
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < 10; i++) {
            Count fakeCount = new Count(currentMeter.getId(), "4444", c.getTime());
            db.countDao().insertAllCounts(fakeCount);
        }
    }


    private void updateCountsList() {
        countsList = db.countDao().getAllCountsByMeterId(currentMeter.getId());
    }


    private void showColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(ScrollingMeterActivity.this);
        colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
            @Override
            public void setOnFastChooseColorListener(int position, int color) {
                // Меняем цвет метера и фона
                // layoutFullMeter.setBackgroundColor(color);
                currentMeter.setColor(color);
                db.meterDao().updateMeter(currentMeter);
            }

            @Override
            public void onCancel() {
                colorPicker.dismissDialog();
            }
        })
                .setTitle("Цвет счетчика")
                .setRoundColorButton(true)
                .setColors(R.array.background_colors)
                .setColumns(4)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meter, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_meter_change_color:
                showColorPicker();
                return true;
            case R.id.action_meter_delete_meter:
                deleteMeterAndGoBack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteMeterAndGoBack() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Удалить счетчик?")
                .setIcon(R.drawable.ic_delete_forever_black_24dp)
                .setPositiveButton("Удалить", (dialogInterface, i) -> {
                    db.meterDao().deleteMeter(currentMeter);
                    finish();
                })
                .setNegativeButton("Отмена", (dialogInterface, i) -> {
                })
                .create();
        dialog.show();

    }

    @Override
    public void onCountClick(int position) {

    }

    @Override
    public void onChangeNamePositiveClick(String newName) {
        currentMeter.setName(newName);
        db.meterDao().updateMeter(currentMeter);
        meterNameTextView.setText(newName);
    }

    @Override
    public void onChangeTariffPositiveClick(String newTariff, String newCurrency) {
        currentMeter.setTariff(newTariff);
        currentMeter.setCurrency(newCurrency);
        db.meterDao().updateMeter(currentMeter);
        meterTariffTextView.setText(newTariff);
        meterTariffCurrencyTextView.setVisibility(View.VISIBLE);
        meterTariffCurrencyTextView.setText(newCurrency);
        Log.i("DIALOGS", newCurrency);

    }

    @Override
    public void onNewCountAdded(Count newCount) {
        db.countDao().insertAllCounts(newCount);
        updateCountsList();
        countAdapter.updateAndAnimateCount(countsList , countsList.size()-1, CountAdapter.ACTION_INSERT);
    }
}
