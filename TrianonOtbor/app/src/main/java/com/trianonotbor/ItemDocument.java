package com.trianonotbor;

class ItemDocument {
    public String tovID;
    public String tovName;
    public String tovPrice;
    public String tovKol;
    public String tovIDL;
    public String tovNomenkl;
    boolean box;

    public ItemDocument(String ID, String NAME, String KOL, String PRICE, String TIPSOPR, String s6)
    {
        tovID=ID;
        tovName=NAME;
        tovKol=KOL;
        tovPrice=PRICE;
        tovIDL=TIPSOPR;
        tovNomenkl=s6;
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
