package com.raymond210129.nctucmc.dataStructure;

public class UserSubscription {
    private String name;
    private boolean groupAll;
    private boolean groupBass;
    private boolean groupFlute;
    private boolean groupHit;
    private boolean groupHu;
    private boolean groupLieu;

    public UserSubscription(){};
    public UserSubscription(String name, boolean groupAll, boolean groupBass, boolean groupFlute, boolean groupHit, boolean groupHu, boolean groupLieu)
    {
        this.name = name;
        this.groupAll = groupAll;
        this.groupBass = groupBass;
        this.groupFlute = groupFlute;
        this.groupHit = groupHit;
        this.groupHu = groupHu;
        this.groupLieu = groupLieu;
    }
    public String getName()
    {
        return this.name;
    }
    public boolean getGroupAll()
    {
        return this.groupAll;
    }
    public boolean getGroupFlute()
    {
        return this.groupFlute;
    }
    public boolean getGroupBass()
    {
        return this.groupBass;
    }
    public boolean getGroupHit()
    {
        return this.groupHit;
    }
    public boolean getGroupHu()
    {
        return this.groupHu;
    }
    public boolean getGroupLieu()
    {
        return this.groupLieu;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setGroupAll(boolean groupAll)
    {
        this.groupAll = groupAll;
    }
    public void setGroupBass(boolean groupBass)
    {
        this.groupBass = groupBass;
    }
    public  void setGroupFlute(boolean groupFlute)
    {
        this.groupFlute = groupFlute;
    }
    public void setGroupHit(boolean groupHit)
    {
        this.groupHit = groupHit;
    }
    public void setGroupHu(boolean groupHu)
    {
        this.groupHu = groupHu;
    }
    public void setGroupLieu(boolean groupLieu)
    {
        this.groupLieu = groupLieu;
    }
}
