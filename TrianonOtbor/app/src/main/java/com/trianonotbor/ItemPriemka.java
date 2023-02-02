package com.trianonotbor;

class ItemPriemka {
    public String sheetID;
    public String sheetTRIP;
    public String sheetORG;
    public String sheetDPICK;
    public String sheetCHECKER;
    public String sheetKOR;
    public String sheetSUMMA;

    public String sheetLINES;
    boolean box;

    public ItemPriemka(String s1, String s2, String s3, String s4, String s5,String s6,String s7,String s8)
    {
        sheetID=s1;
        sheetTRIP=s2;
        sheetORG=s3;
        sheetDPICK=s4;
        sheetLINES=s5;
        sheetCHECKER=s6;
        sheetKOR=s7;
        sheetSUMMA=s8;
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
