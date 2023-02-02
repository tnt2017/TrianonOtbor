package com.trianonotbor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;
import static com.trianonotbor.MyHttp.Get;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentHome.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment implements FragmentTovsheet.OnFragmentInteractionListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String PREF_IZONE = "izone";
    private static final String PREF_IDEMP = "idemp";
    private static final String PREF_PRINTER = "printer";
    private static final String PREF_LOGIN= "login";
    private static final String PREF_PASS = "pass";
    private static final String PREF_MODE = "mode"; // режим работы. 0 - инвент, 1 - приемка, 2 - комплектовка итд
    private static final String PREF_FLASH = "flash"; // вспышка


    // TODO: Rename and change types of parameters
    SharedPreferences settings;
    private String mParam1;
    private String mParam2;
    private EditText Izone;
    private EditText Idemp;
    // private EditText MyPrinter;
    private EditText Login;
    private EditText Pass;

    private RadioGroup radioGroup;

    private RadioButton radio_invent;
    private RadioButton radio_priemka;
    private RadioButton radio_trans;
    private RadioButton radio_complekt;
    private RadioButton radio_complekt2;
    private RadioButton radio_otbor;
    private RadioButton radio_prisvoenie;

    CheckBox checkBox_flash;

    String latt,lngt;

    Button  button_start, button_end, button_save, button_download, button_test, button_md;
    TextView main_latt, main_lngt, base_version;

    private OnFragmentInteractionListener mListener;

    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
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

    public void SetCoords(String s1, String s2)
    {
        latt=s1;
        lngt=s2;
        if(main_latt!=null)
            main_latt.setText(latt);
        if(main_lngt!=null)
            main_lngt.setText(lngt);
    }

    public void getName(View view) {
        // получаем сохраненное имя
        Izone.setText(settings.getString(PREF_IZONE,""));
        Idemp.setText(settings.getString(PREF_IDEMP,""));
        Login.setText(settings.getString(PREF_LOGIN,"ANY_OTBOR$NSK20"));
        Pass.setText(settings.getString(PREF_PASS,"9737534"));



        try {
            switch (Integer.parseInt(settings.getString(PREF_MODE,"")))
            {
                case R.id.radio_invent : radio_invent.setChecked(true); break;
                case R.id.radio_priemka : radio_priemka.setChecked(true); break;
                case R.id.radio_trans : radio_trans.setChecked(true); break;
                case R.id.radio_complekt : radio_complekt.setChecked(true); break;
                case R.id.radio_complekt2 : radio_complekt2.setChecked(true); break;
                case R.id.radio_otbor : radio_otbor.setChecked(true); break;
                case R.id.radio_prisvoenie: radio_prisvoenie.setChecked(true); break;

            }
        }
        catch (Exception ex)
        {

        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_fragment_home, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Izone=rootView.findViewById(R.id.EditTextIzone);
        Idemp=rootView.findViewById(R.id.EditTextIdemp);
        //MyPrinter=rootView.findViewById(R.id.EditTextPrinter);
        Login=rootView.findViewById(R.id.EditTextLogin);
        Pass=rootView.findViewById(R.id.EditTextPass);


        radioGroup=rootView.findViewById(R.id.radioGroup);
        radio_invent=rootView.findViewById(R.id.radio_invent);
        radio_priemka=rootView.findViewById(R.id.radio_priemka);
        radio_trans=rootView.findViewById(R.id.radio_trans);
        radio_complekt=rootView.findViewById(R.id.radio_complekt);
        radio_complekt2=rootView.findViewById(R.id.radio_complekt2);
        radio_otbor=rootView.findViewById(R.id.radio_otbor);
        radio_prisvoenie=rootView.findViewById(R.id.radio_prisvoenie);

        radio_invent.setOnClickListener(this);
        radio_priemka.setOnClickListener(this);
        radio_trans.setOnClickListener(this);
        radio_complekt.setOnClickListener(this);
        radio_complekt2.setOnClickListener(this);
        radio_otbor.setOnClickListener(this);
        radio_prisvoenie.setOnClickListener(this);

        button_start=rootView.findViewById(R.id.button_start);
        button_start.setOnClickListener(this);

        button_save=rootView.findViewById(R.id.button_save);
        button_save.setOnClickListener(this);
        button_download=rootView.findViewById(R.id.button_download);
        button_download.setOnClickListener(this);
        button_test=rootView.findViewById(R.id.button_test);
        button_test.setOnClickListener(this);

        button_md=rootView.findViewById(R.id.button_md);
        button_md.setOnClickListener(this);



        main_latt=rootView.findViewById(R.id.main_latt);
        main_lngt=rootView.findViewById(R.id.main_lngt);
        main_latt.setText(latt);
        main_lngt.setText(lngt);

        checkBox_flash=rootView.findViewById(R.id.checkBox_flash);
        base_version=rootView.findViewById(R.id.base_version);
        base_version.setText("Версия базы=" + SqliteDB.GetBaseVersion(getActivity()));

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
    }
*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public void DownloadFiles(){
        try {
            URL u = new URL("https://svc.trianon-nsk.ru/clients/main/priemka/test.db"); // 192.168.1.145
            InputStream is = u.openStream();

            DataInputStream dis = new DataInputStream(is);

            byte[] buffer = new byte[1024];
            int length;

            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/" + "data/test.db"));
            while ((length = dis.read(buffer))>0)
            {
                fos.write(buffer, 0, length);
            }

            Toast.makeText(getActivity().getApplicationContext(),
                    "Файл скачен!", Toast.LENGTH_LONG).show();


        } catch (MalformedURLException mue) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "malformed url error", Toast.LENGTH_LONG).show();
        } catch (IOException ioe) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "io error", Toast.LENGTH_LONG).show();
        } catch (SecurityException se) {
            Log.e("SYNC getUpdate", "security error", se);
            Toast.makeText(getActivity().getApplicationContext(),
                    "security error", Toast.LENGTH_LONG).show();

        }
    }



    /*@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }*/





    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.button_save ||
                v.getId()==R.id.radio_invent ||
                v.getId()==R.id.radio_priemka ||
                v.getId()==R.id.radio_trans ||
                v.getId()==R.id.radio_complekt ||
                v.getId()==R.id.radio_complekt2 || v.getId()==R.id.radio_otbor || v.getId()==R.id.radio_prisvoenie || v.getId()==R.id.checkBox_flash)
        {
            SharedPreferences.Editor prefEditor = settings.edit();
            prefEditor.putString(PREF_IZONE, Izone.getText().toString());
            prefEditor.putString(PREF_IDEMP, Idemp.getText().toString());
            prefEditor.putString(PREF_PRINTER, "http://10.8.0.25/print/index.php?idsheet=");
            prefEditor.putString(PREF_LOGIN, Login.getText().toString());
            prefEditor.putString(PREF_PASS, Pass.getText().toString());

            prefEditor.putString(PREF_MODE, Integer.toString(radioGroup.getCheckedRadioButtonId()));
            prefEditor.putBoolean(PREF_FLASH, checkBox_flash.isChecked());

            prefEditor.apply();
            Toast.makeText(getActivity().getApplicationContext(),
                    "Cохранено. Режим=" + Integer.toString(radioGroup.getCheckedRadioButtonId()), Toast.LENGTH_LONG).show();
        }

        if(v.getId()==R.id.button_download)
        {
            ProgressDialog myDialog =
                    ProgressDialog.show(getActivity(), "Внимание","Идет скачивание базы штрихкодов...", true);
            //myDialog.getWindow().setGravity(Gravity.BOTTOM);
            new DownloadTask(getActivity(), button_download, "https://svc.trianon-nsk.ru/clients/main/priemka/test.db", myDialog);
        }

        if(v.getId()==R.id.button_test)
        {
            /*SQLiteDatabase db = getContext().openOrCreateDatabase(Environment.getExternalStorageDirectory() + "/" + Utils.downloadDirectory + "/" + "test.db", MODE_PRIVATE, null);
            Cursor query = db.rawQuery("SELECT * FROM KATMC_BARCODE;", null);
            if (query.moveToFirst())
            {
                String barcode = query.getString(0);
                Toast.makeText(getActivity().getApplicationContext(), barcode, Toast.LENGTH_LONG).show();
            }*/

            //int json_len = SqliteDB.GetTovCountByBarcode(getActivity(), "7942361012205");
            //Toast.makeText(getActivity().getApplicationContext(), "Количество товаров=" + json_len, Toast.LENGTH_LONG).show();

            base_version.setText("Версия базы=" + SqliteDB.GetBaseVersion(getActivity()));


            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();

                            // Log and toast
                            String msg = getString(R.string.msg_token_fmt, token);
                            Log.d("TAG", msg);
                            Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    });
            FirebaseMessaging.getInstance().subscribeToTopic("TopicName");

        }


        if(v.getId()==R.id.button_md) {
           /* File mediaFile = new File(Environment.getExternalStorageDirectory(), "NewDirectory");
            boolean r= mediaFile .mkdirs();*/

            File mediaFile = new File(getActivity().getExternalFilesDir(null), "test");
            File file = new File(mediaFile, "");
            boolean r=file.mkdirs();
            Toast.makeText(getActivity().getApplicationContext(), "result1=" + r, Toast.LENGTH_SHORT).show();

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

            File mediaStorageDir = getActivity().getApplicationContext().getExternalFilesDir(null);

           /* String PATH = Environment.getExternalStorageDirectory().getPath()
                    + getActivity().getApplicationContext().getFilesDir().getPath();
            File mediaFile = new File(PATH);*/

            boolean rr= mediaStorageDir .mkdirs();
            Toast.makeText(getActivity().getApplicationContext(), "result2=" + rr, Toast.LENGTH_SHORT).show();

        }


        if (v.getId() ==R.id.button_start  )
        {
            String msg=""; //=textView_prim.getText().toString();

            if(v.getId() ==R.id.button_start)
                msg="Начало работы";

            String login="ANY_OTBOR$NSK20";
            String pass="9737534";

            String url ="https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=register_atwork&login=" + login + "&pass=" + pass +
                    "&idemp=" + Idemp.getText().toString();

            StringBuffer result=Get(url,getContext());
            Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG).show();
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
