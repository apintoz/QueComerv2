package local.quick_stuff;

/**
 * Created by - on 7/11/2017.
 */

public class DataModel {

    String evento_corto;
    String fecha_corta;
    String evento_largo;

    public DataModel(String corto, String fecha, String largo)
    {
        this.evento_corto = corto;
        this.fecha_corta = fecha;
        this.evento_largo = largo;

    }

    public String getEvento_corto() {
        return evento_corto;
    }

    public String getFecha_corta() {
        return fecha_corta;
    }

    public String getEvento_largo() {
        return evento_largo;
    }
}
