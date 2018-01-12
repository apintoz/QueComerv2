package local.quick_stuff;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by - on 16/12/2017.
 */

public class Dialogo_selector_string extends DialogFragment {

    public static  Dialogo_selector_string nuevo_dialogo(ArrayList<String> listado)
    {
        Dialogo_selector_string newborn = new Dialogo_selector_string();
        Bundle args = new Bundle();
        args.putStringArrayList("poblaciones", listado );
        newborn.setArguments(args);
        return newborn;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ArrayList<String> unidades = getArguments().getStringArrayList("poblaciones");
        CharSequence[] secuencia_caracteres = unidades.toArray(new CharSequence[unidades.size()]);
        AlertDialog.Builder constructor = new AlertDialog.Builder(getActivity());
        constructor.setTitle("Escoge una unidad");
        constructor.setItems(secuencia_caracteres, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return constructor.create();
    }
}

