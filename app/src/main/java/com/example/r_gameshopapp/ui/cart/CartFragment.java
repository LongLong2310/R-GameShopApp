package com.example.r_gameshopapp.ui.cart;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.r_gameshopapp.BackgroundMusicService;
import com.example.r_gameshopapp.GridAdapter;
import com.example.r_gameshopapp.Item;
import com.example.r_gameshopapp.ItemList;
import com.example.r_gameshopapp.ListAdapter;
import com.example.r_gameshopapp.R;
import com.example.r_gameshopapp.databinding.FragmentCartBinding;
import com.example.r_gameshopapp.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment{

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button play_button, stop_button, contactConfirm;
    private ImageButton more_button, cancel_button;
    private EditText editTextContact, editTextSubject;
    private FragmentCartBinding binding;
    ListView cartList;
    ArrayList<Item> itemCartList = new ArrayList<>();
    List<Item> itemListCart;

    public CartFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CartViewModel notificationsViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);
        Context context = inflater.getContext();
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        more_button = (ImageButton) root.findViewById(R.id.more_button);

        cartList = root.findViewById(R.id.cart_list);
//        Bundle bundle = this.getArguments();
//        ItemList itemList = (ItemList) bundle.getSerializable("ItemCartList");
//
//        for (int i = 0; i < itemListCart.size(); i++) {
//            itemCartList.add(itemListCart.get(i));
//        }
//        itemCartList.add(new Item(1, "Pokemon", 3, "GAME", 69.99));
//        itemCartList.add(new Item(2, "Pokemon", 3, "GAME", 69.99));
//        itemCartList.add(new Item(3, "Pokemon", 3, "GAME", 69.99));
//        itemCartList.add(new Item(4, "Pokemon", 3, "GAME", 69.99));
//        itemCartList.add(new Item(5, "Pokemon", 3, "GAME", 69.99));

        ListAdapter listAdapter = new ListAdapter(context, R.layout.fragment_cart_item, itemCartList);
        cartList.setAdapter(listAdapter);

        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

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

    public void receiveDataHomeFragment(List<Item> itemListHome) {
        itemListCart = itemListHome;
    }
}