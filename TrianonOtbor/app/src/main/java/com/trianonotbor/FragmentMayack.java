package com.trianonotbor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.util.ArrayList;

//import org.apache.http.HttpRequest;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMayack.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMayack#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMayack extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView textView_Latitude, textView_Longitude, textView_client, mayack_idorg, textView_idadr;
    EditText mayack_prim;
    EditText mayak_summa;
    AutoCompleteTextView mayak_org;
    Button button_newclient;
    ArrayAdapter<String> adapter;
    Spinner spinner;
    ArrayAdapter<String> adapter1;
    private ImageView imageView;
    String local_latt, local_lngt;

    private OnFragmentInteractionListener mListener;

    public FragmentMayack() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMayack.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMayack newInstance(String param1, String param2) {
        FragmentMayack fragment = new FragmentMayack();
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

        String[] countries = {"Обзвон", "замена (дубликат)", "печать доверенности", "Печать прайса", "Печать прайса с картинками", "печать ценников", "печать договоров"};

        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        adapter1 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, countries);
        // Определяем разметку для использования при выборе элемента
        adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
    }

    public void SetCoords(String latt, String lngt)
    {
        local_latt=latt;
        local_lngt=lngt;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_mayack, container, false);
        textView_Latitude = (TextView) rootView.findViewById(R.id.textView_Latitude);
        textView_Longitude = (TextView) rootView.findViewById(R.id.textView_Longitude);

        textView_Latitude.setText(local_latt);
        textView_Longitude.setText(local_lngt);

        textView_client = (TextView) rootView.findViewById(R.id.textView_client);
        mayack_idorg = (TextView) rootView.findViewById(R.id.mayack_idorg);
        textView_idadr = (TextView) rootView.findViewById(R.id.textView_idadr);
        mayack_prim = (EditText) rootView.findViewById(R.id.mayack_prim);
        mayak_summa = (EditText) rootView.findViewById(R.id.mayack_summa);
        mayak_org = (AutoCompleteTextView) rootView.findViewById(R.id.mayak_org);
        mayak_org.setOnClickListener(this);

        imageView = (ImageView)rootView.findViewById(R.id.imageView);
        Button button_getclient=(Button) rootView.findViewById(R.id.button_getclient);
        Button button_savenote=(Button) rootView.findViewById(R.id.button_savenote);
        Button button_pay=(Button) rootView.findViewById(R.id.button_pay);
        button_newclient=(Button)rootView.findViewById(R.id.button_newclient);

        Button button_make_photo=(Button) rootView.findViewById(R.id.button_make_photo);
        Button button_send_photo=(Button) rootView.findViewById(R.id.button_send_photo);

        button_getclient.setOnClickListener(this);
        button_newclient.setOnClickListener(this);
        button_savenote.setOnClickListener(this);
        button_pay.setOnClickListener(this);
        button_make_photo.setOnClickListener(this);
        button_send_photo.setOnClickListener(this);

        final TextView selection=(TextView)(TextView) rootView.findViewById(R.id.selection);

        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        spinner.setLayoutMode(1); //.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        spinner.setAdapter(adapter1);


        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);
                selection.setText(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);





        mayak_org.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String s=mayak_org.getText().toString();
                String ss[]=s.split(":");
                mayak_org.setText(ss[0]);
                mayack_idorg.setText(ss[1]);
            }
        });



        mayak_org.addTextChangedListener(
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
                            mayak_org.setThreshold(1);
                            mayak_org.setAdapter(adapter);
                            mayak_org.setBackgroundColor(Color.parseColor("#DAD871"));
                            mayak_org.setTextColor(Color.BLUE);
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


    String fn;
    Uri outputFileUri;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static int TAKE_PICTURE_REQUEST = 1;

    private void saveFullImage(String fname)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file=new File(Environment.getExternalStorageDirectory(), "p" + fname + "-0.jpg");
        FileProvider.getUriForFile(getContext(),"com.trianonotbor.provider", file);
        outputFileUri = Uri.fromFile(file);
        mayack_prim.setText(file.toString()); //outputFileUri.toString()
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, TAKE_PICTURE_REQUEST);
    }



    @Override
    public void onClick(View v) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String idemp = settings.getString("idemp","");

        String login="ROSTOA$NSK20";
        String pass="345544";

        if (v.getId() == R.id.button_getclient)
        {
            /*String url ="https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=get_adr_by_coords&login=" + login + "&pass=" + pass +
                    "&latt=" + textView_Latitude.getText() + "&lngt=" + textView_Longitude.getText() + "&prim=" + textView_prim.getText();
            StringBuffer result=Get(url);
            textView_client.setText(result);

            if(result.length()>1)
            {
                String[] subStr;
                subStr = result.toString().split(";");

                if (subStr.length > 1) {
                    textView_idadr.setText(subStr[0]);
                    textView_idorg.setText(subStr[1]);
                    AutoComplTV.setText(subStr[2]);
                }*/
            String latt=textView_Latitude.getText().toString();
            String lngt=textView_Longitude.getText().toString();


            Cursor c=SqliteDB.Get_adr_by_coords(getContext(),latt,lngt);

            ArrayList<String> items = new ArrayList<String>();
            int i = 0;
            String result="111";
            if(c!=null)
            {
                while (c.moveToNext()) {
                    String ID = c.getString(c.getColumnIndex("ID"));
                    String NAME = c.getString(c.getColumnIndex("NAME"));
                    result = result + NAME + ":" + ID + "\r\n";
                }
            }
            Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }

        if (v.getId() == R.id.button_savenote || v.getId() ==R.id.button_start) // || v.getId() ==R.id.button_end
        {
            String msg= mayack_prim.getText().toString();

            if(v.getId() ==R.id.button_start)
                msg="Начало работы";
          //  if(v.getId() ==R.id.button_end)
          //      msg="Конец работы";

            String url ="https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=save_orgnote&login=" + login + "&pass=" + pass +
                    "&idemp=1900161" + "&idorg=" + mayack_idorg.getText() + "&idadr=" + textView_idadr.getText() +
                    "&latt=" + textView_Latitude.getText() + "&lngt=" + textView_Longitude.getText() +
                    "&subj=новый маячок" + "&msg=" + msg;

            StringBuffer result=MyHttp.Get(url,getContext());
            Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }

       /* if (v.getId() == R.id.button_1)
        {
            String msg=textView_prim.getText().toString();
            String ityp="0";

            if(v.getId() == R.id.button_1) // обзвон
                 ityp="7";

            String subj="Задача для оператора";

            String url ="https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=save_orgnote&login=" + login + "&pass=" + pass +
                    "&idemp=1900161" + "&idorg=" + textView_idorg.getText() + "&idadr=" + textView_idadr.getText() +
                    "&latt=" + textView_Latitude.getText() + "&lngt=" + textView_Longitude.getText() +
                    "&subj=" + subj + "&msg=" + msg + "&ityp=" + ityp;

            StringBuffer result=Get(url);
            Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG).show();

        }*/

        if (v.getId() == R.id.button_pay)
        {
            String url ="https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=store_outpay&login=" + login + "&pass=" + pass +
                    "&idorg=" + mayack_idorg.getText() + "&summa=" + mayak_summa.getText();
            StringBuffer result=MyHttp.Get(url, getContext());
            Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }


         if(v.getId()==R.id.button_newclient)
         {
                FragmentTransaction ftrans = getActivity().getSupportFragmentManager().beginTransaction();
             //   ftrans.replace(R.id.container, blankFragment2);
              //  ftrans.commit();
         }


         if(v.getId()==R.id.button_make_photo)
         {
             StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
             StrictMode.setVmPolicy(builder.build());
             String x=idemp + "_" + mayack_idorg.getText() + "_" + Long.toString(SystemClock.uptimeMillis());
             saveFullImage(x ); //photo_orderid.getText().toString()

         }

        if(v.getId()==R.id.button_send_photo)
        {
            MyHttp.SendPhoto(mayack_prim.getText().toString(), getContext(), "merchd", "111", "222", idemp);
        }

        if(v.getId()==R.id.mayak_org)
        {
            mayak_org.selectAll();
        }



    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri myuri=Uri.parse(mayack_prim.getText().toString());
        imageView.setImageURI(myuri);

    }

        public interface OnFragmentInteractionListener {
            // TODO: Update argument type and name
            void onFragmentInteraction(Uri uri);
        }


}
