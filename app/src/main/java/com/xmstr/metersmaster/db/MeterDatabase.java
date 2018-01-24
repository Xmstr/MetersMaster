package com.xmstr.metersmaster.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.xmstr.metersmaster.model.Count;
import com.xmstr.metersmaster.model.Meter;

/**
 * Created by Xmstr.
 */
@Database(entities = {Meter.class, Count.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class MeterDatabase extends RoomDatabase {
    private static MeterDatabase INSTANCE;

    public static MeterDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    MeterDatabase.class,
                    "MetersDatabase")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
    public static void destroyInstance(){
        INSTANCE = null;
    }

    public abstract MeterDao meterDao();
    public abstract CountDao countDao();

}
