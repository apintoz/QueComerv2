package alberto.local.quecomer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import local.quick_stuff.utilitario;

public class actividad_lista_recetas extends AppCompatActivity {

    private motor_busqueda mimotor;

    private class item_lista
    {
        private Double puntaje;
        private int id_receta;
        private int visibilidad_receta;

        private ArrayList<ingrediente> ingredientes;

        public item_lista(Double puntaje, int id_receta, ArrayList<ingrediente> ingredientes) {
            this.puntaje = puntaje;
            this.id_receta = id_receta;
            this.ingredientes = ingredientes;
            this.visibilidad_receta = View.GONE;
        }

        public Double getPuntaje() {
            return puntaje;
        }

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

        public ArrayList<ingrediente> getIngredientes() {
            return ingredientes;
        }
    }

    private class adaptador_recetas extends ArrayAdapter<item_lista> implements  AdapterView.OnItemClickListener {
        private ArrayList<item_lista> datos_lista;
        Context micontexto;

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            item_lista item_listado  =(item_lista) parent.getItemAtPosition(position);
            TextView  ni_idea= view.findViewById(R.id.lista_ingredientes_validos);
            ni_idea.setVisibility(View.VISIBLE);
            item_listado.setVisibilidad_receta(View.VISIBLE);
        }

        private  class tarjeta
        {
            TextView vista_receta;
            TextView vista_puntaje;
            TextView vista_ingredientes;
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
            instancia_tarjeta.vista_receta.setText(Integer.toString(chamare.getId_receta()) + "-" + nombre_receta);
            instancia_tarjeta.vista_puntaje.setText(utilitario.formato_2_decimales(chamare.getPuntaje()));
            instancia_tarjeta.boton_receta.setTag(Integer.toString(chamare.getId_receta()));
            instancia_tarjeta.vista_ingredientes.setText("pendiente de desarrollar =)");
            instancia_tarjeta.vista_ingredientes.setVisibility(chamare.getVisibilidad_receta());
            return result;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_lista_recetas);
        ArrayList<ingrediente> p_lista_ingredientes =
                ( ArrayList<ingrediente>) getIntent().getSerializableExtra("listado_ingredientes");
        TreeMap<Integer,Double> puntaje_recetas =
                new TreeMap<>((Map<Integer,Double>)getIntent().getSerializableExtra("calificaciones"));
        gestorBD migestorBd = new gestorBD(this);
        mimotor = new motor_busqueda(migestorBd, p_lista_ingredientes,this,puntaje_recetas);

        ArrayList<item_lista> arreglo_datos = new ArrayList<>();
        Iterator<Integer> iterador = puntaje_recetas.keySet().iterator();
        while (iterador.hasNext())
        {
            int llave_aux = iterador.next();
            item_lista item_lista_aux = new item_lista(puntaje_recetas.get(llave_aux),llave_aux,p_lista_ingredientes);
            arreglo_datos.add(item_lista_aux);
        }
        adaptador_recetas mucha_variable = new adaptador_recetas(this, R.layout.cajita_listado_ingredientes, arreglo_datos);
        ListView listado_recetas_resultado = utilitario.$LV(findViewById(R.id.listado_puntajes));
        listado_recetas_resultado.setAdapter(mucha_variable);
        listado_recetas_resultado.setOnItemClickListener(mucha_variable);
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
}
