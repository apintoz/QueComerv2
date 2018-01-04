package alberto.local.quecomer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import local.quick_stuff.utilitario;

/**
 * Created by - on 18/12/2017.
 */

public class motor_busqueda {

    private gestorBD gestor_base;
    private ArrayList<ingrediente> listado_ingredientes;
    private Context contexto;
    private TreeMap<Integer , Double> tabla_puntajes;

    public TreeMap<Integer, Double> procesar_ingredientes() {
        for ( ingrediente tomoe  : listado_ingredientes)
        {
            procesar_ingrediente(tomoe);
        }
        return tabla_puntajes;
    }

    public ArrayList<ingrediente> obtener_listado_ingredientes()
    {
        return listado_ingredientes;
    }
    public void definir_listado_ingredientes(ArrayList<ingrediente> parametro)
    {
      listado_ingredientes = parametro;
    }

    public void resetear_tabla_puntajes_e_ingredientes()
    {
        tabla_puntajes = new TreeMap<>();
        listado_ingredientes = new ArrayList<>();
    }

    public motor_busqueda(gestorBD mgestor, Context p_contexto)
    {
        gestor_base = mgestor;
        contexto = p_contexto;
        listado_ingredientes = new ArrayList<>();
        tabla_puntajes = new TreeMap<>();
    }

    public motor_busqueda(gestorBD gestor_base, ArrayList<ingrediente> listado_ingredientes, Context contexto, TreeMap<Integer, Double> tabla_puntajes) {
        this.gestor_base = gestor_base;
        this.listado_ingredientes = listado_ingredientes;
        this.contexto = contexto;
        this.tabla_puntajes = tabla_puntajes;
    }

    public void agregar_ingrediente(String p_nombre, int p_cantidad, String p_unidad_medida )
    {
        ingrediente mingrediente = new ingrediente();
        mingrediente.unidad_medida=p_unidad_medida;
        mingrediente.cantidad=p_cantidad;
        Cursor mi_cursor = obtener_campo_con_llave("ID_INGREDIENTE"," INGREDIENTES ", p_nombre," NOMBRE ");
        if (mi_cursor.moveToFirst())
        {
            mingrediente.id_ingrediente_en_bd = mi_cursor.getInt(mi_cursor.getColumnIndex("ID_INGREDIENTE"));
            mingrediente.nombre_ingrediente= p_nombre;
        }
        listado_ingredientes.add(mingrediente);
    }


    public TreeMap<Integer, Double> obtenerTabla_puntajes() {
        return tabla_puntajes;
    }

    public void procesar_ingrediente(ingrediente p_ingrediente)
    {
        SQLiteDatabase bd = gestor_base.getReadableDatabase();
        Cursor mi_cursor = bd.rawQuery(
                "SELECT ID_RECETA, INGREDIENTES.ID_INGREDIENTE, CANTIDAD FROM RECETA_INGREDIENTES INNER JOIN " +
                        "INGREDIENTES ON RECETA_INGREDIENTES.ID_INGREDIENTE = INGREDIENTES.ID_INGREDIENTE "+
                        "WHERE NOMBRE= '" + p_ingrediente.nombre_ingrediente + "'",null);
        if (mi_cursor.moveToFirst()) {
            do {
                int temp_id_int = mi_cursor.getInt(mi_cursor.getColumnIndex("ID_RECETA"));
                Double temp_double = mi_cursor.getDouble(mi_cursor.getColumnIndex("CANTIDAD"));
                temp_double = p_ingrediente.cantidad/temp_double;
                if (tabla_puntajes.containsKey(temp_id_int)) {
                    Double item_en_llave = tabla_puntajes.get(temp_id_int);
                    tabla_puntajes.remove(temp_id_int);
                    tabla_puntajes.put(temp_id_int, temp_double + item_en_llave);
                } else {
                    tabla_puntajes.put(temp_id_int, temp_double);
                }
            }
            while (mi_cursor.moveToNext());

            mi_cursor.close();
        }
    }
    public String mostrar_tabla_String()
    {
        Iterator<Integer> iterador = tabla_puntajes.keySet().iterator();
        String aux = "";
        while (iterador.hasNext())
        {
            int llave = iterador.next();
            Double valor = tabla_puntajes.get(llave);
            aux = aux + "Receta: " + nombre_receta(llave) + "\t" + "puntaje: " + utilitario.formato_2_decimales(valor)+ "\n";
        }
        return aux;

    }
    public String nombre_receta(int id_receta)
    {
        String receta = Integer.toString(id_receta);
        Log.w("damn",Integer.toString(id_receta));
        Cursor cr = gestor_base.getReadableDatabase().rawQuery(
                "SELECT NOMBRE FROM RECETAS WHERE ID_RECETA = " + receta,null   );
        if (cr.moveToFirst())
        {
            String aux = cr.getString(cr.getColumnIndex("NOMBRE"));
            cr.close();
            return aux;
        }
        else
        {
            cr.close();
            return "Nombre de Receta no encontrada";
        }
    }

    public void poblar_temp(ArrayList<String> busqueda, String  columna)
    {
        SQLiteDatabase bd = gestor_base.getReadableDatabase();
        bd.rawQuery("DELETE FROM TEMPORAL",null );
        String valores = "";
        for (String item:busqueda)
        {
            valores=valores +  "('" + item + "')";
        }
        valores = valores.substring(0,valores.length()-1);
        bd.rawQuery("INSERT INTO TEMPORAL (BUSQUEDA) VALUES " + valores,null);
        bd.close();
    }
    public Cursor obtener_campo_con_llave (String nombre_campo, String nombre_tabla,  String valor_buscado, String campo_comparacion)
    {
        SQLiteDatabase bd = gestor_base.getReadableDatabase();
        Cursor micursor = bd.rawQuery("SELECT "  + nombre_campo + " FROM " + nombre_tabla + " WHERE " +
                campo_comparacion  + " LIKE " + "'" + valor_buscado + "'",null );
        return micursor;
    }


    public  <K,V extends Comparable<? super V>>
    ArrayList<Integer> IdByValues(Map<K,V> map)
    {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = -(e1.getValue().compareTo(e2.getValue()));
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        ArrayList<Integer> listado_id = new ArrayList<Integer>();
        Iterator<Map.Entry<K,V>> iterador =sortedEntries.iterator();
        while (iterador.hasNext())
        {
            int llave_aux = (Integer)iterador.next().getKey();
            listado_id.add(llave_aux);
        }
        return listado_id;
    }

}
