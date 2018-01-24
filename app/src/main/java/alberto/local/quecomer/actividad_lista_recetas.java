package alberto.local.quecomer;


import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Filter;
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

public class actividad_lista_recetas extends AppCompatActivity implements mi_dialogo_filtro.escuchador_dialogo {

    private motor_busqueda mimotor;
    public adaptador_recetas miadaptador;
    public ArrayList<item_lista> arreglo_datos;
    private HashSet<String> categorias_dt2;

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
        @Override
        public int getCount() {
            return datos_lista_filtrados.size();
        }

        @Nullable
        @Override
        public item_lista getItem(int position) {
            return datos_lista_filtrados.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private ArrayList<item_lista> datos_lista;
        private ArrayList<item_lista> datos_lista_filtrados;
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


        @Override
        public Filter getFilter() {

            Filter mifiltro = new Filter() {
                @SuppressWarnings("unchecked")
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults resultados_filtrados = new FilterResults();
                    ArrayList<item_lista> temp_lista = new ArrayList<>();
                    boolean filtrar = false;

                    if (constraint != null && !constraint.equals("") && datos_lista != null) {
                        int longitud = datos_lista.size();
                        int i = 0;
                        String categorias = constraint.toString();
                        filtrar = false;
                        String[] array_categorias = categorias.split(",");
                        while (i < longitud) {
                            filtrar= false;
                            for (int j = 0; j < array_categorias.length; j++) {
                                if (datos_lista.get(i).getCategorias_recetas().contains(array_categorias[j])) {
                                    filtrar = true;
                                }
                            }
                            if (!filtrar) {
                                datos_lista.get(i).getCategorias_recetas();
                                temp_lista.add(datos_lista.get(i));
                            }
                            i++;
                        }
                    }
                    else
                    {
                        temp_lista.addAll(datos_lista);
                    }
                    resultados_filtrados.values = temp_lista;
                    resultados_filtrados.count = temp_lista.size();
                    return resultados_filtrados;
                }

                @Override
                @SuppressWarnings("unchecked")
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    datos_lista_filtrados.clear();
                    datos_lista_filtrados.addAll((ArrayList<item_lista>) results.values);
                    notifyDataSetChanged();
                }
            };
            return mifiltro;
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
            this.datos_lista = (ArrayList<item_lista>)objects;
            this.datos_lista_filtrados = new ArrayList<>();
            datos_lista_filtrados.addAll(datos_lista);
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
                instancia_tarjeta.vista_receta = convertView.findViewById(R.id.nombre_receta);
                instancia_tarjeta.vista_puntaje = convertView.findViewById(R.id.puntaje);
                instancia_tarjeta.vista_ingredientes = convertView.findViewById(R.id.lista_ingredientes_validos);
                instancia_tarjeta.boton_receta = convertView.findViewById(R.id.boton_receta_damn);
                instancia_tarjeta.vista_categorias = convertView.findViewById(R.id.texto_categorias);
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
        LinearLayout layout_actividad = findViewById(R.id.cp_lista_receta);
        layout_actividad.addView(barra,0);
        setSupportActionBar(barra);
        getSupportActionBar().setTitle("Listado de Recetas");
        getSupportActionBar().setIcon(R.mipmap.v2);
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
        categorias_dt2 = obtener_categorias_de_tabla(arreglo_datos);
        ListView listado_recetas_resultado = utilitario.$LV(findViewById(R.id.listado_puntajes));
        listado_recetas_resultado.setAdapter(miadaptador);
        listado_recetas_resultado.setOnItemClickListener(miadaptador);
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
                Bundle argumentos = new Bundle();
                argumentos.putSerializable("listado_categorias_bundle", categorias_dt2 );
                dialogo.setArguments(argumentos);
                dialogo.show(getFragmentManager(),"howdy");
                return true;
            case R.id.envio_mail:
                String mailto = "mailto:apintoz@uni.pe" +
                        "?cc=" + "elpadredelcordero@gmail.com" +
                        "&subject=" + "Sugerencia app " + this.toString() +
                        "&body=" + Uri.encode("");

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));
                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    //TODO: Handle case where no email app is available
                }
                return true;
                default:
                return super.onOptionsItemSelected(item);
        }
    }

    public HashSet<String> obtener_categorias_de_tabla(ArrayList<item_lista> parametro)
    {
        HashSet<String>  set_categorias = new HashSet<>();
        for (item_lista aux1 : parametro)
        {
            String[] aux2  = aux1.getCategorias_recetas().split(",");
            for (String aux3 : aux2)
            {
                set_categorias.add(aux3);
            }
        }
        return set_categorias;
    }

    @Override
    public void en_positivo(DialogFragment dialogo) {
        mi_dialogo_filtro  m_dialogo = (mi_dialogo_filtro) dialogo;
        utilitario.pequeño_toast(this, m_dialogo.cadena);
        ListView lista = (ListView)findViewById(R.id.listado_puntajes);
        adaptador_recetas mnadaptador = (adaptador_recetas) lista.getAdapter();
        mnadaptador.getFilter().filter(m_dialogo.cadena);
    }

    @Override
    public void en_negativo(DialogFragment dialogo) {

    }
}
