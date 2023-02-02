package com.trianonotbor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentLogin.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentLogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLogin extends Fragment implements View.OnClickListener, SoundPool.OnLoadCompleteListener {
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
    EditText Login, Pass;
    Button login_submit;


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

    public FragmentLogin() {
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
    public static FragmentLogin newInstance(String param1, String param2) {
        FragmentLogin fragment = new FragmentLogin();
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




    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_login, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        izone = settings.getString(PREF_IZONE,"");
        idemp = settings.getString(PREF_IDEMP,"");

      //  login_submit
        login_submit=rootView.findViewById(R.id.login_submit);
        login_submit.setOnClickListener(this);

        Login=rootView.findViewById(R.id.login_login);
        Pass=rootView.findViewById(R.id.login_pass);
        Login.setText(settings.getString(PREF_LOGIN, "ANY_OTBOR$NSK20"));
        Pass.setText(settings.getString(PREF_PASS, "9737534"));


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
        Toast.makeText(getActivity().getApplicationContext(), "ssssdfdfdfdfdssws", Toast.LENGTH_LONG).show();


        final FragmentTransaction ftrans = getFragmentManager().beginTransaction();

        FragmentHome fragmentHome=new FragmentHome();


        ftrans.replace(R.id.container, fragmentHome); // инвентаризация
        ftrans.commit();


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
