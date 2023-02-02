package com.trianonotbor;

class ItemInformer {
    public String informerID;
    public String informerTRIP;
    public String informerDSTART;
    public String informerKG;

    public String informerLINES;
    public String informerKG_T;
    public String informerM3_T;
    public String informerM3;
    public String informerDSTAMP;
    boolean box;

    public ItemInformer(String s1, String s2, String s3, String s4, String s5, String s6, String s7)
    {
        informerID=s1;
        informerTRIP=s2;
        informerDSTART=s3;
        informerKG=s4;
        informerM3=s5;
        informerLINES=s6;
        informerDSTAMP=s7;
    }


    public String getId()
    {
        return  informerID;
    }

    public String getName()
    {
        return informerLINES;
    }

    public String toString ()
    {
        return informerID + "^" + informerLINES;
    }

}
