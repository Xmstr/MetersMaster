package com.xmstr.metersmaster.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.xmstr.metersmaster.model.Count;
import com.xmstr.metersmaster.model.Meter;

import java.util.List;

/**
 * Created by Xmstr.
 */
@Dao
public interface MeterDao {

    // FOR METER
    @Query("SELECT * FROM meters")
    List<Meter> getAllMeters();

    @Query("SELECT * FROM meters WHERE _id = :id LIMIT 1")
    List<Meter> getMeterById(int id);

    @Query("SELECT * FROM meters WHERE meter_name = :name LIMIT 1")
    List<Meter> getMeterByName(String name);

    @Query("SELECT * FROM meters WHERE meter_type LIKE :meter_type")
    List<Meter> getAllMetersOfType(int meter_type);

    @Query("SELECT EXISTS (SELECT * FROM meters WHERE meter_name = :name LIMIT 1)")
    boolean isExist(String name);

    @Query("SELECT COUNT(*) FROM meters")
    int meterRowCount();

    @Delete
    void deleteMeter(Meter meter);

    @Update
    void updateMeter(Meter meter);

    @Insert
    void insertAllMeters(Meter... meters);

}
