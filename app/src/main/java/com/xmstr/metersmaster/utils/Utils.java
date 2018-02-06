package com.xmstr.metersmaster.utils;

import android.arch.persistence.room.Database;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.xmstr.metersmaster.MainActivity;
import com.xmstr.metersmaster.R;
import com.xmstr.metersmaster.db.MeterDao;
import com.xmstr.metersmaster.db.MeterDatabase;
import com.xmstr.metersmaster.model.Meter;

import java.util.List;

/**
 * Created by Xmstr.
 */

public class Utils {

    public Context context;

    MeterDatabase db = MeterDatabase.getInstance(context);

    public Utils(Context context) {
        this.context = context;
    }

    public boolean checkNameExists(String name){
        return db.meterDao().isExist(name);
    }

    public static void checkCount(){

    }

    public static int getLastMeterId(MeterDao meterDao){
        List<Meter> meters = meterDao.getAllMeters();
        if (meters.size() > 0) return meters.get(meters.size() - 1).getId();
        else return 0;
    }

    public SpannableStringBuilder prepareNumber(String number) {
        int missing = 5 - number.length();
        StringBuilder newNumber = new StringBuilder();
        for (int i = 0; i < missing; i++) newNumber.append("0");
        newNumber.append(number);

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(newNumber);
        stringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,R.color.primary_light)),0,missing,0);
        return stringBuilder;
    }
}
