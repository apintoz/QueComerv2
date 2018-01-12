package local.quick_stuff;

import android.text.Editable;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by - on 21/11/2017.
 */

public class Validadores {

    public static boolean validar_edit_blancos( ViewGroup vista_layout)
    {
        int flag = 1;
        ArrayList<EditText> listado_editores = new ArrayList<>();
        for (int i  = 0;i < vista_layout.getChildCount();i++)
        {
            if (vista_layout.getChildAt(i) instanceof ViewGroup)
            {
                ViewGroup vista_hija = (ViewGroup)vista_layout.getChildAt(i);
                if (validar_edit_blancos(vista_hija))
                    {
                            flag= flag*1;
                    }
                    else
                {
                    flag=flag*0;
                }
            }
            if (vista_layout.getChildAt(i) instanceof  EditText)
            {
                listado_editores.add((EditText)vista_layout.getChildAt(i));
                EditText editor_aux = (EditText)vista_layout.getChildAt(i);
                if (editor_aux.getText().toString().equalsIgnoreCase(""))
                {
                    flag=flag*0;
                }
                else
                    flag=flag*1;
            }
        }
        return flag != 0;
    }
}
