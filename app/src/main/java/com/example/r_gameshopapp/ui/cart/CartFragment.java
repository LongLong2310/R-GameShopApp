package com.example.r_gameshopapp.ui.cart;

import static com.example.r_gameshopapp.ui.home.HomeFragment.isNumeric;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.r_gameshopapp.BackgroundMusicService;
import com.example.r_gameshopapp.DatabaseManager;
import com.example.r_gameshopapp.Item;
import com.example.r_gameshopapp.ListAdapter;
import com.example.r_gameshopapp.R;
import com.example.r_gameshopapp.databinding.FragmentCartBinding;
import com.example.r_gameshopapp.userMain;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
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
    ListView cartList;
    private ArrayList<Item> itemCartList;
    private TextView itemName, itemStock, itemPrice, amount;
    private Button button_update_cart;
    private ImageView img;
    private Item updateItem;
    private ListAdapter listAdapter;

    public CartFragment() {

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

        if (((userMain) getActivity()).getPurchaseStatus()) {
            List<Item> list = new Gson().fromJson(((userMain) getActivity()).getTest(), new TypeToken<List<Item>>(){}.getType());
            itemCartList = new ArrayList<Item>(list);
            listAdapter = new ListAdapter(context, R.layout.fragment_cart_item, itemCartList);
            cartList.setAdapter(listAdapter);
            cartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AlertDialog dialog = new
                            AlertDialog.Builder(context).create();
                    dialog.setTitle(itemCartList.get(position).getitemName());
                    dialog.setMessage("Do you want to delete or update this stock?");
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE,
                            "Delete",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int
                                        which) {
                                    itemCartList.remove(itemCartList.get(position));
                                    ((userMain) getActivity()).setCurrentItemList(itemCartList);
                                    listAdapter.notifyDataSetChanged();
                                    total=0;
                                    for (int i = 0; i < itemCartList.size(); i++) {
                                        total += itemCartList.get(i).getitemPrice()*itemCartList.get(i).getitemStock();
                                    }
                                    totalTextView.setText("TOTAL:  $" + Double.parseDouble(new DecimalFormat("##.##").format(total)));

                                }
                            });
                    dialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                            "Update",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int
                                        which) {
                                    createUpdateItemDetailDialog(context,itemCartList.get(position), position);
                                    listAdapter.notifyDataSetChanged();
                                }
                            });
                    dialog.show();
                }
            });
            totalTextView = root.findViewById(R.id.total_price);
            for (int i = 0; i < itemCartList.size(); i++) {
                total += itemCartList.get(i).getitemPrice()*itemCartList.get(i).getitemStock();
            }
            totalTextView.setText("TOTAL:  $" + Double.parseDouble(new DecimalFormat("##.##").format(total)));

            purchase_button = root.findViewById(R.id.purchase_button);
            purchase_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Double.parseDouble(u.getString(3).replaceAll("[$]", "")) - total>=0) {
                        for (int i = 0; i < list.size(); i++) {
                            String s = list.get(i).getitemName();
                            Cursor c = dbManager.searchStockName(s);
                            dbManager.buy(s, c.getInt(4) - list.get(i).getitemStock());
                        }

                        dbManager.BuyBalance(u.getString(1), Double.parseDouble(u.getString(3).replaceAll("[$]", "")) - Double.parseDouble(new DecimalFormat("##.##").format(total)));
                        dbManager.insertHistory(((userMain) getActivity()).getid(),itemCartList,Double.parseDouble(new DecimalFormat("##.##").format(total)));
                        ((userMain) getActivity()).isPurchase(false);
                        cartList.setAdapter(null);
                        itemCartList.clear();
                        ((userMain) getActivity()).setCurrentItemList(itemCartList);
                        ((userMain) getActivity()).setFirstAddToCart(false);
                        totalTextView.setText("TOTAL:  $0");
                        Cursor u=dbManager.searchAccountID(Integer.toString(((userMain)getActivity()).getid()));
                        balanceTextView.setText("CURRENT BALANCE: $"+ Double.parseDouble(new DecimalFormat("##.##").format(Double.parseDouble(u.getString(3).replaceAll("[$]", "")))));
                        ((userMain) getActivity()).setFirstAddToCart(true);
                    } else {
                        Toast.makeText(getContext(), " over balance", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

        balanceTextView = root.findViewById(R.id.balance);
        balanceTextView.setText("CURRENT BALANCE: $"+ Double.parseDouble(new DecimalFormat("##.##").format(Double.parseDouble(u.getString(3).replaceAll("[$]", "")))));

        ((userMain) getActivity()).setCurrentItemList(itemCartList);
        return root;
    }

    public void createUpdateItemDetailDialog(Context context, Item item, int position){
        dialogBuilder = new AlertDialog.Builder(context);
        dbManager = new DatabaseManager(getActivity());
        dbManager.open();
        Cursor cursor = dbManager.searchStockName(item.getitemName());
        String itemStockDB = cursor.getString(4);
        final View itemDetailPopupView = getLayoutInflater().inflate(R.layout.item_detail, null);
        itemName = (TextView) itemDetailPopupView.findViewById(R.id.item_name);
        itemName.setText(item.getitemName());
        itemStock = (TextView) itemDetailPopupView.findViewById(R.id.item_stock);
        itemStock.setText("STOCK: " +  itemStockDB);
        itemPrice = (TextView) itemDetailPopupView.findViewById(R.id.item_price);
        itemPrice.setText("$" + (item.getitemPrice()));
        amount = (EditText) itemDetailPopupView.findViewById(R.id.amount);
        img = (ImageView) itemDetailPopupView.findViewById(R.id.imageView);
        if (item.getitemCategory().equals("GAME")) {
            img.setImageResource(R.drawable.game);
        }
        if (item.getitemCategory().equals("CONSOLE")) {
            img.setImageResource(R.drawable.console);
        }
        if (item.getitemCategory().equals("ACCESSORY")) {
            img.setImageResource(R.drawable.accessories);
        }

        if (item.getitemStock() == 0) {
            itemStock.setTextColor(Color.RED);
        }

        cancel_button = (ImageButton) itemDetailPopupView.findViewById(R.id.cancel_button);
        button_update_cart = (Button) itemDetailPopupView.findViewById(R.id.button_add_to_cart);
        button_update_cart.setText("Update");

        dialogBuilder.setView(itemDetailPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dialog.dismiss();
            }
        });

        button_update_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNumeric(amount.getText().toString())) {
                    if (Integer.parseInt(amount.getText().toString()) <= Integer.parseInt(itemStockDB)) {
                        String NameItem = itemName.getText().toString();
                        double PriceItem = Double.parseDouble(itemPrice.getText().toString().replaceAll("[$]", ""));
                        updateItem = new Item(NameItem, Integer.parseInt(amount.getText().toString()),item.getitemCategory(), PriceItem);
                        itemCartList.set(position, updateItem);
                        listAdapter.notifyDataSetChanged();
                        total=0;
                        for (int i = 0; i < itemCartList.size(); i++) {
                            total += itemCartList.get(i).getitemPrice()*itemCartList.get(i).getitemStock();
                        }
                        totalTextView.setText("TOTAL:  $" + Double.parseDouble(new DecimalFormat("##.##").format(total)));
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Buy amount over stock", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
}