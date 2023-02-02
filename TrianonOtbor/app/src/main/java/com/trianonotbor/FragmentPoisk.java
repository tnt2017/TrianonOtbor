package com.trianonotbor;

import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

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
 * {@link FragmentPoisk.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentPoisk#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPoisk extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText  poisk_dbeg, poisk_dend;
    Button btn_poisk_new;
    ImageButton btn_poisk_apply;
    List<ItemPoisk> items = new ArrayList<ItemPoisk>();
    MyItemRecyclerViewAdapterPoisk adapter;
    RecyclerView mRecyclerView;
    SharedPreferences settings;
    private static final String PREF_IZONE = "izone";
    private static final String PREF_IDEMP = "idemp";
    private static final String PREF_PRINTER = "printer";
    String api_url="https://svc.trianon-nsk.ru/clients/code_reader/index.php";
    String izone, idemp, printer="http://10.8.0.25/print/?idsheet=";

    EditText finddoc_selection;
    EditText finddoc_tovar_barcodes, finddoc_idorg, finddoc_idadr, finddoc_idtov;
    AutoCompleteTextView finddoc_org;
    AutoCompleteTextView finddoc_tov;
    Spinner spinner;
    ArrayAdapter<String> adapter1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public FragmentPoisk() {
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
    public static FragmentPoisk newInstance(String param1, String param2) {
        FragmentPoisk fragment = new FragmentPoisk();
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

        String[] countries = {"Приход", "Расход", "ВПК", "ВПС", "БНК", "БНП"};

        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        adapter1 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, countries);
        // Определяем разметку для использования при выборе элемента
        adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner

    }


    public void fillData(View rootView, String param) throws JSONException {
        String login = "ROSTOA$NSK20";
        String pass = "345544";
        String url =api_url +"?p=" + param + "&login=" + login + "&pass=" + pass +
                "&dbeg=" + poisk_dbeg.getText() + "&dend=" + poisk_dend.getText() +
                "&idorg=" + finddoc_idorg.getText() +
                "&cmc=" + finddoc_idtov.getText() +
                "&tip=" + finddoc_selection.getText() +
                "&json=1";
       // items.clear();
        items = new ArrayList<ItemPoisk>();
        StringBuffer http_result = MyHttp.Get(url, getActivity().getApplicationContext());
        String response=http_result.toString();

        JSONObject userJson = new JSONObject(response);
        JSONArray data= (JSONArray) userJson.getJSONArray("data");

        for(int i=0;i<data.length();i++) {
            String KOR = "", ID = "", NNAKL = "";
            JSONObject data0 = data.getJSONObject(i);

            try {
                NNAKL = data0.getString("NNAKL");
            }
            catch (Exception ex)
            {

            }


            String ORG=data0.getString("ID");
            String DN=data0.getString("DNAKL");
            String CHECKER=data0.getString("SUMMA");
            String REM_D="0";//data0.getString("SUMM");

            try {
                REM_D=data0.getString("REM_D");
                //KOR=data0.getString("");

            }
            catch (Exception ex)
            {

            }

            items.add(new ItemPoisk(ID, NNAKL, ORG, REM_D, KOR, CHECKER, DN,  ""));
        }






        adapter = new MyItemRecyclerViewAdapterPoisk(items, mListener);
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

        final View rootView=inflater.inflate(R.layout.fragment_poisk, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_tovsheets);
        poisk_dbeg=(EditText)rootView.findViewById(R.id.poisk_dbeg);
        poisk_dend=(EditText)rootView.findViewById(R.id.poisk_dend);

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date d = new Date();//intialize your date to any date
        Date dbeg = new Date(d.getTime());
        poisk_dbeg.setText(dateFormat.format(dbeg));
        Date dend = new Date(d.getTime() + 1 * 24 * 3600 * 1000l); //+1 days
        poisk_dend.setText(dateFormat.format(dend));

        btn_poisk_apply=(ImageButton) rootView.findViewById(R.id.btn_poisk_apply);
        btn_poisk_apply.setOnClickListener(this);

        btn_poisk_new=(Button) rootView.findViewById(R.id.btn_poisk_new);
        btn_poisk_new.setOnClickListener(this);

        getName(rootView);

        try
        {
            fillData(rootView, "find_docs"); // было list_nakl 20-04-2021
        }
        catch (Exception ex)
        {
        }


        finddoc_org = (AutoCompleteTextView) rootView.findViewById(R.id.finddoc_org);
        finddoc_tov = (AutoCompleteTextView) rootView.findViewById(R.id.finddoc_tov);
        finddoc_idorg = (EditText) rootView.findViewById(R.id.finddoc_idorg);
        finddoc_idadr = (EditText) rootView.findViewById(R.id.finddoc_idadr);
        finddoc_idtov = (EditText) rootView.findViewById(R.id.finddoc_idtov);
        finddoc_selection=(EditText) rootView.findViewById(R.id.finddoc_selection);
        //  finddoc_add_line = (Button)rootView.findViewById(R.id.finddoc_add_line);
        //  finddoc_add_line.setOnClickListener(this);

        //finddoc_savedoc = (Button)rootView.findViewById(R.id.finddoc_savedoc);
        //  finddoc_savedoc.setOnClickListener(this);

        finddoc_tov.setOnClickListener(this);
        finddoc_org.setOnClickListener(this);
        finddoc_idorg.setOnClickListener(this);
        finddoc_idtov.setOnClickListener(this);

        finddoc_tov.setSelectAllOnFocus(true);
        finddoc_org.setSelectAllOnFocus(true);
        finddoc_idorg.setSelectAllOnFocus(true);
        finddoc_idtov.setSelectAllOnFocus(true);

        finddoc_org.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String s= finddoc_org.getText().toString();
                String ss[]=s.split(":");
                finddoc_org.setText(ss[0]);
                finddoc_idorg.setText(ss[1]);
            }
        });

        finddoc_tov.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String s= finddoc_tov.getText().toString();
                String ss[]=s.split(":");
                finddoc_tov.setText(ss[0]);
                finddoc_idtov.setText(ss[1]);
            }
        });


        finddoc_org.addTextChangedListener(
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
                            finddoc_org.setThreshold(1);
                            finddoc_org.setAdapter(adapter);
                            finddoc_org.setBackgroundColor(Color.parseColor("#DAD871"));
                            finddoc_org.setTextColor(Color.BLUE);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s)
                    {

                    }
                });

        finddoc_tov.addTextChangedListener(
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
                            Cursor c = SqliteDB.GetTovPatt(getContext(), s.toString());
                            int i = 0;

                            while (c.moveToNext())
                            {
                                String ID = c.getString(c.getColumnIndex("ID"));
                                String NAME = c.getString(c.getColumnIndex("NAME"));
                                items.add(NAME + ":" + ID);
                                i++;
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_item, items);
                            finddoc_tov.setThreshold(1);
                            finddoc_tov.setAdapter(adapter);
                            finddoc_tov.setBackgroundColor(Color.parseColor("#DAD871"));
                            finddoc_tov.setTextColor(Color.BLUE);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s)
                    {

                    }
                });

        spinner = (Spinner) rootView.findViewById(R.id.finddoc_spinner);
        spinner.setLayoutMode(1); //.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        spinner.setAdapter(adapter1);


        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                String item = Integer.toString(position+1); //(String)parent.getItemAtPosition(position);
                finddoc_selection.setText(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);



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
            case R.id.btn_poisk_new:
                try
                {
                    fillData(v, "find_docs"); // было list_nakl 20-04-2021
                }
                catch (JSONException e)
                {
                    Toast.makeText(getActivity().getApplicationContext(),  e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.finddoc_tov:
            {
                finddoc_tov.selectAll();
                break;
            }
            case R.id.finddoc_org:
            {
                finddoc_org.selectAll();
                break;
            }
            case R.id.finddoc_idorg:
            {
                finddoc_idorg.selectAll();
                break;
            }
            case R.id.finddoc_idtov:
            {
                finddoc_idtov.selectAll();
                break;
            }
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
