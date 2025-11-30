package com.example.prctica3_listadelacompra;

import android.content.ClipData;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Item> shoppingList;
    private ShoppingListAdapter adapter;
    private EditText editName, editQuantity;
    private Spinner spinnerImages;
    private ListView listView;
    private int[] images = {
            R.drawable.fruit_icon,
            R.drawable.dairy_icon,
            R.drawable.vegetable_icon,
            R.drawable.meat_icon,
            R.drawable.drink_icon
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicializar lista
        shoppingList = new ArrayList<>();

        // Configurar vistas
        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        spinnerImages = findViewById(R.id.spinnerImages);
        listView = findViewById(R.id.listView);
        Button btnAdd = findViewById(R.id.btnAdd);

        // Configurar adapter para la lista
        adapter = new ShoppingListAdapter(this, shoppingList);
        listView.setAdapter(adapter);

        // Configurar spinner con imágenes
        ImageSpinnerAdapter imageAdapter = new ImageSpinnerAdapter(this, images);
        spinnerImages.setAdapter(imageAdapter);

        // Registrar menú contextual para la lista
        registerForContextMenu(listView);

        // Botón agregar
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

    }
    private void addItem() {
        String name = editName.getText().toString().trim();
        String quantityStr = editQuantity.getText().toString().trim();

        if (name.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            int selectedImage = images[spinnerImages.getSelectedItemPosition()];

            Item newItem = new Item(name, quantity, selectedImage);
            shoppingList.add(newItem);
            adapter.notifyDataSetChanged();

            // Limpiar campos
            editName.setText("");
            editQuantity.setText("");

        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.invalid_quantity, Toast.LENGTH_SHORT).show();
        }
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String selectedItem = shoppingList.get(info.position).getNombre();
        menu.setHeaderTitle(R.string.options + selectedItem);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        if (item.getItemId() == R.id.menu_delete) {
            // Eliminar elemento
            shoppingList.remove(position);
            adapter.notifyDataSetChanged();
            return true;
        } else if (item.getItemId() == R.id.menu_add) {
            // Añadir elemento por defecto (como pide el ejercicio)
            Item defaultItem = new Item(getString(R.string.default_product), 1, images[0]);
            shoppingList.add(defaultItem);
            adapter.notifyDataSetChanged();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}