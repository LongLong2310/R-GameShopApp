package com.example.r_gameshopapp.ui.account;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.r_gameshopapp.BackgroundMusicService;
import com.example.r_gameshopapp.DatabaseManager;
import com.example.r_gameshopapp.Item;
import com.example.r_gameshopapp.R;
import com.example.r_gameshopapp.databinding.FragmentAccountBinding;
import com.example.r_gameshopapp.userMain;

public class AccountFragment extends Fragment {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView current_balance_popup, user_name, current_balance;
    private ImageButton cancel_button, more_button;
    private DatabaseManager dbManager;
    private Button add_balance, play_button, stop_button;
    private EditText amount;
    private Cursor cursor;
    private FragmentAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        more_button = (ImageButton) root.findViewById(R.id.more_button);
        dbManager = new DatabaseManager(getActivity());
        dbManager.open();
        user_name = (TextView) root.findViewById(R.id.user_name);
        current_balance = (TextView) root.findViewById(R.id.current_balance);
        cursor = dbManager.searchAccountID(Integer.toString(((userMain)getActivity()).getid()));

        user_name.setText(cursor.getString(1));
        current_balance.setText("CURRENT BALANCE:  " + cursor.getString(3));

        add_balance = (Button) root.findViewById(R.id.add_balance_button);
        add_balance.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    createAddBalanceDialog(inflater.getContext());
               }
        });

        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

//        final TextView textView = binding.textAccount;
//        accountViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void showMenu(View v) {
        Context wrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenu);
        PopupMenu popupMenu = new PopupMenu(wrapper, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.music)
                    createServiceDialog(getLayoutInflater().getContext());
                if(item.getItemId() == R.id.logout)
                    requireActivity().finish();
                return true;
            }
        });
        popupMenu.show();
    }

    public void createServiceDialog(Context context) {
        dialogBuilder = new AlertDialog.Builder(context);
        final View backgroundMusicPopupView = getLayoutInflater().inflate(R.layout.music_service, null);
        dialogBuilder.setView(backgroundMusicPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        play_button = (Button) backgroundMusicPopupView.findViewById(R.id.play_button);
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().startService(new Intent(requireActivity(), BackgroundMusicService.class));
            }
        });
        stop_button = (Button) backgroundMusicPopupView.findViewById(R.id.stop_button);
        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().stopService(new Intent(requireActivity(), BackgroundMusicService.class));
            }
        });
        cancel_button = (ImageButton) backgroundMusicPopupView.findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dialog.dismiss();
            }
        });
    }

    public void createAddBalanceDialog(Context context) {
        dialogBuilder = new AlertDialog.Builder(context);
        final View addBalancePopupView = getLayoutInflater().inflate(R.layout.add_balance_popup, null);
        current_balance_popup = (TextView) addBalancePopupView.findViewById(R.id.current_balance_popup);
        current_balance_popup.setText("CURRENT BALANCE:  " + cursor.getString(3));

        dialogBuilder.setView(addBalancePopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        amount= (EditText) addBalancePopupView.findViewById(R.id.amount);
        cancel_button = (ImageButton) addBalancePopupView.findViewById(R.id.button_cancel);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dialog.dismiss();
            }
        });
        add_balance = (Button) addBalancePopupView.findViewById(R.id.button_add);
        add_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (isNumeric(amount.getText().toString()) == true) {
                    dbManager.changeBalance(cursor.getString(1),Double.parseDouble(cursor.getString(3).replaceAll("[$]", ""))+Double.parseDouble(amount.getText().toString()));
                }
                dialog.dismiss();
            }
        });
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}