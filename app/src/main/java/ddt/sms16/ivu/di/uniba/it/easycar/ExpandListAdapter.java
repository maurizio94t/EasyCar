package ddt.sms16.ivu.di.uniba.it.easycar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.Problema;

/**
 * Created by Maurizio on 09/07/16.
 */
public class ExpandListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Group> groups;

    public ExpandListAdapter(Context context, ArrayList<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<Problema> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        Problema childProblema = (Problema) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_item, null);
        }
        TextView descrizione = (TextView) convertView.findViewById(R.id.descrizione);
        ImageView img = (ImageView) convertView.findViewById(R.id.img);
        LinearLayout row = (LinearLayout) convertView.findViewById(R.id.row);

        descrizione.setText(childProblema.getDescrizione());
        img.setImageResource(R.drawable.ic_map);
        if(isYourProblema(childProblema)) {
            row.setBackgroundColor(Color.parseColor("#373fac"));
            //row.setAlpha((float) 0.4);
        }

        return convertView;
    }

    private boolean isYourProblema(Problema p) {
        if(MainActivity.sharedpreferences.getString(MainActivity.TAG_UTENTE_EMAIL, "").equalsIgnoreCase(p.getAuto().getUtente().getEmail())) {
            return true;
        }
        return  false;
        /*
        List<AutoUtente> listaAuto = MainActivity.mySQLiteHelper.getAllMieAutoUtente();
        for(AutoUtente a : listaAuto) {
            if(a.getTarga().equalsIgnoreCase(p.getAuto().getTarga())) {
                return true;
            }
        }
        return false;
        */
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<Problema> chList = groups.get(groupPosition).getItems();
        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Group group = (Group) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.group_item, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.group_name);
        tv.setText(group.getName());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}