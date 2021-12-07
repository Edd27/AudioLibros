package com.example.audiolibros;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private com.example.audiolibros.Aplicacion app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        });


        app = new com.example.audiolibros.Aplicacion();



        if (findViewById(R.id.contenedor_pequeno) != null &&
                getSupportFragmentManager()
                        .findFragmentById(R.id.contenedor_pequeno) == null
        ) {

            getSupportFragmentManager().beginTransaction().
                    setReorderingAllowed(true)
                    .add(
                            R.id.contenedor_pequeno,
                            com.example.audiolibros.SelectorFragment.class, null).commit();

        }
    }

    public void mostrarDetalle(int pos) {
        DetalleFragment detalleFragment =
                (DetalleFragment) getSupportFragmentManager().
                        findFragmentById(R.id.detalle_fragment);
        if (detalleFragment != null) {
            detalleFragment.setInfoLibro(pos);
        } else {

            detalleFragment =
                    new DetalleFragment();
            Bundle bundle = new Bundle();

            bundle.putInt(DetalleFragment.ARG_INDEX_LIBRO,
                    pos
            );

            detalleFragment.setArguments(
                    bundle
            );

            getSupportFragmentManager().beginTransaction().
                    setReorderingAllowed(true)
                    .replace(R.id.contenedor_pequeno, detalleFragment)
                    .addToBackStack(null)
                    .commit();
        }
        SharedPreferences pref = getSharedPreferences(
                "course.android.audiolibros_v1_internal", MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("ultimo", pos);
        editor.commit();
    }

    public void irUltimoVisitado() {
        SharedPreferences pref = getSharedPreferences(
                "course.android.audiolibros_v1_internal", MODE_PRIVATE);
        int id = pref.getInt("ultimo", -1);
        if (id >= 0) {
            mostrarDetalle(id);
        } else {
            Toast.makeText(this,"Sin última vista",Toast.LENGTH_LONG).show();
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menu_preferencias){
            Toast.makeText(this, "Preferencias", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.menu_acerca) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Mensaje de Acerca De");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}