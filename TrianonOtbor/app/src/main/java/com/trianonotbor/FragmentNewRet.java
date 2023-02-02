package com.trianonotbor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentNewRet.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentNewRet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNewRet extends Fragment implements View.OnClickListener, SoundPool.OnLoadCompleteListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String section; // = "210010496";
    EditText textbox_section, IdEmp;
    EditText section_textView_prim;
    Button newret_refresh, newret_saveret;
    SharedPreferences settings;
    private static final String PREF_IZONE = "izone";
    private static final String PREF_IDEMP = "idemp";
    private static final String PREF_PRINTER = "printer";
    private static final String PREF_LOGIN= "login";
    private static final String PREF_PASS = "pass";
    Spinner newret_spinner_adr;

    String api_url="https://svc.trianon-nsk.ru/clients/code_reader/index.php";
    String izone, idemp, printer="http://192.168.1.117/print/?idsheet="; //http://10.8.0.25
    String login, pass;
    ArrayList local_arrlist=new ArrayList();

    SoundPool sp;
    int soundIdShot;
    int soundIdExplosion;
    final int MAX_STREAMS = 5;

    List<ItemDocument> items = new ArrayList<ItemDocument>();
    MyItemRecyclerViewAdapterNewRet adapter;
    RecyclerView mRecyclerView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    final String PREF_CURRENT_SHEET = "current_sheet";
    final String PREF_CHECKBOX_COUNT = "checkbox_count";

    private OnFragmentInteractionListener mListener;

    public FragmentNewRet() {
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
    public static FragmentNewRet newInstance(String param1, String param2) {
        FragmentNewRet fragment = new FragmentNewRet();
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


    String ConvertEAN(String barcode)
    {
        barcode=barcode.substring(1,13);

        int t1=0,t2=0;
        for(int i=0;i<12;i++)
        {
            char c=barcode.charAt(i);
            int a=Integer.parseInt(String.valueOf(c));
            if(i%2==0)
                t1+=a;
            else
                t2+=a;

        }

        int summa=t1+t2*3;
        String str_summa=String.valueOf(summa);
        char last_sym=str_summa.charAt(str_summa.length()-1);
        int res=10-Integer.parseInt(String.valueOf(last_sym));
        String ret=barcode + String.valueOf(res);
       // Toast.makeText(this, ret, Toast.LENGTH_SHORT).show();

        return ret;
    }



    public void setSection(String text)
    {
        section=text;

        if(textbox_section!=null)
        {
            textbox_section.setText(section);
        }

    }

    public void setBarcodesList(ArrayList l) {
        local_arrlist = l;
        //String ids="9999"; settings.getString(PREF_CURRENT_SHEET, "3456");

        if (newret_tovar_barcodes != null)
        {
            String temp = local_arrlist.toString();
            temp = temp.replaceAll(",", "\r\n");
            newret_tovar_barcodes.setText(temp);
        }
    }


    EditText section_status_msg;
    EditText newret_tovar_barcodes,  newret_idorg, newret_idadr, newret_idtov;
    AutoCompleteTextView newret_org;
    AutoCompleteTextView newret_tov;


    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_newret, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        izone = settings.getString(PREF_IZONE,"");
        idemp = settings.getString(PREF_IDEMP,"");

        newret_tovar_barcodes = (EditText) rootView.findViewById(R.id.newret_tovar_barcodes);
        newret_org = (AutoCompleteTextView) rootView.findViewById(R.id.newret_org);
        newret_idorg = (EditText) rootView.findViewById(R.id.newret_idorg);
        newret_idadr = (EditText) rootView.findViewById(R.id.newret_idadr);
        newret_spinner_adr = (Spinner) rootView.findViewById(R.id.newret_spinner_adr);
        newret_refresh = (Button)rootView.findViewById(R.id.newret_refresh);
        newret_refresh.setOnClickListener(this);
        newret_saveret = (Button)rootView.findViewById(R.id.newret_saveret);
        newret_saveret.setOnClickListener(this);

        mRecyclerView=(RecyclerView)rootView.findViewById(R.id.newret_recyclerview);


        try {
            fillData(rootView, "1900036", "1900066");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //newret_tov.setOnClickListener(this);
        //newret_org.setOnClickListener(this);

        newret_org.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String s=newret_org.getText().toString();
                String ss[]=s.split(":");
                newret_org.setText(ss[0]);
                newret_idorg.setText(ss[1]);
                String login = "ROSTOA$NSK20";
                String pass = "345544";
                String[] countries = { "Все", "Адрес1", "Адрес2", "Адрес3"};
                String url2 = "https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=adr_list&login=" + login + "&pass=" + pass + "&idorg=" + newret_idorg.getText();
                StringBuffer result2 = MyHttp.Get(url2, getContext());
                String[] adrs = result2.toString().split(";");

                // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                        R.layout.support_simple_spinner_dropdown_item, adrs);
                // Определяем разметку для использования при выборе элемента
                adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                // Применяем адаптер к элементу spinner
                newret_spinner_adr.setLayoutMode(1); //.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                newret_spinner_adr.setAdapter(adapter1);

            }
        });



        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);
                String ss[]=item.split(":");
                newret_idadr.setText(ss[0]);

               /* String url2 = "https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=prh_list&login=" + login + "&pass=" + pass + "&idorg=" + takeover_idorg.getText();
                StringBuffer result2 = MyHttp.Get(url2, getContext());
                String[] adrs = result2.toString().split(";");

                // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                        R.layout.support_simple_spinner_dropdown_item, adrs);
                // Определяем разметку для использования при выборе элемента
                adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                // Применяем адаптер к элементу spinner
                spinner2.setLayoutMode(1); //.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                spinner2.setAdapter(adapter1);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        newret_spinner_adr.setOnItemSelectedListener(itemSelectedListener);












        /*newret_tov.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String s=newret_tov.getText().toString();
                String ss[]=s.split(":");
                newret_tov.setText(ss[0]);
                newret_idtov.setText(ss[1]);
            }
        });*/


        newret_org.addTextChangedListener(
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
                            newret_org.setThreshold(1);
                            newret_org.setAdapter(adapter);
                            newret_org.setBackgroundColor(Color.parseColor("#DAD871"));
                            newret_org.setTextColor(Color.BLUE);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s)
                    {

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    Uri outputFileUri;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static int TAKE_PICTURE_REQUEST = 1;

    private void saveFullImage(String fname)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file=new File(Environment.getExternalStorageDirectory(), "p" + fname + "-0.jpg");
        FileProvider.getUriForFile(getContext(),"com.example.trianonotbor.provider", file);
        outputFileUri = Uri.fromFile(file);
        section_textView_prim.setText(file.toString()); //outputFileUri.toString()
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, TAKE_PICTURE_REQUEST);
    }


    public void fillData(View rootView, String idorg, String idadr) throws JSONException {
        String login = "ROSTOA$NSK20";
        String pass = "345544";
        String url = api_url + "?p=get_list_ret_tovs&login=" + login + "&pass=" + pass + "&idorg=" + idorg + "&idadr=" + idadr + "&json=1";
        items = new ArrayList<ItemDocument>();

        StringBuffer http_result = MyHttp.Get(url, getContext());
        String response = http_result.toString();

        JSONObject userJson = new JSONObject(response);
        JSONArray data = (JSONArray) userJson.getJSONArray("data");

        for (int i = 0; i < data.length(); i++) {
            JSONObject data0 = data.getJSONObject(i);
            String CMC = data0.getString("ID");
            String NAME = data0.getString("NAME");
            String KOL = data0.getString("SOLD");
            String PRICE = data0.getString("PRICE");
            String TIPSOPR = "3"; //data0.getString("TIPSOPR");
            items.add(new ItemDocument(CMC, NAME, KOL, PRICE, TIPSOPR, ""));
        }

        adapter = new MyItemRecyclerViewAdapterNewRet(items, mListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }


    @Override
    public void onClick(View v) {
        /*login = settings.getString(PREF_LOGIN,""); // 01-02-2021
        pass = settings.getString(PREF_PASS,"");
        String url;
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date();
        section = textbox_section.getText().toString();*/



        if(v.getId()==R.id.newret_refresh)
        {
            try {
                fillData(rootView, String.valueOf(newret_idorg.getText()), String.valueOf(newret_idadr.getText()));
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }

        if(v.getId()==R.id.newret_saveret)
        {
             Toast.makeText(getActivity().getApplicationContext(), "response", Toast.LENGTH_LONG).show();
        }

        if(v.getId()==R.id.newret_org)
        {
            newret_org.selectAll();
        }
    }


    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        sp.play(soundIdShot, 1, 1, 0, 0, 1);

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
