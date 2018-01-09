package alberto.local.quecomer;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import local.quick_stuff.*;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toolbar;

import java.util.ArrayList;

public class BuscaReceta extends AppCompatActivity {

    public static int MAX_INGREDIENTES_BUSQUEDA = 12;
    public static String NOMBRE_BD = "cascaron_comida12";
    public ArrayList<String> listado;
    private adaptadorBDIngredientes madaptadorbdingredientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_receta);
        android.support.v7.widget.Toolbar barra =(android.support.v7.widget.Toolbar) getLayoutInflater().inflate(local.quick_stuff.R.layout.barra_herramientas,null);
        LinearLayout layout_actividad = (LinearLayout) findViewById(R.id.cp_buscar_receta);
        layout_actividad.addView(barra,0);
        setSupportActionBar(barra);
        getSupportActionBar().setTitle("Buscador de Recetas");
        inicializar_listado();
        madaptadorbdingredientes = new adaptadorBDIngredientes(this);
        madaptadorbdingredientes.open();
        LinearLayout contenedor_ingredientes= (LinearLayout) findViewById(R.id.contenedor_ingredientes);
        for (int i= 0;i<3;i++)
        {
            agregar_ingrediente(contenedor_ingredientes);
        }

    }

    public void nuevo_ingrediente(View view) {


        LinearLayout contenedor_ingredientes= (LinearLayout) findViewById(R.id.contenedor_ingredientes);
        int cantidad_ingredientes = contenedor_ingredientes.getChildCount();
        if (cantidad_ingredientes>MAX_INGREDIENTES_BUSQUEDA)
        {
            utilitario.pequeño_toast(this,"El maximo numero de ingrediente es 12");
        }
        else
        {
            agregar_ingrediente(contenedor_ingredientes);
        }


    }

    public void agregar_ingrediente( LinearLayout layout)
    {
        LinearLayout cajita = (LinearLayout) this.getLayoutInflater().inflate(R.layout.caja_ingrediente,null);
        Spinner boton_selector_unidades = (Spinner) cajita.findViewById(R.id.selector_unidades);
        AutoCompleteTextView texto_ingredientes = (AutoCompleteTextView) cajita.findViewById(R.id.input_ingrediente);
        adaptador_autocompletar funciona = new adaptador_autocompletar(madaptadorbdingredientes,this);
        texto_ingredientes.setAdapter(funciona);
        texto_ingredientes.setOnItemClickListener(funciona);
        ArrayAdapter<String > adaptador_spinner = new  ArrayAdapter<String>(this, R.layout.spiner_item,listado);
        adaptador_spinner.setDropDownViewResource(android.R.layout.simple_spinner_item);
        boton_selector_unidades.setAdapter(adaptador_spinner);
        layout.addView(cajita);
    }

    public void inicializar_listado()
    {
        listado = new ArrayList<>();
        SQLiteDatabase db = openOrCreateDatabase(
                NOMBRE_BD, MODE_PRIVATE, null);
        Cursor cr = db.rawQuery(
                "SELECT OUTPUT_TEXT FROM LVAL WHERE USO = 'UNIDADES'", null);
        if (cr.moveToFirst()) {
            do{
                String cadena_aux = cr.getString(cr.getColumnIndex("OUTPUT_TEXT"));
                listado.add(cadena_aux);
            } while (cr.moveToNext());
            cr.close();
        }
    }

    public void Buscar_recetas(View view) {
        gestorBD mi_gestor = new gestorBD(view.getContext());
        motor_busqueda motor = new motor_busqueda(mi_gestor,view.getContext());

        LinearLayout contenedor_ingredientes = (LinearLayout) findViewById(R.id.contenedor_ingredientes);
        for (int i =0; i<contenedor_ingredientes.getChildCount();i++)
        {
            LinearLayout cajita = (LinearLayout) contenedor_ingredientes.getChildAt(i);
            EditText caja_nombre_ingrediente = (EditText) cajita.findViewById(R.id.input_ingrediente);
            EditText caja_cantidad_ingrediente = (EditText) cajita.findViewById(R.id.cantidades_ingrediente);
            String ingrediente = caja_nombre_ingrediente.getText().toString();
            int cantidad_ingrediente = Integer.parseInt(caja_cantidad_ingrediente.getText().toString());
            motor.agregar_ingrediente(ingrediente,cantidad_ingrediente,"GR" );

        }
        motor.procesar_ingredientes();
        Intent intento = new Intent(this,actividad_lista_recetas.class);
        intento.putExtra("categorias", motor.getCategorias_recetas());
        intento.putExtra("listado_ingredientes", motor.obtener_listado_ingredientes() );
        intento.putExtra("calificaciones",motor.obtenerTabla_puntajes());
        startActivity(intento);

    }


}
