package com.example.r_gameshopapp.ui.account;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.r_gameshopapp.DatabaseManager;
import com.example.r_gameshopapp.Item;
import com.example.r_gameshopapp.R;
import com.example.r_gameshopapp.databinding.FragmentAccountBinding;
import com.example.r_gameshopapp.userMain;

public class AccountFragment extends Fragment {
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private TextView current_balance_popup, user_name, current_balance;
    private ImageButton button_cancel;
    private DatabaseManager dbManager;
    private Button add_balance;
    private Cursor cursor;
    private FragmentAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dbManager = new DatabaseManager(getActivity());
        dbManager.open();
        user_name = (TextView) root.findViewById(R.id.user_name);
        current_balance = (TextView) root.findViewById(R.id.current_balance);
        cursor = dbManager.searchAccountID(Integer.toString(((userMain)getActivity()).getid()));

        user_name.setText(cursor.getString(1));
        current_balance.setText("CURRENT BALANCE: " + cursor.getString(3));

        add_balance = (Button) root.findViewById(R.id.add_balance_button);
        add_balance.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    createAddBalanceDialog(inflater.getContext());
               }
        });

//        final TextView textView = binding.textAccount;
//        accountViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    public void createAddBalanceDialog(Context context) {
        dialogbuilder = new AlertDialog.Builder(context);
        final View addBalancePopupView = getLayoutInflater().inflate(R.layout.add_balance_popup, null);
        current_balance_popup = (TextView) addBalancePopupView.findViewById(R.id.current_balance_popup);
        current_balance_popup.setText("CURRENT BALANCE: $1000");

        dialogbuilder.setView(addBalancePopupView);
        dialog = dialogbuilder.create();
        dialog.show();

        button_cancel = (ImageButton) addBalancePopupView.findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dialog.dismiss();
            }
        });
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}