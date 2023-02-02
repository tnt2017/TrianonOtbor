package com.trianonotbor;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentDocument.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentDocument#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDocument extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String IdDoc = "210009635";
    EditText docum_IDDOC, docum_NNAKL;
    List<ItemDocument> items = new ArrayList<ItemDocument>();
    MyItemRecyclerViewAdapterDocument adapter;
    RecyclerView mRecyclerView;
    Button docum_button_getdocbyid, docum_button_info, docum_print;
    SharedPreferences settings;
    private static final String PREF_IZONE = "izone";
    private static final String PREF_IDEMP = "idemp";
    private static final String PREF_PRINTER = "printer";

    String api_url = "https://svc.trianon-nsk.ru/clients/code_reader/index.php";
    String izone, idemp, printer = "http://10.8.0.25/print/?idsheet=";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentDocument() {
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
    public static FragmentDocument newInstance(String param1, String param2) {
        FragmentDocument fragment = new FragmentDocument();
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

    public void fillData(View rootView, String IdSheet) throws JSONException {
        String login = "ROSTOA$NSK20";
        String pass = "345544";
        String url = api_url + "?p=get_rnk&login=" + login + "&pass=" + pass + "&iddoc=" + IdSheet + "&json=1";
        items = new ArrayList<ItemDocument>();

        StringBuffer http_result = MyHttp.Get(url, getContext());
        String response = http_result.toString();

        JSONObject userJson = new JSONObject(response);
        JSONArray data = (JSONArray) userJson.getJSONArray("data");

        for (int i = 0; i < data.length(); i++) {
            JSONObject data0 = data.getJSONObject(i);
            String CMC = data0.getString("CMC");
            String NAME = data0.getString("NAME");
            String KOL = data0.getString("KOL");
            String PRICE = data0.getString("PRICE");
            String TIPSOPR = data0.getString("TIPSOPR");
            items.add(new ItemDocument(CMC, NAME, KOL, PRICE, TIPSOPR, ""));
        }

        adapter = new MyItemRecyclerViewAdapterDocument(items, mListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
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
        View rootView = inflater.inflate(R.layout.fragment_document, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());

        docum_IDDOC = (EditText) rootView.findViewById(R.id.docum_IDDOC);
        docum_IDDOC.setText(IdDoc);
        docum_NNAKL = (EditText) rootView.findViewById(R.id.docum_NNAKL);
        docum_button_getdocbyid = (Button) rootView.findViewById(R.id.docum_button_getdocbyid);
        docum_button_info = (Button) rootView.findViewById(R.id.docum_button_info);
        docum_print = (Button) rootView.findViewById(R.id.docum_print);
        docum_button_getdocbyid.setOnClickListener(this);
        docum_button_info.setOnClickListener(this);
        docum_print.setOnClickListener(this);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerviewtest2);
        try {
            GetDocHeader(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            fillData(rootView, docum_IDDOC.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
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

        url =api_url + "?p=get_rnk_header&login=" + login + "&pass=" + pass + "&iddoc=" + docum_IDDOC.getText() + "&json=1";
        items = new ArrayList<ItemDocument>();

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
            docum_NNAKL.setText(NNAKL);

            String result="Накладная: " + NNAKL + "\r\n";
            result+="ID документа: " + DNAKL + "\r\n";
            result+="Клиент: " + DNAKL + "\r\n";
            result+="В док-те: " + "\r\n";
            result+="Строк: " + LINES + "/" + "Кг" + VES + "/" + "М3" + VOL + "\r\n";
            result+="Дата: " + DNAKL + "\r\n";
            result+="Дата: " + DPLAT + "\r\n";

            if(show_hint)
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
         switch(v.getId())
         {
               case R.id.docum_button_getdocbyid:
                   try {
                       GetDocHeader(false);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   try
                   {
                       fillData(v, docum_IDDOC.getText().toString());
                   }
                   catch (JSONException e) {
                       e.printStackTrace();
                   }
                   break;

               case R.id.docum_button_info:
                   try {
                       GetDocHeader(true);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   break;

               case R.id.docum_print:
                    Toast.makeText(getActivity().getApplicationContext(), "Тест", Toast.LENGTH_LONG).show();
                    break;
         }
    }

    public void setIdDoc(String text) {
        IdDoc=text;
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
