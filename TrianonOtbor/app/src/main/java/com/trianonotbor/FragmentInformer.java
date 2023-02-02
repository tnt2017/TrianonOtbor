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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement ttovsheets_IdEmphe
 * {@link FragmentInformer.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentInformer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInformer extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText informer_dt, informer_idemp;
    List<ItemInformer> items = new ArrayList<ItemInformer>();
    MyItemRecyclerViewAdapterInformer adapter;
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

    public FragmentInformer() {
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
    public static FragmentInformer newInstance(String param1, String param2) {
        FragmentInformer fragment = new FragmentInformer();
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

        String url =api_url +"?p=" + param + "&login=" + login + "&pass=" + pass + "&dt=" + dt + "&idemp=" + informer_idemp.getText() + "&json=1";
        //items.clear();
        items = new ArrayList<ItemInformer>();
        StringBuffer http_result = MyHttp.Get(url, getActivity().getApplicationContext());
        String response=http_result.toString();

        JSONObject userJson = new JSONObject(response);
        JSONArray data= (JSONArray) userJson.getJSONArray("data");

        for(int i=0;i<data.length();i++)
        {
            String LINES="";
            String EMP2="";
            String MSG_PART="";
            JSONObject data0=data.getJSONObject(i);
            String ID=data0.getString("AUT");
            try {
                 EMP2 = data0.getString("EMP2");
                 MSG_PART=data0.getString("MSG_PART");
            }
            catch (Exception ex)
            {

            }
            String SUBJ="0", AUT="0", LINES_A="0", DSTAMP="0";

//            String SKL_ZONE=data0.getString("SKL_ZONE");

            try {
                SUBJ=data0.getString("SUBJ");
                AUT=data0.getString("AUT");
                LINES_A="tetetete"; ///data0.getString("AUT");
                DSTAMP=data0.getString("DSTAMP");
            }
            catch (Exception ex)
            {

            }

            items.add(new ItemInformer(ID, EMP2, MSG_PART, SUBJ, AUT , LINES_A, DSTAMP)); //SKL_ZONE
        }

        adapter = new MyItemRecyclerViewAdapterInformer(items, mListener);
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
        View rootView=inflater.inflate(R.layout.fragment_informer, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        informer_dt=(EditText)rootView.findViewById(R.id.informer_dt);
        informer_idemp=(EditText)rootView.findViewById(R.id.informer_idemp);

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date dend = new Date();
        informer_dt.setText(dateFormat.format(dend));

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_informer);
        Button informer_button_apply=(Button)rootView.findViewById(R.id.informer_button_apply);
        informer_button_apply.setOnClickListener(this);
        getName(rootView);

        try
        {
            fillData(rootView, informer_dt.getText().toString(), "list_info_msg");
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
            case R.id.informer_button_apply:
                try {
                    fillData(v, informer_dt.getText().toString(), "list_info_msg");
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
