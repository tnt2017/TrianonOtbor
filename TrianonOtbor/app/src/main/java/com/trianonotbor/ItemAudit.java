package com.trianonotbor;

class ItemAudit {
    public String auditHEAD;
    public String auditTEXT;

    boolean box;

    public ItemAudit(String s1, String s2)
    {
        auditHEAD=s1;
        auditTEXT=s2;
    }


    public String getId()
    {
        return  auditHEAD;
    }

    public String getName()
    {
        return auditTEXT;
    }

    public String toString ()
    {
        return auditHEAD + "^" + auditTEXT;
    }

}
