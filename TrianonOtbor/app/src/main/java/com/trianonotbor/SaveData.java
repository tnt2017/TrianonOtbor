package com.trianonotbor;

import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class SaveData {

    public static void SaveSection(final Activity ac, String idemp, String idtov, String section, String inv_login, String inv_pass, TextView status_message)
    {
        RequestQueue queue = Volley.newRequestQueue(ac);

        String url = "https://svc.trianon-nsk.ru/clients/code_reader/index.php?p=save_section&login=" + inv_login + "&pass=" + inv_pass
                + "&idemp=" + idemp
                + "&idtov=" + idtov
                + "&section=" + section;


        //status_message.setText(url);
        //url ="https://tnt-nets.ru/code_reader/?id=" + barcode.displayValue;

        // Request a string response from the provided URL.
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // status_message.setText(response); //"Response is: "+ .substring(0,500)
                        Toast.makeText(ac.getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //status_message.setText("That didn't work!" + error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest2);
    }

    }

