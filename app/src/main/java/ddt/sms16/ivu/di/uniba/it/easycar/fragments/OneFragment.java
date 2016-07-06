package ddt.sms16.ivu.di.uniba.it.easycar.fragments;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ddt.sms16.ivu.di.uniba.it.easycar.MySQLiteHelper;
import ddt.sms16.ivu.di.uniba.it.easycar.R;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Manutenzione;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Marca;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Modello;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Problema;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Scadenza;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Utente;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class OneFragment extends Fragment {

    Context thisContext;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_one, container, false);

        Button b = (Button) view.findViewById(R.id.btnIntend);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(getActivity().getApplicationContext());

                Utente utenteE = new Utente("Enrico", "d'Elia", "16-04-1994", "e.marzo@gmail.com");
                Utente utenteG = new Utente("Giovanni", "d'Elia", "16-04-1994", "g.marzo@gmail.com");
                Utente utenteM = new Utente("Mario", "d'Elia", "16-04-1994", "m.marzo@gmail.com");
                Utente utenteZ = new Utente("Zeon", "d'Elia", "16-04-1994", "z.marzo@gmail.com");

                Marca marcaFiat = new Marca(678,"Fiat");
                Marca marcaAudi = new Marca(784,"Audi");

                Modello modelloFiatPunto = new Modello(2334,"Punto", "B", "benzina", "1200", "66", marcaFiat);
                Modello modelloAudiA4 = new Modello(444,"A4", "C", "diesel", "1900", "90", marcaAudi);

                AutoUtente autoUtenteE0 = new AutoUtente("BN897MN", 88809, "2013",utenteE, modelloFiatPunto, 0);
                AutoUtente autoUtenteE1 = new AutoUtente("AN889MN", 88809, "2011",  utenteE, modelloAudiA4, 0);
                AutoUtente autoUtenteG = new AutoUtente("MM788EE",454643,"2010" ,utenteG,modelloFiatPunto,0);
                AutoUtente autoUtenteM0 = new AutoUtente("IE456BB",343353,"2009" ,utenteM,modelloFiatPunto,0);
                AutoUtente autoUtenteM1 = new AutoUtente("EN889MN",88809,"2001",utenteM,modelloAudiA4,0);

                Manutenzione manutenzione0 = new Manutenzione(156556,"cambio olio", "16-09-2010", 0, "7890000", autoUtenteE0);
                Manutenzione manutenzione1 = new Manutenzione(287654,"cambio filtri", "16-09-2010", 0, "7890000", autoUtenteE0);
                Manutenzione manutenzione2 = new Manutenzione(14432,"cambio olio","16-09-2010",0,"4525242",autoUtenteG);

                Problema problemaAutoE1 = new Problema(12,"Braccio sinistro", autoUtenteE1);
                Problema problemaAutoG = new Problema(44,"Braccio destro", autoUtenteG);



                mySQLiteHelper.aggiungiUtente(utenteE);
                mySQLiteHelper.aggiungiUtente(utenteG);
                mySQLiteHelper.aggiungiUtente(utenteM);
                mySQLiteHelper.aggiungiUtente(utenteZ);


                mySQLiteHelper.aggiungiMarca(marcaFiat);
                mySQLiteHelper.aggiungiMarca(marcaAudi);


                mySQLiteHelper.aggiungiModello(modelloFiatPunto);
                mySQLiteHelper.aggiungiModello(modelloAudiA4);


                mySQLiteHelper.aggiungiAutoUtente(autoUtenteE0);
                mySQLiteHelper.aggiungiAutoUtente(autoUtenteE1);
                mySQLiteHelper.aggiungiAutoUtente(autoUtenteG);
                mySQLiteHelper.aggiungiAutoUtente(autoUtenteM0);
                mySQLiteHelper.aggiungiAutoUtente(autoUtenteM1);

                mySQLiteHelper.aggiungiManutenzione(manutenzione0);
                mySQLiteHelper.aggiungiManutenzione(manutenzione1);
                mySQLiteHelper.aggiungiManutenzione(manutenzione2);


                Scadenza scadenza0 = new Scadenza(23,"bollo","12-08-2015",autoUtenteG);

                mySQLiteHelper.aggiungiProblemi(problemaAutoE1);
                mySQLiteHelper.aggiungiProblemi(problemaAutoG);


                mySQLiteHelper.aggiungiScadenza(scadenza0);

                mySQLiteHelper.getAllUtenti();
                mySQLiteHelper.getAllMarche();
                mySQLiteHelper.getAllModelli();
                mySQLiteHelper.getAllAutoUtente();
                mySQLiteHelper.getAllManutenzioni();
                mySQLiteHelper.getAllScadenze();
                mySQLiteHelper.getAllProblemi();

/*
                Utente utenteE0 = new Utente("ocirne", "d'Elia", "16-04-1994", "e.marzo@gmail.com");
             mySQLiteHelper.updateUtente(utenteE0);
                mySQLiteHelper.getAllUtenti();




                AutoUtente autoUtenteG0 = new AutoUtente("MM788EE",454643,"2001" ,utenteG,modelloFiatPunto,0);
               mySQLiteHelper.updateAutoUtente(autoUtenteG0);
                mySQLiteHelper.getAllAutoUtente();

                Manutenzione manutenzione00 = new Manutenzione(156556,"motorino avviamento", "16-09-2010", 0, "7890000", autoUtenteE0);

                Problema problemaAutoE11 = new Problema(12,"Braccio", autoUtenteE1);
                Scadenza scadenza00 = new Scadenza(23,"bolletto","12-08-2015",autoUtenteG);

                //mySQLiteHelper.updateScadenza(scadenza00);
                //mySQLiteHelper.getAllScadenze();
               mySQLiteHelper.updateProblema(problemaAutoE11);
                //    mySQLiteHelper.getAllProblemi();

               mySQLiteHelper.updateMantenzione(manutenzione00);
                mySQLiteHelper.getAllManutenzioni();
                */
               /*

               Utente utenteE = new Utente("Enrico", "d'Elia", "16-04-1994", "e.marzo@gmail.com");
       Utente utenteG = new Utente("Giovanni", "d'Elia", "16-04-1994", "g.marzo@gmail.com");
        Utente utenteM = new Utente("Mario", "d'Elia", "16-04-1994", "m.marzo@gmail.com");
        Utente utenteZ = new Utente("Zeon", "d'Elia", "16-04-1994", "z.marzo@gmail.com");

        Marca marcaFiat = new Marca(22,"Fiat");
        Marca marcaAudi = new Marca(78,"Audi");

        Modello modelloFiatPunto = new Modello("Punto", "B", "benzina", "1200", "66", marcaFiat);
        Modello modelloAudiA4 = new Modello("A4", "C", "diesel", "1900", "90", marcaAudi);

        AutoUtente autoUtenteE0 = new AutoUtente("BN897MN", 88809, "2013",utenteE, modelloFiatPunto, 0);
        AutoUtente autoUtenteE1 = new AutoUtente("AN889MN", 88809, "2011",  utenteE, modelloAudiA4, 0);
       AutoUtente autoUtenteG = new AutoUtente("MM788EE",454643,"2010" ,utenteG,modelloFiatPunto,0);
        AutoUtente autoUtenteM0 = new AutoUtente("IE456BB",343353,"2009" ,utenteM,modelloFiatPunto,0);
        AutoUtente autoUtenteM1 = new AutoUtente("EN889MN",88809,"2001",utenteM,modelloAudiA4,0);


        Problema problemaAutoE1 = new Problema("Braccio sinistro", autoUtenteE1);
             Problema problemaAutoG = new Problema("Braccio destro", autoUtenteG);


        Manutenzione manutenzione0 = new Manutenzione("cambio olio", "16-09-2010", 0, "7890000", autoUtenteE0);
        Manutenzione manutenzione1 = new Manutenzione("cambio filtri", "16-09-2010", 0, "7890000", autoUtenteE0);
      Manutenzione manutenzione2 = new Manutenzione("cambio olio","16-09-2010",0,"4525242",autoUtenteG);


        Scadenza scadenza0 = new Scadenza("bollo","12-08-2015",autoUtenteG);


        mySQLiteHelper.aggiungiUtente(utenteE);
        mySQLiteHelper.aggiungiUtente(utenteG);
        mySQLiteHelper.aggiungiUtente(utenteM);
        mySQLiteHelper.aggiungiUtente(utenteZ);


        mySQLiteHelper.aggiungiMarca(marcaFiat);
        mySQLiteHelper.aggiungiMarca(marcaAudi);


        mySQLiteHelper.aggiungiModello(modelloFiatPunto);
        mySQLiteHelper.aggiungiModello(modelloAudiA4);


        mySQLiteHelper.aggiungiAutoUtente(autoUtenteE0);
        mySQLiteHelper.aggiungiAutoUtente(autoUtenteE1);
      mySQLiteHelper.aggiungiAutoUtente(autoUtenteG);
        mySQLiteHelper.aggiungiAutoUtente(autoUtenteM0);
        mySQLiteHelper.aggiungiAutoUtente(autoUtenteM1);


        mySQLiteHelper.aggiungiProblemi(problemaAutoE1);
               mySQLiteHelper.aggiungiProblemi(problemaAutoG);

        mySQLiteHelper.aggiungiManutenzione(manutenzione0);
        mySQLiteHelper.aggiungiManutenzione(manutenzione1);
           mySQLiteHelper.aggiungiManutenzione(manutenzione2);

        mySQLiteHelper.aggiungiScadenza(scadenza0);




        mySQLiteHelper.getAllUtenti();
      mySQLiteHelper.getAllMarche();
        mySQLiteHelper.getAllModelli();
     mySQLiteHelper.getAllAutoUtente();



                */
                // mySQLiteHelper.getAllAutoUtente();
                // Intent intent0 = new Intent(getActivity(), MyCameraActivity.class);
                //startActivity(intent0);

                /*

                -----> !!! Enrico ti ho commentato questa roba xk ho tolto il commento della password nel costruttore !!! <-----

                mySQLiteHelper.aggiungiUtente(new Utente("Enrico", "d'Elia", "1996-04-04", "enrico96@gmail.com"));
                Utente utente = mySQLiteHelper.prendiUtente("enrico96@gmail.com");
                if (utente != null)
                    Log.d("utente preso: ", utente.toString());
                else
                    Log.d("utente ", "null");
                */


            }
        });


        return view;
    }

}