package com.xmstr.metersmaster.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.xmstr.metersmaster.R;
import com.xmstr.metersmaster.utils.Utils;


/**
 * Created by Xmstr.
 */

public class ChangeTariffDialog extends DialogFragment implements DialogInterface.OnClickListener {

    TextInputLayout tariffTextInputLayout;
    EditText tariffEditText;
    TextView prevTariffTextView;
    TextView prevTariffLabelTextView;
    TextView tariffCurrencyTextView;
    TextView tariffCurrencyLabelTextView;
    Spinner tariffSpinner;
    boolean isTariffValidated = false;
    String currentTariff = "";
    String currentCurrency = "";
    String newTariff = "";
    String newCurrency = "";
    Utils utils = new Utils(getContext());

    public interface DialogChangeTariffListener {
        void onChangeTariffPositiveClick(String newTariff, String newCurrency);
    }

    public ChangeTariffDialog() {

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.change_tariff_dialog_layout, null);


        if (getArguments() != null) {
            currentTariff = getArguments().getString("meterTariff");
            currentCurrency = getArguments().getString("meterCurrency");
        }
        tariffTextInputLayout = view.findViewById(R.id.textInputLayout_tariff);
        tariffEditText = view.findViewById(R.id.editText_meter_tariff);
        prevTariffTextView = view.findViewById(R.id.textView_prevTariffContent);
        tariffSpinner = view.findViewById(R.id.currency_spinner);
        tariffCurrencyTextView = view.findViewById(R.id.textView_prevTariffCurrency);
        if (currentTariff.equals("")) {
            prevTariffTextView.setText("нет данных");
            tariffCurrencyTextView.setVisibility(View.GONE);
        } else {
            prevTariffTextView.setText(currentTariff);
            tariffCurrencyTextView.setVisibility(View.VISIBLE);
        }
        builder.setIcon(R.drawable.ic_edit_black_24dp)
                .setView(view)
                .setTitle("Смена тарифа")
                .setPositiveButton("СМЕНИТЬ", this)
                .setNegativeButton("ОТМЕНА", this);
        AlertDialog changeDialog = builder.create();


        return changeDialog;


    }

    public static ChangeTariffDialog newInstance(String meterTariff, String meterCurrency) {
        ChangeTariffDialog changeNameDialog = new ChangeTariffDialog();
        Bundle args = new Bundle();
        args.putString("meterTariff", meterTariff);
        args.putString("meterCurrency", meterCurrency);
        changeNameDialog.setArguments(args);
        return changeNameDialog;
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
                    validateTariff(tariffEditText.getText().toString());
                    if (isTariffValidated) {
                        tariffTextInputLayout.setError("");
                        newTariff = tariffEditText.getText().toString().trim();
                        wantToCloseDialog = true;
                    } else {
                        tariffTextInputLayout.setError("Введите новый тариф!");
                        wantToCloseDialog = false;
                    }

                    if (wantToCloseDialog) {
                        sendBackResult();
                        dialog.dismiss();
                    }
                }
            });

            tariffSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemPosition, long selectedId) {
                    if (!currentTariff.equals("")) {
                        switch (selectedItemPosition) {
                            case 0:
                                newCurrency = getString(R.string.currency_label_ruble);
                                tariffCurrencyTextView.setText(getString(R.string.currency_label_ruble));
                                break;
                            case 1:
                                newCurrency = getString(R.string.currency_label_hrivna);
                                tariffCurrencyTextView.setText(getString(R.string.currency_label_hrivna));
                                break;
                            default:
                                newCurrency = getString(R.string.currency_label_ruble);
                                tariffCurrencyTextView.setText(getString(R.string.currency_label_ruble));
                                break;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }

            });
        }
    }

    private void sendBackResult() {
        DialogChangeTariffListener listener = (DialogChangeTariffListener) getActivity();
        listener.onChangeTariffPositiveClick(newTariff, newCurrency);
    }


    private void validateTariff(String enteredString) {
        if (enteredString.startsWith("00")) {
            if (enteredString.length() > 0) {
                tariffEditText.setText(enteredString.substring(1));
                tariffTextInputLayout.setError("Лишний 0");
            } else {
                tariffEditText.setText("");
                tariffTextInputLayout.setError("Лишний 0");
            }
        } else tariffTextInputLayout.setError("");

        if (TextUtils.isEmpty(enteredString.trim())) {
            tariffTextInputLayout.setError("Введите новый тариф!");
            isTariffValidated = false;
        } else isTariffValidated = true;
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }
}
