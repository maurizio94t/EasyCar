package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Manutenzione;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Marca;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Modello;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Problema;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Scadenza;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Utente;

public class AggiuntaScadenza extends AppCompatActivity {
    Calendar myCalendar = Calendar.getInstance();

    EditText editTextDate;

    String dataN;
    RadioButton tipoScadenzaRadioGroupSelected;
    Spinner spinnerTarghe;

    int anno, mese, giorno = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiunta_scadenza);
        final MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(AggiuntaScadenza.this);
        Utente utenteE = new Utente("Enrico", "d'Elia", "16-04-1994", 0, "e.marzo@gmail.com");
        /*Utente utenteG = new Utente("Giovanni", "d'Elia", "16-04-1994", 0, "g.marzo@gmail.com");
        Utente utenteM = new Utente("Mario", "d'Elia", "16-04-1994", 0, "m.marzo@gmail.com");
        Utente utenteZ = new Utente("Zeon", "d'Elia", "16-04-1994", 0, "z.marzo@gmail.com");
*/
        Marca marcaFiat = new Marca("Fiat");
        Marca marcaAudi = new Marca("Audi");

        Modello modelloFiatPunto = new Modello("Punto", "B", "benzina", "1200", "66", marcaFiat);
        Modello modelloAudiA4 = new Modello("A4", "C", "diesel", "1900", "90", marcaAudi);

        AutoUtente autoUtenteE0 = new AutoUtente("BN897MN", 88809, "2013", 0, utenteE, modelloFiatPunto, 0);
        AutoUtente autoUtenteE1 = new AutoUtente("AN889MN", 88809, "2011", 0, utenteE, modelloAudiA4, 0);
  /*      AutoUtente autoUtenteG = new AutoUtente("MM788EE",454643,"2010",0,utenteG,modelloFiatPunto,0);
        AutoUtente autoUtenteM0 = new AutoUtente("IE456BB",343353,"2009",0,utenteM,modelloFiatPunto,0);
        AutoUtente autoUtenteM1 = new AutoUtente("EN889MN",88809,"2001",0,utenteM,modelloAudiA4,0);
*/

        Problema problemaAutoE1 = new Problema("Braccio sinistro", autoUtenteE1);
        //      Problema problemaAutoG = new Problema("Braccio destro", autoUtenteG);


        Manutenzione manutenzione0 = new Manutenzione("cambio olio", "16-09-2010", 0, "7890000", autoUtenteE0);
        Manutenzione manutenzione1 = new Manutenzione("cambio filtri", "16-09-2010", 0, "7890000", autoUtenteE0);
//        Manutenzione manutenzione2 = new Manutenzione("cambio olio","16-09-2010",0,"4525242",autoUtenteG);


//        Scadenza scadenza0 = new Scadenza("bollo","12-08-2015",autoUtenteG);


        mySQLiteHelper.aggiungiUtente(utenteE);
/*        mySQLiteHelper.aggiungiUtente(utenteG);
        mySQLiteHelper.aggiungiUtente(utenteM);
        mySQLiteHelper.aggiungiUtente(utenteZ);
*/

        mySQLiteHelper.aggiungiMarca(marcaFiat);
        mySQLiteHelper.aggiungiMarca(marcaAudi);


        mySQLiteHelper.aggiungiModello(modelloFiatPunto);
        mySQLiteHelper.aggiungiModello(modelloAudiA4);


        mySQLiteHelper.aggiungiAutoUtente(autoUtenteE0);
        mySQLiteHelper.aggiungiAutoUtente(autoUtenteE1);
  /*      mySQLiteHelper.aggiungiAutoUtente(autoUtenteG);
        mySQLiteHelper.aggiungiAutoUtente(autoUtenteM0);
        mySQLiteHelper.aggiungiAutoUtente(autoUtenteM1);
*/

        mySQLiteHelper.aggiungiProblemi(problemaAutoE1);
        //      mySQLiteHelper.aggiungiProblemi(problemaAutoG);

        mySQLiteHelper.aggiungiManutenzione(manutenzione0);
//        mySQLiteHelper.aggiungiManutenzione(manutenzione1);
        //    mySQLiteHelper.aggiungiManutenzione(manutenzione2);

//        mySQLiteHelper.aggiungiScadenza(scadenza0);




     /*  mySQLiteHelper.getAllUtenti();
      mySQLiteHelper.getAllMarche();
        mySQLiteHelper.getAllModelli();
     mySQLiteHelper.getAllAutoUtente();
        */

        // /prova aggiunta autoUtente
        // mySQLiteHelper.aggiungiAutoUtente(new AutoUtente("BN 456 NM",0,"",0,"",null,false));
        //  int km, String annoImmatricolazione, int fotoAuto, String utente_email, Modello modello, boolean selected


        final RadioGroup tipoScadenzaRadioGroup = (RadioGroup) findViewById(R.id.tipoScadenzaRadioGroup);
        Button bottoneAggiungi = (Button) findViewById(R.id.buttonAggiungiScadenza);
        spinnerTarghe = (Spinner) findViewById(R.id.spinnerTarghe);

        List<AutoUtente> auto = mySQLiteHelper.getAllAutoUtente();


        String[] targhe = new String[auto.size()];
        int i = 0;
        for (AutoUtente a : auto
                ) {
            targhe[i] = a.getTarga();
            i++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, targhe);
        spinnerTarghe.setAdapter(adapter);


        tipoScadenzaRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                tipoScadenzaRadioGroupSelected = (RadioButton) findViewById(checkedId);
                Log.d("dati", tipoScadenzaRadioGroupSelected.getText().toString());

            }
        });



        editTextDate = (EditText) findViewById(R.id.dataScadenza);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                anno = year;
                mese = monthOfYear;
                giorno = dayOfMonth;
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
                    new DatePickerDialog(getApplicationContext(), date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
        editTextDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getApplicationContext(), date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });



        bottoneAggiungi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Scadenza scadenza = new Scadenza(tipoScadenzaRadioGroupSelected.getText().toString(), editTextDate.toString(), new AutoUtente(spinnerTarghe.getSelectedItem().toString()));
                mySQLiteHelper.aggiungiScadenza(scadenza);
                Toast.makeText(getApplicationContext(), "hai aggiunto una nuova scadenza!", Toast.LENGTH_LONG);
                mySQLiteHelper.getAllScadenze();


            }
        });


    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);

        Calendar today = Calendar.getInstance();

        int anni = today.get(Calendar.YEAR) - myCalendar.get(Calendar.YEAR);
        if (myCalendar.get(Calendar.MONTH) > today.get(Calendar.MONTH) ||
                (myCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && myCalendar.get(Calendar.DATE) > today.get(Calendar.DATE))) {

        }

        dataN = sdf.format(myCalendar.getTime());
        editTextDate.setText(dataN);

        Log.d("dati", editTextDate.getText().toString());







    }





}
