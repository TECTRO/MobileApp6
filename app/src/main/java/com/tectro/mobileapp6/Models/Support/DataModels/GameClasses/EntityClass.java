package com.tectro.mobileapp6.Models.Support.DataModels.GameClasses;

import com.tectro.mobileapp6.Models.Support.Enums.EEntityNames;
import com.tectro.mobileapp6.Models.Support.Enums.EEntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class EntityClass implements Comparable<EntityClass> {

    //region Accessors

    public EEntityNames getClassName() {
        return ClassName;
    }

    public EEntityType getClassType() {
        return ClassType;
    }

    public int getId() {
        return holder.indexOf(this);
    }

    public void setWeakness(EEntityNames... strongerClass) {
        Weaknesses.addAll(Arrays.asList(strongerClass));
    }

    public int getWeaknessCount() {
        return Weaknesses.size();
    }

    public EEntityNames getWeakness(int id) {
        return Weaknesses.get(id);
    }
    //endregion

    //region Constructor
    public EntityClass(List<EntityClass> holder, EEntityNames className, EEntityType classType) {
        this.holder = holder;
        ClassName = className;
        ClassType = classType;
        Weaknesses = new ArrayList<>();
    }
    //endregion

    private List<EntityClass> holder;

    private EEntityNames ClassName;
    private EEntityType ClassType;
    private List<EEntityNames> Weaknesses;

    @Override
    public int compareTo(EntityClass entityClass) {
        if(Weaknesses.contains(entityClass))
            return 1;
        if(entityClass.Weaknesses.contains(this))
            return -1;
        return 0;
    }
}
