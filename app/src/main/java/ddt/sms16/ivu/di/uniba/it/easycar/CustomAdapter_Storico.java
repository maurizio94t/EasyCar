package ddt.sms16.ivu.di.uniba.it.easycar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.Manutenzione;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Scadenza;

/**
 * Created by Maurizio on 22/06/16.
 */
public class CustomAdapter_Storico extends ArrayAdapter<Object> {
    List<Object> lista;
    Context context;

    public CustomAdapter_Storico(Context context, int textViewResourceId, List<Object> objects) {
        super(context, textViewResourceId, objects);
        lista = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_storico, null);
        TextView titolo = (TextView) convertView.findViewById(R.id.titolo);
        TextView info = (TextView) convertView.findViewById(R.id.info);
        TextView giorno = (TextView) convertView.findViewById(R.id.giorno);
        TextView mese = (TextView) convertView.findViewById(R.id.mese);
        ImageView img = (ImageView) convertView.findViewById(R.id.img);

        if(getItem(position) instanceof Manutenzione) {
            Manutenzione m = (Manutenzione) getItem(position);
            titolo.setText(m.getDescrizione());
            info.setText("Manutenzione");
            int [] dataVector = Utility.getData(m.getData());
            giorno.setText(String.valueOf(dataVector[2]));
            mese.setText(Utility.controllaMese(dataVector[1]));
            img.setImageResource(R.drawable.ic_settings);
        } else {
            Scadenza s = (Scadenza) getItem(position);
            titolo.setText(s.getDescrizione());
            info.setText("Scadenza");
            int [] dataVector = Utility.getData(s.getDataScadenza());
            giorno.setText(String.valueOf(dataVector[2]));
            mese.setText(Utility.controllaMese(dataVector[1]));
            img.setImageResource(R.drawable.ic_clock);
        }

        return convertView;
    }

    public List<Object> getLista() {
        return lista;
    }

}
