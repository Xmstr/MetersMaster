package com.xmstr.metersmaster.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.xmstr.metersmaster.R;
import com.xmstr.metersmaster.db.MeterDatabase;
import com.xmstr.metersmaster.model.Count;
import com.xmstr.metersmaster.model.Meter;
import com.xmstr.metersmaster.utils.Utils;

import java.util.Calendar;

/**
 * Created by Xmstr.
 */

public class NewMeterDialog extends DialogFragment implements DialogInterface.OnClickListener {

    EditText countEditText;
    EditText nameEditText;
    EditText tariffEditText;
    TextInputLayout nameTextInputLayout;
    TextInputLayout countEditTextLayout;
    TextInputLayout tariffInputLayout;
    boolean isCountValidated = false;
    boolean isNameValidated = false;
    boolean isTariffValidated = false;
    Switch switchUnknownTariff;
    MeterDatabase db;

    Count firstCount;


    Utils utils = new Utils(getContext());


    public interface OnDialogAddMeterListener {
        void onDialogPositiveClick(Meter meter, Count count);
    }


    public NewMeterDialog() {
        db = MeterDatabase.getInstance(getContext());
    }

    public static NewMeterDialog newInstance(String title) {
        NewMeterDialog frag = new NewMeterDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public void sendBackResult() {
        OnDialogAddMeterListener listener = (OnDialogAddMeterListener) getTargetFragment();

        int requestedMeterType = getTargetRequestCode();
        // Делаем счетчик для отправки
        listener.onDialogPositiveClick(formNewMeter(requestedMeterType), firstCount);
        //listener.onDialogPositiveClick(makeFateMeter());
        dismiss();
    }

    private Meter makeFakeMeter() {
        Meter meter = new Meter();
        meter.setId(1);
        meter.setType(Meter.METER_TYPE_GAS);
        meter.setTariff("5");
        meter.setName("test meter");
        meter.setColor(Color.BLACK);
        return meter;
    }

    private Meter formNewMeter(int requestedMeterType) {
        //MAKING REAL METER AND FIRST COUNT
        Meter meter = new Meter();
        Log.i("METER", "new meter ID = " + meter.getId());
        meter.setName(nameTextInputLayout.getEditText().getText().toString().trim());
        meter.setTariff(tariffInputLayout.getEditText().getText().toString().trim());
        meter.setColor(Color.WHITE);
        switch (requestedMeterType){
            case Meter.METER_TYPE_ELECTRO:
                meter.setVolumeType("кВ/ч");
                break;
            case Meter.METER_TYPE_WATER:
                meter.setVolumeType("м3");
                break;
            case Meter.METER_TYPE_GAS:
                meter.setVolumeType("м3");
                break;
            default:
                meter.setVolumeType("м3");
                break;
        }
        meter.setCurrency("\u20BD");
        meter.setType(requestedMeterType);

        Count count = new Count();
        count.setMeterId(meter.getId());
        //Log.i("METER", "new Count ID = " + count.getId());
        //Log.i("METER", "new count meterID = " + meter.getId());
        count.setNumber(countEditText.getText().toString().trim());
        Calendar calendar = Calendar.getInstance();
        count.setDate(calendar.getTime());

        firstCount = count;
        return meter;
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean wantToCloseDialog = false;
                    if (switchUnknownTariff.isChecked()) {
                        validateName(nameEditText.getText());
                        validateCount(countEditText.getText().toString());
                        if (isNameValidated && isCountValidated)
                            wantToCloseDialog = true;
                    } else {
                        validateName(nameEditText.getText());
                        validateCount(countEditText.getText().toString());
                        validateTariff(tariffEditText.getText().toString());
                        if (isNameValidated && isCountValidated && isTariffValidated)
                            wantToCloseDialog = true;
                    }

                    if (wantToCloseDialog) {
                        sendBackResult();
                        dialog.dismiss();
                    }
                }
            });
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_meter_dialog_layout, null);

        nameTextInputLayout = view.findViewById(R.id.textInputLayout_name);
        nameEditText = view.findViewById(R.id.editText_meter_name);
        countEditTextLayout = view.findViewById(R.id.textInputLayout_count);
        countEditText = view.findViewById(R.id.editText_meter_count);
        tariffInputLayout = view.findViewById(R.id.textInputLayout_tariff);
        tariffEditText = view.findViewById(R.id.editText_meter_tariff);
        switchUnknownTariff = view.findViewById(R.id.switch_unknown_tariff);


        builder.setTitle("Новый счетчик")
                .setView(view)
                .setPositiveButton("OK", this)
                .setNegativeButton("Отмена", this);

        AlertDialog addDialog = builder.create();

        view.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View view12, View view1) {
                validateName(nameEditText.getText());
            }
        });


        switchUnknownTariff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tariffInputLayout.setVisibility(View.GONE);
                    tariffEditText.setEnabled(false);
                    tariffEditText.setText("");
                    tariffInputLayout.setError("");
                } else {
                    tariffInputLayout.setVisibility(View.VISIBLE);
                    tariffEditText.setEnabled(true);
                }
            }
        });

        countEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String enteredString = charSequence.toString();
                validateCount(enteredString);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        nameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateName(editable);
            }
        });

        tariffEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String enteredString = charSequence.toString();
                validateTariff(enteredString);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return addDialog;
    }


    private void validateName(Editable editable) {
        if (TextUtils.isEmpty(editable.toString().trim())) {
            Log.i("checking", "NAME EMPTY");
            nameTextInputLayout.setError("Введите имя, например 'Квартира'");
            isNameValidated = false;
        } else {
            if (utils.checkNameExists(editable.toString())) {
                Log.i("checking", "NAME EXISTS! ERROR");
                nameTextInputLayout.setError("Имя уже существует!");
                isNameValidated = false;
            } else {
                nameTextInputLayout.setError("");
                isNameValidated = true;
            }
        }
    }

    private void validateCount(String enteredString) {
        if (enteredString.startsWith("0")) {
            if (enteredString.length() > 0) {
                countEditText.setText(enteredString.substring(1));
                countEditTextLayout.setError("Без нулей в начале!");
            } else {
                countEditText.setText("");
                countEditTextLayout.setError("Без нулей в начале!");
            }
        } else countEditTextLayout.setError("");

        if (TextUtils.isEmpty(enteredString.trim())) {
            countEditTextLayout.setError("Введите показание!");
            isCountValidated = false;
        } else isCountValidated = true;
    }

    private void validateTariff(String enteredString) {
        if (enteredString.startsWith("00")) {
            if (enteredString.length() > 0) {
                tariffEditText.setText(enteredString.substring(1));
                tariffInputLayout.setError("Лишний 0");
            } else {
                tariffEditText.setText("");
                tariffInputLayout.setError("Лишний 0");
            }
        } else tariffInputLayout.setError("");

        if (TextUtils.isEmpty(enteredString.trim())) {
            tariffInputLayout.setError("Введите тариф!");
            isTariffValidated = false;
        } else isTariffValidated = true;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }
}
