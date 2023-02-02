package com.trianonotbor;

class ItemDocumentPrihod {
    public String tovID;
    public String tovName;
    public String tovPrice;
    public String tovKol;
    public String tovIDL;
    public String tovNomenkl;
    public String tovBarcode;
    public String tovSector;
    public String tovSheet;

    boolean box, box2;

    public ItemDocumentPrihod(String ID, String NAME, String KOL, String IDL, String sector, String ts, String barcode, boolean b, boolean b2)
    {
        tovID=ID;
        tovName=NAME;
        tovKol=KOL;
       // tovPrice=PRICE;
        tovIDL=IDL;
        tovSector=sector;
        tovBarcode=barcode;
        tovSheet=ts;
        box=b;
        box2=b2;
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
