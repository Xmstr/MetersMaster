package com.xmstr.metersmaster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xmstr.metersmaster.R;
import com.xmstr.metersmaster.db.DateConverter;
import com.xmstr.metersmaster.db.MeterDatabase;
import com.xmstr.metersmaster.model.Count;
import com.xmstr.metersmaster.model.Meter;
import com.xmstr.metersmaster.utils.Utils;

import java.util.List;

/**
 * Created by Xmstr.
 */

public class CountAdapter extends RecyclerView.Adapter<CountAdapter.MyViewHolder> {

    private Context mContext;
    private Context newContext;

    public static final int ACTION_INSERT = 1;
    public static final int ACTION_DELETE = 2;
    public static final int ACTION_EDIT = 3;

    MeterDatabase db;

    private List<Count> countsListOfCurrentMeter;
    private CountListener listener;
    private Utils utils;

    public CountAdapter(CountListener listener, List<Count> countsListOfCurrentMeter, Context context) {
        //this.metersList = metersList;

        this.listener = listener;
        this.countsListOfCurrentMeter = countsListOfCurrentMeter;
        this.mContext = context;
        db = MeterDatabase.getInstance(context);
    }

    public void updateAndAnimateCount(List<Count> countsListOfCurrentMeter, int position, int typeOfAction) {
        this.countsListOfCurrentMeter = countsListOfCurrentMeter;
        switch (typeOfAction) {
            case CountAdapter.ACTION_INSERT:
                notifyItemInserted(position);
                break;
            case CountAdapter.ACTION_DELETE:
                notifyItemRemoved(position);
                break;
            case CountAdapter.ACTION_EDIT:
                notifyItemChanged(position);
                break;
        }
    }

    public void updateAndAnimateCount(List<Count> countsListOfCurrentMeter) {
        this.countsListOfCurrentMeter = countsListOfCurrentMeter;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return countsListOfCurrentMeter.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        newContext = parent.getContext();

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.count_row, parent, false);
        Log.i("COUNTADAPTER","ViewHolder Created");

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.bind(position);
    }


    public interface CountListener {
        //void onAddNewMeterClick();

        //void onCountDelete(Count count, int position);

        void onCountClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView countRowNumber;
        TextView countRowDate;


        public MyViewHolder(View itemView) {
            super(itemView);

            countRowDate = itemView.findViewById(R.id.textView_count_row_date);
            countRowNumber = itemView.findViewById(R.id.textView_count_row_number);


        }

        void bind(int position) {

            Count count = countsListOfCurrentMeter.get(position);
            utils = new Utils(newContext);
            if (countsListOfCurrentMeter.size() > 0) {
                countRowNumber.setText(utils.prepareNumber(count.getNumber()));
                countRowDate.setText(DateConverter.toString(count.getDate()));
                    /*Drawable background = notiImageView.getBackground();
                    if (background instanceof ShapeDrawable) {
                        ((ShapeDrawable)background).getPaint().setColor(meter.getColor());
                    } else if (background instanceof GradientDrawable) {
                        ((GradientDrawable)background).setColor(meter.getColor());
                    } else if (background instanceof ColorDrawable) {
                        ((ColorDrawable)background).setColor(meter.getColor());
                    }*/
                Log.i("COUNTADAPTER","ItemBinded with position =  " + position);
            }

        }
    }
}
