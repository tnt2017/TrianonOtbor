package com.trianonotbor;

class ItemTovsheet {
    public String tovID;
    public String tovName;
    public String tovSection;
    public String tovKol;
    public String tovIDL;
    public String tovNomenkl;
    public String tovKor;
    public String tovBARCODES;
    boolean box;

    public ItemTovsheet(String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8, boolean b)
    {
        tovID=s1;
        tovName=s2;
        tovSection=s3;
        tovKol=s4;
        tovIDL=s5;
        tovNomenkl=s6;
        tovKor=s7;
        tovBARCODES=s8;
        box=b;
    }


    public String getId()
    {
        return  tovID;
    }

    public String getName()
    {
        return tovName;
    }

    public String toString ()
    {
        return tovID + "^" + tovKol;
    }

}
