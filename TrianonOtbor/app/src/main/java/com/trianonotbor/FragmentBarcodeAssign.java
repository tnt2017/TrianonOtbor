package com.trianonotbor;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

//import org.apache.http.HttpRequest;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentBarcodeAssign.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentBarcodeAssign#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBarcodeAssign extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText textView_assign_barcode, textView_assign_idtov;
    AutoCompleteTextView AutoComplTovar;
    Button button_assign_barcode;
    ArrayAdapter<String> adapter1;
    String barcode;
    View rootView;


    private OnFragmentInteractionListener mListener;

    public FragmentBarcodeAssign() {
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
    public static FragmentBarcodeAssign newInstance(String param1, String param2) {
        FragmentBarcodeAssign fragment = new FragmentBarcodeAssign();
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

    public void setBarcode(String text)
    {
        barcode=text;
        if(textView_assign_barcode!=null)
        {
            textView_assign_barcode.setText(text);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_barcode_assign, container, false);
        textView_assign_barcode=(EditText) rootView.findViewById(R.id.textView_assign_barcode);
        textView_assign_barcode.setText(barcode);
        textView_assign_idtov=(EditText) rootView.findViewById(R.id.textView_assign_idtov);
        button_assign_barcode=(Button)rootView.findViewById(R.id.button_assign_barcode);

        button_assign_barcode.setOnClickListener(this);

        AutoComplTovar = (AutoCompleteTextView) rootView.findViewById(R.id.AutoComplTovar);
        AutoComplTovar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String s=AutoComplTovar.getText().toString();
                String ss[]=s.split(":");
                AutoComplTovar.setText(ss[0]);
                textView_assign_idtov.setText(ss[1]);
            }
        });

        AutoComplTovar.addTextChangedListener(
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
                        StringBuffer result = MyHttp.Get(url,getContext());
                        String[] items = result.toString().split(";");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_item, items);
                        AutoComplTovar.setThreshold(1);
                        AutoComplTovar.setAdapter(adapter);
                        AutoComplTovar.setBackgroundColor(Color.parseColor("#DAD871"));
                        AutoComplTovar.setTextColor(Color.BLUE);
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

    @Override
    public void onClick(View v) {
        String login="ROSTOA$NSK20";
        String pass="345544";
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String idemp = settings.getString("idemp","");

        if (v.getId() == R.id.button_assign_barcode)
        {
            String sdata=textView_assign_idtov.getText() + "^" + textView_assign_barcode.getText() + "^0^";
            String url ="https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=save_barcode&login=" + login + "&pass=" + pass +
                        "&sdata=" + sdata + "&idemp=" + idemp;
            StringBuffer result=MyHttp.Get(url, getContext());

            if(result.length()>1)
            {
                String[] subStr;
                subStr = result.toString().split(";");

                if (subStr.length > 1)
                {
                    AutoComplTovar.setText(subStr[2]);
                }

                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

        public interface OnFragmentInteractionListener {
            // TODO: Update argument type and name
            void onFragmentInteraction(Uri uri);
        }


}
