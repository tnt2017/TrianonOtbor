package com.trianonotbor;

class ItemTovcard {
    public String docID;
    public String docEMP;
    public String docDAT;
    public String docKol;
    public String docNNAKL;
    public String docTYP;
    public String docTXT;
    boolean box;

    public ItemTovcard(String ID,String DAT, String NNAKL, String EMP,  String KOL_MV,  String TYP,  String TXT)
    {
        docID=ID;
        docDAT=DAT;
        docNNAKL=NNAKL;
        docEMP=EMP;
        docKol=KOL_MV;
        docTYP=TYP;
        docTXT=TXT;
    }


    /*public String getId()
    {
        return  tovID;
    }
*/
    public String getName()
    {
        return docNNAKL;
    }

   /* public String toString ()
    {
        return tovID + "^" + tovKol;
    }*/

}
