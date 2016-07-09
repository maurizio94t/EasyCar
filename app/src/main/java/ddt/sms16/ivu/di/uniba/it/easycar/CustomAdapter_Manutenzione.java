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

/**
 * Created by Maurizio on 22/06/16.
 */
public class CustomAdapter_Manutenzione extends ArrayAdapter<Manutenzione> {
    List<Manutenzione> lista;
    Context context;

    public CustomAdapter_Manutenzione(Context context, int textViewResourceId, List<Manutenzione> objects) {
        super(context, textViewResourceId, objects);
        lista = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_manutenzione, null);
        ImageView img = (ImageView) convertView.findViewById(R.id.img);
        TextView descrizione = (TextView) convertView.findViewById(R.id.descrizione);
        TextView auto = (TextView) convertView.findViewById(R.id.auto);
        TextView giorno = (TextView) convertView.findViewById(R.id.giorno);
        TextView mese = (TextView) convertView.findViewById(R.id.mese);

        Manutenzione s = getItem(position);
        int [] dataVector = Utility.getData(s.getData());
        descrizione.setText(s.getDescrizione());
        auto.setText(s.getAuto().getModello().getMarca().getNome() + " " + s.getAuto().getModello().getNome());
        giorno.setText(String.valueOf(dataVector[2]));
        mese.setText(Utility.controllaMese(dataVector[1]));

        descrizione.setText(s.getDescrizione());
        auto.setText(s.getAuto().getModello().getMarca().getNome() + " " + s.getAuto().getModello().getNome());

//        img.setImageResource(R.drawable.ic_menu_gallery);

        return convertView;
    }

    public List<Manutenzione> getLista() {
        return lista;
    }

}
