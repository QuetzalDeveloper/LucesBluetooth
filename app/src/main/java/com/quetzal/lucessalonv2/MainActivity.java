package com.quetzal.lucessalonv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton iluminacion, seguridad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iluminacion = (ImageButton) findViewById(R.id.iluminacion);
        seguridad = (ImageButton) findViewById(R.id.seguridad);

        iluminacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbrirIluminacion();
            }
        });
        seguridad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbrirSeguridad();
            }
        });
    }

    private void AbrirIluminacion(){
        Intent i = new Intent(this, Iluminacion.class);
        startActivity(i);
    }

    private void AbrirSeguridad(){
        Intent i = new Intent(this, seguridad.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
