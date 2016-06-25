package ddt.sms16.ivu.di.uniba.it.easycar.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ddt.sms16.ivu.di.uniba.it.easycar.R;

/**
 * Created by Giuseppe-PC on 24/06/2016.
 */
public class InfoAutoFragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_detail_auto, container, false);
        }
}
