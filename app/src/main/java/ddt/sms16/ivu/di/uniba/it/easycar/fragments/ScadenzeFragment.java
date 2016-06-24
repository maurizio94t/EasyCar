package ddt.sms16.ivu.di.uniba.it.easycar.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ddt.sms16.ivu.di.uniba.it.easycar.ExpandableListAdapter;
import ddt.sms16.ivu.di.uniba.it.easycar.R;

/**
 * Created by Maurizio on 01/06/16.
 */
public class ScadenzeFragment extends Fragment {
    Context thisContext;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scadenze, container, false);


        return view;
    }

}
