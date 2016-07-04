package ddt.sms16.ivu.di.uniba.it.easycar.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ddt.sms16.ivu.di.uniba.it.easycar.PrendiFoto;
import ddt.sms16.ivu.di.uniba.it.easycar.R;


public class OneFragment extends Fragment {

    Context thisContext;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_one, container, false);

        Button b = (Button) view.findViewById(R.id.btnIntend);
b.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent0 = new Intent(getActivity(), PrendiFoto.class);
        startActivity(intent0);



    }
});




return view;
    }

   }