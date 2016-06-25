package ddt.sms16.ivu.di.uniba.it.easycar.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ddt.sms16.ivu.di.uniba.it.easycar.MySQLiteHelper;
import ddt.sms16.ivu.di.uniba.it.easycar.R;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;

public class AggiuntaScadenzaFragment extends Fragment {
    View view;

    Calendar myCalendar = Calendar.getInstance();

    EditText editTextDate;

    String dataN;

    int anno,mese,giorno=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
          view =inflater.inflate(R.layout.fragment_aggiunta_scadenza, container, false);

        MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(getActivity().getApplicationContext());
        //prova aggiunta autoUtente
       // mySQLiteHelper.aggiungiAutoUtente(new AutoUtente("BN 456 NM",0,"",0,"",null,false));
        //  int km, String annoImmatricolazione, int fotoAuto, String utente_email, Modello modello, boolean selected





        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.tipoScadenzaRadioGroup);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb=(RadioButton) view.findViewById(checkedId);
                Toast.makeText(getActivity().getApplicationContext(), rb.getText(), Toast.LENGTH_LONG).show();
            }
        });

        editTextDate = (EditText) view.findViewById(R.id.dataScadenza);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                anno=year;
                mese = monthOfYear;
                giorno=dayOfMonth;
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        editTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog( getActivity(), date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
        editTextDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        Spinner dropdown = (Spinner) view.findViewById(R.id.spinnerTarghe);


        String[] items = new String[mySQLiteHelper.getAllTarghe().size()];
        int i=0;
        for (AutoUtente a : mySQLiteHelper.getAllTarghe()
             ) {
            items[i]=a.getTarga();
            i++;
        }

        String[] targhe = new String[]{"BN 678 MN","FF567HJ" ,"RR66YY"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, targhe);
        dropdown.setAdapter(adapter);
        return view;
    }
    private void updateLabel() {

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);

        Calendar today = Calendar.getInstance();

        int anni = today.get(Calendar.YEAR) - myCalendar.get(Calendar.YEAR);
        if(myCalendar.get(Calendar.MONTH) > today.get(Calendar.MONTH) ||
                (myCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && myCalendar.get(Calendar.DATE) > today.get(Calendar.DATE))) {

        }

        dataN = sdf.format(myCalendar.getTime());
        editTextDate.setText(dataN);
    }





}
