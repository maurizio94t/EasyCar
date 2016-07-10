package ddt.sms16.ivu.di.uniba.it.easycar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;

/**
 * Created by Maurizio on 22/06/16.
 */
public class CustomAdapter_AutoUtente extends ArrayAdapter<AutoUtente> {
    List<AutoUtente> lista;
    Context context;

    public CustomAdapter_AutoUtente(Context context, int textViewResourceId, List<AutoUtente> objects) {
        super(context, textViewResourceId, objects);
        lista = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_auto, null);
        TextView marca = (TextView) convertView.findViewById(R.id.autoMarca);
        TextView modello = (TextView) convertView.findViewById(R.id.autoModello);
        TextView targa = (TextView) convertView.findViewById(R.id.autoTarga);
        ImageView img = (ImageView) convertView.findViewById(R.id.autoImg);
        ImageView imgSel = (ImageView) convertView.findViewById(R.id.autoSelected);

        AutoUtente a = getItem(position);
        marca.setText(a.getModello().getMarca().getNome());
        modello.setText(a.getModello().getNome());
        targa.setText(a.getTarga());
        img.setImageResource(R.drawable.ic_car);
        imgSel.setImageResource(R.drawable.star_icon);
        if (a.getSelected() == 1) {
            imgSel.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public List<AutoUtente> getLista() {
        return lista;
    }

}
