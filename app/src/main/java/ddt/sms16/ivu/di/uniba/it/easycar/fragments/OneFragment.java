package ddt.sms16.ivu.di.uniba.it.easycar.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ddt.sms16.ivu.di.uniba.it.easycar.R;

/**
 * Created by Maurizio on 01/06/16.
 */
public class OneFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_one, container, false);
    }
}
