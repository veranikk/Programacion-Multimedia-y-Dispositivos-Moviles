package com.example.textimputlayout;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear);

        TextInputLayout emailLayout= findViewById(R.id.emailLayout);
        TextInputEditText emailEditText= findViewById(R.id.emailEditText);
        Button validateButton= findViewById(R.id.validateButton);
        TextView textView= findViewById(R.id.textView);
        FloatingActionButton fab = findViewById(R.id.fab);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= emailEditText.getText().toString();
                if (!isValidEmail(email)) {
                    emailLayout.setError("Invalid email address");
                } else {
                    emailLayout.setError(null); //quita el mensaje de error
                }
                Snackbar.make(v,"This is a SnackBar!", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {
                                //accion al presionar "UNDO"
                                Snackbar.make(v,"Action undone", Snackbar.LENGTH_SHORT).show();
                            }
                        } ).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Floating Action Button Clicked!");
                Toast.makeText(MainActivity.this, "FAB Pressed!",
                        Toast.LENGTH_SHORT).show();
            }
        });



    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}