package com.trianonotbor;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class SqliteDB
{

    public static Cursor GetTovByID(Context ctx, String idtov)
    {
        Cursor query=null;
        try {
            SQLiteDatabase db = ctx.openOrCreateDatabase(ctx.getExternalFilesDir(null) + "/" + Utils.downloadDirectory + "/" + "test.db", MODE_PRIVATE, null);
            query = db.rawQuery("SELECT * from KATMC kmc, SKLAD_SECTION ss WHERE kmc.ID=ss.CMC AND ID='" + idtov + "';", null);
        }
        catch (Exception exception)
        {
            Toast.makeText(ctx, "Скачайте базу" + exception.getMessage(), Toast.LENGTH_LONG).show();
        }
        return query;
    }

    public static Cursor GetLastPrice(Context ctx, String idtov)
    {
        Cursor query=null;

        try
        {
            SQLiteDatabase db = ctx.openOrCreateDatabase(ctx.getExternalFilesDir(null) + "/" + Utils.downloadDirectory + "/" + "test.db", MODE_PRIVATE, null);
            query = db.rawQuery("SELECT DAT, PRICE_B, ONSALE from SEB_PRICE_CACHE WHERE cmc='" + idtov + "';", null);
        }
        catch (Exception exception)
        {
            Toast.makeText(ctx, "Скачайте базу" + exception.getMessage(), Toast.LENGTH_LONG).show();
        }
        return query;
    }

    public static Cursor GetTovByBarcode(Context ctx, String barcode)
    {
        Cursor query=null;
        try {
            SQLiteDatabase db = ctx.openOrCreateDatabase(ctx.getExternalFilesDir(null) + "/" + Utils.downloadDirectory + "/" + "test.db", MODE_PRIVATE, null);
            query = db.rawQuery("select kmc.ID, kmc.NAME, ot.OST_FREE, ss.SECTION, ITEM_L, ITEM_W, ITEM_H, BOX_L, BOX_W, BOX_H, GROUP_FLAG, CPRIME," +
                    "VID, kmc.FLAGS, PROC_NDS, VES, RAZMER, NOMENKL, NAME, KOL_UPAK, PACK, kmc.NSERT, PALLET_KG, PAL_LAYERS, PAL_LAY_BOX, " +
                    "PAL_LAY_HT, ENLISTED, KOL_MIN, CROOT, KOL_BLISTER, EX_DATA, KOL_MAX, VALID_MTH, CMANUF, CFILESERT, PRICE_B " +
                    "from KATMC kmc, KATMC_BARCODE bc, SKLAD_SECTION ss, OST_TOV ot, LAST_BASE lb WHERE kmc.ID = bc.ID AND kmc.ID=ss.CMC " +
                    "AND kmc.ID = ot.CMC AND lb.CMC=kmc.ID AND bc.BARCODE = '" + barcode + "' ORDER by OST_FREE DESC;", null);
        }
        catch (Exception exception)
        {
            Toast.makeText(ctx, "Скачайте базу" + exception.getMessage(), Toast.LENGTH_LONG).show();
        }
        return query;
    }

    public static Cursor GetCountByBarcode(Context ctx, String barcode)
    {
        Cursor query=null;

        try {

            SQLiteDatabase db = ctx.openOrCreateDatabase(ctx.getExternalFilesDir(null)  + "/" + Utils.downloadDirectory + "/" + "test.db", MODE_PRIVATE, null);
            query = db.rawQuery("SELECT COUNT(*) FROM KATMC_BARCODE WHERE BARCODE='" + barcode + "';", null);
        }
        catch (Exception exception)
        {
            Toast.makeText(ctx, "Скачайте базу" + exception.getMessage(), Toast.LENGTH_LONG).show();
        }
        return query;
    }



    public static Cursor GetOrgPatt(Context ctx, String patt)
    {
        Cursor query=null;

        try
        {
            SQLiteDatabase db = ctx.openOrCreateDatabase(ctx.getExternalFilesDir(null)  + "/" + Utils.downloadDirectory + "/" + "test.db", MODE_PRIVATE, null);
            query = db.rawQuery("SELECT * from KATORG WHERE NAME LIKE '%" + patt + "%'  LIMIT 0,20;", null);
            return query;
        }
        catch (Exception exception)
        {
            Toast.makeText(ctx, "Скачайте базу" + exception.getMessage(), Toast.LENGTH_LONG).show();
        }
        return query;
    }


    public static Cursor GetTovPatt(Context ctx, String patt)
    {
        Cursor query=null;

        try
        {
            SQLiteDatabase db = ctx.openOrCreateDatabase(ctx.getExternalFilesDir(null)  + "/" + Utils.downloadDirectory + "/" + "test.db", MODE_PRIVATE, null);
            query = db.rawQuery("SELECT * from KATMC WHERE NAME LIKE '%" + patt + "%'  LIMIT 0,20;", null);
            return query;
        }
        catch (Exception exception)
        {
            Toast.makeText(ctx, "Скачайте базу" + exception.getMessage(), Toast.LENGTH_LONG).show();
        }
        return query;
    }


    public static Cursor GetTovList(Context ctx, String patt)
    {
        Cursor query=null;

        try
        {
            SQLiteDatabase db = ctx.openOrCreateDatabase(ctx.getExternalFilesDir(null)  + "/" + Utils.downloadDirectory + "/" + "test.db", MODE_PRIVATE, null);
            query = db.rawQuery("SELECT CMC, NAME, nvl(GROUP_FLAG,0) GF, nvl(NOMENKL,'-') NOMENKL, VES, RAZMER, KOL_UPAK, " +
                    "to_char(kmc.DSERT_BEG,'yyyy.mm.dd') DSERT_BEG, " +
                    "to_char(kmc.DSERT,'yyyy.mm.dd') DSERT, CIERARH," +
                    "NSERT, NOMENKL, VALID_MTH, CFILESERT from NSK20.KATMC_EXT kmce, NSK20.KATMC kmc " +
                    "WHERE kmce.CMC=kmc.ID AND CPARENT_GR=288125 AND CIERARH=2 ORDER by NAME", null);
            return query;
        }
        catch (Exception exception)
        {
            Toast.makeText(ctx, "Скачайте базу" + exception.getMessage(), Toast.LENGTH_LONG).show();
        }
        return query;
    }


    public static Cursor Get_adr_by_coords(Context ctx, String latt, String lngt)
    {
        Cursor query=null;
        try
        {
            SQLiteDatabase db = ctx.openOrCreateDatabase(ctx.getExternalFilesDir(null)  + "/" + Utils.downloadDirectory + "/" + "test.db", MODE_PRIVATE, null);
            query = db.rawQuery("SELECT CADR, CORG, ko.NAME, NAME_PRCH, NUM_DOMA from ORG_ADR_COORD oac, ORG_ADR oa, KATORG ko\n" +
                    "WHERE LATT>" + latt + "-0.0001 AND LATT<" + latt + "+0.0001 AND\n" +
                    "      LNGT>" + lngt + "-0.0001 AND LNGT<" + lngt + "+0.0001 AND\n" +
                    "      oa.ID=oac.CADR AND ko.ID=oa.CORG", null);
            return query;
        }
        catch (Exception exception)
        {
            Toast.makeText(ctx, "Скачайте базу" + exception.getMessage(), Toast.LENGTH_LONG).show();
        }
        return query;
    }


    static Cursor  SQLIte_GetCount(Activity ac, String barcode)
    {

        Cursor query=null;
        try {
            SQLiteDatabase db = ac.openOrCreateDatabase(ac.getApplicationContext().getExternalFilesDir(null) + "/" + Utils.downloadDirectory + "/" + "test.db", MODE_PRIVATE, null);
            query = db.rawQuery("SELECT COUNT(*) FROM KATMC_BARCODE WHERE BARCODE='" + barcode + "';", null);
        }
        catch (Exception exception)
        {
            Toast.makeText(ac.getApplicationContext(), "Скачайте базу" + exception.getMessage(), Toast.LENGTH_LONG).show();
        }
        return query;
    }

    public static int GetTovCountByBarcode(Activity ac, String barcode)
    {
        Cursor query=SQLIte_GetCount(ac, barcode);
        if (query.moveToFirst())
            return query.getInt(0);
        else
            return 0;
    }


    static String GetBaseVersion(Activity ac)
    {
        try {
            SQLiteDatabase db = ac.openOrCreateDatabase(ac.getApplicationContext().getExternalFilesDir(null) + "/" + Utils.downloadDirectory + "/" + "test.db", MODE_PRIVATE, null);
            Cursor c = db.rawQuery("SELECT * FROM VERSION;", null);
            while (c.moveToNext()) {
                String DT = c.getString(c.getColumnIndex("DT"));
                String VER = c.getString(c.getColumnIndex("VER"));
                return DT + " " + VER;
            }
            return "";
        }
        catch (Exception exception)
        {
            return exception.getMessage();
            //Toast.makeText(ac.getApplicationContext(), "Скачайте базу" + exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
