package local.quick_stuff;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by - on 5/11/2017.
 */

public class utilitario {


    @NonNull
    public static String $B (View view)
    {
        Button auxiliar =  (Button) view;
        return auxiliar.getText().toString();
    }
    public static Button $$B (View vista)
    {
        Button auxiliar = (Button) vista;
        return auxiliar;
    }

    @NonNull
    public static String $TV (View view)
    {
        TextView auxiliar = (TextView) view;
        return auxiliar.getText().toString();
    }

    @NonNull
    public static String $ET (View view)
    {
        EditText auxiliar = (EditText) view;
        return auxiliar.getText().toString();
    }

    public static void pequeño_toast(Context contexto, String cadena)
    {
        Toast.makeText(contexto,cadena,Toast.LENGTH_LONG).show();
    }

    public static String generar_codigo_fechas()
    {
        Date hoy = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("yyMMdd-hhmm");
        String codigo = dt.format(hoy);
        return codigo;
    }

    public static String formato_2_decimales(Double nro)
    {
        return String.format("%,8.2f%n",nro);
    }
    public static ListView $LV(View vista )
    {
        return (ListView) vista;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
