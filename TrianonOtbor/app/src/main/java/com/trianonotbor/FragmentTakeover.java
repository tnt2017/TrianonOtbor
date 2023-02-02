package com.trianonotbor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentTakeover.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentTakeover#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTakeover extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AutoCompleteTextView takeover_orgname,takeover_exped;
    EditText takeover_idorg,takeover_idadr, takeover_iddoc, takeover_palets, takeover_packages, takeover_prim, takeover_idexp, takeover_tovlist;
    Spinner spinner,spinner2;
    Button takeover_savebtn;
    SharedPreferences settings;
    String idemp;
    private static final String PREF_IDEMP = "idemp";


    String login = "ROSTOA$NSK20";
    String pass = "345544";

   private FragmentTakeover.OnFragmentInteractionListener mListener;

    public FragmentTakeover() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTakeover.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTakeover newInstance(String param1, String param2) {
        FragmentTakeover fragment = new FragmentTakeover();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_takeover, container, false);

        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        idemp = settings.getString(PREF_IDEMP,"");

        takeover_orgname = (AutoCompleteTextView) rootView.findViewById(R.id.takeover_orgname);
        takeover_exped = (AutoCompleteTextView) rootView.findViewById(R.id.takeover_exped);

        takeover_idorg = (EditText)  rootView.findViewById(R.id.takeover_idorg);
        takeover_idadr = (EditText)  rootView.findViewById(R.id.takeover_idadr);
        takeover_iddoc = (EditText)  rootView.findViewById(R.id.takeover_iddoc);
        takeover_idexp = (EditText)  rootView.findViewById(R.id.takeover_idexp);
        takeover_palets = (EditText)  rootView.findViewById(R.id.takeover_palets);
        takeover_packages = (EditText)  rootView.findViewById(R.id.takeover_packages);
        takeover_prim = (EditText)  rootView.findViewById(R.id.takeover_prim);

        takeover_tovlist= (EditText)  rootView.findViewById(R.id.takeover_tovlist);

        takeover_savebtn=(Button) rootView.findViewById(R.id.takeover_savebtn);

        final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner_adr);
        final Spinner spinner2 = (Spinner) rootView.findViewById(R.id.spinner_doc);

        takeover_savebtn.setOnClickListener(this);



        //settings = PreferenceManager.getDefaultSharedPreferences(getActivity());   ;

        //idemp = settings.getString(PREF_IDEMP,"");


        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);
                String ss[]=item.split(":");
                takeover_idadr.setText(ss[0]);

                String url2 = "https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=prh_list&login=" + login + "&pass=" + pass + "&idorg=" + takeover_idorg.getText();
                StringBuffer result2 = MyHttp.Get(url2, getContext());
                String[] adrs = result2.toString().split(";");

                // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                        R.layout.support_simple_spinner_dropdown_item, adrs);
                // Определяем разметку для использования при выборе элемента
                adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                // Применяем адаптер к элементу spinner
                spinner2.setLayoutMode(1); //.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                spinner2.setAdapter(adapter1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };


        AdapterView.OnItemSelectedListener itemSelectedListener2 = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String) parent.getItemAtPosition(position);
                String ss[] = item.split(":");
                takeover_iddoc.setText(ss[0]);

                String url = "https://svc.trianon-nsk.ru/clients/code_reader/index.php?login=ROSTOA$NSK20&pass=345544&p=get_rnk&iddoc=" + ss[0] + "&json=1";
                StringBuffer result = MyHttp.Get(url, getContext());

                try {
                    JSONObject userJson = new JSONObject(String.valueOf(result));
                    JSONArray data = (JSONArray) userJson.getJSONArray("data");

                    String out = "";

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject data0 = data.getJSONObject(i);
                        String CMC = data0.getString("CMC");
                        String TOV = data0.getString("NAME");
                        out += CMC + ":" + TOV +"\r\n";
                    }

                    takeover_tovlist.setText(out);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };



        spinner.setOnItemSelectedListener(itemSelectedListener);
        spinner2.setOnItemSelectedListener(itemSelectedListener2);



        takeover_orgname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String s=takeover_orgname.getText().toString();
                String ss[]=s.split(":");
                takeover_orgname.setText(ss[0]);
                takeover_idorg.setText(ss[1]);

                String login = "ROSTOA$NSK20";
                String pass = "345544";
                String[] countries = { "Все", "Адрес1", "Адрес2", "Адрес3"};
                String url2 = "https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=adr_list&login=" + login + "&pass=" + pass + "&idorg=" + takeover_idorg.getText();
                StringBuffer result2 = MyHttp.Get(url2, getContext());
                String[] adrs = result2.toString().split(";");

                // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                        R.layout.support_simple_spinner_dropdown_item, adrs);
                // Определяем разметку для использования при выборе элемента
                adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                // Применяем адаптер к элементу spinner
                spinner.setLayoutMode(1); //.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                spinner.setAdapter(adapter1);
            }
        });


        takeover_orgname.addTextChangedListener(
                new TextWatcher() {
                    private Object receiver;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        receiver = null;
                        ArrayList<String> items = new ArrayList<String>();

                        if(s.length()>1)
                        {
                            Cursor c = SqliteDB.GetOrgPatt(getContext(), s.toString());
                            int i = 0;

                            while (c.moveToNext())
                            {
                                String ID = c.getString(c.getColumnIndex("ID"));
                                String NAME = c.getString(c.getColumnIndex("NAME"));
                                items.add(NAME + ":" + ID);
                                i++;
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_item, items);
                            takeover_orgname.setThreshold(1);
                            takeover_orgname.setAdapter(adapter);
                            takeover_orgname.setBackgroundColor(Color.parseColor("#DAD871"));
                            takeover_orgname.setTextColor(Color.BLUE);
                        }                    }

                    @Override
                    public void afterTextChanged(Editable s)
                    {
                       /* String login = "ROSTOA$NSK20";
                        String pass = "345544";
                        String url = "https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=org_list&login=" + login + "&pass=" + pass + "&patt=" + s.toString();
                        StringBuffer result = MyHttp.Get(url, getContext());
                        String[] items = result.toString().split(";");

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_item, items);
                        takeover_orgname.setThreshold(1);
                        takeover_orgname.setAdapter(adapter);
                        takeover_orgname.setBackgroundColor(Color.parseColor("#DAD871"));
                        takeover_orgname.setTextColor(Color.BLUE);*/
                    }
                });


        takeover_exped.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String s=takeover_exped.getText().toString();
                String ss[]=s.split(":");
                takeover_exped.setText(ss[0]);
                takeover_idexp.setText(ss[1]);

                String login = "ROSTOA$NSK20";
                String pass = "345544";
                String[] countries = { "Все", "Адрес1", "Адрес2", "Адрес3"};
                String url2 = "https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=adr_list&login=" + login + "&pass=" + pass + "&idorg=" + takeover_idorg.getText();
                StringBuffer result2 = MyHttp.Get(url2, getContext());
                String[] adrs = result2.toString().split(";");

                // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                        R.layout.support_simple_spinner_dropdown_item, adrs);
                // Определяем разметку для использования при выборе элемента
                adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                // Применяем адаптер к элементу spinner
                spinner.setLayoutMode(1); //.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                spinner.setAdapter(adapter1);
            }
        });


        takeover_exped.addTextChangedListener(
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
                        String url = "https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=emp_list&login=" + login + "&pass=" + pass + "&patt=" + s.toString();
                        StringBuffer result = MyHttp.Get(url, getContext());
                        String[] items = result.toString().split(";");

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_item, items);
                        takeover_exped.setThreshold(1);
                        takeover_exped.setAdapter(adapter);
                        takeover_exped.setBackgroundColor(Color.parseColor("#DAD871"));
                        takeover_exped.setTextColor(Color.BLUE);
                    }
                });



        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
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
        final RequestQueue queue = Volley.newRequestQueue(getActivity());

        switch(v.getId()) {

            case R.id.takeover_savebtn: //  -- 26.08.20  input: msg='cadr^cexp^pkg^pall^idDoc^txt^'


                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                String msg=takeover_idadr.getText().toString() + '^' + takeover_idexp.getText().toString() + '^' +
                                        takeover_packages.getText().toString() + '^' + takeover_palets.getText().toString() + '^' +
                                        takeover_iddoc.getText().toString() + '^' + takeover_prim.getText().toString() + '^';

                                String url ="https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=save_takeover&login=" + login + "&pass=" + pass
                                        + "&idemp=" + idemp + "&msg=" + msg;

                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                // Display the first 500 characters of the response string.
                                                //status_message.setText(response); //"Response is: "+ .substring(0,500)
                                                Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getActivity().getApplicationContext(), "error=" + error, Toast.LENGTH_LONG).show();
                                    }
                                });
                                queue.add(stringRequest);

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };


                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Вы уверены?").setPositiveButton("Да", dialogClickListener)
                        .setNegativeButton("Нет", dialogClickListener).show();

                break;
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


}

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }
}
