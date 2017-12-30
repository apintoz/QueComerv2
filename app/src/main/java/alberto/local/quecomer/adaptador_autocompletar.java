package alberto.local.quecomer;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.AdapterView;
import android.widget.TextView;

/**
 * Created by - on 18/12/2017.
 */

public class adaptador_autocompletar extends CursorAdapter implements AdapterView.OnItemClickListener {

    private adaptadorBDIngredientes madaptador_bd_ingredientes;

    public adaptador_autocompletar( adaptadorBDIngredientes adaptador, Context contexto  )
    {
        super(contexto, null, FLAG_REGISTER_CONTENT_OBSERVER);
        madaptador_bd_ingredientes = adaptador;
        adaptador.open();
    }

        @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (getFilterQueryProvider()!=null)
        {
            return getFilterQueryProvider().runQuery(constraint);
        }
        Cursor cursor = madaptador_bd_ingredientes.fetchItemsByDesc((constraint!=null ? constraint.toString():"@@@@"));
        return cursor;
    }

    @Override
    public String convertToString(Cursor cursor) {
        final int columnIndex = cursor.getColumnIndexOrThrow("NOMBRE");
        final String str = cursor.getString(columnIndex);
        return str;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final String text = convertToString(cursor);
        ((TextView) view).setText(text);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(android.R.layout.simple_list_item_1,parent, false);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
