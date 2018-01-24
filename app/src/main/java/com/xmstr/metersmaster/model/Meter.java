package com.xmstr.metersmaster.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Xmstr.
 */
@Entity(tableName = "meters")
public class Meter {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;

    public static final int METER_TYPE_ELECTRO = 1;
    public static final int METER_TYPE_WATER = 2;
    public static final int METER_TYPE_GAS = 3;

    @ColumnInfo(name = "meter_name")
    private String name;
    @ColumnInfo(name = "meter_type")
    private int type;
    @ColumnInfo(name = "meter_color")
    private int color;
    @ColumnInfo(name = "meter_tariff")
    private String tariff;
    @ColumnInfo(name = "meter_volume_type")
    private String volumeType;
    @ColumnInfo(name = "meter_currency")
    private String currency;

    public String getVolumeType() {
        return volumeType;
    }

    public void setVolumeType(String volumeType) {
        this.volumeType = volumeType;
    }



    public Meter(int type, String name, String count) {
        this.type = type;
        this.name = name;
    }

    @Ignore
    public Meter(String name, String count) {
        this.name = name;
    }

    public Meter() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTariff() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
