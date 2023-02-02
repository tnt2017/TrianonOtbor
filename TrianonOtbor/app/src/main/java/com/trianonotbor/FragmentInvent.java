package com.trianonotbor;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInvent.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentInvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInvent extends Fragment implements View.OnClickListener, SoundPool.OnLoadCompleteListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SharedPreferences settings;


    String error_text="";

    private OnFragmentInteractionListener mListener;
    private String barcode;

    public FragmentInvent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentInvent.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentInvent newInstance(String param1, String param2) {
        FragmentInvent fragment = new FragmentInvent();
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

    String login = "ROSTOA$NSK20";
    String pass = "345544";
    String inv_login = "ROSTOA$NSK20";
    String inv_pass = "345544";
    String idemp;
    public String idtov;

    AutoCompleteTextView invent_tovname;
    TextView status_message;
    TextView barcodeValue;
    EditText invent_tovBarcode, invent_tovID, invent_tovsection, invent_tovkol;
    EditText invent_tovdtill, tovnmth;
    Button button_inv_readtovbyid, button_saveinv, button_srok, button_hist_section, button_save_section;
    Button invent_button1,invent_button2, button_zavesti_tovar, button_audit;


    Cursor SQLIte_gettov(String barcode)
    {

        SQLiteDatabase db = getContext().openOrCreateDatabase(getActivity().getExternalFilesDir(null)+ "/" + Utils.downloadDirectory + "/" + "test.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("select kmc.ID, kmc.NAME, ot.OST_FREE, ss.SECTION, ITEM_L, ITEM_W, ITEM_H, BOX_L, BOX_W, BOX_H, GROUP_FLAG, CPRIME," +
                "VID, kmc.FLAGS, PROC_NDS, VES, RAZMER, NOMENKL, NAME, KOL_UPAK, PACK, kmc.NSERT, PALLET_KG, PAL_LAYERS, PAL_LAY_BOX, " +
                "PAL_LAY_HT, ENLISTED, KOL_MIN, CROOT, KOL_BLISTER, EX_DATA, KOL_MAX, VALID_MTH, CMANUF, CFILESERT " +
                "from KATMC kmc, KATMC_BARCODE bc, SKLAD_SECTION ss, OST_TOV ot WHERE kmc.ID = bc.ID AND kmc.ID=ss.CMC " +
                "AND kmc.ID = ot.CMC AND bc.BARCODE = '" + barcode + "';", null);
        return query;
    }

    public void GetTovIdByBarcodeSQLITE(String barcode)
    {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        invent_tovID.setText("");
        invent_tovsection.setText("");
        invent_tovname.setText("");
        invent_tovkol.setText("");

        Cursor query=SQLIte_gettov(barcode);

        /****** 18-08-2021 ******/
        if (query.moveToFirst())
        {
            invent_tovID.setText(query.getString(0));
            invent_tovname.setText(query.getString(1));
            invent_tovkol.setText(query.getString(2));
            invent_tovsection.setText(query.getString(3));
            Toast.makeText(getActivity().getApplicationContext(), barcode, Toast.LENGTH_LONG).show();
        }
        sp.play(soundIdShot, 1, 1, 0, 0, 1);
        //invent_tovname.setText("нет штрихкода в базе");
      }





    public void GetTovIdByBarcode(String barcode)
    {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        invent_tovID.setText("");
        invent_tovsection.setText("");
        invent_tovname.setText("");
        invent_tovkol.setText("");

        final String url = "https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=get_tov&login=ROSTOA$NSK20&pass=345544&json=1&barcode=" + barcode;
        // "https://svc.trianon-nsk.ru/clients/main/pages/jrn_kk_svc.php?svc_id=get_tov&barcode=" // 26-07-2021

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject userJson = new JSONObject(response);
                            JSONArray data= (JSONArray) userJson.getJSONArray("data");
                            JSONObject data0=data.getJSONObject(0);
                            idtov=data0.getString("ID");
                            invent_tovID.setText(data0.getString("ID"));
                            invent_tovname.setText(data0.getString("NAME"));
                            invent_tovkol.setText(data0.getString("OST_FREE"));
                            invent_tovsection.setText(data0.getString("SECTION"));
                            sp.play(soundIdShot, 1, 1, 0, 0, 1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // blankFragment1.setText("error" + error);
                invent_tovname.setText("нет штрихкода в базе");
            }
        });
        // statusMessage_Properties.setText(url);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
     }


    final String LOG_TAG = "myLogs";
    final int MAX_STREAMS = 5;

    SoundPool sp;
    int soundIdShot;
    int soundIdExplosion;


    int streamIDShot;
    int streamIDExplosion;

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_invent, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());   ;

        idemp = settings.getString(Utils.PREF_IDEMP,"");
        inv_login = settings.getString(Utils.PREF_LOGIN,"ROSTOA$NSK20"); // 01-02-2021
        inv_pass = settings.getString(Utils.PREF_PASS,"345544");

        barcodeValue=(TextView)getActivity().findViewById(R.id.barcodeValue);
        invent_tovBarcode=(EditText)rootView.findViewById(R.id.invent_tovBarcode);
        invent_tovBarcode.setText(barcodeValue.getText()); //
        invent_tovID=(EditText) rootView.findViewById(R.id.invent_tovID);
        invent_tovID.setText(idtov);

        invent_tovsection=(EditText) rootView.findViewById(R.id.invent_tovsection);
        invent_tovname= (AutoCompleteTextView) rootView.findViewById(R.id.invent_tovname);
        invent_tovkol= (EditText) rootView.findViewById(R.id.invent_tovkol);

        invent_tovdtill=(EditText) rootView.findViewById(R.id.invent_tovdtill);
        status_message = (TextView) rootView.findViewById(R.id.statusMessage);

        tovnmth=(EditText) rootView.findViewById(R.id.tovnmth);
        button_inv_readtovbyid = (Button) rootView.findViewById(R.id.invent_readtovbyid);
        button_saveinv = (Button) rootView.findViewById(R.id.button_saveinv);
        button_srok= (Button) rootView.findViewById(R.id.button_srok);
        button_hist_section=(Button) rootView.findViewById(R.id.button_hist_section);
        button_save_section=(Button) rootView.findViewById(R.id.button_save_section);

        button_zavesti_tovar=(Button) rootView.findViewById(R.id.button_zavesti_tovar);
        button_audit=(Button) rootView.findViewById(R.id.button_audit);

        invent_button1= (Button) rootView.findViewById(R.id.invent_button1);
        invent_button2= (Button) rootView.findViewById(R.id.invent_button2);

        invent_button1.setOnClickListener(this);
        invent_button2.setOnClickListener(this);

        button_inv_readtovbyid.setOnClickListener(this);
        button_saveinv.setOnClickListener(this);
        button_hist_section.setOnClickListener(this);
        button_zavesti_tovar.setOnClickListener(this);
        button_audit.setOnClickListener(this);

        button_save_section.setOnClickListener(this);
        button_srok.setOnClickListener(this);

        //button_save_section.
        // Instantiate the RequestQueue.


        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);
        soundIdShot = sp.load(getContext(), R.raw.shot, 1);

        if(barcode!=null)
            GetTovIdByBarcodeSQLITE(barcode);
        else
            ClickReadTovByIDBtn();

        if(!error_text.equals(""))
        invent_tovname.setText(error_text);

        invent_tovname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String s=invent_tovname.getText().toString();
                String ss[]=s.split(":");
                invent_tovname.setText(ss[0]);
                invent_tovID.setText(ss[1]);
                RequestQueue queue = Volley.newRequestQueue(getActivity());

                String url ="https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=get_tov_by_id&login=" + login + "&pass=" + pass
                        + "&idtov=" + invent_tovID.getText();

                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                status_message.setText(response); //"Response is: "+ .substring(0,500)

                                String[] subStr;
                                subStr=response.split("<br>");
                                invent_tovBarcode.setText(subStr[0]); //"Response is: "+ .substring(0,500)
                                invent_tovkol.setText(subStr[6]);
                                invent_tovsection.setText(subStr[2]);
                                sp.play(soundIdShot, 1, 1, 0, 0, 1);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        status_message.setText("That didn't work!" + error);
                    }
                });

                queue.add(stringRequest2);
            }
        });



        invent_tovname.addTextChangedListener(
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
                        StringBuffer result = MyHttp.Get(url, getContext());
                        String[] items = result.toString().split(";");

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_item, items);
                        invent_tovname.setThreshold(1);
                        invent_tovname.setAdapter(adapter);
                        invent_tovname.setBackgroundColor(Color.parseColor("#DAD871"));
                        invent_tovname.setTextColor(Color.BLUE);
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

    void ClickReadTovByIDBtn()
    {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final String url = "https://svc.trianon-nsk.ru/clients/main/pages/jrn_kk_svc.php?svc_id=get_tov&idtov=" + idtov;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject userJson = new JSONObject(response);
                            JSONArray data= (JSONArray) userJson.getJSONArray("data");
                            JSONObject data0=data.getJSONObject(0);
                           // tovid.setText(data0.getString("ID"));
                            invent_tovname.setText(data0.getString("NAME"));
                            invent_tovsection.setText(data0.getString("SECTION"));
                            invent_tovkol.setText(data0.getString("OST_FREE"));
                            invent_tovBarcode.setText(data0.getString("BARCODE"));
                            sp.play(soundIdShot, 1, 1, 0, 0, 1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             }
        });
        // statusMessage_Properties.setText(url);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


    @Override
    public void onClick(View v)
    {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url;

            switch(v.getId()) {

                case R.id.invent_button1:
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    FragmentTovcard myFragment = new FragmentTovcard();
                    myFragment.setIdTov(invent_tovID.getText().toString());
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();

                    break;

                case R.id.invent_button2:
                    AppCompatActivity activity1 = (AppCompatActivity) v.getContext();
                    FragmentTovProps myFragment1 = new FragmentTovProps();
                    myFragment1.setIDTOV(invent_tovID.getText().toString());
                    activity1.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment1).addToBackStack(null).commit();

                    break;


                case R.id.invent_readtovbyid:
                    idtov=invent_tovID.getText().toString();
                    ClickReadTovByIDBtn();
                    break;

                case R.id.button_saveinv:
                    url ="https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=save_inv&login=" + inv_login + "&pass=" + inv_pass
                            + "&idemp=" + idemp
                            + "&idtov=" + invent_tovID.getText()
                            + "&kol=" + invent_tovkol.getText()
                            + "&section" + invent_tovsection.getText();


                    //status_message.setText(url);
                    //url ="https://tnt-nets.ru/code_reader/?id=" + barcode.displayValue;

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    status_message.setText(response); //"Response is: "+ .substring(0,500)
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            status_message.setText("That didn't work!" + error);
                        }
                    });

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                    break;


                case R.id.button_hist_section:
                    AppCompatActivity activity2 = (AppCompatActivity) v.getContext();
                    FragmentHistSection myFragment2 = new FragmentHistSection();
                    try {
                        myFragment2.setBarcode(invent_tovBarcode.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    activity2.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment2).addToBackStack(null).commit();
                    break;

                case R.id.button_zavesti_tovar:
                    AppCompatActivity activity3 = (AppCompatActivity) v.getContext();
                    FragmentBarcodeAssign myFragment3 = new FragmentBarcodeAssign();
                    myFragment3.setBarcode(invent_tovBarcode.getText().toString());
                    activity3.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment3).addToBackStack(null).commit();
                    break;

                case R.id.button_audit:
                    AppCompatActivity activity4 = (AppCompatActivity) v.getContext();
                    FragmentAudit myFragment4 = new FragmentAudit();
                    try {
                        myFragment4.setIdTov(invent_tovID.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    activity4.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment4).addToBackStack(null).commit();

                    break;



                case R.id.button_save_section:
                    String tovid=invent_tovID.getText().toString();
                    String tovsection=invent_tovsection.getText().toString();

                    SaveData.SaveSection(getActivity(), idemp, tovid, tovsection, inv_login, inv_pass, status_message);

                    break;

                case R.id.button_srok:
                    url ="https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=save_party_terms&login=" + inv_login + "&pass=" + inv_pass
                            + "&idemp=" + idemp
                            + "&idtov=" + idtov
                            + "&kol=" + tovnmth.getText()
                            + "&section=" + invent_tovsection.getText()
                            + "&dt=" + invent_tovdtill.getText();

                    //status_message.setText(url);
                    //url ="https://tnt-nets.ru/code_reader/?id=" + barcode.displayValue;

                    // Request a string response from the provided URL.
                    StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    status_message.setText(response); //"Response is: "+ .substring(0,500)
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            status_message.setText("That didn't work!" + error);
                        }
                    });

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest1);
                    break;
            }
    }

    public void setIdemp(String item)
    {
        idemp=item;
    }

    public void setIdtov(String item)
    {
        idtov=item;

        if(invent_tovID!=null) {
            invent_tovID.setText(idtov);
            //ClickReadTovByIDBtn();
        }
    }

    public void setBarcode(String response)
    {
        barcode=response;

        if(response.length()<13)
        {
            error_text = "штрихкод неверной длины";
        }

        if(invent_tovBarcode!=null)
        {
            //invent_tovBarcode.setText(response);
            //GetTovIdByBarcode(barcode);
        }
    }

    public void setTextResults(String response)
    {


    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

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
