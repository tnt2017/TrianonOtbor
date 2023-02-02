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
 * {@link FragmentAudit.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentAudit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAudit extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText audit_idtov;
    List<ItemAudit> items = new ArrayList<ItemAudit>();
    MyItemRecyclerViewAdapterAudit adapter;
    RecyclerView mRecyclerView;
    SharedPreferences settings;
    private static final String PREF_IZONE = "izone";
    private static final String PREF_IDEMP = "idemp";
    private static final String PREF_PRINTER = "printer";
    String api_url="https://svc.trianon-nsk.ru/clients/code_reader/index.php";
    String izone, idemp, printer="http://10.8.0.25/print/?idsheet=";
    String IdTov;
    View rootView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public FragmentAudit() {
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
    public static FragmentAudit newInstance(String param1, String param2) {
        FragmentAudit fragment = new FragmentAudit();
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

    public void setIdTov(String text) throws JSONException {
        IdTov=text;
        if(audit_idtov!=null)
        {
            audit_idtov.setText(IdTov);
            fillData(rootView, audit_idtov.getText().toString(), "get_tov_audit");
        }
    }

    public void fillData(View rootView, String idtov, String param) throws JSONException {
        String login = "ROSTOA$NSK20";
        String pass = "345544";
        String url =api_url +"?p=" + param + "&login=" + login + "&pass=" + pass + "&idtov=" + idtov + "&json=1";
        // items.clear();
        items = new ArrayList<ItemAudit>();
        StringBuffer http_result = MyHttp.Get(url, getActivity().getApplicationContext());
        String response=http_result.toString();

        JSONObject userJson = new JSONObject(response);
        JSONArray data= (JSONArray) userJson.getJSONArray("data");

        for(int i=0;i<data.length();i++)
        {
            String EMP="";
            JSONObject data0=data.getJSONObject(i);

            try {
                EMP=data0.getString("EMP");
            }
            catch (Exception ex)
            {

            }

            //data0.getString("IDMA") +
            String auditHEAD=data0.getString("DMODF") + "(" + EMP  + ")";

            String auditTEXT="VES=" + data0.getString("VES") +
                    " RAZMER=" + data0.getString("RAZMER") +
                    " PACK=" + data0.getString("PACK") +
                    " KOL_UPAK=" + data0.getString("KOL_UPAK");

            items.add(new ItemAudit(auditHEAD, auditTEXT)); //SKL_ZONE
        }

        adapter = new MyItemRecyclerViewAdapterAudit(items, mListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

    public void getName(View view) {
        // получаем сохраненное имя
        izone = settings.getString(PREF_IZONE,"");
        idemp = settings.getString(PREF_IDEMP,"");
        printer = settings.getString(PREF_PRINTER,"");
        //audit_idtov.setText("231946");
       // Izone.setText(name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_audit, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        audit_idtov=(EditText)rootView.findViewById(R.id.audit_idtov);
        audit_idtov.setText(IdTov);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_audit);
        Button audit_button_apply=(Button)rootView.findViewById(R.id.audit_button_apply);
        audit_button_apply.setOnClickListener(this);
        getName(rootView);

        try
        {
            fillData(rootView, audit_idtov.getText().toString(), "get_tov_audit");
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
            case R.id.audit_button_apply:
                try {
                    fillData(v, audit_idtov.getText().toString(), "get_tov_audit");
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
