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
import com.xmstr.metersmaster.db.MeterDatabase;
import com.xmstr.metersmaster.interfaces.FullMeterListener;
import com.xmstr.metersmaster.model.Count;
import com.xmstr.metersmaster.model.Meter;
import com.xmstr.metersmaster.utils.Utils;

import java.util.Calendar;


/**
 * Created by Xmstr.
 */

public class NewCountDialog extends DialogFragment implements DialogInterface.OnClickListener {

    TextInputLayout countTextInputLayout;
    EditText countEditText;
    TextView prevCountTextView;
    TextView prevCountLabelTextView;
    boolean isCountValidated = false;
    int meterId;
    Count count;
    Meter currentMeter;
    String meterName = "";
    String lastCount = "";
    String newCount = "";
    MeterDatabase db;
    Utils utils = new Utils(getContext());

    public NewCountDialog() {
        db = MeterDatabase.getInstance(getContext());
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_count_dialog_layout, null);
        if (getArguments() != null) {
            lastCount = getArguments().getString("lastCount");
            meterId = getArguments().getInt("meterId");
        }
        countTextInputLayout = view.findViewById(R.id.textInputLayout_count);
        countEditText = view.findViewById(R.id.editText_new_count);
        prevCountLabelTextView = view.findViewById(R.id.textView_prevCountLabel);
        prevCountTextView = view.findViewById(R.id.textView_prevCountContent);

        prevCountTextView.setText(utils.prepareNumber(lastCount));

        builder.setIcon(R.drawable.ic_edit_black_24dp)
                .setView(view)
                .setTitle("Смена названия")
                .setPositiveButton("ЗАПИСАТЬ", this)
                .setNegativeButton("ОТМЕНА", this);
        AlertDialog changeDialog = builder.create();

        return changeDialog;


    }

    public static NewCountDialog newInstance(int meterId, String lastCountText) {
        NewCountDialog changeNameDialog = new NewCountDialog();
        Bundle args = new Bundle();
        args.putInt("meterId", meterId);
        args.putString("lastCount", lastCountText);
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
                    validateCount(countEditText.getText().toString());
                    if (isCountValidated) {
                        countTextInputLayout.setError("");
                        newCount = countEditText.getText().toString().trim();
                        wantToCloseDialog = true;
                    } else {
                        countTextInputLayout.setError("Введите показание!");
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
        FullMeterListener listener = (FullMeterListener) getActivity();
        listener.onNewCountAdded(formNewCount());
    }

    private Count formNewCount(){
        count = new Count();
        count.setMeterId(currentMeter.getId());
        count.setNumber(newCount);
        Calendar calendar = Calendar.getInstance();
        count.setDate(calendar.getTime());
        return count;
    }


    private void validateCount(String enteredString) {
        if (enteredString.startsWith("0")) {
            if (enteredString.length() > 0) {
                countEditText.setText(enteredString.substring(1));
                countTextInputLayout.setError("Без нулей в начале!");
            } else {
                countEditText.setText("");
                countTextInputLayout.setError("Без нулей в начале!");
            }
        } else countTextInputLayout.setError("");

        if (TextUtils.isEmpty(enteredString.trim())) {
            countTextInputLayout.setError("Введите показание!");
            isCountValidated = false;
        } else isCountValidated = true;

        if (Integer.parseInt(enteredString) <= Integer.parseInt(lastCount)){
            countTextInputLayout.setError("Не может быть меньше текущего");
            isCountValidated = false;
        } else isCountValidated = true;
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }
}
