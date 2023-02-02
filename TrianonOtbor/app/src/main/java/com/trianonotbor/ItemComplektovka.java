package com.trianonotbor;

class ItemComplektovka {
    public String tovSheet;
    public String tovPrice;
    public String tovKol;
    public String tovNomenkl;
    public String tovBarcode;
    public String tovSector;
    boolean box;

    public ItemComplektovka(String ID, String KOL, String sector,  String barcode, boolean b)
    {
        tovSheet=ID;
        tovKol=KOL;
        tovSector=sector;
        tovBarcode=barcode;
        box=b;
    }

    public String getId()
    {
        return  tovSheet;
    }
    public String getName()
    {
        return tovSheet;
    }
    public String toString ()
    {
        return tovSheet + "^" + tovKol;
    }
}
