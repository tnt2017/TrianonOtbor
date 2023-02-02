package com.trianonotbor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements FragmentTovsheet.OnFragmentInteractionListener,
        FragmentDocument.OnFragmentInteractionListener,
        FragmentNewRet.OnFragmentInteractionListener,
        FragmentDocumentPrihod.OnFragmentInteractionListener,
        FragmentTovcard.OnListFragmentInteractionListener,
        FragmentPrihodi.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        FragmentTovsheets.OnFragmentInteractionListener,
        FragmentStrikeouts.OnFragmentInteractionListener,
        FragmentTakeover.OnFragmentInteractionListener,
        FragmentPriemka.OnFragmentInteractionListener,
        FragmentComplektovka.OnFragmentInteractionListener,
        FragmentComplektovka2.OnFragmentInteractionListener,
        FragmentTrans.OnFragmentInteractionListener,
        FragmentFrontComplekt.OnFragmentInteractionListener,
        FragmentTrips.OnFragmentInteractionListener,
        FragmentInformer.OnFragmentInteractionListener,
        FragmentInformerBranch.OnFragmentInteractionListener,
        FragmentOrders.OnFragmentInteractionListener,
        FragmentHistSection.OnFragmentInteractionListener,
        FragmentAudit.OnFragmentInteractionListener,
        FragmentTovWithOneBarcode.OnFragmentInteractionListener,
        FragmentNotMatched.OnFragmentInteractionListener,
        FragmentPoisk.OnFragmentInteractionListener,
        FragmentZakaz.OnFragmentInteractionListener,
        FragmentZakazTovs.OnFragmentInteractionListener,
        View.OnClickListener, SoundPool.OnLoadCompleteListener {

    SharedPreferences settings;
    FragmentHome fragmentHome;

    FragmentLogin fragmentLogin;

    FragmentTovsheet fragmentTovsheet;
    FragmentTovsheets fragmentTovsheets;
    FragmentInvent fragmentInvent;
    FragmentTovProps fragmentTovProps;
    FragmentTovcard fragmentTovcard;
    FragmentPrihodi fragmentPrihodi;
    FragmentDocument fragmentDocument;
    FragmentDocumentPrihod fragmentDocumentPrihod;
    FragmentTakeover fragmentTakeover;
    FragmentStrikeouts fragmentStrikeouts;
    FragmentZarplata fragmentZarplata;
    FragmentPriemka fragmentPriemka;
    FragmentMayack fragmentMayack;
    FragmentComplektovka fragmentComplektovka;
    FragmentComplektovka2 fragmentComplektovka2;
    FragmentTrans fragmentTrans;
    FragmentFrontComplekt fragmentFrontComplekt;
    FragmentTrips fragmentTrips;
    FragmentInformer fragmentInformer;
    FragmentOrders fragmentOrders;
    FragmentHistSection fragmentHistSection;
    FragmentSection fragmentSection;
    FragmentNewDoc fragmentNewDoc;
    FragmentNewRet fragmentNewRet;
    FragmentFindDoc fragmentFindDoc;
    FragmentPoisk fragmentPoisk;

    FragmentTovWithOneBarcode fragmentTovWithOneBarcode;
    FragmentNotMatched fragmentNotMatched;

    FragmentZakaz fragmentZakaz;
    FragmentZakazTovs fragmentZakazTovs;


    private String locationProvider;
    private LocationManager locationManager;

    private TextView barcodeValue;
    TextView main_latt, main_lngt;
    EditText idtov;
    String login, pass;
    String tov_id = "20002425";
    String doc_id = "210006159";
    String sheet_id = "210014232";
    String section = "E-01-1";
    Context ctx;
    int json_len=0;
    boolean IsBadBarcode=false;

    ArrayList<String> barcodesList = new ArrayList<>();
    ArrayList<String> barcodesList_ol = new ArrayList<>();
    ArrayList<String> tovid_List = new ArrayList<>();


    private static final String PREF_NAME = "login";
    private static final String PREF_PASS = "pass";
    private static final String PREF_MODE = "mode"; // режим работы. 0 - инвент, 1 - приемка
    private static final String PREF_FLASH = "flash";

    private static final String PREF_CURRENT_SHEET = "current_sheet";
    private static final String PREF_CURRENT_SECTION = "current_section";



    private static final int RC_BARCODE_CAPTURE = 9001;
    private AppBarConfiguration mAppBarConfiguration;

    // File url to download
    private static String file_url = "https://svc.trianon-nsk.ru/clients/main/priemka/test.db"; //trianon-sklad.ru

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        boolean flash = settings.getBoolean(PREF_FLASH, true);
        intent.putExtra(BarcodeCaptureActivity.AutoFocus, true); //autoFocus.isChecked()
        intent.putExtra(BarcodeCaptureActivity.UseFlash, flash); //useFlash.isChecked()
        startActivityForResult(intent, RC_BARCODE_CAPTURE);

    }

    String prev_barcodeValue = "111";

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        Log.d(LOG_TAG, "onLoadComplete, sampleId = " + sampleId + ", status = " + status);
    }

    //Check if internet is present or not
    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            try {
                //System.out.println(location.getLatitude());
                String s1 = String.valueOf(location.getLatitude());
                String s2 = String.valueOf(location.getLongitude());
                String s3 = String.valueOf(location.getSpeed());
                main_latt.setText(s1);
                main_lngt.setText(s2);

                fragmentHome.SetCoords(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));

            } catch (Exception ex) {
                System.out.println("error location");
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };


    String ConvertEAN(String barcode) {
        barcode = barcode.substring(1, 13);

        int t1 = 0, t2 = 0;
        for (int i = 0; i < 12; i++) {
            char c = barcode.charAt(i);
            int a = Integer.parseInt(String.valueOf(c));
            if (i % 2 == 0)
                t1 += a;
            else
                t2 += a;

        }

        int summa = t1 + t2 * 3;
        String str_summa = String.valueOf(summa);
        char last_sym = str_summa.charAt(str_summa.length() - 1);
        int res = 10 - Integer.parseInt(String.valueOf(last_sym));
        String ret = barcode + String.valueOf(res);
        Toast.makeText(this, ret, Toast.LENGTH_SHORT).show();

        return ret;
    }

    int GetJSONCount(String s)
    {
        JSONObject userJson = null;
        try {
            userJson = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray data = null;
        try {
            data = (JSONArray) userJson.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data.length();
    }


    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "HH:mm:ss a", Locale.getDefault()); //dd:MMMM:yyyy
            final String strDate = simpleDateFormat.format(calendar.getTime());

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //barcodeValue.setText(strDate);
                    String mode = settings.getString(PREF_MODE, Integer.toString(R.id.radio_invent));

                    if (!prev_barcodeValue.equals(barcodeValue.getText().toString()) && barcodeValue.getText().toString() != "") {
                        if (barcodeValue.getText().toString().substring(0, 2).equals("OL")) {
                            //barcodesList.clear();
                            if (mode.equals(Integer.toString(R.id.radio_trans)) ||
                                    mode.equals(Integer.toString(R.id.radio_complekt)) ||
                                    mode.equals(Integer.toString(R.id.radio_complekt2))) {
                                final FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();

                                if (mode.equals(Integer.toString(R.id.radio_trans)))
                                    ftrans.replace(R.id.container, fragmentTrans); // транспортировка
                                if (mode.equals(Integer.toString(R.id.radio_complekt)))
                                    ftrans.replace(R.id.container, fragmentComplektovka);
                                if (mode.equals(Integer.toString(R.id.radio_complekt2)))
                                    ftrans.replace(R.id.container, fragmentComplektovka2);
                                ftrans.commit();
                            } else {
                                final FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
                                ftrans.replace(R.id.container, fragmentTovsheet); // просто попадаем в отборочный лист
                                ftrans.commit();
                            }
                        }

                        if (barcodeValue.getText().toString().substring(0, 2).equals("BD") || barcodeValue.getText().toString().substring(0, 2).equals("ИВ")) {
                            //  barcodesList.clear();

                            String api_url = "https://svc.trianon-nsk.ru/clients/code_reader/index.php";
                            String iddoc = barcodeValue.getText().toString().substring(2, barcodeValue.getText().toString().length());
                            String url = api_url + "?p=get_doctype&login=" + login + "&pass=" + pass + "&iddoc=" + iddoc + "&json=1";

                            StringBuffer http_result = MyHttp.Get(url, ctx);
                            String response = http_result.toString();


                            final FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
                            /////////////////////////////

                            if (response.equals("2") || response.equals("4") || response.equals("6"))
                                ftrans.replace(R.id.container, fragmentDocument); // НАКЛАДНАЯ
                            else
                                ftrans.replace(R.id.container, fragmentDocumentPrihod); // приход ИЛИ ВОЗВРАТ
                            ftrans.commit();

                        }


                        if (barcodeValue.getText().toString().substring(0, 2).equals("TR")) {
                            //  barcodesList.clear();

                            String idtrip = barcodeValue.getText().toString().substring(2, barcodeValue.getText().toString().length());
                            String api_url = "https://svc.trianon-nsk.ru/clients/code_reader/index.php";
                            String url = api_url + "?p=get_doctype&login=" + login + "&pass=" + pass + "&iddoc=" + idtrip + "&json=1";

                            StringBuffer http_result = MyHttp.Get(url, ctx);
                            String response = http_result.toString();

                            final FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
                            try {
                                fragmentOrders.setIdTrip(idtrip);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ftrans.replace(R.id.container, fragmentOrders); // приход ИЛИ ВОЗВРАТ
                            ftrans.commit();
                        }


                        if (barcodeValue.getText().toString().substring(0, 2).equals("SE")) {
                            //  barcodesList.clear(); // 13-08-2021 /* отсюда переход в секцию идет !!! */

                            tovid_List.clear();


                            String section = barcodeValue.getText().toString().substring(2, barcodeValue.getText().toString().length());
                            section = section.substring(0, 1) + "-" + section.substring(1, 3) + "-" + section.substring(4, 5);

                            SharedPreferences.Editor prefEditor = settings.edit();  /* 08-09-2021 */
                            prefEditor.putString(PREF_CURRENT_SECTION, section);
                            prefEditor.apply();

                            final FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
                            fragmentSection.setSection(section);
                            ftrans.replace(R.id.container, fragmentSection); // приход ИЛИ ВОЗВРАТ
                            ftrans.commit();
                        }


                        if (!barcodeValue.getText().toString().substring(0, 2).equals("OL") &&
                                !barcodeValue.getText().toString().substring(0, 2).equals("BD") &&
                                !barcodeValue.getText().toString().substring(0, 2).equals("TR") &&
                                !barcodeValue.getText().toString().substring(0, 2).equals("SE")) {
                            if (barcodeValue.getText().toString().length() > 8) {

                                if (barcodeValue.getText().toString().length() == 14) {
                                    String s = ConvertEAN(barcodeValue.getText().toString());
                                    barcodeValue.setText(s);
                                }

                                barcodesList.add(barcodeValue.getText().toString());

                                if (mode.equals(Integer.toString(R.id.radio_priemka)) || mode.equals(Integer.toString(R.id.radio_otbor)) || mode.equals(Integer.toString(R.id.radio_prisvoenie)) || mode.equals(Integer.toString(R.id.radio_newdoc)))
                                {
                                    if (mode.equals(Integer.toString(R.id.radio_priemka)))
                                    {
                                        if(!IsBadBarcode) {
                                            final FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
                                            ftrans.replace(R.id.container, fragmentDocumentPrihod); // карточка товара //container
                                            ftrans.commit();
                                        }
                                    }

                                    if (mode.equals(Integer.toString(R.id.radio_otbor)))
                                    {  // добавлено 30-06-2021
                                        final FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
                                        String current_sheet = settings.getString(PREF_CURRENT_SHEET, "1234");

                                        fragmentTovsheet.IdSheet = current_sheet; // 30-06-2021
                                        fragmentTovsheet.setBarcodesList(barcodesList);
                                        ftrans.replace(R.id.container, fragmentTovsheet);
                                        ftrans.commit();
                                    }

                                    if (mode.equals(Integer.toString(R.id.radio_prisvoenie))) // 30-08-2021
                                    {  // добавлено 30-08-2021
                                        final FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
                                        String current_section = settings.getString(PREF_CURRENT_SECTION, "W-01-1");

                                        Cursor query = SqliteDB.GetTovByBarcode(ctx, barcodeValue.getText().toString());
                                        if (query.moveToFirst()) {
                                            String ID = query.getString(query.getColumnIndex("ID"));
                                            String NAME = query.getString(query.getColumnIndex("NAME"));
                                            tovid_List.add(ID + ":" + NAME);
                                        }

                                        fragmentSection.section = current_section; // 30-06-2021
                                        fragmentSection.setBarcodesList(tovid_List);
                                        ftrans.replace(R.id.container, fragmentSection);
                                        ftrans.commit();
                                    }

                                    if (mode.equals(Integer.toString(R.id.radio_newdoc))) // 30-08-2021
                                    {  // добавлено 30-08-2021
                                        final FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
                                        String current_section = settings.getString(PREF_CURRENT_SECTION, "W-01-1");

                                        Cursor query = SqliteDB.GetTovByBarcode(ctx, barcodeValue.getText().toString());
                                        if (query.moveToFirst()) {
                                            String ID = query.getString(query.getColumnIndex("ID"));
                                            String NAME = query.getString(query.getColumnIndex("NAME"));
                                            tovid_List.add(ID + ":" + NAME);
                                        }

                                        fragmentNewDoc.section = current_section; // 30-06-2021
                                        fragmentNewDoc.setBarcodesList(tovid_List);
                                        ftrans.replace(R.id.container, fragmentNewDoc);
                                        ftrans.commit();
                                    }



                                    /*AppCompatActivity activity = (AppCompatActivity) ctx;
                                    FragmentDocumentPrihod myFragment = new FragmentDocumentPrihod();
                                    myFragment.setBarcodesList(barcodesList);
                                    myFragment.setBarcode(barcodeValue.getText().toString());
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();*/
                                }
                                else
                                {
                                    if (mode.equals(Integer.toString(R.id.radio_newdoc))) // 30-08-2021
                                    {
                                        Toast.makeText(getApplicationContext(), "Режим новый документ", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        json_len = SQLITE_GetTovCountByBarcode(barcodeValue.getText().toString());
                                        if (json_len > 1) {
                                            //Toast.makeText(getApplicationContext(), "Несколько товаров со штрихкодом1", Toast.LENGTH_SHORT).show();
                                            AppCompatActivity activity = (AppCompatActivity) ctx;
                                            FragmentTovWithOneBarcode myFragment = new FragmentTovWithOneBarcode();
                                            myFragment.setBarcode(barcodeValue.getText().toString());
                                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
                                        } else {
                                            AppCompatActivity activity = (AppCompatActivity) ctx;
                                            FragmentInvent myFragment = new FragmentInvent();
                                            myFragment.setBarcode(barcodeValue.getText().toString());
                                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
                                        }
                                    }
                                }
                            }
                        }
                        prev_barcodeValue = barcodeValue.getText().toString();
                    }
                }
            });
        }
    }

    final String LOG_TAG = "myLogs";
    final int MAX_STREAMS = 5;

    SoundPool sp;
    int soundIdShot;
    int soundIdExplosion;
    int streamIDShot;
    int streamIDExplosion;


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocationInfo() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // get all available location provider
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER))
        {
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            Toast.makeText(this, "not available location provider", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        //textView_Latitude.setText(Double.toString(location.getLatitude()));
        //textView_Longitude.setText(Double.toString(location.getLongitude()));
        if(location!=null)
        {
            Toast.makeText(this, Double.toString(location.getLatitude()), Toast.LENGTH_LONG).show();
            String s1 = String.valueOf(location.getLatitude());
            String s2 = String.valueOf(location.getLongitude());
            fragmentHome.SetCoords(s1, s2);
        }
        else
        {
            Toast.makeText(this, "locationManager.getLastKnownLocation выдал NULL", Toast.LENGTH_LONG).show();
        }

        try {
            locationManager.requestLocationUpdates(locationProvider, 200, 1, listener);
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }
    }


    private void requestPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED

        ) {
            Toast.makeText(this, "Permissions не получены" , Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA}, 0);
        }
        else
        {
            Toast.makeText(this, "Permissions уже получены" , Toast.LENGTH_LONG).show();
        }

    }




    boolean CheckBarcode(String barcode)
    {
        char[] chArray = barcode.toCharArray();
        int sum_chet=0;
        int sum_nechet=0;

        for(int i=0;i<chArray.length-1;i++)
        {
            if(i%2==0) // нечетные
            {
                String s=Character.toString(chArray[i]);
                int i1 = Integer.parseInt(s);
                sum_nechet+=i1;
            }
            else
            {
                String s=Character.toString(chArray[i]);
                int i2 = Integer.parseInt(s);
                sum_chet+=i2;
            }
        }
        int summa=sum_chet*3+sum_nechet;
        String s=Integer.toString(summa);
        String s2=Character.toString(s.charAt(s.length()-1));
        int i3 = Integer.parseInt(s2);
        int itogo=10-i3;

        String scontrol=Character.toString(chArray[chArray.length-1]);
        int control_number = Integer.parseInt(scontrol);

        if(itogo==10 && control_number==0)
        {
            return true;
        }
        if(itogo==control_number)
        {
            return true;
        }
        else
        {
            return false;
        }

    }






    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String t = ConvertEAN("14601313011375"); //14620747587713


        //4606453045160 проблемный код
        CheckBarcode("4606453044965"); //4606453045245=код краски // 4601546021298 - эталон из примера




        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        fragmentHome = new FragmentHome();
        fragmentLogin = new FragmentLogin();

        fragmentTovsheet = new FragmentTovsheet();
        fragmentTovsheets = new FragmentTovsheets();
        fragmentInvent = new FragmentInvent();
        //itemFragment4=new ItemFragment4();
        fragmentDocument = new FragmentDocument();
        fragmentDocumentPrihod = new FragmentDocumentPrihod();
        fragmentStrikeouts = new FragmentStrikeouts();
        fragmentTovcard = new FragmentTovcard();
        fragmentPrihodi = new FragmentPrihodi();
        fragmentTakeover = new FragmentTakeover();
        fragmentTovProps = new FragmentTovProps();
        fragmentZarplata = new FragmentZarplata();
        fragmentMayack = new FragmentMayack();

        fragmentPriemka = new FragmentPriemka();
        fragmentComplektovka = new FragmentComplektovka();
        fragmentComplektovka2 = new FragmentComplektovka2();
        fragmentTrans = new FragmentTrans();
        fragmentFrontComplekt = new FragmentFrontComplekt();

        fragmentTrips = new FragmentTrips();
        fragmentInformer = new FragmentInformer();
        fragmentOrders = new FragmentOrders();
        fragmentHistSection = new FragmentHistSection();
        fragmentSection = new FragmentSection();
        fragmentTovWithOneBarcode = new FragmentTovWithOneBarcode();
        fragmentNewDoc = new FragmentNewDoc();
        fragmentNewRet = new FragmentNewRet();
        fragmentFindDoc = new FragmentFindDoc();
        fragmentPoisk = new FragmentPoisk();
        fragmentNotMatched = new FragmentNotMatched();
        fragmentZakaz = new FragmentZakaz();
        fragmentZakazTovs = new FragmentZakazTovs();

        main_latt = (TextView) findViewById(R.id.main_latt);
        main_lngt = (TextView) findViewById(R.id.main_lngt);

        barcodeValue = findViewById(R.id.barcodeValue);
        barcodeValue.setText("2222");
        settings = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_gallery, R.id.nav_home, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);
        ctx = this;

        final FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        ftrans.replace(R.id.container, fragmentLogin); // инвентаризация
        ftrans.commit();


        MyTimerTask mMyTimerTask = new MyTimerTask();
        Timer mTimer = new Timer();
        mTimer.schedule(mMyTimerTask, 0, 250);
        Toast.makeText(this, "timer start!!!", Toast.LENGTH_LONG);

        requestPermission();
        getLocationInfo();

       /* WifiManager wifiMan = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        Toast.makeText(this, "ip=" + ip, Toast.LENGTH_SHORT).show();*/

        //new DownloadFileFromURL().execute(file_url);
        //

/*
        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);

        soundIdShot = sp.load(this, R.raw.shot, 1);
        //Log.d(LOG_TAG, "soundIdShot = " + soundIdShot);
        try {
            soundIdExplosion = sp.load(this.getAssets().openFd("explosion.ogg"), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "soundIdExplosion = " + soundIdExplosion);

        sp.play(soundIdExplosion, 1, 0.1f, 0, 0, 1);


        sp.play(soundIdShot, 1, 1, 0, 5, 1);
        sp.play(soundIdExplosion, 1, 1, 0, 2, 1);*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void showInfo(View view) {
    }

    public void getTovsheet(View view) {
    }

    public void showResult(View view) {
    }

    public void printTovsheet(View view) {
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        final FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();


        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, true); //autoFocus.isChecked()
            intent.putExtra(BarcodeCaptureActivity.UseFlash, false); //useFlash.isChecked()
            startActivityForResult(intent, RC_BARCODE_CAPTURE);
            // Handle the camera action
            //ftrans.replace(R.id.container,fragmentInvent);
        } else if (id == R.id.nav_home) {
            //fragmentInvent.setIdtov(tov_id);
            ftrans.replace(R.id.container, fragmentHome); // настройки ID итд
        }

        else if (id == R.id.nav_mayak)
        {
            if (main_latt != null && main_lngt != null)
                fragmentMayack.SetCoords(main_latt.getText().toString(), main_lngt.getText().toString());
            ftrans.replace(R.id.container, fragmentMayack); // маячок
        }
        else if (id == R.id.nav_invent) {
            fragmentInvent.setIdtov(tov_id);
            ftrans.replace(R.id.container, fragmentInvent); // инвентаризация
        } else if (id == R.id.nav_tovprops) {
            tov_id = fragmentInvent.idtov;
            fragmentTovProps.setIDTOV(tov_id);
            ftrans.replace(R.id.container, fragmentTovProps); // св-ва товара
        } else if (id == R.id.nav_tovcard) {
            tov_id = fragmentInvent.idtov;
            fragmentTovcard.setIdTov(tov_id);
            ftrans.replace(R.id.container, fragmentTovcard); // карточка товара
        } else if (id == R.id.nav_prihodi) {
            //tov_id=fragmentPrihodi.idtov;
            // if(!tov_id.equals(""))
            fragmentPrihodi.setIdTov("307477");
            ftrans.replace(R.id.container, fragmentPrihodi); // приходы товара по ID
        } else if (id == R.id.nav_priemka) {
            ftrans.replace(R.id.container, fragmentPriemka); // приемка
        } else if (id == R.id.nav_rnk) {
            // itemFragment4.setIdDoc(doc_id);
            ftrans.replace(R.id.container, fragmentDocument); // накладная
        } else if (id == R.id.nav_prihod) {
            // itemFragment4.setIdDoc(doc_id);
            ftrans.replace(R.id.container, fragmentDocumentPrihod); // приход

        } else if (id == R.id.nav_otbor) {
            //idtov.getText().toString()
            //fragmentTovsheet.setIdTovsheet(sheet_id);
            ftrans.replace(R.id.container, fragmentTovsheet); // отборка
        } else if (id == R.id.nav_mysheets) {
            //idtov.getText().toString()
            //fragmentTovsheet.setIdTovsheet(sheet_id);
            ftrans.replace(R.id.container, fragmentTovsheets); // мои листы
        } else if (id == R.id.nav_strikeouts) {
            //itemFragment.setIDTOV(text_id);
            ftrans.replace(R.id.container, fragmentStrikeouts); // вычерки
        } else if (id == R.id.nav_diff) {
            //itemFragment.setIDTOV(text_id);
            ftrans.replace(R.id.container, fragmentNotMatched); // расхождения
        } else if (id == R.id.nav_zarplata) {
            //itemFragment.setIDTOV(text_id);
            ftrans.replace(R.id.container, fragmentZarplata); // зарплата
        } else if (id == R.id.nav_complekt) {
            //itemFragment.setIDTOV(text_id);
            ftrans.replace(R.id.container, fragmentComplektovka); // комплектовка адреса
        } else if (id == R.id.nav_front_complekt) {
            //itemFragment.setIDTOV(text_id);
            ftrans.replace(R.id.container, fragmentFrontComplekt); // фронт комплектовки
        } else if (id == R.id.nav_complekt2) {
            //itemFragment.setIDTOV(text_id);
            ftrans.replace(R.id.container, fragmentComplektovka2); // комплектовка рейса
        } else if (id == R.id.nav_trans) {
            //itemFragment.setIDTOV(text_id);
            ftrans.replace(R.id.container, fragmentTrans); // транспортировка
        } else if (id == R.id.nav_trips) {
            //itemFragment.setIDTOV(text_id);
            ftrans.replace(R.id.container, fragmentTrips); // рейсы
        }
        else if (id == R.id.nav_informer) {
            //itemFragment.setIDTOV(text_id);
            ftrans.replace(R.id.container, fragmentInformer); // рейсы
        } else if (id == R.id.nav_orders) {
            //itemFragment.setIDTOV(text_id);
            ftrans.replace(R.id.container, fragmentOrders); // ордера
        } else if (id == R.id.nav_section) {
            //itemFragment.setIDTOV(text_id);
            fragmentSection.setSection(section);
            ftrans.replace(R.id.container, fragmentSection); // секции
        } else if (id == R.id.nav_section) {
            //itemFragment.setIDTOV(text_id);
            fragmentNewDoc.setSection(section);
            ftrans.replace(R.id.container, fragmentNewDoc); // секции
        } else if (id == R.id.new_doc) {
            //itemFragment.setIDTOV(text_id);
            fragmentNewDoc.setSection(section);
            ftrans.replace(R.id.container, fragmentNewDoc); // новый док
        } else if (id == R.id.new_ret) {
            //itemFragment.setIDTOV(text_id);
            fragmentNewRet.setSection(section);
            ftrans.replace(R.id.container, fragmentNewRet); // новый возврат
        } else if (id == R.id.find_doc) {
            //itemFragment.setIDTOV(text_id);
           // fragmentFindDoc.setSection(section);
            ftrans.replace(R.id.container, fragmentPoisk); // поиск документа
        } else if (id == R.id.zakaz) {
            //itemFragment.setIDTOV(text_id);
            // fragmentFindDoc.setSection(section);
            ftrans.replace(R.id.container, fragmentZakaz); // заказ от клиента
        }






        //fragmentSection
        ftrans.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public int SQLITE_GetTovCountByBarcode(String barcode)
    {
        Cursor query=SqliteDB.GetCountByBarcode(this, barcode);
        if (query.moveToFirst())
            return query.getInt(0);
        else
            return 0;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        login = "ROSTOA$NSK20";
        pass = "345544";
        // String login = settings.getString(PREF_NAME,"");
        // String pass = settings.getString(PREF_PASS,"");

        try {

            if (requestCode == RC_BARCODE_CAPTURE) {
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    if (data != null) {
                        final Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

                        if(!CheckBarcode(barcode.displayValue))    /// проверяем корректность по формуле.
                        {
                            Toast.makeText(this, "Неверный штрихкод (подделка ???)", Toast.LENGTH_SHORT).show();
                            IsBadBarcode=true;
                            return;
                        }

                        barcodeValue.setText(barcode.displayValue);
                        String mode = settings.getString(PREF_MODE, "");

                        if (barcode.displayValue.substring(0, 2).equals("OL")) // OL=OTBORLIST
                        {
                            if (mode.equals(Integer.toString(R.id.radio_trans)) ||
                                    mode.equals(Integer.toString(R.id.radio_complekt)) ||
                                    mode.equals(Integer.toString(R.id.radio_complekt2))) {
                                String idsheet = barcodeValue.getText().toString().substring(2);
                                barcodesList_ol.add(idsheet);

                                if (mode.equals(Integer.toString(R.id.radio_trans))) {
                                    Toast.makeText(this, "транспортировка" + idsheet, Toast.LENGTH_SHORT).show();
                                    fragmentTrans.setBarcodesList(barcodesList_ol);
                                }

                                if (mode.equals(Integer.toString(R.id.radio_complekt))) {
                                    Toast.makeText(this, "комплектовка" + idsheet, Toast.LENGTH_SHORT).show();
                                    fragmentComplektovka.setBarcodesList(barcodesList_ol);
                                }

                                if (mode.equals(Integer.toString(R.id.radio_complekt2))) {
                                    Toast.makeText(this, "комплектовка2" + idsheet, Toast.LENGTH_SHORT).show();
                                    fragmentComplektovka2.setBarcodesList(barcodesList_ol);
                                }

                                if (mode.equals(Integer.toString(R.id.radio_otbor))) {
                                    Toast.makeText(this, "отбор" + idsheet, Toast.LENGTH_SHORT).show();
                                    fragmentTovsheet.setBarcodesList(barcodesList_ol);
                                }
                                //fragmentComplektovka.setIdDoc();
                            } else {
                                String idtovsheet = barcode.displayValue.substring(2, barcode.displayValue.length());
                                fragmentTovsheet.setIdTovsheet(idtovsheet);
                            }
                        }

                        if (barcode.displayValue.substring(0, 2).equals("BD")) // BD=BASEDOC
                        {
                            String iddoc = barcode.displayValue.substring(2, barcode.displayValue.length());
                            fragmentDocument.setIdDoc(iddoc);
                            // fragmentDocumentPrihod.setIdDoc(iddoc);
                        }

                        if (barcode.displayValue.substring(0, 2).equals("TR")) // TR=TRIP // 15-07-2021
                        {
                            String idtrip = barcode.displayValue.substring(2, barcode.displayValue.length());
                            fragmentOrders.setIdTrip(idtrip);
                            // fragmentDocumentPrihod.setIdDoc(iddoc);
                        }

                        if (barcode.displayValue.substring(0, 2).equals("SE")) // SE=SECTION // 13-08-2021
                        {
                            String section = barcode.displayValue.substring(2, barcode.displayValue.length());
                            section = section.substring(0, 1) + "-" + section.substring(1, 3) + "-" + section.substring(4, 5);
                            SharedPreferences.Editor prefEditor = settings.edit();  /* 08-09-2021 */
                            prefEditor.putString(PREF_CURRENT_SECTION, section);
                            prefEditor.apply();
                            tovid_List.clear();
                            fragmentSection.setSection(section);
                        }
                        // radio_rasstanovka
                        // radio_prisvoenie

                        if (!barcode.displayValue.substring(0, 2).equals("OL") && !barcode.displayValue.substring(0, 2).equals("BD")) {
                            // Toast.makeText(this, "mode=" + mode, Toast.LENGTH_SHORT).show(); //12-07-2021

                            if (mode.equals(Integer.toString(R.id.radio_priemka)) || mode.equals(Integer.toString(R.id.radio_otbor)) || mode.equals(Integer.toString(R.id.radio_prisvoenie)) || mode.equals(Integer.toString(R.id.radio_newdoc)))
                            {
                                if (mode.equals(Integer.toString(R.id.radio_otbor)))
                                {
                                    // 30-06-2021
                                    String bcode = barcode.displayValue;
                                    String url = "https://svc.trianon-nsk.ru/clients/main/pages/jrn_kk_svc.php?svc_id=get_tov&barcode=" + bcode; /// исправил на get_tov 12-07-2021
                                    StringBuffer http_result = MyHttp.Get(url, ctx);
                                    String response = http_result.toString();
                                    fragmentTovsheet.setBarcode(response);
                                    fragmentTovsheet.setBarcodesList(barcodesList);
                                }
                                if (mode.equals(Integer.toString(R.id.radio_priemka)))
                                {
                                    String bcode = barcode.displayValue;
                                    String url = "https://svc.trianon-nsk.ru/clients/main/pages/jrn_kk_svc.php?svc_id=get_doc_by_barcode&barcode=" + bcode;
                                    StringBuffer http_result = MyHttp.Get(url, ctx);
                                    String iddoc = http_result.toString();
                                    fragmentDocumentPrihod.setIdDoc(iddoc);
                                    fragmentDocumentPrihod.setBarcodesList(barcodesList);
                                }
                                if (mode.equals(Integer.toString(R.id.radio_prisvoenie)))
                                {
                                    // 08-09-2021
                                    String bcode = barcode.displayValue;
                                    Cursor query = SqliteDB.GetTovByBarcode(ctx, bcode);
                                    if (query.moveToFirst())
                                    {
                                        String ID = query.getString(query.getColumnIndex("ID"));
                                        String NAME = query.getString(query.getColumnIndex("NAME"));
                                    }
                                    //fragmentSection.setBarcodesList(tovid_List);
                                }

                                if (mode.equals(Integer.toString(R.id.radio_newdoc)))
                                {
                                    // 08-09-2021
                                    String bcode = barcode.displayValue;
                                    Cursor query = SqliteDB.GetTovByBarcode(ctx, bcode);
                                    if (query.moveToFirst())
                                    {
                                        String ID = query.getString(query.getColumnIndex("ID"));
                                        String NAME = query.getString(query.getColumnIndex("NAME"));
                                    }
                                    //fragmentSection.setBarcodesList(tovid_List);
                                }


                            } else {
                                String bcode = barcode.displayValue;
                                json_len = SQLITE_GetTovCountByBarcode(bcode);
                                Toast.makeText(getApplicationContext(), "Количество товаров со штрихкодом=" + json_len, Toast.LENGTH_SHORT).show();

                                if (json_len > 1) // 18-08-2021
                                {
                                    AppCompatActivity activity = (AppCompatActivity) ctx;
                                    FragmentTovWithOneBarcode myFragment = new FragmentTovWithOneBarcode();
                                    myFragment.setBarcode(barcodeValue.getText().toString());
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
                                    // fragmentTovWithOneBarcode.setBarcode(bcode);
                                    //Toast.makeText(getApplicationContext(), "Несколько товаров со штрихкодом2", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    fragmentInvent.setBarcode(bcode);
                                }

                            }
                        }
                    } else {
                        //  statusMessage.setText(R.string.barcode_failure);
                        //Log.d(TAG, "No barcode captured, intent data is null");
                    }
                } else {
                    // statusMessage.setText(String.format(getString(R.string.barcode_error),
                    //        CommonStatusCodes.getStatusCodeString(resultCode)));
                }
            } else if (resultCode == RESULT_CANCELED) /////
            {
                Toast.makeText(this, "inet error", Toast.LENGTH_SHORT).show();
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception ex)
        {
            Toast.makeText(this, "ОШИБКА ИНТЕРНЕТА !!!!" + ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}