package com.example.materialdesignapp_tarea;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Fragmento3 extends Fragment {

    private ArrayList<Item> shoppingList;
    private ShoppingListAdapter adapter;
    private EditText editName, editQuantity;
    private Spinner spinnerImages;
    private ListView listView;
    private TextView tvEmptyList;

    private int[] images = {
            R.drawable.fruit_icon,
            R.drawable.dairy_icon,
            R.drawable.vegetable_icon,
            R.drawable.meat_icon,
            R.drawable.drink_icon
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragmento3, container, false);

        // Inicializar lista
        shoppingList = new ArrayList<>();

        // Configurar vistas
        editName = view.findViewById(R.id.editName);
        editQuantity = view.findViewById(R.id.editQuantity);
        spinnerImages = view.findViewById(R.id.spinnerImages);
        listView = view.findViewById(R.id.listView);
        tvEmptyList = view.findViewById(R.id.tvEmptyList);
        Button btnAdd = view.findViewById(R.id.btnAdd);

        adapter = new ShoppingListAdapter(getActivity(), shoppingList);
        listView.setAdapter(adapter);

        // Agregar listener para actualizar visibilidad
        adapter.setOnItemRemovedListener(new ShoppingListAdapter.OnItemRemovedListener() {
            @Override
            public void onItemRemoved() {
                updateEmptyListVisibility();
            }
        });

        // Configurar adapter para la lista
        adapter = new ShoppingListAdapter(getActivity(), shoppingList);
        listView.setAdapter(adapter);

        // Actualizar visibilidad de lista vacía
        updateEmptyListVisibility();

        // Configurar spinner con imágenes
        ImageSpinnerAdapter imageAdapter = new ImageSpinnerAdapter(getActivity(), images);
        spinnerImages.setAdapter(imageAdapter);

        // Botón agregar
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        return view;
    }

    private void addItem() {
        String name = editName.getText().toString().trim();
        String quantityStr = editQuantity.getText().toString().trim();

        if (name.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(getActivity(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            int selectedImage = images[spinnerImages.getSelectedItemPosition()];

            Item newItem = new Item(name, quantity, selectedImage);
            shoppingList.add(newItem);
            adapter.notifyDataSetChanged();
            updateEmptyListVisibility();

            // Limpiar campos
            editName.setText("");
            editQuantity.setText("");

            // Enfocar el primer campo
            editName.requestFocus();

        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Cantidad no válida", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEmptyListVisibility() {
        if (shoppingList.isEmpty()) {
            listView.setVisibility(View.GONE);
            tvEmptyList.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            tvEmptyList.setVisibility(View.GONE);
        }
    }
}