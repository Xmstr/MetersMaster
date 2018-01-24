package com.xmstr.metersmaster.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Xmstr.
 */
@Entity(tableName = "counts", foreignKeys =
@ForeignKey(entity = Meter.class, onDelete = CASCADE, parentColumns = "_id", childColumns = "count_meter_id"))
public class Count {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;
    @ColumnInfo(name = "count_meter_id")
    private int meterId;
    @ColumnInfo(name = "count_number")
    private String number;
    @ColumnInfo(name = "count_date")
    private Date date;


    public Count(int meterId, String number, Date date) {
        this.meterId = meterId;
        this.number = number;
        this.date = date;
    }

    public Count() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMeterId() {
        return meterId;
    }

    public void setMeterId(int meterId) {
        this.meterId = meterId;
    }
}
