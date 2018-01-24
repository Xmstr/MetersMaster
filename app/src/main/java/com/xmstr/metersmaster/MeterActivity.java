package com.xmstr.metersmaster;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xmstr.metersmaster.db.MeterDatabase;
import com.xmstr.metersmaster.model.Count;
import com.xmstr.metersmaster.model.Meter;
import com.xmstr.metersmaster.utils.Utils;

import java.util.List;

import petrov.kristiyan.colorpicker.ColorPicker;

public class MeterActivity extends AppCompatActivity {

    int meterId;
    TextView meterNameTextView;
    TextView meterCountTextView;
    TextView meterTariffTextView;
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
        setContentView(R.layout.activity_meter);
        Toolbar toolbar = findViewById(R.id.toolbar_meter);
        setSupportActionBar(toolbar);

        meterNameTextView = findViewById(R.id.textView_full_meter_name);
        meterCountTextView = findViewById(R.id.textView_full_meter_count);
        meterTariffTextView = findViewById(R.id.textView_full_meter_tariff_content);
        changeNameImageView = findViewById(R.id.imageView_edit_name);
        changeTariffImageView = findViewById(R.id.imageView_edit_tariff);
        layoutFullMeter = findViewById(R.id.layout_full_meter);

        utils = new Utils(getApplicationContext());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Новое показание", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        meterId = intent.getIntExtra("meterId", 0);
        db = MeterDatabase.getInstance(getApplicationContext());
        currentMeter = db.meterDao().getMeterById(meterId).get(0);
        countsList = db.countDao().getAllCountsByMeterId(currentMeter.getId());

        meterNameTextView.setText("Счетчик: " + currentMeter.getName());
        meterCountTextView.setText(utils.prepareNumber(countsList.get(countsList.size() - 1).getNumber()));
        meterTariffTextView.setText(currentMeter.getTariff() + " рублей");
        layoutFullMeter.setBackgroundColor(currentMeter.getColor());


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

    private void showColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(MeterActivity.this);
        colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
            @Override
            public void setOnFastChooseColorListener(int position, int color) {
                // Меняем цвет метера и фона
                layoutFullMeter.setBackgroundColor(color);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
