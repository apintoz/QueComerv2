package alberto.local.quecomer;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import local.quick_stuff.utilitario;

public class VisorReceta extends AppCompatActivity {
    public gestorBD migestor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_receta);
        migestor = new gestorBD(this);
        String receta = getIntent().getStringExtra("id_receta");
        Cursor cursor_pasos_receta = migestor.obtener_campo_con_llave2("TEXTO_HTML, NOMBRE","RECETAS",receta,"ID_RECETA");
        cursor_pasos_receta.moveToFirst();
        String pasos_receta = cursor_pasos_receta.getString(cursor_pasos_receta.getColumnIndex("TEXTO_HTML"));
        String titulo_receta = cursor_pasos_receta.getString(cursor_pasos_receta.getColumnIndex("NOMBRE"));
        TextView caja_detalles_receta = (TextView) findViewById(R.id.texto_receta);
        TextView caja_titulo_receta_= (TextView) findViewById(R.id.titulo_receta);
        caja_detalles_receta.setText(Html.fromHtml(pasos_receta));
        caja_titulo_receta_.setText(titulo_receta);
    }
}
