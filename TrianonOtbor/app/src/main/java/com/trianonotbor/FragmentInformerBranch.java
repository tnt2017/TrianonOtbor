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
 * {@link FragmentInformerBranch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentInformerBranch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInformerBranch extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText informer_branch_id;
    String IdTrip="21008778";
    List<ItemInformerBranch> items = new ArrayList<ItemInformerBranch>();
    MyItemRecyclerViewAdapterInformerBranch adapter;
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

    public FragmentInformerBranch() {
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
    public static FragmentInformerBranch newInstance(String param1, String param2) {
        FragmentInformerBranch fragment = new FragmentInformerBranch();
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
        items = new ArrayList<ItemInformerBranch>();
        StringBuffer http_result = MyHttp.Get(url, getActivity().getApplicationContext());
        String response=http_result.toString();

        JSONObject userJson = new JSONObject(response);
        JSONArray data= (JSONArray) userJson.getJSONArray("data");

        for(int i=0;i<data.length();i++)
        {
            String LINES="";
            JSONObject data0=data.getJSONObject(i);
            String SUBJ=data0.getString("SUBJ");
            String TXTMSG=data0.getString("TXTMSG");
            String AUTHOR=data0.getString("AUTHOR") + "руб";
            //String DSTART=
            String KG_A=data0.getString("EMP2");
            //  String SKL_ZONE=data0.getString("SKL_ZONE");

            try {
                LINES=data0.getString("LINES");
            }
            catch (Exception ex)
            {

            }

            items.add(new ItemInformerBranch(SUBJ, TXTMSG, AUTHOR, KG_A,LINES, "1")); //SKL_ZONE
        }

        adapter = new MyItemRecyclerViewAdapterInformerBranch(items, mListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

    public void setIdTrip(String text) throws JSONException {
        IdTrip=text;
        if(informer_branch_id !=null)
        {
            informer_branch_id.setText(IdTrip);
            fillData(rootView, informer_branch_id.getText().toString(), "informer_branch");
        }
    }


    public void getName(View view) {
        // получаем сохраненное имя
        izone = settings.getString(PREF_IZONE,"");
        idemp = settings.getString(PREF_IDEMP,"");
        printer = settings.getString(PREF_PRINTER,"");
        informer_branch_id.setText("21008710");
       // Izone.setText(name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_informer_branch, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        informer_branch_id =(EditText)rootView.findViewById(R.id.informer_branch_id);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_informer_branch);
        Button button_mysheets=(Button)rootView.findViewById(R.id.informer_branch_button_apply);
        button_mysheets.setOnClickListener(this);
        getName(rootView);

        informer_branch_id.setText(IdTrip);

        try
        {
            fillData(rootView, informer_branch_id.getText().toString(), "informer_branch");
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
            case R.id.informer_branch_button_apply:
                try {
                    fillData(v, informer_branch_id.getText().toString(), "informer_branch");
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
