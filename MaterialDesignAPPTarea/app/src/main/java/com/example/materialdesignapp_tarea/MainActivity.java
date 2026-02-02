package com.example.materialdesignapp_tarea;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar Toolbar
        setSupportActionBar(findViewById(R.id.toolbar));

        // Referencias
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        fab = findViewById(R.id.fab);

        // Configurar ViewPager
        setupViewPager();

        // Vincular TabLayout con ViewPager
        tabLayout.setupWithViewPager(viewPager);

        // Agregar iconos a las pestañas
        tabLayout.getTabAt(0).setIcon(android.R.drawable.ic_dialog_email);
        tabLayout.getTabAt(1).setIcon(android.R.drawable.ic_dialog_info);
        tabLayout.getTabAt(2).setIcon(android.R.drawable.ic_dialog_dialer);

        // Cambiar icono del FAB
        fab.setImageResource(android.R.drawable.ic_menu_send);

        // Acción del FAB
        fab.setOnClickListener(v -> {
            Snackbar.make(v, "Mensaje enviado correctamente", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Llamar al método undoValidation del Fragment 1
                            undoEmailValidation();
                            Snackbar.make(v, "Validación deshecha", Snackbar.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        });
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragmento1(), "Formulario");
        adapter.addFragment(new Fragmento2(), "Info");
        adapter.addFragment(new Fragmento3(), "Lista Compra");
        viewPager.setAdapter(adapter);
    }

    private void undoEmailValidation() {
        // Obtener el Fragment 1 actual del ViewPager
        Fragmento1 fragmentOne = (Fragmento1) ((ViewPagerAdapter) viewPager.getAdapter())
                .getItem(viewPager.getCurrentItem());

        // Verificar que estamos en la pestaña 1
        if (viewPager.getCurrentItem() == 0) {
            fragmentOne.undoValidation();
        }
    }
}