package com.trianonotbor;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

/*import com.google.android.gms.common.net.SSLCertificateSocketFactory;*/

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by SONU on 29/10/15.
 */
public class DownloadTask {


    // Progress Dialog
     public static final int progress_bar_type = 0;


    /**
     * Showing Dialog
     */




    private static final String TAG = "Download Task";
    private Context context;
    private Button buttonText;
    private String downloadUrl = "", downloadFileName = "";
    ProgressDialog myProgress;

    public DownloadTask(Context context, Button buttonText, String downloadUrl, ProgressDialog pd) {
        this.context = context;
        this.buttonText = buttonText;
        this.downloadUrl = downloadUrl;
        this.myProgress = pd;

        downloadFileName = "test.db"; //downloadUrl.replace(downloadUrl, "");//Create file name by picking download file name from URL
        Log.e(TAG, downloadFileName);

        //Start Downloading Task
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            buttonText.setEnabled(false);
            buttonText.setText("started");//Set Button Text when download started
            //showDialog(progress_bar_type);

        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    buttonText.setEnabled(true);
                    buttonText.setText("completed");//If Download completed then change button text
                } else {
                    buttonText.setText("failed");//If download failed change button text
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buttonText.setEnabled(true);
                            buttonText.setText("again");//Change button text again after 3sec
                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed");

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs
                buttonText.setText("failed");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonText.setEnabled(true);
                        buttonText.setText("again");
                    }
                }, 3000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }

            myProgress.cancel();
            //dismissDialog(progress_bar_type);
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection

                /*if (c instanceof HttpsURLConnection) {
                    HttpsURLConnection httpsConn = (HttpsURLConnection) c;
                    httpsConn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
                    httpsConn.setHostnameVerifier(new AllowAllHostnameVerifier());
                }*/




                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }


                //Get File if SD card is present
                if (new CheckForSDCard().isSDCardPresent())
                {
                    apkStorage = new File(context.getExternalFilesDir(null) + "/" + "test");
                }
                else
                    Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e(TAG, "File Created");
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                //Close all connection after doing task
                fos.close();
                is.close();

            }
            catch (Exception e)
            {
                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
                //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            return null;
        }
    }
}