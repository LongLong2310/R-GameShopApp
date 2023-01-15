package com.example.r_gameshopapp.ui.cart;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.example.r_gameshopapp.BackgroundMusicService;
import com.example.r_gameshopapp.DatabaseManager;
import com.example.r_gameshopapp.GridAdapter;
import com.example.r_gameshopapp.Item;
import com.example.r_gameshopapp.ItemList;
import com.example.r_gameshopapp.ListAdapter;
import com.example.r_gameshopapp.R;
import com.example.r_gameshopapp.databinding.FragmentCartBinding;
import com.example.r_gameshopapp.ui.home.HomeFragment;
import com.example.r_gameshopapp.userMain;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment{

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button play_button, stop_button, contactConfirm, purchase_button;
    private ImageButton more_button, cancel_button;
    private EditText editTextContact, editTextSubject;
    private FragmentCartBinding binding;
    private DatabaseManager dbManager;
    private TextView balanceTextView, totalTextView;
    private double balance, total = 0;
    ObjectMapper mapper = new ObjectMapper();
    ListView cartList;
//    ArrayList<Item> itemCartList;
    private String string;
//    private List<Item> itemListCart;
    private List<Item> itemListCart = new ArrayList<>();
    private userMain userMain;
    private ArrayList<Item> itemCartList;

    public CartFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null) {
            System.out.println("098");
            string = savedInstanceState.getString("outState");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dbManager = new DatabaseManager(getActivity());
        dbManager.open();
        Cursor u=dbManager.searchAccountID(Integer.toString(((userMain)getActivity()).getid()));
        Context context = inflater.getContext();
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        more_button = (ImageButton) root.findViewById(R.id.more_button);

        cartList = root.findViewById(R.id.cart_list);

        userMain = (com.example.r_gameshopapp.userMain) getActivity();
        List<Item> list = new Gson().fromJson(userMain.getTest(), new TypeToken<List<Item>>(){}.getType());
        itemCartList = new ArrayList<Item>(list);
        ListAdapter listAdapter = new ListAdapter(context, R.layout.fragment_cart_item, itemCartList);
        cartList.setAdapter(listAdapter);

        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

        balanceTextView = root.findViewById(R.id.balance);
        balanceTextView.setText("CURRENT BALANCE:  "+u.getString(3));
        totalTextView = root.findViewById(R.id.total_price);
        for (int i = 0; i < itemCartList.size(); i++) {
            total += itemCartList.get(i).getitemPrice()*itemCartList.get(i).getitemStock();
        }
        totalTextView.setText("TOTAL:  $" + total);
        purchase_button = root.findViewById(R.id.purchase_button);
        purchase_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Double.parseDouble(u.getString(3).replaceAll("[$]", "")) - total>=0) {
                    for (int i = 0; i < list.size(); i++) {
                        System.out.println(list.get(i).getitemName());
                        String s = list.get(i).getitemName();
                        Cursor c = dbManager.searchStockName(s);
                        dbManager.buy(s, c.getInt(4) - list.get(i).getitemStock());
                    }

                    dbManager.BuyBalance(u.getString(1), Double.parseDouble(u.getString(3).replaceAll("[$]", "")) - total);
//                    dbManager.insertCart(((userMain) getActivity()).getid(),itemCartList,total);
                    dbManager.insertHistory(((userMain) getActivity()).getid(),itemCartList,total);
                }else {
                    Toast.makeText(getContext(), " over balance", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        System.out.println(string);
        outState.putString("outState", string);
    }

    private List<Item> toList(String jsonString) {
        try {
            return mapper.readValue(jsonString, new TypeReference<List<Item>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractInt(String str)
    {
        // Replacing every non-digit number
        // with a space(" ")
        str = str.replaceAll("[^0-9]", " "); // regular expression

        // Replace all the consecutive white
        // spaces with a single space
        str = str.replaceAll(" +", "");

        if (str.equals(""))
            return "-1";

        return str;
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
                if(item.getItemId() == R.id.contact)
                    createContactUsDialog(getLayoutInflater().getContext());
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

    public void createContactUsDialog(Context context) {
        dialogBuilder = new AlertDialog.Builder(context);
        final View contactUsPopupView = getLayoutInflater().inflate(R.layout.contact_popup, null);
        editTextContact = (EditText) contactUsPopupView.findViewById(R.id.editTextContact);
        editTextSubject = (EditText) contactUsPopupView.findViewById(R.id.editTextSubject);
        dialogBuilder.setView(contactUsPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        contactConfirm = (Button) contactUsPopupView.findViewById(R.id.button_send);
        contactConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to="chitoan.tran@yahoo.com.vn";
                String subject = editTextSubject.getText().toString();
                String message = editTextContact.getText().toString();

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                //need this to prompts email client only
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });
        cancel_button = (ImageButton) contactUsPopupView.findViewById(R.id.button_cancel);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    @Override
//    public void sendData(ItemList ItemList) {
//        System.out.println(ItemList);
//    }

    public List<Item> receiveDataHomeFragment(List<Item> itemListHome) {
//        itemListCart = new ArrayList<>(itemListHome);
        return itemListHome;
    }
}