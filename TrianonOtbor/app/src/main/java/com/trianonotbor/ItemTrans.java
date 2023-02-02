package com.trianonotbor;

class ItemTrans {
    public String tovSheet;
    public String tovPrice;
    public String tovKol;
    public String tovNomenkl;
    public String tovBarcode;
    public String tovSector;
    public String tovOrg;
    boolean box;

    public ItemTrans(String ID, String KOL, String sector, String barcode, String org, boolean b)
    {
        tovSheet=ID;
        tovKol=KOL;
        tovSector=sector;
        tovBarcode=barcode;
        tovOrg=org;
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
