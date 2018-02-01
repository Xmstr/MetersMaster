package com.xmstr.metersmaster.interfaces;

/**
 * Created by Xmstr.
 */

public interface FullMeterListener {
    void onChangeNamePositiveClick(String newName);
    void onChangeTariffPositiveClick(String newTariff, String newCurrency);

}
