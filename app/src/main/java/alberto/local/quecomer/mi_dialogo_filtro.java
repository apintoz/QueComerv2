package alberto.local.quecomer;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by - on 11/01/2018.
 */
public class mi_dialogo_filtro extends DialogFragment {

    public  String cadena="";
    public escuchador_dialogo m_escuchador;

    public interface escuchador_dialogo
    {
        void en_positivo (DialogFragment dialogo);
        void en_negativo (DialogFragment dialogo);
    }

    @Override
    public void onAttach(Activity actividad ) {
        super.onAttach(actividad);
        try
        {
            m_escuchador = (escuchador_dialogo)actividad;
        }
        catch (ClassCastException e )
        {
            throw new ClassCastException(actividad.toString() + "debe implementar el escuchador");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final HashSet<String> damn = (HashSet<String>) getArguments().getSerializable("listado_categorias_bundle");
        cadena="";
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        LinearLayout contenedor_dialogo = (LinearLayout) inflater.inflate(R.layout.dialogo_filtro, null);
        final LinearLayout contenedor_categorias_filtro = (LinearLayout) inflater.inflate(R.layout.filter_part, null);

        LinearLayout.LayoutParams parametros_layout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        parametros_layout.setMargins(20, 20, 10, 20);
        contenedor_categorias_filtro.setOrientation(LinearLayout.VERTICAL);
        contenedor_categorias_filtro.setLayoutParams(parametros_layout);

        Iterator<String> mi_iterador = damn.iterator();
        while (mi_iterador.hasNext()) {
            CheckBox mibox = new CheckBox(getActivity());
            mibox.setText(mi_iterador.next());
            mibox.setChecked(true);
            mibox.setLayoutParams(parametros_layout);
            contenedor_categorias_filtro.addView(mibox);

        }

        contenedor_dialogo.addView(contenedor_categorias_filtro);
        builder.setView(contenedor_dialogo)
                // Add action buttons
                .setPositiveButton("Filtrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Iterator<String> iterador = damn.iterator();
                        String categorias_uncheckeadas = "";
                        int i = 1;
                        while (i < contenedor_categorias_filtro.getChildCount()) {
                            CheckBox mibox = (CheckBox) contenedor_categorias_filtro.getChildAt(i);
                            if (!mibox.isChecked()) {
                                categorias_uncheckeadas = categorias_uncheckeadas + mibox.getText() + ",";
                            }
                            i++;
                        }
                        cadena = categorias_uncheckeadas;
                        m_escuchador.en_positivo(mi_dialogo_filtro.this);
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
