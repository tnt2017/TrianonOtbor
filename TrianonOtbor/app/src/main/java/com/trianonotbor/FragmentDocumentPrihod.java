package com.trianonotbor;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentDocumentPrihod.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentDocumentPrihod#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDocumentPrihod extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String IdDoc = "210025265";
    EditText docum_prihod_IDDOC, docum_prihod_NNAKL, docum_prihod_barcodes;
    ArrayList<ItemDocumentPrihod> items = new ArrayList<ItemDocumentPrihod>();
    View rootView;

   // items = new ArrayList<ItemDocumentPrihod>();

    MyItemRecyclerViewAdapterDocumentPrihod adapter;
    RecyclerView mRecyclerView;
    Button docum_prihod_button_getdocbyid, docum_prihod_button_info;
    ImageButton docum_prihod_save, docum_prihod_save2;
    SharedPreferences settings;
    private static final String PREF_IZONE = "izone";
    private static final String PREF_IDEMP = "idemp";
    private static final String PREF_PRINTER = "printer";

    String api_url = "https://svc.trianon-nsk.ru/clients/code_reader/index.php";
    String izone, idemp, printer = "http://10.8.0.25/print/?idsheet=";
    ArrayList local_arrlist=new ArrayList();
    String last_barcode="test";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentDocumentPrihod() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTovsheet.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDocumentPrihod newInstance(String param1, String param2) {
        FragmentDocumentPrihod fragment = new FragmentDocumentPrihod();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void fillData(View rootView, String iddoc) throws JSONException {
        String login = "ROSTOA$NSK20";
        String pass = "345544";
        String url = api_url + "?p=get_rnk&login=" + login + "&pass=" + pass + "&iddoc=" + iddoc + "&json=1";
        int scrollto=0;

        items.clear();
        StringBuffer http_result = MyHttp.Get(url, getContext());
        String response = http_result.toString();

        JSONObject userJson = new JSONObject(response);
        JSONArray data = (JSONArray) userJson.getJSONArray("data");

        for (int i = 0; i < data.length(); i++) {
            JSONObject data0 = data.getJSONObject(i);
            String CMC = data0.getString("CMC");
            String NAME = data0.getString("NAME");

            String NOMENKL = "";
            try
            {
                NOMENKL=data0.getString("NOMENKL");
            }
            catch (Exception ex)
            {

            }
            String KOL = data0.getString("KOL");

            String BARCODES = data0.getString("BARCODES");
            String IDL = data0.getString("IDL");
            String SECTOR = data0.getString("SECTOR");
            String IS_CHECK = data0.getString("IS_CHECK");

            String TOVSHEET="-";
            try
            {
                TOVSHEET = data0.getString("TOVSHEET");
            }
            catch (Exception ex)
            {

            }

            boolean already_checked=false;
            boolean b2=false;

            if(IS_CHECK.equals("1"))
                already_checked=true;

            last_barcode=local_arrlist.get(local_arrlist.size()-1).toString();

            if(local_arrlist!=null)
            {
                for (int j = 0; j < local_arrlist.size(); j++)
                {
                    if (BARCODES.indexOf(local_arrlist.get(j).toString())>-1)
                        already_checked = true;

                    if (BARCODES.contains(last_barcode))
                    {
                        b2 = true;
                        scrollto=i;
                    }
                }
            }
            //"idl=" + точно не надо!!!!!! поле IDL используется при составлении запроса //"ts=" +
            items.add(new ItemDocumentPrihod(CMC, NAME, KOL, IDL, SECTOR ,TOVSHEET ,NOMENKL, already_checked, b2));
        }


        adapter = new MyItemRecyclerViewAdapterDocumentPrihod(items, mListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mRecyclerView.scrollToPosition(scrollto);
    }

    public void getName(View view) {
        // получаем сохраненное имя
        izone = settings.getString(PREF_IZONE, "");
        idemp = settings.getString(PREF_IDEMP, "");
        printer = settings.getString(PREF_PRINTER, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_document_prihod, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());

        docum_prihod_IDDOC = (EditText) rootView.findViewById(R.id.docum_prihod_IDDOC);
        docum_prihod_IDDOC.setText(IdDoc);

        docum_prihod_NNAKL = (EditText) rootView.findViewById(R.id.docum_prihod_NNAKL);
        docum_prihod_button_getdocbyid = (Button) rootView.findViewById(R.id.docum_prihod_button_getdocbyid);
        docum_prihod_button_info = (Button) rootView.findViewById(R.id.docum_prihod_button_info);
        docum_prihod_save = (ImageButton) rootView.findViewById(R.id.docum_prihod_save);
        docum_prihod_save2 = (ImageButton) rootView.findViewById(R.id.docum_prihod_save2);


        docum_prihod_button_getdocbyid.setOnClickListener(this);
        docum_prihod_button_info.setOnClickListener(this);
        docum_prihod_save.setOnClickListener(this);
        docum_prihod_save2.setOnClickListener(this);

        docum_prihod_barcodes=(EditText) rootView.findViewById(R.id.docum_prihod_barcodes);
        docum_prihod_barcodes.setText(local_arrlist.toString());

           mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerviewtest25555555);
        try {
            GetDocHeader(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            fillData(rootView, docum_prihod_IDDOC.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int checked=0;

        if(adapter!=null)
        {
            for (ItemDocumentPrihod p : adapter.getBox()) {
                checked++;
            }
            Toast.makeText(getActivity().getApplicationContext(), "checked=" + checked, Toast.LENGTH_LONG).show();
            docum_prihod_NNAKL.setText("Проверено " + checked + " из " + mRecyclerView.getAdapter().getItemCount());

        }

        getName(rootView);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    void GetDocHeader(final boolean show_hint) throws JSONException {
        String login="ROSTOA$NSK20";
        String pass="345544";
        String url;

        url =api_url + "?p=get_rnk_header&login=" + login + "&pass=" + pass + "&iddoc=" + docum_prihod_IDDOC.getText() + "&json=1";
        items = new ArrayList<ItemDocumentPrihod>();

        StringBuffer http_result = MyHttp.Get(url, getContext());
        String response = http_result.toString();

        JSONObject userJson = new JSONObject(response);
        JSONArray data = (JSONArray) userJson.getJSONArray("data");

        for (int i = 0; i < data.length(); i++) {
            JSONObject data0 = data.getJSONObject(i);
            String NNAKL = data0.getString("NNAKL");
            String DNAKL = data0.getString("DNAKL");
            String ORG = data0.getString("ORG");
            String LINES = data0.getString("LINES");
            String VES = data0.getString("VES");
            String VOL = data0.getString("VOL");
            String TRIP = data0.getString("TRIP");

            String DPLAT = data0.getString("DPLAT");
            docum_prihod_NNAKL.setText(NNAKL);

            String result="Накладная: " + NNAKL + "\r\n";
            result+="ID документа: " + docum_prihod_IDDOC.getText() + "\r\n";
            result+="Клиент: " + ORG + "\r\n";
            result+="В док-те: " + "\r\n";
            result+="Строк: " + LINES + "/" + " Кг" + VES + "/" + "М3" + VOL + "\r\n";
            result+="Дата: " + DNAKL + "\r\n";
            result+="Дата: " + DPLAT + "\r\n";

            if(show_hint)
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        final Date date = new Date();
        final String login="ROSTOA$NSK20";
        final String pass="345544";

        switch(v.getId())
         {
               case R.id.docum_prihod_button_getdocbyid:
                   IdDoc=docum_prihod_IDDOC.getText().toString();
                   try {
                       GetDocHeader(false);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   try
                   {
                       fillData(v, docum_prihod_IDDOC.getText().toString());
                   }
                   catch (JSONException e) {
                       e.printStackTrace();
                   }
                   break;

               case R.id.docum_prihod_button_info:
                   try {
                       GetDocHeader(true);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   break;


             case R.id.docum_prihod_save2:

                 for (ItemDocumentPrihod p : adapter.getBox()) {
                     String url="http://192.168.1.145/insert_priemka.php?iddoc=" + p.tovSheet + "&idl=" + p.tovIDL + "&kol=" + p.tovKol;
                     String save_result=MyHttp.Get(url,  getActivity().getApplicationContext()).toString();
                     Toast.makeText(getActivity().getApplicationContext(), save_result, Toast.LENGTH_LONG).show();
                 }


                 break;
             case R.id.docum_prihod_save:


                   DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           switch (which){
                               case DialogInterface.BUTTON_POSITIVE:
                                   String idsheet=""; ///"-" + docum_prihod_IDDOC.getText();
                                   //IdEmp.getText()

                                   System.out.println(dateFormat.format(date));
                                   String hdr = "";

                                   String lns="";
                                   for (ItemDocumentPrihod p : adapter.getBox2()) {
                                       idsheet=p.tovSheet;
                                       hdr =  p.tovSheet + "^" + dateFormat.format(date) + "^" + idemp + "^16^"; //hdr='idSh^dcurr^cemp^flg^'
                                       lns += p.tovIDL + "^" + p.tovKol + "^^^^" + dateFormat.format(date) + "^T^^"; /// 28-02-2022
                                   }
                                   //idemp="1900161";
                                   String url =api_url + "?p=save_tovsheet_priem&login=" + login + "&pass=" + pass + "&idemp=" + idemp + "&izone=" + izone + "&idSheet=" + idsheet  + "&iflg=16" +
                                           "&hdr=" + hdr + "&lns=" + lns;

                                   ClipboardManager clipboard = (ClipboardManager) getActivity().getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                   ClipData clip = ClipData.newPlainText("", url);
                                   clipboard.setPrimaryClip(clip);



                                   String save_result=MyHttp.Get(url,  getActivity().getApplicationContext()).toString();
                                   Toast.makeText(getActivity().getApplicationContext(), "Товары в корзине:\n" + hdr + "\n" + lns + "\n" + save_result, Toast.LENGTH_LONG).show();
                                   break;

                               case DialogInterface.BUTTON_NEGATIVE:
                                   //No button clicked
                                   break;
                           }
                       }
                   };


                   AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                   builder.setMessage("Вы уверены?").setPositiveButton("Да", dialogClickListener)
                           .setNegativeButton("Нет", dialogClickListener).show();

         }
    }

    public void setIdDoc(String text) {
        IdDoc=text;
    }




    public void setBarcodesList(ArrayList l) {
        local_arrlist=l;

        if(docum_prihod_barcodes!=null)
        {
            String temp=local_arrlist.toString();
            temp=temp.replaceAll(",","\r\n");
            docum_prihod_barcodes.setText(temp);


            try {
                fillData(rootView, docum_prihod_IDDOC.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
