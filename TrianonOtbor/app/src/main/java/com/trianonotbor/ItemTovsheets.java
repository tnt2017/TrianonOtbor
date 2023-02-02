package com.trianonotbor;

class ItemTovsheets {
    public String sheetID;
    public String sheetTRIP;
    public String sheetORG;
    public String sheetDPICK;

    public String sheetLINES;
    public String sheetKG_T;
    public String sheetM3_T;
    public String sheetSECTOR;
    boolean box;

    public ItemTovsheets(String s1, String s2, String s3, String s4, String s5, String s6)
    {
        sheetID=s1;
        sheetTRIP=s2;
        sheetORG=s3;
        sheetDPICK=s4;
        sheetLINES=s5;
        sheetSECTOR=s6;
    }


    public String getId()
    {
        return  sheetID;
    }

    public String getName()
    {
        return sheetLINES;
    }

    public String toString ()
    {
        return sheetID + "^" + sheetLINES;
    }

}
