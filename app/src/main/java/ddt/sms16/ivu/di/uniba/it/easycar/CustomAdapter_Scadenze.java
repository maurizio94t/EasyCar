package ddt.sms16.ivu.di.uniba.it.easycar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.Scadenza;

/**
 * Created by Maurizio on 22/06/16.
 */
public class CustomAdapter_Scadenze extends ArrayAdapter<Scadenza> {
    List<Scadenza> lista;
    Context context;

    public CustomAdapter_Scadenze(Context context, int textViewResourceId, List<Scadenza> objects) {
        super(context, textViewResourceId, objects);
        lista = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_scadenza, null);
        ImageView img = (ImageView) convertView.findViewById(R.id.img);
        TextView descrizione = (TextView) convertView.findViewById(R.id.descrizione);
        TextView auto = (TextView) convertView.findViewById(R.id.auto);
        TextView giorno = (TextView) convertView.findViewById(R.id.giorno);
        TextView mese = (TextView)convertView.findViewById(R.id.mese);
        TextView anno = (TextView)convertView.findViewById(R.id.anno);
        Scadenza s = getItem(position);
        int [] dataVector = Utility.getData(s.getDataScadenza());
        descrizione.setText(s.getDescrizione());
        auto.setText(s.getAuto().getModello().getMarca().getNome() + " " + s.getAuto().getModello().getNome());
        giorno.setText(String.valueOf(dataVector[2]));
        anno.setText(String.valueOf(dataVector[0]));
        mese.setText(Utility.controllaMese(dataVector[1]));

//        img.setImageResource(R.drawable.ic_menu_gallery);

        return convertView;
    }

    public List<Scadenza> getLista() {
        return lista;
    }


}
