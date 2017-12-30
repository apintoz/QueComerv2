package alberto.local.quecomer;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by - on 18/12/2017.
 */

public class adaptadorBDIngredientes {

    public static final String KEY_ROWID ="_id";
    public static final String KEY_ITEM = "NOMBRE";

    private static final String TAG  = "adaptadorBDIngredientes";

    private SQLiteDatabase mDb;
    private gestorBD gestor_bd;

    private static final String TABLA_INGREDIENTES = "INGREDIENTES";

    private Context micontexto;



    public adaptadorBDIngredientes open() throws SQLException {
        gestor_bd = new gestorBD(micontexto);
        mDb = gestor_bd.getWritableDatabase();
        return this;
    }
    public adaptadorBDIngredientes(Context ctx) {
        this.micontexto = ctx;
    }


    public Cursor fetchItemsByDesc(String inputText) throws SQLException {

        Log.w("damn",inputText);
        Cursor cr = mDb.rawQuery(
                "SELECT ID_INGREDIENTE AS _id, NOMBRE FROM INGREDIENTES WHERE NOMBRE  like '%" + inputText + "%'",null );

        /*Cursor cr = mDb.query(true,"INGREDIENTES",new String[] {KEY_ROWID,KEY_ITEM },KEY_ITEM + " like '%" + inputText + "%'",null,
                null,null,null,null);*/
        if (cr != null) {
            cr.moveToFirst();
        }
        return cr;
    }

}
