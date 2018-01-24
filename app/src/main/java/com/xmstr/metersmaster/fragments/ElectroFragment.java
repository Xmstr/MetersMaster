package com.xmstr.metersmaster.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xmstr.metersmaster.MeterActivity;
import com.xmstr.metersmaster.dialogs.NewMeterDialog;
import com.xmstr.metersmaster.model.Count;
import com.xmstr.metersmaster.model.Meter;
import com.xmstr.metersmaster.R;
import com.xmstr.metersmaster.adapters.CardAdapter;
import com.xmstr.metersmaster.db.MeterDatabase;
import com.xmstr.metersmaster.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xmstr.
 */

public class ElectroFragment extends Fragment implements CardAdapter.CardListener, NewMeterDialog.OnDialogAddMeterListener {
    private static final String ARG_TEXT = "arg_text";
    public static final String TAG = "tag:electro";
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private List<Meter> metersList = new ArrayList<>();
    private List<Count> countsList = new ArrayList<>();
    Context mContext = getContext();
    MeterDatabase db;


    public static ElectroFragment newInstance(String name) {
        ElectroFragment frag = new ElectroFragment();
        //frag.setRetainInstance(true);
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, name);
        frag.setArguments(args);
        return frag;
    }

    public ElectroFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.electro_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.electro_recycler_view);

        db = MeterDatabase.getInstance(getContext());
        updateMetersList();
        cardAdapter = new CardAdapter(this, metersList, mContext);
        Log.i("ADAPTER", "Adapter Created");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cardAdapter);
        Log.i("ADAPTER", "Adapter set");

        return rootView;
    }

    private void updateMetersList() {
        metersList = db.meterDao().getAllMetersOfType(Meter.METER_TYPE_ELECTRO);
        countsList = db.countDao().getAllCounts();
    }

    @Override
    public void onAddNewMeterClick() {
        //TODO включить тип счетчика сразу при создании
        showNewMeterDialog();
    }

    @Override
    public void onDeleteConfirmed(Meter meter, int position) {

        db.meterDao().deleteMeter(meter);
        updateMetersList();
        cardAdapter.updateAndAnimateCard(metersList, position, CardAdapter.ACTION_DELETE);
    }

    @Override
    public void onCardClick(int meterId) {
        Log.i("METER", "meter intent ID = "+ meterId);
        Intent meterIntent = new Intent(getContext(), MeterActivity.class);
        meterIntent.putExtra("meterId", meterId);
        startActivity(meterIntent);
    }

    private void showNewMeterDialog() {
        FragmentManager fm = getFragmentManager();
        NewMeterDialog newMeterDialog = NewMeterDialog.newInstance("Новый счетчик");
        newMeterDialog.setTargetFragment(ElectroFragment.this, Meter.METER_TYPE_ELECTRO);
        newMeterDialog.show(fm, "new meter");
    }

    @Override
    public void onDialogPositiveClick(Meter meter, Count firstCount) {
        db.meterDao().insertAllMeters(meter);
        int id = Utils.getLastMeterId(db.meterDao());
        firstCount.setMeterId(id);
        db.countDao().insertAllCounts(firstCount);
        updateMetersList();
        cardAdapter.updateAndAnimateCard(metersList, metersList.size()-1, CardAdapter.ACTION_INSERT);
    }
}