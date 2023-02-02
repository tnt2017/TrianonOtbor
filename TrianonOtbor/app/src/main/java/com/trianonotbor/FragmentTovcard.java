package com.trianonotbor;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentTovcard.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentTovcard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTovcard extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String IdTov = "283008";
    EditText IdTovEditText, IdEmp;
    List<ItemTovcard> items = new ArrayList<ItemTovcard>();
    MyItemRecyclerViewAdapterTovcard adapter;
    RecyclerView mRecyclerView;
    Button tovcard_button1, tovcard_button2,tovcard_button3;
    SharedPreferences settings;
    AutoCompleteTextView AutoComplTV;
    private static final String PREF_IZONE = "izone";
    private static final String PREF_IDEMP = "idemp";
    private static final String PREF_PRINTER = "printer";

    String api_url="https://svc.trianon-nsk.ru/clients/code_reader/index.php";
    String izone, idemp, printer="http://10.8.0.25/print/?idsheet=";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentTovcard() {
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
    public static FragmentTovcard newInstance(String param1, String param2) {
        FragmentTovcard fragment = new FragmentTovcard();
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




    public void fillData(View rootView, String IdTov) throws JSONException {
        //items.add(new ItemTovsheet("1","2","3","4","5","6"));
        String login = "ROSTOA$NSK20";
        String pass = "345544";
        String url =api_url +"?p=get_tovcard&login=" + login + "&pass=" + pass + "&idtov=" + IdTov + "&json=1";
       // items.clear();
        items = new ArrayList<ItemTovcard>();

        StringBuffer http_result = MyHttp.Get(url, getActivity().getApplicationContext());
        String response=http_result.toString();

        JSONObject userJson = new JSONObject(response);
        JSONArray data= (JSONArray) userJson.getJSONArray("data");

        for(int i=0;i<data.length();i++)
        {
            JSONObject data0=data.getJSONObject(i);

            String ID=data0.getString("ID");
            String NNAKL=data0.getString("NNAKL");
            String DAT=data0.getString("DAT");
            String EMP=data0.getString("EMP");
            String KOL_MV=data0.getString("KOL_MV");
            String TYP=data0.getString("TYP");
            String TXT=data0.getString("TXT");
            items.add(new ItemTovcard(ID, DAT, NNAKL, EMP, KOL_MV, TYP, TXT ));
        }


        adapter = new MyItemRecyclerViewAdapterTovcard(items, mListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

    }

    public void getName(View view) {
        // получаем сохраненное имя
        izone = settings.getString(PREF_IZONE,"");
        idemp = settings.getString(PREF_IDEMP,"");
        printer = settings.getString(PREF_PRINTER,"");
        IdEmp.setText(idemp);
       // Izone.setText(name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView=inflater.inflate(R.layout.fragment_tovcard, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());

        IdTovEditText=(EditText)rootView.findViewById(R.id.IdTov);
        IdTovEditText.setText(IdTov);
        IdEmp=(EditText)rootView.findViewById(R.id.IdEmp);

        tovcard_button1=(Button)rootView.findViewById(R.id.tovcard_button1);
        tovcard_button2=(Button)rootView.findViewById(R.id.tovcard_button2);
        tovcard_button3=(Button)rootView.findViewById(R.id.tovcard_button3);

        tovcard_button1.setOnClickListener(this);
        tovcard_button2.setOnClickListener(this);
        tovcard_button3.setOnClickListener(this);

        AutoComplTV=(AutoCompleteTextView)rootView.findViewById(R.id.AutoComplTovar);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerviewtest3);
        try {
            fillData(rootView, IdTovEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity   ());
        final String url = "https://svc.trianon-nsk.ru/clients/main/pages/jrn_kk_svc.php?svc_id=get_tov&idtov=" + IdTov;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject userJson = new JSONObject(response);
                            JSONArray data= (JSONArray) userJson.getJSONArray("data");
                            JSONObject data0=data.getJSONObject(0);
                            AutoComplTV.setText(data0.getString("NAME"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // blankFragment1.setText("error" + error);
                AutoComplTV.setText(url + "That didn't work!" + error);
            }
        });
        // statusMessage_Properties.setText(url);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        AutoComplTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String s=AutoComplTV.getText().toString();
                String ss[]=s.split(":");
                AutoComplTV.setText(ss[0]);
                IdTovEditText.setText(ss[1]);
            }
        });

        AutoComplTV.addTextChangedListener(
                new TextWatcher() {
                    private Object receiver;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        receiver = null;
                    }

                    @Override
                    public void afterTextChanged(Editable s)
                    {
                        String login = "ROSTOA$NSK20";
                        String pass = "345544";
                        String url = "https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=tov_list&login=" + login + "&pass=" + pass + "&patt=" + s.toString();
                        StringBuffer result = MyHttp.Get(url, getActivity().getApplicationContext());
                        String[] items = result.toString().split(";");

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_item, items);
                        AutoComplTV.setThreshold(1);
                        AutoComplTV.setAdapter(adapter);
                        AutoComplTV.setBackgroundColor(Color.parseColor("#DAD871"));
                        AutoComplTV.setTextColor(Color.BLUE);
                    }
                });


        getName(rootView);
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
        String login="ROSTOA$NSK20";
        String pass="345544";
        String url;

            switch(v.getId()) {
                case R.id.tovcard_button1:
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    FragmentInvent myFragment = new FragmentInvent();
                    myFragment.setIdtov(IdTovEditText.getText().toString());
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();

                    break;

                case R.id.tovcard_button2:
                    try {
                        fillData(v, IdTovEditText.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    url =api_url + "?p=get_tov_info&login=" + login + "&pass=" + pass + "&idtov=" + IdTovEditText.getText();
                    StringBuffer sb=MyHttp.Get(url,v.getContext());

                    String[] subStr=sb.toString().split(";");
                    AutoComplTV.setText(subStr[0]);

                    break;

                case R.id.tovcard_button3:
                    AppCompatActivity activity1 = (AppCompatActivity) v.getContext();
                    FragmentTovProps myFragment1 = new FragmentTovProps();
                    myFragment1.setIDTOV(IdTovEditText.getText().toString());
                    activity1.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment1).addToBackStack(null).commit();
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }


    }

    public void setIdTov(String text) {
        IdTov=text;
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
