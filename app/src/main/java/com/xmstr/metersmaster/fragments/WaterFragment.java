package com.xmstr.metersmaster.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xmstr.metersmaster.model.Meter;
import com.xmstr.metersmaster.R;
import com.xmstr.metersmaster.adapters.CardAdapter;
import com.xmstr.metersmaster.db.MeterDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xmstr.
 */

public class WaterFragment extends Fragment implements CardAdapter.CardListener {
    private static final String ARG_TEXT = "arg_text";
    public static final String TAG = "tag:water";
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private List<Meter> metersList = new ArrayList<>();
    Context mContext = getContext();
    MeterDatabase db;

    public static WaterFragment newInstance(String name) {
        WaterFragment frag = new WaterFragment();
        frag.setRetainInstance(true);
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, name);
        frag.setArguments(args);
        return frag;
    }

    public WaterFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.water_fragment, container, false);

        metersList = new ArrayList<>();

        recyclerView = rootView.findViewById(R.id.water_recycler_view);

        db = MeterDatabase.getInstance(getContext());
        metersList = db.meterDao().getAllMetersOfType(Meter.METER_TYPE_WATER);
        cardAdapter = new CardAdapter(this, metersList, mContext);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();
        return rootView;

    }

    @Override
    public void onAddNewMeterClick() {

        Toast.makeText(getContext(), "NEW METER", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteConfirmed(Meter meter, int position) {

    }

    @Override
    public void onCardClick(int position) {

    }
}
