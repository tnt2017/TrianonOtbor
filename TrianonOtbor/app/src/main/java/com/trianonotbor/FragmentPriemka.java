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
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
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
 * {@link FragmentPriemka.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentPriemka#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPriemka extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText  priemka_dbeg, priemka_dend;
    Button btn_priemka_new;
    ImageButton btn_priemka_apply;
    List<ItemPriemka> items = new ArrayList<ItemPriemka>();
    MyItemRecyclerViewAdapterPriemka adapter;
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

    public FragmentPriemka() {
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
    public static FragmentPriemka newInstance(String param1, String param2) {
        FragmentPriemka fragment = new FragmentPriemka();
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


    public void fillData(View rootView, String param) throws JSONException {
        String login = "ROSTOA$NSK20";
        String pass = "345544";
        String url =api_url +"?p=" + param + "&login=" + login + "&pass=" + pass + "&dbeg=" + priemka_dbeg.getText() + "&dend=" + priemka_dend.getText() + "&json=1";
       // items.clear();
        items = new ArrayList<ItemPriemka>();
        StringBuffer http_result = MyHttp.Get(url, getActivity().getApplicationContext());
        String response=http_result.toString();

        JSONObject userJson = new JSONObject(response);
        JSONArray data= (JSONArray) userJson.getJSONArray("data");

        for(int i=0;i<data.length();i++) {
            String KOR = "", ID = "", NNAKL = "";
            JSONObject data0 = data.getJSONObject(i);

            try {
                ID = data0.getString("CBDOC");
                NNAKL = data0.getString("NNAKL");
            }
            catch (Exception ex)
            {

            }


            String ORG=data0.getString("ORG");
            String DN=data0.getString("DT");
            String CHECKER=data0.getString("EMP");
            String SUMMA="0";//data0.getString("SUMM");

            try {
                KOR=data0.getString("KOR");
            }
            catch (Exception ex)
            {

            }

            items.add(new ItemPriemka(ID, NNAKL, ORG, DN, KOR, CHECKER, KOR, SUMMA ));
        }

        adapter = new MyItemRecyclerViewAdapterPriemka(items, mListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

    public void getName(View view) {
        // получаем сохраненное имя
        izone = settings.getString(PREF_IZONE,"");
        idemp = settings.getString(PREF_IDEMP,"");
        printer = settings.getString(PREF_PRINTER,"");
       // IdEmpEditText.setText(idemp);
       // Izone.setText(name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_priemka, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_tovsheets);
        priemka_dbeg=(EditText)rootView.findViewById(R.id.priemka_dbeg);
        priemka_dend=(EditText)rootView.findViewById(R.id.priemka_dend);

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date d = new Date();//intialize your date to any date
        Date dbeg = new Date(d.getTime());
        priemka_dbeg.setText(dateFormat.format(dbeg));
        Date dend = new Date(d.getTime() + 1 * 24 * 3600 * 1000l); //+1 days
        priemka_dend.setText(dateFormat.format(dend));

        btn_priemka_apply=(ImageButton) rootView.findViewById(R.id.btn_priemka_apply);
        btn_priemka_apply.setOnClickListener(this);

        btn_priemka_new=(Button) rootView.findViewById(R.id.btn_priemka_new);
        btn_priemka_new.setOnClickListener(this);

        getName(rootView);

        try
        {
            fillData(rootView, "list_takeover"); // было list_nakl 20-04-2021
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
            case R.id.btn_priemka_apply:
                try {
                    fillData(v, "list_takeover"); // было list_nakl 20-04-2021
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_priemka_new:

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                FragmentTakeover myFragment = new FragmentTakeover();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
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
