package com.trianonotbor;

class ItemInformerBranch {
    public String informer_branchID;
    public String informer_branchTRIP;
    public String informer_branchDSTART;
    public String informer_branchKG;

    public String informer_branchLINES;
    public String informer_branchKG_T;
    public String informer_branchM3_T;
    public String informer_branchM3;
    boolean box;

    public ItemInformerBranch(String s1, String s2, String s3, String s4, String s5, String s6)
    {
        informer_branchID=s1;
        informer_branchTRIP=s2;
        informer_branchDSTART=s3;
        informer_branchKG=s4;
        informer_branchM3=s5;
        informer_branchLINES=s6;
    }


    public String getId()
    {
        return  informer_branchID;
    }

    public String getName()
    {
        return informer_branchLINES;
    }

    public String toString ()
    {
        return informer_branchID + "^" + informer_branchLINES;
    }

}
