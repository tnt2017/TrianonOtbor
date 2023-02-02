package com.trianonotbor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentTovsheet.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentTovsheet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTovsheet extends Fragment implements View.OnClickListener, SoundPool.OnLoadCompleteListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String IdSheet; // = "210010496";
    EditText IdSheetEditText, IdEmp;
    List<ItemTovsheet> items = new ArrayList<ItemTovsheet>();
    MyItemRecyclerViewAdapter adapter;
    RecyclerView mRecyclerView;
    Button button_newsheet,button2,button3,button_print,button_otbor,button_check,button_pack,button_collect;
    SharedPreferences settings;
    private static final String PREF_IZONE = "izone";
    private static final String PREF_IDEMP = "idemp";
    private static final String PREF_PRINTER = "printer";
    private static final String PREF_LOGIN= "login";
    private static final String PREF_PASS = "pass";

    String api_url="https://svc.trianon-nsk.ru/clients/code_reader/index.php";
    String izone, idemp, printer="http://192.168.1.117/print/?idsheet="; //http://10.8.0.25
    String login, pass;
    ArrayList local_arrlist=new ArrayList();

    SoundPool sp;
    int soundIdShot;
    int soundIdExplosion;
    final int MAX_STREAMS = 5;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    final String PREF_CURRENT_SHEET = "current_sheet";
    final String PREF_CHECKBOX_COUNT = "checkbox_count";

    private OnFragmentInteractionListener mListener;

    public FragmentTovsheet() {
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
    public static FragmentTovsheet newInstance(String param1, String param2) {
        FragmentTovsheet fragment = new FragmentTovsheet();
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


    public void fillData(View rootView, String IdSheet) throws JSONException {
        //items.add(new ItemTovsheet("1","2","3","4","5","6"));
        login = settings.getString(PREF_LOGIN,""); // 01-02-2021
        pass = settings.getString(PREF_PASS,"");
       // IdSheet=settings.getString(PREF_CURRENT_SHEET, "0000");


        String login = "ROSTOA$NSK20"; // 30-06-2021
        String pass = "345544";  // 30-06-2021

        String url =api_url +"?p=get_tovsheet&login=" + login + "&pass=" + pass + "&idtovsheet=" + IdSheet + "&json=1";
       // items.clear();
        items = new ArrayList<ItemTovsheet>();
        StringBuffer http_result = MyHttp.Get(url, getActivity().getApplicationContext());
        String response=http_result.toString();

        JSONObject userJson = new JSONObject(response);
        JSONArray data= (JSONArray) userJson.getJSONArray("data");
        int checkbox_count=0;

        for(int i=0;i<data.length();i++)
        {
            JSONObject data0=data.getJSONObject(i);
            String CMC=data0.getString("CMC");
            String NAME=data0.getString("NAME");
            String SECTION=data0.getString("SECTION");
            String KOL=data0.getString("KOL");
            String IDL=data0.getString("IDL");
            String BARCODES=data0.getString("BARCODES");
            String NOMENKL="";

            try {
                NOMENKL=data0.getString("NOMENKL");
            }
            catch (Exception ex)
            {

            }

            //int ISPICK=data0.getInt("ISPICK");
            boolean checked=false;

            if(local_arrlist!=null) {
                for (int j = 0; j < local_arrlist.size(); j++)
                {
                   if(BARCODES.indexOf((String)local_arrlist.get(j))>-1 || BARCODES.indexOf(ConvertEAN((String)local_arrlist.get(j)))>-1) //02-07-2021
                   {
                       checked = true;
                       checkbox_count++;
                   }
                }
            }

            String KOR=data0.getString("KOR");
            items.add(new ItemTovsheet(CMC, NAME, SECTION, KOL,IDL, NOMENKL, KOR, BARCODES, checked));
        }

        int prev_checkbox_count = settings.getInt(PREF_CHECKBOX_COUNT,0);

        if(checkbox_count!=prev_checkbox_count)
        sp.play(soundIdShot, 1, 1, 0, 0, 1);

        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putInt(PREF_CHECKBOX_COUNT, checkbox_count);
        prefEditor.apply();

        /*StringBuffer http_result = Get(url);
        String[] items2 = http_result.toString().split("<br>");

        for (int i = 0; i < items2.length; i++)
        {
            String[] words = items2[i].split(";");
            if(words.length>5)
            {
                boolean ispick=false;
                if(words[7].equals("1"))
                    ispick=true;
                else
                    ispick=false;

                items.add(new ItemTovsheet(words[0], words[1], words[2], words[3], words[4], words[5], words[6], ispick));
            }
            else
                items.add(new ItemTovsheet(words[0], words[1], words[2], words[3], words[4], words[5], "", false));
        }*/

        adapter = new MyItemRecyclerViewAdapter(items,  mListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

    }

    public void getName(View view) {
        // получаем сохраненное имя
        izone = settings.getString(PREF_IZONE,"");
        idemp = settings.getString(PREF_IDEMP,"");
       // printer = settings.getString(PREF_PRINTER,"");
        IdEmp.setText(idemp);
       // Izone.setText(name);
    }

    public void setBarcode(String text) {
       /* IdDoc=text;*/
    }



    public void setBarcodesList(ArrayList l) {
        local_arrlist = l;

        //String ids="9999"; settings.getString(PREF_CURRENT_SHEET, "3456");


        if (tovar_barcodes != null) {
            String temp = local_arrlist.toString();
            temp = temp.replaceAll(",", "\r\n");
            tovar_barcodes.setText(temp);

            try {
                fillData(rootView, IdSheetEditText.getText().toString()); // 01-07-2021
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(mRecyclerView!=null)
        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());

    }


    EditText EditTextPlaces;
    EditText tovar_barcodes;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_tovsheet, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());

        IdSheetEditText=(EditText)rootView.findViewById(R.id.IdSheet);
        IdSheetEditText.setText(IdSheet); /// вернул 30-06-2021

        EditTextPlaces=(EditText)rootView.findViewById(R.id.EditTextPlaces);
        IdEmp=(EditText)rootView.findViewById(R.id.IdEmp);
        button_newsheet=(Button)rootView.findViewById(R.id.button_newsheet);
        button2=(Button)rootView.findViewById(R.id.tovcard_button2);
        button3=(Button)rootView.findViewById(R.id.button3);

        button_newsheet.setOnClickListener(this);
        button_print=(Button)rootView.findViewById(R.id.button_print);
        button_otbor=(Button)rootView.findViewById(R.id.button_otbor);
        button_pack=(Button)rootView.findViewById(R.id.button_pack);
        button_check=(Button)rootView.findViewById(R.id.button_check);
        button_collect=(Button)rootView.findViewById(R.id.button_collect);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

        button_print.setOnClickListener(this);
        button_otbor.setOnClickListener(this);
        button_check.setOnClickListener(this);
        button_pack.setOnClickListener(this);
        button_collect.setOnClickListener(this);

        tovar_barcodes=(EditText) rootView.findViewById(R.id.tovsheet_barcodes);
        tovar_barcodes.setText(local_arrlist.toString());

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerviewtest1);

        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);
        soundIdShot = sp.load(getContext(), R.raw.shot, 1);
        //IdSheet=settings.getString(PREF_CURRENT_SHEET, "0000");

        try {
            fillData(rootView, IdSheet);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        getName(rootView);
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
    public void onClick(View v) {
        login = settings.getString(PREF_LOGIN,""); // 01-02-2021
        pass = settings.getString(PREF_PASS,"");
        String url;
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date();

            switch(v.getId()) {
                case R.id.button_newsheet:
                    url ="https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=get_new_tovsheet&login=" + login + "&pass=" + pass + "&idemp=" + idemp + "&izone=" + izone;
                    String res=MyHttp.Get(url, getActivity().getApplicationContext()).toString();
                    Toast.makeText(getActivity().getApplicationContext(), res, Toast.LENGTH_LONG).show();
                    IdSheetEditText.setText(res);
                    break;

                case R.id.tovcard_button2:
                    try {
                        fillData(v, IdSheetEditText.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.button3:
                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    url =api_url + "?p=get_tovsheet_header&login=" + login + "&pass=" + pass + "&idtovsheet=" + IdSheetEditText.getText();
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response)
                                {
                                    String result = "Отборочник :";
                                    String[] subStr;
                                    subStr=response.split(";");
                                    for(int i=0;i<subStr.length;i++)
                                    {
                                        result += "\n" + subStr[i];
                                    }
                                    Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG).show();

                                    //idemp.setText(response);
                                    //fragmentInvent.setText(response); //"Response is: "+ .substring(0,500)
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    queue.add(stringRequest);
                    break;



                case R.id.button_print:
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(printer + IdSheetEditText.getText() )); //IdSheet.getText()
                    startActivity(browserIntent);
                    break;


                case R.id.button_otbor: // отборка флаг 2
                    System.out.println(dateFormat.format(date));
                    url =api_url + "?p=save_tovsheet_header&login=" + login + "&pass=" + pass + "&idemp=" + idemp + "&izone=" + izone + "&idsheet=" + IdSheetEditText.getText()
                                 + "&iflg=2&npkgkg=" + EditTextPlaces.getText();

                    String save_result2=MyHttp.Get(url,  getActivity().getApplicationContext()).toString();
                    Toast.makeText(getActivity().getApplicationContext(),  "Результат отбора: \r\n" + save_result2, Toast.LENGTH_LONG).show();
                    break;

                case R.id.button_check:

                    System.out.println(dateFormat.format(date));
                    String hdr = IdSheetEditText.getText() + "^" + dateFormat.format(date) + "^" + IdEmp.getText() + "^2^"; //hdr='idSh^dcurr^cemp^flg^'
                    String lns="";

                    //'idL^kol^afx^inv^sec^dt^chktip^dtil^

                    for (ItemTovsheet p : adapter.getBox()) {
                        lns += p.tovIDL + "^" + p.tovKol + "^^^^06.07.2021 13:23:00^M^^\n";
                    }

                    url =api_url + "?p=save_tovsheet&login=" + login + "&pass=" + pass + "&idemp=" + idemp + "&izone=" + izone +
                            "&idsheet=" + IdSheetEditText.getText()  + "&iflg=16" +
                            "&npkgkg=" + EditTextPlaces.getText() + "&hdr=" + hdr + "&lns=" + lns;

                    String save_result=MyHttp.Get(url,  getActivity().getApplicationContext()).toString();

                    Toast.makeText(getActivity().getApplicationContext(), "Товары в корзине:\n" + hdr + "\n" + lns + "\n" + save_result, Toast.LENGTH_LONG).show();
                    break;

                case R.id.button_pack: // упаковка флаг 1
                    System.out.println(dateFormat.format(date));
                    url =api_url + "?p=save_tovsheet_header&login=" + login + "&pass=" + pass + "&idemp=" + idemp + "&izone=" + izone + "&idsheet=" + IdSheetEditText.getText()
                            + "&iflg=1&npkgkg=" + EditTextPlaces.getText();

                    String save_result3=MyHttp.Get(url,  getActivity().getApplicationContext()).toString();
                    Toast.makeText(getActivity().getApplicationContext(),  "Результат упаковки: \r\n" + save_result3, Toast.LENGTH_LONG).show();
                    break;

                case R.id.button_collect:
                    String sdata=IdSheetEditText.getText().toString() + "^" + EditTextPlaces.getText() + "^";
                    url =api_url + "?p=save_collect&login=" + login + "&pass=" + pass + "&sdata=" + sdata + "&msg=" + idemp;
                    String save_result4=MyHttp.Get(url,  getActivity().getApplicationContext()).toString();
                    Toast.makeText(getActivity().getApplicationContext(), "Комплектовка:\n" + sdata + "\n" + save_result4, Toast.LENGTH_LONG).show();

                    if(save_result4.indexOf("сохранено")>0)
                    {

                    }

                    break;


                    
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }


    }

    public void setIdTovsheet(String text) {
        IdSheet=text;

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
