package alberto.local.quecomer;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import local.quick_stuff.utilitario;

public class VisorReceta extends AppCompatActivity {
    public gestorBD migestor;

     public static String TEXTO_HTML ="TEXTO_HTML";
     public static String TEXTO_SIMPLE = "TEXTO_SIMPLE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_receta);
        migestor = new gestorBD(this);
        String receta = getIntent().getStringExtra("id_receta");
        String cadena_consulta = TEXTO_HTML + ", " + TEXTO_SIMPLE;
        TextView caja_detalles_receta = findViewById(R.id.texto_receta);
        TextView caja_titulo_receta_= findViewById(R.id.titulo_receta);
        Cursor cursor_pasos_receta = migestor.obtener_campo_con_llave2(cadena_consulta + ", NOMBRE ","RECETAS",receta,"ID_RECETA");
        cursor_pasos_receta.moveToFirst();
        String pasos_receta = cursor_pasos_receta.getString(cursor_pasos_receta.getColumnIndex(TEXTO_HTML));
        if (pasos_receta==null)
        {
            pasos_receta = cursor_pasos_receta.getString(cursor_pasos_receta.getColumnIndex(TEXTO_SIMPLE));
            caja_detalles_receta.setText(pasos_receta);
        }
        else
        {
            caja_detalles_receta.setText(Html.fromHtml(pasos_receta));
        }
        String titulo_receta = cursor_pasos_receta.getString(cursor_pasos_receta.getColumnIndex("NOMBRE"));
        caja_titulo_receta_.setText(titulo_receta);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.envio_mail:
                String mailto = "mailto:apintoz@uni.pe" +
                        "?cc=" + "elpadredelcordero@gmail.com" +
                        "&subject=" + "Sugerencia app " + this.toString() +
                        "&body=" + Uri.encode("");

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));
                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    //TODO: Handle case where no email app is available
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
