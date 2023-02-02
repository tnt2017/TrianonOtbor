package com.trianonotbor;

class ItemHistSection {
    public String tripID;
    public String tripTRIP;
    public String tripDSTART;
    public String tripKG;

    public String tripLINES;
    public String tripKG_T;
    public String tripM3_T;
    public String tripM3;
    boolean box;

    public ItemHistSection(String s1, String s2, String s3, String s4, String s5, String s6)
    {
        tripID=s1;
        tripTRIP=s2;
        tripDSTART=s3;
        tripKG=s4;
        tripM3=s5;
        tripLINES=s6;
    }


    public String getId()
    {
        return  tripID;
    }

    public String getName()
    {
        return tripLINES;
    }

    public String toString ()
    {
        return tripID + "^" + tripLINES;
    }

}
