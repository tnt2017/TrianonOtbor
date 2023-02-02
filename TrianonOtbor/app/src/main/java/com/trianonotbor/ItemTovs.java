package com.trianonotbor;

class ItemTovs {
    public String tovCMC;
    public String tovNAME;
    public String tovGROUP_FLAG;
    public String tovKOL_UPAK;

    public String tripLINES;
    /*public String tripKG_T;
    public String tripM3_T;
    public String tripM3;*/
    boolean box;

    public ItemTovs(String s1, String s2, String s3, String s4, String s5, String s6)
    {
        tovCMC=s1;
        tovNAME=s2;
        tovGROUP_FLAG=s3;
        tovKOL_UPAK=s4;
        //tripM3=s5;
        //tripLINES=s6;
    }


    public String getId()
    {
        return  tovCMC;
    }

    public String getName()
    {
        return tripLINES;
    }

    public String toString ()
    {
        return tovCMC + "^" + tripLINES;
    }

}
