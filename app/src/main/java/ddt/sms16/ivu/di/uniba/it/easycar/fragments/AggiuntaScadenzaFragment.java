package ddt.sms16.ivu.di.uniba.it.easycar.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import ddt.sms16.ivu.di.uniba.it.easycar.R;

public class AggiuntaScadenzaFragment extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
          view =inflater.inflate(R.layout.fragment_aggiunta_scadenza, container, false);

RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.tipoScadenzaRadioGroup);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb=(RadioButton) view.findViewById(checkedId);
                Toast.makeText(getActivity().getApplicationContext(), rb.getText(), Toast.LENGTH_LONG).show();
            }
        });




        return view;
    }




}
