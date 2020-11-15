package com.tectro.mobileapp6.Models.Support.DataModels;


import android.widget.LinearLayout;

import com.tectro.mobileapp6.Models.Support.DataModels.GameClasses.ClassManager;
import com.tectro.mobileapp6.Models.Support.DataModels.GameClasses.EntityClass;

import java.util.List;

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
        ValidClasses = ClassManager.getValid(HClass.getClassType());
    }
    //endregion

    private boolean Changeable;
    private EntityClass HClass;
    private List<EntityClass> ValidClasses;


    public void Lock() {
        if (Changeable) Changeable = false;
    }

    public boolean ChangeClassNext() {
        if (!Changeable) return false;

        if (HClass.getId() + 1 < ValidClasses.size())
            HClass = ValidClasses.get(HClass.getId() + 1);
        else
            HClass = ValidClasses.get(0);

        return true;
    }

    public boolean ChangeClassPrevious() {
        if (!Changeable) return false;

        if (HClass.getId() - 1 >= 0)
            HClass = ValidClasses.get(HClass.getId() - 1);
        else
            HClass = ValidClasses.get(ValidClasses.size() - 1);

        return true;
    }
}
