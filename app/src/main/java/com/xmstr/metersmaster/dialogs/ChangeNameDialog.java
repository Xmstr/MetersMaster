package com.xmstr.metersmaster.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xmstr.metersmaster.R;
import com.xmstr.metersmaster.utils.Utils;


/**
 * Created by Xmstr.
 */

public class ChangeNameDialog extends DialogFragment implements DialogInterface.OnClickListener {

    TextInputLayout nameTextInputLayout;
    EditText nameEditText;
    TextView prevNameTextView;
    TextView prevNameLabelTextView;
    boolean isNameValidated = false;
    String currentName = "";
    String newName = "";
    Utils utils = new Utils(getContext());

    public interface DialogChangeNameListener {
        void onChangeNamePositiveClick(String newName);
    }

    public ChangeNameDialog() {

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.change_name_dialog_layout, null);


        if (getArguments() != null) {
            currentName = getArguments().getString("meterName");
        }
        nameTextInputLayout = view.findViewById(R.id.textInputLayout_tariff);
        nameEditText = view.findViewById(R.id.editText_meter_tariff);
        prevNameLabelTextView = view.findViewById(R.id.textView_prevTariffLabel);
        prevNameTextView = view.findViewById(R.id.textView_prevTariffContent);

        prevNameTextView.setText(currentName);
        builder.setIcon(R.drawable.ic_edit_black_24dp)
                .setView(view)
                .setTitle("Смена названия")
                .setPositiveButton("СМЕНИТЬ", this)
                .setNegativeButton("ОТМЕНА", this);
        AlertDialog changeDialog = builder.create();


        return changeDialog;


    }

    public static ChangeNameDialog newInstance(String meterName) {
        ChangeNameDialog changeNameDialog = new ChangeNameDialog();
        Bundle args = new Bundle();
        args.putString("meterName", meterName);
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
                    validateName(nameEditText.getText());
                    if (isNameValidated) {
                        nameTextInputLayout.setError("");
                        newName = nameEditText.getText().toString().trim();
                        wantToCloseDialog = true;
                    } else {
                        nameTextInputLayout.setError("Введите новое имя!");
                        wantToCloseDialog = false;
                    }

                    if (wantToCloseDialog) {
                        sendBackResult();
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    private void sendBackResult() {
        DialogChangeNameListener listener = (DialogChangeNameListener) getActivity();
        listener.onChangeNamePositiveClick(newName);
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


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }
}
