package alberto.local.quecomer;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by - on 21/01/2018.
 */

public class Adaptador_spinner extends ArrayAdapter<Adaptador_spinner.item> {

    private adaptadorBDIngredientes madaptadorbdingredientes;
    private ArrayList<item> listado ;
    public static int MAX_INGREDIENTES_BUSQUEDA = 12;
    public static String NOMBRE_BD = "cascaron_comida12";
    private LayoutInflater inflador;

    public class item
    {
        private  String valor;
        private String descripcion;

        public item(String val, String desc)
        {
             valor = val;
             descripcion= desc;
        }

        public String getValor()
        {
            return valor;
        }
        public String getDescripcion()
        {
            return descripcion;
        }


    }

    public Adaptador_spinner(Activity contexto, int resource, int textViewid)
    {

        super(contexto, resource, textViewid );
        inicializar_listado();
        addAll(listado);
        inflador = contexto.getLayoutInflater();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        item linea_item = getItem(position);
        View vista_linea = inflador.inflate(R.layout.spiner_item,null,true);
        TextView descrip = (TextView) vista_linea.findViewById(R.id.descrip);
        descrip.setText(linea_item.getDescripcion());

        TextView valor = (TextView) vista_linea.findViewById(R.id.valor);
        valor.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT) );
        valor.setText(linea_item.getValor());

        return valor;
    }

    @Override
    public item getItem(int position) {
        return listado.get(position);
    }

    public void inicializar_listado()
    {
        listado = new ArrayList<>();
        gestorBD mi_gestor = new gestorBD(getContext());
        SQLiteDatabase db =  mi_gestor.getReadableDatabase();
        Cursor cr = db.rawQuery(
                "SELECT OUTPUT_TEXT, INPUT FROM LVAL WHERE USO = 'UNIDADES'", null);
        if (cr.moveToFirst()) {
            do{
                String cadena_aux = cr.getString(cr.getColumnIndex("OUTPUT_TEXT"));
                String cadena_aux_desc = cr.getString(cr.getColumnIndex("INPUT"));
                item item_aux = new item(cadena_aux,cadena_aux_desc);
                listado.add(item_aux);
            } while (cr.moveToNext());
            cr.close();
        }
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        item linea_item = getItem(position);
        View vista_linea = inflador.inflate(R.layout.spiner_item,null,true);
            TextView descrip = (TextView) vista_linea.findViewById(R.id.descrip);
        descrip.setText(linea_item.getDescripcion());

        TextView valor = (TextView) vista_linea.findViewById(R.id.valor);
        valor.setText(linea_item.getValor());

        return vista_linea;

    }
}
