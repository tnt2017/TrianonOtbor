package com.trianonotbor;

class ItemStrikeout {
    public String tovID;
    public String tovName;
    public String tovSection;
    public String tovNNAKL;
    public String tovDSTAMP;
    public String tovEMP;
    public String tovTRIP;
    public String strikeouts_tovKol;

    boolean box;

    public ItemStrikeout(String s1, String s2, String s3, String s4, String s5, String s6, String s7, String TRIP)
    {
        tovID=s1;
        tovName=s2;
        tovSection=s3;
        tovNNAKL=s4;
        tovDSTAMP=s5;
        tovEMP=s6;
        strikeouts_tovKol=s7;
        tovTRIP=TRIP;
    }


    /*public String getId()
    {
        return  tovID;
    }
*/
    public String getName()
    {
        return tovName;
    }

   /* public String toString ()
    {
        return tovID + "^" + tovKol;
    }*/

}
