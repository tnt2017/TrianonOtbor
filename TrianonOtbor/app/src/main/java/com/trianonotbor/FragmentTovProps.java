package com.trianonotbor;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentTovProps.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentTovProps#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTovProps extends Fragment implements View.OnClickListener  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SharedPreferences settings;
    private static final String PREF_NAME = "login";
    private static final String PREF_PASS = "pass";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private String barcode;

    public FragmentTovProps() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTovProps.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTovProps newInstance(String param1, String param2) {
        FragmentTovProps fragment = new FragmentTovProps();
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

    String idtov;
    TextView tovid, tovname, tovves, tovrazmer, tovprops_section, statusMessage_Properties;
    EditText tovprops_kolupak, tovBOX_L, tovBOX_W, tovBOX_H, tovITEM_L, tovITEM_W, tovITEM_H,tovprops_nomenkl;
    Button button_fr3,button_goto_invent,button_goto_tovcard;
    ImageView imageView_tov;



    public void SQLITE_GetTov(String idtov)
    {
        Cursor query=SqliteDB.GetTovByID(getActivity(), idtov);
            if (query.moveToFirst())
            {
                tovid.setText(query.getString(query.getColumnIndex("ID")));
                tovname.setText(query.getString(query.getColumnIndex("NAME")));
                tovprops_nomenkl.setText(query.getString(query.getColumnIndex("NOMENKL")));
                tovprops_kolupak.setText(query.getString(query.getColumnIndex("KOL_UPAK")));
                tovprops_section.setText(query.getString(query.getColumnIndex("SECTION")));
                tovves.setText(query.getString(query.getColumnIndex("VES")));
                tovrazmer.setText(query.getString(query.getColumnIndex("RAZMER")));

                tovBOX_L.setText(query.getString(query.getColumnIndex("BOX_L")));
                tovBOX_W.setText(query.getString(query.getColumnIndex("BOX_W")));
                tovBOX_H.setText(query.getString(query.getColumnIndex("BOX_H")));

                try {
                    tovITEM_L.setText(query.getString(query.getColumnIndex("ITEM_L")));
                    tovITEM_W.setText(query.getString(query.getColumnIndex("ITEM_W")));
                    tovITEM_H.setText(query.getString(query.getColumnIndex("ITEM_H")));
                }
                catch (Exception ex)
                {

                }
                //Toast.makeText(getActivity().getApplicationContext(), barcode, Toast.LENGTH_LONG).show();
            }
    }


    public void UpdateProperties(final View v)
    {
        SQLITE_GetTov(idtov);

        /*String login="ROSTOA$NSK20";
        String pass="345544";

        RequestQueue queue = Volley.newRequestQueue(getActivity   ());
        final String url = "https://svc.trianon-nsk.ru/clients/main/pages/jrn_kk_svc.php?svc_id=get_tov&idtov=" + idtov;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject userJson = new JSONObject(response);
                            JSONArray data= (JSONArray) userJson.getJSONArray("data");
                            JSONObject data0=data.getJSONObject(0);
                            tovid.setText(data0.getString("ID"));
                            idtov=data0.getString("ID");
                            tovname.setText(data0.getString("NAME"));
                            tovprops_nomenkl.setText(data0.getString("NOMENKL"));
                            tovprops_kolupak.setText(data0.getString("KOL_UPAK"));
                            tovprops_section.setText(data0.getString("SECTION"));
                            tovves.setText(data0.getString("VES"));
                            tovrazmer.setText(data0.getString("RAZMER"));
                            Glide.with(v).clear(imageView_tov);

                            Glide.with(v)
                                    .load("https://svc.trianon-nsk.ru/tpic/preview.php?src=" + idtov).override(100,100).into(imageView_tov);



                            try
                            {
                                tovBOX_L.setText(data0.getString("BOX_L"));
                                tovBOX_W.setText(data0.getString("BOX_W"));
                                tovBOX_H.setText(data0.getString("BOX_H"));
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                            try
                            {
                                tovITEM_L.setText(data0.getString("ITEM_L"));
                                tovITEM_W.setText(data0.getString("ITEM_W"));
                                tovITEM_H.setText(data0.getString("ITEM_H"));
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // blankFragment1.setText("error" + error);
                tovBOX_L.setText(url + "That didn't work!" + error);
            }
        });
        // statusMessage_Properties.setText(url);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_tovprops, container, false);
        Context hostActivity = getActivity();
        tovid=(EditText) rootView.findViewById(R.id.tovprops_id);
        tovname=(EditText) rootView.findViewById(R.id.tovprops_name);
        tovprops_section=(TextView) rootView.findViewById(R.id.tovprops_section);
        tovprops_kolupak=(EditText) rootView.findViewById(R.id.tovprops_kolupak);
        tovves= (TextView) rootView.findViewById(R.id.tovprops_ves);
        tovrazmer= (TextView) rootView.findViewById(R.id.tovprops_razmer);
        tovBOX_L=(EditText)  rootView.findViewById(R.id.tovBOX_L);
        tovBOX_W=(EditText)  rootView.findViewById(R.id.tovBOX_W);
        tovBOX_H=(EditText)  rootView.findViewById(R.id.tovBOX_H);
        tovITEM_L=(EditText)  rootView.findViewById(R.id.tovITEM_L);
        tovITEM_W=(EditText)  rootView.findViewById(R.id.tovITEM_W);
        tovITEM_H=(EditText)  rootView.findViewById(R.id.tovITEM_H);
        tovprops_nomenkl=(EditText)  rootView.findViewById(R.id.tovprops_nomenkl);
        statusMessage_Properties=(TextView) rootView.findViewById(R.id.statusMessage_Properties);

        imageView_tov=(ImageView)rootView.findViewById(R.id.imageView_tov);

        imageView_tov.setOnClickListener(this);


        Glide.with(rootView)
                .load("https://svc.trianon-nsk.ru/tpic/preview.php?src=" + idtov).override(100,100).into(imageView_tov);

        button_fr3=(Button)rootView.findViewById(R.id.tovprops_button3);
        button_fr3.setOnClickListener(this);

        button_goto_invent=(Button)rootView.findViewById(R.id.tovprops_button1);
        button_goto_invent.setOnClickListener(this);
        button_goto_tovcard=(Button)rootView.findViewById(R.id.tovprops_button2);
        button_goto_tovcard.setOnClickListener(this);

        final AutoCompleteTextView AutoComplTV=(AutoCompleteTextView)rootView.findViewById(R.id.tovprops_name);


        AutoComplTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String s=AutoComplTV.getText().toString();
                String ss[]=s.split(":");
                AutoComplTV.setText(ss[0]);
                idtov=ss[1];
                tovid.setText(ss[1]);
                UpdateProperties(view);
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
                        StringBuffer result = MyHttp.Get(url,getActivity().getApplicationContext());
                        String[] items = result.toString().split(";");

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_item, items);
                        AutoComplTV.setThreshold(1);
                        AutoComplTV.setAdapter(adapter);
                        AutoComplTV.setBackgroundColor(Color.parseColor("#DAD871"));
                        AutoComplTV.setTextColor(Color.BLUE);
                    }
                });






        UpdateProperties(rootView);

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
    }
*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public void setIDTOV(String response)
    {
        idtov=response;
    }


    public void setBarcode(String response)
    {
        barcode=response;
    }
    

    @Override
    public void onClick(View v)
    {
        idtov=tovid.getText().toString();

        if(v.getId() == R.id.tovprops_button3)
        {
            UpdateProperties(v);
        }

        if(v.getId() == R.id.tovprops_button1)
        {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            FragmentInvent myFragment = new FragmentInvent();
            myFragment.setIdtov(idtov);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
        }

        if(v.getId() == R.id.tovprops_button2)
        {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            FragmentTovcard myFragment = new FragmentTovcard();
            myFragment.setIdTov(idtov);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
        }

        if(v.getId() == R.id.imageView_tov)
        {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            FragmentPhoto myFragment = new FragmentPhoto();
            myFragment.setIdTovsheet(idtov);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
        }

    }
}
