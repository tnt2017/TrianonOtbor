package com.trianonotbor;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class MyHttp {

    static StringBuffer Get(String URL, Context ctx) {
        System.out.println("URL=" + URL);
        //AppCompatActivity aa=new AppCompatActivity;

        RequestQueue queue = Volley.newRequestQueue(ctx);

        java.net.URL obj = null;
        try {
            obj = new URL(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;
        BufferedReader in = null;
        String inputLine = "";
        StringBuffer response = new StringBuffer();

        try {
            connection = (HttpURLConnection) obj.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            response.append( "error");
        }
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
            response.append( "error");
        }

        try
        {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "Windows-1251"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            response.append( "error");
        }

        while (true)
        {
            if(in==null)
            {
                response.append( "new error 1207");
                return response;
            }


            try
            {
                if (!((inputLine = in.readLine()) != null)) break;
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            response.append(inputLine);
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //button1=(Button)findViewById(R.id.button1);
        // button1.setOnClickListener(this);
        return response;
    }


    static void SendPhoto(String outfn, Context ctx, String folder, String idorg, String idadr, String idemp)
    {
        int permission = ActivityCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(null,PERMISSIONS_STORAGE, 1);
        }

        MultipartUtility multipart = null;
        try
        {
            String url="https://svc.trianon-nsk.ru/clients/merch_photos/index.php?idorg=" +
                    idorg + "&idadr=" + idadr + "&folder=" + folder + "&idemp=" + idemp;

            multipart = new MultipartUtility(url, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*This is to add file content*/
        try {
            String fn=outfn;
            multipart.addFilePart("file", new File(fn));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        List<String> response = null;
        try {
            response = multipart.finish();
            Toast.makeText(ctx, "Success upload!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
