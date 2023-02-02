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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement ttovsheets_IdEmphe
 * {@link FragmentHistSection.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentHistSection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHistSection extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText histsec_barcode;
    String IdTrip="21008778";
    List<ItemHistSection> items = new ArrayList<ItemHistSection>();
    MyItemRecyclerViewAdapterHistSection adapter;
    RecyclerView mRecyclerView;
    SharedPreferences settings;
    private static final String PREF_IZONE = "izone";
    private static final String PREF_IDEMP = "idemp";
    private static final String PREF_PRINTER = "printer";
    String api_url="https://svc.trianon-nsk.ru/clients/code_reader/index.php";
    String izone, idemp, printer="http://10.8.0.25/print/?idsheet=";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    View rootView;

    public FragmentHistSection() {
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
    public static FragmentHistSection newInstance(String param1, String param2) {
        FragmentHistSection fragment = new FragmentHistSection();
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


    public void fillData(View rootView, String barcode, String param) throws JSONException {
        String login = "ROSTOA$NSK20";
        String pass = "345544";
        String url =api_url +"?p=" + param + "&login=" + login + "&pass=" + pass + "&barcode=" + barcode + "&json=1";
       // items.clear();
        items = new ArrayList<ItemHistSection>();
        StringBuffer http_result = MyHttp.Get(url, getActivity().getApplicationContext());
        String response=http_result.toString();

        JSONObject userJson = new JSONObject(response);
        JSONArray data= (JSONArray) userJson.getJSONArray("data");

        for(int i=0;i<data.length();i++)
        {
            String LINES="";
            JSONObject data0=data.getJSONObject(i);
            String ID=data0.getString("ID");
            String OSNOVANIE=data0.getString("SECTION");
            String SUMPAY=data0.getString("NAME")  ;
            String DSTART=""; //data0.getString("DSTART");
            String KG_A=""; //data0.getString("KG_A");
            //  String SKL_ZONE=data0.getString("SKL_ZONE");

            try {
                LINES=data0.getString("DEMP");
            }
            catch (Exception ex)
            {

            }

            items.add(new ItemHistSection(ID, OSNOVANIE, SUMPAY, KG_A, LINES, "1")); //SKL_ZONE
        }

        adapter = new MyItemRecyclerViewAdapterHistSection(items, mListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

    public void setBarcode(String text) throws JSONException {
        IdTrip=text;
        if(histsec_barcode!=null)
        {
            histsec_barcode.setText(IdTrip);
            fillData(rootView, histsec_barcode.getText().toString(), "get_hist_section");
        }
    }


    public void getName(View view) {
        // получаем сохраненное имя
        izone = settings.getString(PREF_IZONE,"");
        idemp = settings.getString(PREF_IDEMP,"");
        printer = settings.getString(PREF_PRINTER,"");
        histsec_barcode.setText("21008710");
       // Izone.setText(name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_histsection, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        histsec_barcode=(EditText)rootView.findViewById(R.id.histsec_barcode);
        Button histsec_apply=(Button)rootView.findViewById(R.id.histsec_apply);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_histsec);

        histsec_apply.setOnClickListener(this);
        getName(rootView);

        histsec_barcode.setText(IdTrip);

        try
        {
            fillData(rootView, histsec_barcode.getText().toString(), "get_hist_section");
        }
        catch (Exception ex)
        {
        }
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

    @Override
    public void onClick(View v)
    {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url;

        switch(v.getId())
        {
            case R.id.histsec_apply:
                try {
                    fillData(v, histsec_barcode.getText().toString(), "get_hist_section");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
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
