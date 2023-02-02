package com.trianonotbor;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
 * Activities that contain this fragment must implement the
 * {@link FragmentStrikeouts.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentStrikeouts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentStrikeouts extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";    List<ItemStrikeout> items = new ArrayList<ItemStrikeout>();
    MyItemRecyclerViewAdapterStrikeouts adapter;
    RecyclerView mRecyclerView;
    SharedPreferences settings;

    private static final String PREF_IZONE = "izone";
    private static final String PREF_IDEMP = "idemp";
    private static final String PREF_PRINTER = "printer";

    String api_url="https://svc.trianon-nsk.ru/clients/code_reader/index.php";
    String izone, idemp, printer="http://10.8.0.25/print/?idsheet=";
    TextView strikeouts_dbeg,strikeouts_dend;
    Button btn_strikeouts_apply;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentStrikeouts() {
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
    public static FragmentStrikeouts newInstance(String param1, String param2) {
        FragmentStrikeouts fragment = new FragmentStrikeouts();
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


    public void fillData(View rootView ) throws JSONException {
        String login = "ROSTOA$NSK20";
        String pass = "345544";
        String url =api_url +"?p=get_strikeouts&login=" + login + "&pass=" + pass + "&dbeg=" + strikeouts_dbeg.getText() + "&dend=" + strikeouts_dend.getText() +  "&json=1";
       // items.clear();
        items = new ArrayList<ItemStrikeout>();

        RequestQueue queue = Volley.newRequestQueue(getActivity   ());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject userJson = new JSONObject(response);
                            JSONArray data= (JSONArray) userJson.getJSONArray("data");

                            for(int i=0;i<data.length();i++)
                            {
                                JSONObject data0=data.getJSONObject(i);
                                String CMC=data0.getString("CMC");
                                String TOV=data0.getString("TOV");
                                String SECTION=data0.getString("SECTION");
                                String NNAKL=data0.getString("NNAKL");
                                String DSTAMP=data0.getString("DSTAMP");
                                String EMP=data0.getString("EMP");
                                String KOL_V=data0.getString("KOL_V");
                                String TRIP=data0.getString("TRIP");

                                items.add(new ItemStrikeout(CMC, TOV, SECTION, NNAKL,DSTAMP,EMP,KOL_V,TRIP ));
                            }

                            adapter = new MyItemRecyclerViewAdapterStrikeouts(items, mListener);
                            mRecyclerView.setAdapter(adapter);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // blankFragment1.setText("error" + error);
             }
        });
         queue.add(stringRequest);
     }

    public void getName(View view) {
        // получаем сохраненное имя
        izone = settings.getString(PREF_IZONE,"");
        idemp = settings.getString(PREF_IDEMP,"");
        printer = settings.getString(PREF_PRINTER,"");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView=inflater.inflate(R.layout.fragment_strikeouts, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerviewtest4);
        strikeouts_dbeg=(EditText)rootView.findViewById(R.id.strikeouts_dbeg);
        strikeouts_dend=(EditText)rootView.findViewById(R.id.strikeouts_dend);

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date d = new Date();//intialize your date to any date
        Date dbeg = new Date(d.getTime() - 1 * 24 * 3600 * 1000l); //Subtract n days
        strikeouts_dbeg.setText(dateFormat.format(dbeg));
        Date dend = new Date();
        strikeouts_dend.setText(dateFormat.format(dend));

        try {
            fillData(rootView );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_strikeouts_apply=(Button) rootView.findViewById(R.id.btn_strikeouts_apply);
        btn_strikeouts_apply.setOnClickListener(this);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url;

        switch(v.getId())
        {
            case R.id.btn_strikeouts_apply:
                try {
                    fillData(v);
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

    public interface OnListFragmentInteractionListener {
    }
}
