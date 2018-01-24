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
public interface CountDao {

    // FOR COUNTS
    @Query("SELECT * FROM counts")
    List<Count> getAllCounts();

    @Query("SELECT * FROM counts WHERE _id = :id LIMIT 1")
    List<Count> getCountById(int id);

    @Query("SELECT * FROM counts WHERE count_meter_id = :meterId")
    List<Count> getAllCountsByMeterId(int meterId);

    @Query("SELECT COUNT(*) FROM counts")
    int countRowCount();

    @Delete
    void deleteCount(Count count);

    @Update
    void updateCount(Count count);

    @Insert
    void insertAllCounts(Count... counts);

}
