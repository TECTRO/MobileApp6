package com.tectro.mobileapp6.Models.Support.DataModels;


import com.tectro.mobileapp6.Models.Support.DataModels.GameClasses.ClassManager;
import com.tectro.mobileapp6.Models.Support.DataModels.GameClasses.EntityClass;

public class Hero {
    //region Accessors
    public EntityClass getHClass() {
        return HClass;
    }
    //endregion

    //region Constructor
    public Hero(EntityClass HClass, boolean changeable) {
        this.HClass = HClass;
        Changeable = changeable;
    }
    //endregion

    private boolean Changeable;
    private EntityClass HClass;

    public void Lock() {
        if (Changeable) Changeable = false;
    }

    public boolean ChangeClassNext() {
        if (!Changeable) return false;

        if (HClass.getId() + 1 < ClassManager.size())
            HClass = ClassManager.get(HClass.getId() + 1);
        else
            HClass = ClassManager.get(0);

        return true;
    }

    public boolean ChangeClassPrevious() {
        if (!Changeable) return false;

        if (HClass.getId() - 1 >= 0)
            HClass = ClassManager.get(HClass.getId() - 1);
        else
            HClass = ClassManager.get(ClassManager.size() - 1);

        return true;
    }
}
