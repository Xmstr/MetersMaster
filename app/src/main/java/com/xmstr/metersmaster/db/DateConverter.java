package com.xmstr.metersmaster.db;

import android.arch.persistence.room.TypeConverter;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Xmstr.
 */

public class DateConverter {
    public static final String PATTERN = "dd/MM/yyyy";
    public static SimpleDateFormat FORMATTER = new SimpleDateFormat(PATTERN, Locale.getDefault());

    @TypeConverter
    public static Date toDate(Long timestamp) {
        if (timestamp == null) return null;
        else return new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        if (date == null) return null;
        else return date.getTime();
    }

    public static String toString(Date date){
        return FORMATTER.format(date);
    }
}
