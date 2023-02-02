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
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement ttovsheets_IdEmphe
 * {@link FragmentFrontComplekt.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentFrontComplekt#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFrontComplekt extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText front_complekt_dt, front_complekt_results;
    Spinner front_spinner;
    List<ItemTovsheets> items = new ArrayList<ItemTovsheets>();
    MyItemRecyclerViewAdapterFrontComplekt adapter;
    RecyclerView mRecyclerView;
    SharedPreferences settings;
    private static final String PREF_IZONE = "izone";
    private static final String PREF_IDEMP = "idemp";
    private static final String PREF_PRINTER = "printer";
    String api_url="https://svc.trianon-nsk.ru/clients/code_reader/index.php";
    String izone, idemp, printer="http://10.8.0.25/print/?idsheet=";
    Button front_complekt_button_apply;
    RadioGroup radioGroup_front;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public FragmentFrontComplekt() {
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
    public static FragmentFrontComplekt newInstance(String param1, String param2) {
        FragmentFrontComplekt fragment = new FragmentFrontComplekt();
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


    public void fillData(View rootView, String dt, String param) throws JSONException {
        String login = "ROSTOA$NSK20";
        String pass = "345544";
        String url =api_url +"?p=" + param + "&login=" + login + "&pass=" + pass + "&dt=" + dt + "&json=1";
       // items.clear();
        items = new ArrayList<ItemTovsheets>();
        StringBuffer http_result = MyHttp.Get(url, getActivity().getApplicationContext());
        String response=http_result.toString();

        JSONObject userJson = new JSONObject(response);
        JSONArray data= (JSONArray) userJson.getJSONArray("data");

        float summa_kg=0;
        float summa_m3=0;
        int summa_ln=0;
        int summa_pl=0;

        for(int i=0;i<data.length();i++)
        {
            String LINES="",KG_T="",M3_T="",PLACES="";
            JSONObject data0=data.getJSONObject(i);
            String ID=data0.getString("ID");
            String TRIP=data0.getString("TRIP");
            String ORG=data0.getString("ORG");
            String PLAN=data0.getString("PLAN");
            String SKL_ZONE=data0.getString("SKL_ZONE");

            try {
                LINES=data0.getString("LINES");
                PLACES=data0.getString("PLACES");
                KG_T=data0.getString("KG_T");
                M3_T=data0.getString("M3_T");

                summa_kg+=Float.parseFloat(KG_T);
                summa_m3+=Float.parseFloat(M3_T);
                summa_ln+=Integer.parseInt(LINES);
                summa_pl+=Integer.parseInt(PLACES);
            }
            catch (Exception ex)
            {

            }
            //+ LINES + "строк" +  + M3_T + "м3
            items.add(new ItemTovsheets(ID, TRIP, ORG + "(" + KG_T + "кг/" + LINES + "строк)", "План выезда: " + PLAN,LINES, SKL_ZONE ));

            //

        }

        front_complekt_results.setText(summa_pl + "мест / " + String.format("%.1f",summa_kg) + "кг / " +
                String.format("%.1f",summa_m3) + "м3 / " + summa_ln + "строк ");

        adapter = new MyItemRecyclerViewAdapterFrontComplekt(items, mListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

    public void getName(View view) {
        // получаем сохраненное имя
        izone = settings.getString(PREF_IZONE,"");
        idemp = settings.getString(PREF_IDEMP,"");
        printer = settings.getString(PREF_PRINTER,"");
       // Izone.setText(name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_front_complekt, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        front_complekt_dt=(EditText)rootView.findViewById(R.id.front_complekt_dt);
        front_complekt_results=(EditText)rootView.findViewById(R.id.front_complekt_results);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_tovsheets);
        front_complekt_button_apply=(Button)rootView.findViewById(R.id.front_complekt_button_apply);
        front_complekt_button_apply.setOnClickListener(this);

        radioGroup_front=(RadioGroup)rootView.findViewById(R.id.radioGroup_front);
        String[] countries = {"Комплектовка", "Упаковка"};

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date d = new Date();//intialize your date to any date
        Date dt = new Date(d.getTime() + 1 * 24 * 3600 * 1000l); //Subtract n days
        front_complekt_dt.setText(dateFormat.format(dt));

        getName(rootView);
        try
        {
            String param="get_front_complekt";

            if(radioGroup_front.getCheckedRadioButtonId()==R.id.radio_front_complekt)
                param="get_front_complekt";
            if(radioGroup_front.getCheckedRadioButtonId()==R.id.radio_front_upak)
                param="get_front_upak";

            fillData(rootView, front_complekt_dt.getText().toString(), param);
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
            case R.id.front_complekt_button_apply:
                try {
                    String param="get_front_complekt";

                    if(radioGroup_front.getCheckedRadioButtonId()==R.id.radio_front_complekt)
                        param="get_front_complekt";
                    if(radioGroup_front.getCheckedRadioButtonId()==R.id.radio_front_upak)
                        param="get_front_upak";

                    fillData(v, front_complekt_dt.getText().toString(), param);
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
