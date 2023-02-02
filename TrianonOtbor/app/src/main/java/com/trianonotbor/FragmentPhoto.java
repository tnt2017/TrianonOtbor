package com.trianonotbor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

//import org.apache.http.HttpRequest;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentPhoto.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentPhoto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPhoto extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText photo_textView_prim;
    EditText photo_orderid;
    AutoCompleteTextView AutoComplTV;
    Button button_newclient;
    ArrayAdapter<String> adapter;
    Spinner spinner;
    ArrayAdapter<String> adapter1;
    private ImageView imageView;
    String IdSheet;

    private OnFragmentInteractionListener mListener;

    public FragmentPhoto() {
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
    public static FragmentPhoto newInstance(String param1, String param2) {
        FragmentPhoto fragment = new FragmentPhoto();
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

    public void setIdTovsheet(String text) {
        IdSheet=text;

    }

    public void SetCoords(String latt, String lngt)
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_photo, container, false);
        photo_orderid =(EditText)rootView.findViewById(R.id.photo_idtrip);
        photo_orderid.setText(IdSheet); /// вернул 30-06-2021
        imageView=(ImageView) rootView.findViewById(R.id.photo_imageView);
        photo_textView_prim=(EditText) rootView.findViewById(R.id.photo_textView_prim);
        Button photo_make_photo=(Button) rootView.findViewById(R.id.photo_make_photo);
        Button photo_send_photo=(Button) rootView.findViewById(R.id.photo_send_photo);
        photo_make_photo.setOnClickListener(this);
        photo_send_photo.setOnClickListener(this);
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


    StringBuffer Get(String URL)
    {
        System.out.println("URL=" + URL);
        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        URL obj = null;
        try {
            obj = new URL(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) obj.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"Windows-1251"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String inputLine="";
        StringBuffer response = new StringBuffer();

        while (true) {
            try {
                if (!((inputLine = in.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.append(inputLine);
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private static final int REQUEST_TAKE_PHOTO1 = 11;


    final int REQUEST_ACTION_CAMERA = 9;
    void openCamra() {
        Intent cameraImgIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        cameraImgIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID +".provider",
                        new File("your_file_name_with_dir")));
        startActivityForResult(cameraImgIntent, REQUEST_ACTION_CAMERA);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String idemp = settings.getString("idemp","");

        String login="ROSTOA$NSK20";
        String pass="345544";

         if(v.getId()==R.id.photo_make_photo)
         {
             StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
             StrictMode.setVmPolicy(builder.build());
             saveFullImage(photo_orderid.getText().toString() );
         }

        if(v.getId()==R.id.photo_send_photo)
        {
            MyHttp.SendPhoto(photo_textView_prim.getText().toString(), getContext(), "plat", "111", "222", idemp);
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(getActivity().getApplicationContext(), "onActivityResult", Toast.LENGTH_LONG).show();
        if (requestCode == TAKE_PICTURE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra("data")) {

                    // Фотка сделана, извлекаем миниатюру картинки
                    Bundle extras = data.getExtras();
                    Bitmap thumbnailBitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(thumbnailBitmap);

                    FileOutputStream out = null;
                    try {

                        out = new FileOutputStream(fn);
                        photo_textView_prim.setText(fn);
                        thumbnailBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                        // PNG is a lossless format, the compression factor (100) is ignored
                    } catch (Exception e) {
                        e.printStackTrace();

                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            /*else {
                // Какие-то действия с полноценным изображением,
                // сохранённым по адресу outputFileUri
                imageView.setImageURI(outputFileUri);
            }*/
        }
    }

    String fn;
    Uri outputFileUri;
    private static int TAKE_PICTURE_REQUEST = 1;



    private void saveFullImage(String fname)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file=new File(Environment.getExternalStorageDirectory(), "p" + fname + "-0.jpg");
        FileProvider.getUriForFile(getContext(),"com.trianonotbor.provider", file);
        outputFileUri = Uri.fromFile(file);
        photo_textView_prim.setText(file.toString()); //outputFileUri.toString()
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, TAKE_PICTURE_REQUEST);
    }

    public interface OnFragmentInteractionListener {
            // TODO: Update argument type and name
            void onFragmentInteraction(Uri uri);
        }


}
