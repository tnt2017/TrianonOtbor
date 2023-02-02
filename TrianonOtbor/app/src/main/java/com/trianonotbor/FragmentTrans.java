package com.trianonotbor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentTrans.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentTrans#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTrans extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String IdSheet = "210006206";
    EditText trans_idsheet, trans_barcodes ;
    ArrayList<ItemTrans> items = new ArrayList<ItemTrans>();
    View rootView;

   // items = new ArrayList<ItemDocumentPrihod>();

    MyItemRecyclerViewAdapterTrans  adapter;
    RecyclerView mRecyclerView;
    Button trans_getlist_btn;
    ImageButton trans_save;

    SharedPreferences settings;
    private static final String PREF_IZONE = "izone";
    private static final String PREF_IDEMP = "idemp";
    private static final String PREF_PRINTER = "printer";

    String api_url = "https://svc.trianon-nsk.ru/clients/code_reader/index.php";
    String izone, idemp, printer = "http://10.8.0.25/print/?idsheet=";
    ArrayList local_arrlist=new ArrayList();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentTrans() {
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
    public static FragmentTrans newInstance(String param1, String param2) {
        FragmentTrans fragment = new FragmentTrans();
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

    public void fillData(View rootView, String idsheets) throws JSONException {
        String login = "ROSTOA$NSK20";
        String pass = "345544";
        String url = api_url + "?p=get_sheets&login=" + login + "&pass=" + pass + "&idsheets=" + idsheets + "&json=1";

        items.clear();
        StringBuffer http_result = MyHttp.Get(url, getContext());
        String response = http_result.toString();

        JSONObject userJson = new JSONObject(response);
        JSONArray data = (JSONArray) userJson.getJSONArray("data");

        for (int i = 0; i < data.length(); i++) {
            JSONObject data0 = data.getJSONObject(i);

            String IDTOVSHEET="",PLACES="", SHEET_INFO="", SECTOR="", IS_COLL="", ORG="";
            try {
                 IDTOVSHEET = data0.getString("ID");
                 PLACES = data0.getString("PLACES");
                 SHEET_INFO = data0.getString("KG_T") + "кг " + data0.getString("M3_T") + "м3";
                 SECTOR = data0.getString("SKL_ZONE");
                 ORG = data0.getString("ORG");
                 IS_COLL = data0.getString("IS_COLL");
            }//
            catch (Exception ex)
            {


            }

            boolean b=false;
            if(IS_COLL.equals("1"))
                b=true;

            if(local_arrlist!=null) {
                for (int j = 0; j < local_arrlist.size(); j++) {
                    if (IDTOVSHEET.equals(local_arrlist.get(j)))
                        b = true;
                }
            }

            items.add(new ItemTrans(IDTOVSHEET, PLACES, SECTOR , SHEET_INFO, ORG, b));
        }

        adapter = new MyItemRecyclerViewAdapterTrans(items, mListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_trans, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        idemp = settings.getString(PREF_IDEMP,"");

        trans_getlist_btn = (Button) rootView.findViewById(R.id.trans_getlist_btn);
        trans_save = (ImageButton) rootView.findViewById(R.id.trans_save);
        trans_getlist_btn.setOnClickListener(this);
        trans_save.setOnClickListener(this);
        trans_barcodes=(EditText) rootView.findViewById(R.id.trans_barcodes);
        String temp=local_arrlist.toString();
        temp=temp.replace("[","");
        temp=temp.replace("]","");
        trans_barcodes.setText(temp);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_trans);

        try
        {
            fillData(rootView, temp);
        }
        catch (JSONException e) {
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
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date();
        final String login="ROSTOA$NSK20";
        final String pass="345544";

        switch(v.getId())
         {
               case R.id.trans_getlist_btn:

                   try
                   {
                       fillData(v, trans_barcodes.getText().toString());
                   }
                   catch (JSONException e) {
                       e.printStackTrace();
                   }
                   break;


               case R.id.trans_save:

                   DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           switch (which){
                               case DialogInterface.BUTTON_POSITIVE:
                                   String sdata="";

                                   //if(adapter!=null)////////// 30-04-2021 вернуть обратно
                                   //{
                                       for (ItemTrans p : adapter.getBox()) {
                                           sdata += p.tovSheet + "^" + p.tovKol + "^"; //-
                                       }
                                   //}

                                   String url =api_url + "?p=save_collect&login=" + login + "&pass=" + pass + "&sdata=" + sdata + "&msg=" + idemp;
                                   String save_result=MyHttp.Get(url,  getActivity().getApplicationContext()).toString();
                                   Toast.makeText(getActivity().getApplicationContext(), "Комплектовка:\n" + sdata + "\n" + save_result, Toast.LENGTH_LONG).show();

                                   if(save_result.indexOf("сохранено")>0)
                                   {
                                       local_arrlist.clear();
                                       items.clear();
                                       trans_barcodes.setText("");
                                       adapter = new MyItemRecyclerViewAdapterTrans(items, mListener);
                                       mRecyclerView.setAdapter(adapter);
                                       mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                                   }

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

         }
    }

    public void setIdDoc(String text) {
        IdSheet=text;
    }

    public void setBarcode(String text) {
        IdSheet=text;
    }

    public void setBarcodesList(ArrayList l) {
        local_arrlist=l;

        if(trans_barcodes!=null)
        {
            String temp=local_arrlist.toString();
            temp=temp.replace("[","");
            temp=temp.replace("]","");

            if(trans_idsheet!=null)
                trans_idsheet.setText(IdSheet);

            trans_barcodes.setText(temp);

            try {
                fillData(rootView, temp);
            } catch (JSONException e) {
                e.printStackTrace();
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
