package com.xmstr.metersmaster.interfaces;

import com.xmstr.metersmaster.model.Count;

/**
 * Created by Xmstr.
 */

public interface FullMeterListener {
    void onChangeNamePositiveClick(String newName);
    void onChangeTariffPositiveClick(String newTariff, String newCurrency);
    void onNewCountAdded(Count newCount);
}
