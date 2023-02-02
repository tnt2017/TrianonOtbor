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

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentFindDoc.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentFindDoc#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFindDoc extends Fragment implements View.OnClickListener, SoundPool.OnLoadCompleteListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String section; // = "210010496";
    EditText textbox_section, IdEmp;
    EditText section_textView_prim;
    List<ItemTovsheet> items = new ArrayList<ItemTovsheet>();
    MyItemRecyclerViewAdapterSection adapter;
    RecyclerView mRecyclerView;
    Button newdoc_add_line, newdoc_savedoc;
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

    public FragmentFindDoc() {
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
    public static FragmentFindDoc newInstance(String param1, String param2) {
        FragmentFindDoc fragment = new FragmentFindDoc();
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


    }



    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_finddoc, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        izone = settings.getString(PREF_IZONE,"");
        idemp = settings.getString(PREF_IDEMP,"");


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

    @Override
    public void onClick(View v) {
        /*login = settings.getString(PREF_LOGIN,""); // 01-02-2021
        pass = settings.getString(PREF_PASS,"");
        String url;
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date();
        section = textbox_section.getText().toString();*/





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
