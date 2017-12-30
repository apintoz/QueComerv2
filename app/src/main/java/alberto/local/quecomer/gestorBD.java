package alberto.local.quecomer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import local.quick_stuff.utilitario;

/**
 * Created by - on 17/12/2017.
 */

public class gestorBD extends SQLiteOpenHelper {

    private static String RUTA_BD;

    private SQLiteDatabase base_datos;
    private final Context mcontexto;

    private static String nombre_bd = "cascaron_comida";
    private static int DB_VERSION = 12;

    public gestorBD (Context contextito)
    {
        super(contextito, nombre_bd + DB_VERSION,null, DB_VERSION);
        if (Build.VERSION.SDK_INT>=4.2)
        {
            RUTA_BD = contextito.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            RUTA_BD ="/data/data/" + contextito.getPackageName() + "/databases";
        }
        this.mcontexto = contextito;
    }

    public void crear_base_de_datos() throws IOException
    {
        boolean existedb = verificarbd();
        if (!existedb)
        {
            this.getReadableDatabase();
            this.close();
            try
            {
                copiarbd();
                Log.e("Databasehelper", "base de datos creada");

            }
            catch (IOException excepcionIO)
            {
                throw new Error("Error cpiando base");
            }
        }
    }

    private boolean verificarbd()
    {
        File archivobd = new File(RUTA_BD + nombre_bd + DB_VERSION);
        return archivobd.exists();
    }

    private void copiarbd() throws IOException
    {
        InputStream input = mcontexto.getAssets().open(nombre_bd+".db");
        String nombre_bd_salida = RUTA_BD + nombre_bd + DB_VERSION;
        OutputStream mOutput = new FileOutputStream(nombre_bd_salida);
        byte[] mBuffer = new byte[1024];
        int longitud;
        while ((longitud = input.read(mBuffer))>0)
        {
            mOutput.write(mBuffer,0,longitud);
        }
        mOutput.flush();
        mOutput.close();
        input.close();
    }


    @Override
    public synchronized void close() {
        if (base_datos!=null)
            base_datos.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor obtener_campo_con_llave2 (String nombre_campo, String nombre_tabla, String valor_buscado, String campo_comparacion)
    {
        SQLiteDatabase bd = getReadableDatabase();
        Cursor micursor = bd.rawQuery("SELECT "  + nombre_campo + " FROM " + nombre_tabla + " WHERE " +
                campo_comparacion  + " LIKE " + "'" + valor_buscado + "'",null );
        return micursor;
    }
}
