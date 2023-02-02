package com.trianonotbor;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.SoundPool;
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

import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentTovWithOneBarcode.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentTovWithOneBarcode#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTovWithOneBarcode extends Fragment implements View.OnClickListener, SoundPool.OnLoadCompleteListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String barcode="7942361012205";
    EditText tovwithonebarcode_barcode, IdEmp;
    List<ItemTovsheet> items = new ArrayList<ItemTovsheet>();
    MyItemRecyclerViewAdapterTovWithOneBarcode adapter;
    RecyclerView mRecyclerView;
    Button tovwithonebarcode_button_read;
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

    public FragmentTovWithOneBarcode() {
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
    public static FragmentTovWithOneBarcode newInstance(String param1, String param2) {
        FragmentTovWithOneBarcode fragment = new FragmentTovWithOneBarcode();
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

    public void fillData(View rootView, String mybarcode) throws JSONException {
        login = settings.getString(PREF_LOGIN,""); // 01-02-2021
        pass = settings.getString(PREF_PASS,"");
        items = new ArrayList<ItemTovsheet>();
        int checkbox_count=0;
        Cursor query=SqliteDB.GetTovByBarcode(getActivity(), mybarcode);

        while (query.moveToNext())
        {
            String ID=query.getString(query.getColumnIndex("ID"));
            String NAME=query.getString(query.getColumnIndex("NAME"));
            String OST_FREE=query.getString(query.getColumnIndex("OST_FREE"));
            String SECTION=query.getString(query.getColumnIndex("SECTION"));
            String PRICE_B=query.getString(query.getColumnIndex("PRICE_B"));
            items.add(new ItemTovsheet(ID, NAME, SECTION, OST_FREE,"", "", "", PRICE_B + "р" , true));
        }

        int prev_checkbox_count = settings.getInt(PREF_CHECKBOX_COUNT,0);

        if(checkbox_count!=prev_checkbox_count)
        sp.play(soundIdShot, 1, 1, 0, 0, 1);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putInt(PREF_CHECKBOX_COUNT, checkbox_count);
        prefEditor.apply();
        adapter = new MyItemRecyclerViewAdapterTovWithOneBarcode(items,  mListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

    public void getName(View view) {
        // получаем сохраненное имя
        izone = settings.getString(PREF_IZONE,"");
        idemp = settings.getString(PREF_IDEMP,"");
        IdEmp.setText(idemp);
    }

    public void setBarcode(String text)
    {
        barcode=text;
        if(tovwithonebarcode_barcode!=null) {
            tovwithonebarcode_barcode.setText(text);
        }
    }

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_tovwithonebarcode, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        tovwithonebarcode_barcode=(EditText)rootView.findViewById(R.id.tovwithonebarcode_barcode);
        tovwithonebarcode_barcode.setText(barcode); /// вернул 30-06-2021
        tovwithonebarcode_button_read=(Button) rootView.findViewById(R.id.tovwithonebarcode_button_read);
        tovwithonebarcode_button_read.setOnClickListener(this);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.tovwithonebarcode_recyclerview);

        try {
            fillData(rootView, barcode);
        } catch (JSONException e) {
            e.printStackTrace();
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
                case R.id.tovwithonebarcode_button_read:
                    try {
                        fillData(v, tovwithonebarcode_barcode.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        sp.play(soundIdShot, 1, 1, 0, 0, 1);

    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
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
