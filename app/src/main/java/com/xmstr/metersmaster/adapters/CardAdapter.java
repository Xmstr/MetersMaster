package com.xmstr.metersmaster.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

    private Context mContext;
    private Context newContext;

    public static final int ACTION_INSERT = 1;
    public static final int ACTION_DELETE = 2;
    public static final int ACTION_EDIT = 3;

    MeterDatabase db;

    private List<Meter> metersList;
    private List<Count> countsList;
    private CardListener listener;
    private Utils utils;

    public CardAdapter(CardListener listener, List<Meter> metersList, Context context) {
        this.metersList = metersList;
        this.listener = listener;
        this.mContext = context;
        db = MeterDatabase.getInstance(context);
    }

    public void updateAndAnimateCard(List<Meter> metersList, int position, int typeOfAction) {
        this.metersList = metersList;
        switch (typeOfAction) {
            case CardAdapter.ACTION_INSERT:
                notifyItemInserted(position);
                break;
            case CardAdapter.ACTION_DELETE:
                notifyItemRemoved(position);
                break;
            case CardAdapter.ACTION_EDIT:
                notifyItemChanged(position);
                break;
        }
    }
    public void updateAndAnimateCard(List<Meter> metersList) {
        this.metersList = metersList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == metersList.size()) {
            return R.layout.last_item;
        }
        return R.layout.card_row;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        newContext = parent.getContext();


        if (viewType == R.layout.card_row) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_row, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.last_item, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return metersList.size() + 1;
    }


    public interface CardListener {
        void onAddNewMeterClick();

        void onDeleteConfirmed(Meter meter, int position);

        void onCardClick(int position);
    }
    //TODO написать 2 вьюхолдера

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mainCountTextView;
        TextView nameOfMeterTextView;
        TextView tariffContentTextView;
        TextView lastDataDateTextView;
        CardView card;
        Button addNewMeterButton;
        ImageView deleteImageButton;
        ImageView notiImageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            mainCountTextView = itemView.findViewById(R.id.textView_main_count_of_meter);
            nameOfMeterTextView = itemView.findViewById(R.id.textView_name_of_meter);
            tariffContentTextView = itemView.findViewById(R.id.textView_tariff_content);
            lastDataDateTextView = itemView.findViewById(R.id.textView_last_data_date);
            card = itemView.findViewById(R.id.card_view);
            addNewMeterButton = itemView.findViewById(R.id.button_add_new_meter);
            deleteImageButton = itemView.findViewById(R.id.imageView_delete);
            notiImageView = itemView.findViewById(R.id.imageButton_noti);

        }

        void bind(int position) {
            if (position == metersList.size()) {
                addNewMeterButton.setOnClickListener(view -> {
                    listener.onAddNewMeterClick();
                });
            } else {

                Meter meter = metersList.get(position);
                countsList = db.countDao().getAllCountsByMeterId(meter.getId());
                Log.i("COUNTS", "countList GET");
                utils = new Utils(newContext);
                Count lastCount;
                if (countsList.size() > 0){
                    lastCount = countsList.get(countsList.size()- 1);
                    mainCountTextView.setText(utils.prepareNumber(lastCount.getNumber()));
                    //Log.i("COUNTS", "countList number: "+ countsList.get(countsList.size()-1).getNumber());
                    nameOfMeterTextView.setText(meter.getName());
                    if (meter.getTariff().equals(""))
                        tariffContentTextView.setText("неизвестен");
                    else tariffContentTextView.setText(meter.getTariff());
                    lastDataDateTextView.setText(DateConverter.toString(lastCount.getDate()));
                    /*Drawable background = notiImageView.getBackground();
                    if (background instanceof ShapeDrawable) {
                        ((ShapeDrawable)background).getPaint().setColor(meter.getColor());
                    } else if (background instanceof GradientDrawable) {
                        ((GradientDrawable)background).setColor(meter.getColor());
                    } else if (background instanceof ColorDrawable) {
                        ((ColorDrawable)background).setColor(meter.getColor());
                    }*/
                    card.setCardBackgroundColor(meter.getColor());
                    card.setOnClickListener(view -> {
                        listener.onCardClick(meter.getId());
                    });
                }

                deleteImageButton.setOnClickListener(view -> {
                    AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                            .setTitle("Удалить счетчик?")
                            .setIcon(R.drawable.ic_delete_forever_black_24dp)
                            .setPositiveButton("Удалить", (dialogInterface, i) -> {
                                listener.onDeleteConfirmed(meter, position);
                            })
                            .setNegativeButton("Отмена", (dialogInterface, i) -> {
                            })
                            .create();
                    dialog.show();
                });
            }
        }
    }

}
