package alberto.local.quecomer;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import local.quick_stuff.utilitario;

public class actividad_lista_recetas extends AppCompatActivity {

    private motor_busqueda mimotor;
    public adaptador_recetas miadaptador;
    public ArrayList<item_lista> arreglo_datos;


    private class item_lista
    {
        private Double puntaje;
        private int id_receta;
        private int visibilidad_receta;
        private String categorias_recetas;
        private int visibilidad_barrita;
        private ArrayList<ingrediente> ingredientes;

        public item_lista(Double puntaje, int id_receta, ArrayList<ingrediente> ingredientes, String categorias) {
            this.puntaje = puntaje;
            this.id_receta = id_receta;
            this.ingredientes = ingredientes;
            this.categorias_recetas = categorias;
            this.visibilidad_receta = View.GONE;
            this.visibilidad_barrita = View.GONE;
        }

        public Double getPuntaje() {
            return puntaje;
        }

        public String getCategorias_recetas() { return categorias_recetas;}

        public int getId_receta() {
            return id_receta;
        }

        public int getVisibilidad_receta()
        {
            return visibilidad_receta;
        }
        public void setVisibilidad_receta(int para)
        {
            this.visibilidad_receta = para;
        }
        public int getVisibilidad_barrita() { return visibilidad_barrita;}
        public void setVisibilidad_barrita(int para) { this.visibilidad_barrita = para;}
                public ArrayList<ingrediente> getIngredientes() {
            return ingredientes;
        }
    }



    private class adaptador_recetas extends ArrayAdapter<item_lista> implements  AdapterView.OnItemClickListener, Filterable {
        private ArrayList<item_lista> datos_lista;
        Context micontexto;

        public ArrayList<item_lista> getDatos_lista() {
            return datos_lista;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            item_lista item_listado  =(item_lista) parent.getItemAtPosition(position);
            TextView  ni_idea= view.findViewById(R.id.lista_ingredientes_validos);
            View barrita_divisoria = view.findViewById(R.id.barrita_divisoria);
            if (ni_idea.getVisibility() == View.VISIBLE)
            {
                ni_idea.setVisibility(View.GONE);
                barrita_divisoria.setVisibility(View.GONE);
                item_listado.setVisibilidad_receta(View.GONE);
                item_listado.setVisibilidad_barrita(View.GONE);
            }
            else
            {
                ni_idea.setVisibility(View.VISIBLE);
                item_listado.setVisibilidad_receta(View.VISIBLE);
                item_listado.setVisibilidad_barrita(View.VISIBLE);
                barrita_divisoria.setVisibility(View.VISIBLE);
            }

        }

        private  class tarjeta
        {
            TextView vista_receta;
            TextView vista_puntaje;
            TextView vista_ingredientes;
            TextView vista_categorias;
            ImageButton boton_receta;

        }

        public adaptador_recetas(@NonNull Context context, int resource, @NonNull List<item_lista> objects) {
            super(context, resource, objects);
            this.datos_lista = datos_lista;
            this.micontexto = micontexto;
        }



        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            item_lista chamare = getItem(position);
            tarjeta instancia_tarjeta;
            final View result;

            if (convertView==null)
            {
                instancia_tarjeta = new tarjeta();
                LayoutInflater inflador = LayoutInflater.from(getContext());
                convertView = inflador.inflate(R.layout.cajita_listado_ingredientes,parent,false);
                instancia_tarjeta.vista_receta = (TextView)convertView.findViewById(R.id.nombre_receta);
                instancia_tarjeta.vista_puntaje = (TextView)convertView.findViewById(R.id.puntaje);
                instancia_tarjeta.vista_ingredientes = (TextView) convertView.findViewById(R.id.lista_ingredientes_validos);
                instancia_tarjeta.boton_receta = (ImageButton) convertView.findViewById(R.id.boton_receta_damn);
                instancia_tarjeta.vista_categorias =  (TextView) convertView.findViewById(R.id.texto_categorias);
                result = convertView;
                convertView.setTag(instancia_tarjeta);
            }
            else
            {
                instancia_tarjeta = (tarjeta) convertView.getTag();
                result = convertView;
            }
            Cursor micursor_id_recetas = mimotor.obtener_campo_con_llave("NOMBRE", "RECETAS",
                    Integer.toString(chamare.getId_receta()),"ID_RECETA");
            micursor_id_recetas.moveToFirst();
            String nombre_receta = micursor_id_recetas.getString(micursor_id_recetas.getColumnIndex("NOMBRE"));
            instancia_tarjeta.vista_receta.setText(nombre_receta);
            instancia_tarjeta.vista_puntaje.setText(utilitario.formato_2_decimales(chamare.getPuntaje()));
            instancia_tarjeta.boton_receta.setTag(Integer.toString(chamare.getId_receta()));
            instancia_tarjeta.vista_categorias.setText(chamare.categorias_recetas.replace(","," - ").toLowerCase());
            pintar_ingredientes_por_receta(instancia_tarjeta.vista_ingredientes , mimotor, chamare.getId_receta() );
            instancia_tarjeta.vista_ingredientes.setVisibility(chamare.getVisibilidad_receta());
            return result;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_lista_recetas);
        android.support.v7.widget.Toolbar barra =(android.support.v7.widget.Toolbar) getLayoutInflater().inflate(local.quick_stuff.R.layout.barra_herramientas,null);
        LinearLayout layout_actividad = (LinearLayout) findViewById(R.id.cp_lista_receta);
        layout_actividad.addView(barra,0);
        setSupportActionBar(barra);
        getSupportActionBar().setTitle("Listado de Recetas");
        ArrayList<ingrediente> p_lista_ingredientes =
                ( ArrayList<ingrediente>) getIntent().getSerializableExtra("listado_ingredientes");
        TreeMap<Integer,Double> puntaje_recetas =
                new TreeMap<>((Map<Integer,Double>)getIntent().getSerializableExtra("calificaciones"));
        TreeMap<Integer,String> categorias_recet =
                new TreeMap<>((Map<Integer, String>)getIntent().getSerializableExtra("categorias"));
        gestorBD migestorBd = new gestorBD(this);
        mimotor = new motor_busqueda(migestorBd, p_lista_ingredientes,this,puntaje_recetas);


        arreglo_datos = new ArrayList<>();
        Iterator<Integer> iterador =mimotor.IdByValues(mimotor.obtenerTabla_puntajes()).iterator();
        while (iterador.hasNext())
        {
            int llave_aux = iterador.next();
            item_lista item_lista_aux = new item_lista(puntaje_recetas.get(llave_aux),llave_aux,p_lista_ingredientes, categorias_recet.get(llave_aux));
            arreglo_datos.add(item_lista_aux);
        }
        miadaptador = new adaptador_recetas(this, R.layout.cajita_listado_ingredientes, arreglo_datos);
        ListView listado_recetas_resultado = utilitario.$LV(findViewById(R.id.listado_puntajes));
        listado_recetas_resultado.setAdapter(miadaptador);
        listado_recetas_resultado.setOnItemClickListener(miadaptador);
        //utilitario.setListViewHeightBasedOnChildren(listado_recetas_resultado);
    }
    public void me_clickearon(View vista)
    {
        ImageButton boton = (ImageButton) vista;
        LinearLayout layout_damn = (LinearLayout)boton.getParent();
        Intent intento = new Intent(this,VisorReceta.class);
        intento.putExtra("listado_ingredientes", mimotor.obtener_listado_ingredientes() );
        intento.putExtra("calificaciones",mimotor.obtenerTabla_puntajes());
        intento.putExtra("id_receta",(String)vista.getTag());
        startActivity(intento);
    }

    public void pintar_ingredientes_por_receta ( TextView vista, motor_busqueda mimotor, int id_receta )
    {
     ArrayList<ingrediente> mi_listado_ingredientes = mimotor.obtener_listado_ingredientes();
        Cursor micursor_id_ingredientes = mimotor.obtener_campo_con_llave("ID_INGREDIENTE", "RECETA_INGREDIENTES"
                , Integer.toString(id_receta),"ID_RECETA");
        String cadena = "";
        if (micursor_id_ingredientes.moveToFirst())
            do {
                {
//                  int id_ingrediente  = mi_listado_ingredientes.get(0).id_ingrediente_en_bd;
                    int aux = micursor_id_ingredientes.getInt(micursor_id_ingredientes.getColumnIndex("ID_INGREDIENTE"));
                    Cursor curso_aux = mimotor.obtener_campo_con_llave("NOMBRE", "INGREDIENTES", Integer.toString(aux), "ID_INGREDIENTE");
                    curso_aux.moveToFirst();
                    String nombre_ingrediente = curso_aux.getString(curso_aux.getColumnIndex("NOMBRE"));
                    cadena = cadena + "- " + nombre_ingrediente.toLowerCase() + "\n";
                }
            }
            while (micursor_id_ingredientes.moveToNext());
        vista.setText(cadena);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_barra, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.filtro:
                mi_dialogo_filtro dialogo = new mi_dialogo_filtro();
                dialogo.show(getFragmentManager(),"howdy");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class mi_dialogo_filtro extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();


            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            LinearLayout contenedor_dialogo = (LinearLayout) inflater.inflate(R.layout.dialogo_filtro,null);
            LinearLayout contenedor_categorias_filtro = (LinearLayout) inflater.inflate(R.layout.filter_part,null);

            LinearLayout.LayoutParams  parametros_layout= new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            parametros_layout.setMargins(20,20,10,20);
            contenedor_categorias_filtro.setLayoutParams(parametros_layout);

            ArrayList <item_lista>  listado_items = arreglo_datos;
            HashSet<String>  set_categorias = new HashSet<>();
            for (item_lista aux1 : listado_items)
            {
                String[] aux2  = aux1.getCategorias_recetas().split(",");
                for (String aux3 : aux2)
                {
                    set_categorias.add(aux3);
                }
            }
            Iterator mi_iterador = set_categorias.iterator();
            while (mi_iterador.hasNext())
            {
                CheckBox mibox = new CheckBox(getActivity());
                mibox.setText((String)mi_iterador.next());
                mibox.setChecked(true);
                mibox.setLayoutParams(parametros_layout);
                contenedor_categorias_filtro.addView(mibox);

            }

            contenedor_dialogo.addView(contenedor_categorias_filtro);
            builder.setView(contenedor_dialogo)
                    // Add action buttons
                    .setPositiveButton("Filtrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // sign in the user ...
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mi_dialogo_filtro.this.getDialog().cancel();
                        }
                    });
            return builder.create();

        }

    }


}
