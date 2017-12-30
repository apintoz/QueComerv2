package alberto.local.quecomer;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class Presentacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion);
        gestorBD migestorBD = new gestorBD(this);
        try
        {
            migestorBD.crear_base_de_datos();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void iniciar_buscador(View view) {
        Intent intento = new Intent(this,BuscaReceta.class);
        startActivity(intento);
    }

    public void mostrarAyuda(View view) {
            Intent auxiliar=new Intent(this,actividadAyuda.class);
            startActivity(auxiliar);
    }
}
