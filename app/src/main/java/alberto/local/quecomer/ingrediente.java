package alberto.local.quecomer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by - on 22/12/2017.
 */
public class ingrediente implements Parcelable {
    public String nombre_ingrediente;
    public int cantidad;
    public String unidad_medida;
    public int id_ingrediente_en_bd;


    public ingrediente() {
        nombre_ingrediente = "";
        cantidad = -1;
        unidad_medida = "";
        id_ingrediente_en_bd=0;
    }

    protected ingrediente(Parcel in) {
        nombre_ingrediente = in.readString();
        cantidad = in.readInt();
        unidad_medida = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre_ingrediente);
        dest.writeInt(cantidad);
        dest.writeString(unidad_medida);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ingrediente> CREATOR = new Creator<ingrediente>() {
        @Override
        public ingrediente createFromParcel(Parcel in) {
            return new ingrediente(in);
        }

        @Override
        public ingrediente[] newArray(int size) {
            return new ingrediente[size];
        }
    };
}
