package com.tectro.mobileapp6.Models.Support.DataModels.GameClasses;

import com.tectro.mobileapp6.Models.Support.DataModels.GameClasses.EntityClass;
import com.tectro.mobileapp6.Models.Support.Enums.EEntityNames;
import com.tectro.mobileapp6.Models.Support.Enums.EEntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class ClassManager {
    private static ArrayList<EntityClass> EntityClasses;

    private static EntityClass getItem(EEntityNames GClass) {
        Optional<EntityClass> result = EntityClasses.stream().filter(t -> t.getClassName().equals(GClass)).findFirst();
        return result.orElse(null);
    }

    private static void Initialise(int seed) {
        EntityClasses = new ArrayList<>();
        EntityClasses.addAll(Arrays.asList(
                new EntityClass(EntityClasses, EEntityNames.бард, EEntityType.Hero),
                new EntityClass(EntityClasses, EEntityNames.варвар, EEntityType.Hero),
                new EntityClass(EntityClasses, EEntityNames.воин, EEntityType.Hero),
                new EntityClass(EntityClasses, EEntityNames.друид, EEntityType.Hero),
                new EntityClass(EntityClasses, EEntityNames.жрец, EEntityType.Hero),
                new EntityClass(EntityClasses, EEntityNames.маг, EEntityType.Hero),
                new EntityClass(EntityClasses, EEntityNames.монах, EEntityType.Hero),
                new EntityClass(EntityClasses, EEntityNames.паладин, EEntityType.Hero),
                new EntityClass(EntityClasses, EEntityNames.рейнджер, EEntityType.Hero),

                new EntityClass(EntityClasses, EEntityNames.Поскребыш, EEntityType.Enemy),
                new EntityClass(EntityClasses, EEntityNames.Скелет, EEntityType.Enemy),
                new EntityClass(EntityClasses, EEntityNames.Василиск, EEntityType.Enemy),
                new EntityClass(EntityClasses, EEntityNames.Гоблин, EEntityType.Enemy),
                new EntityClass(EntityClasses, EEntityNames.Змеи, EEntityType.Enemy),
                new EntityClass(EntityClasses, EEntityNames.Зверь, EEntityType.Enemy),
                new EntityClass(EntityClasses, EEntityNames.Наблюдатель, EEntityType.Enemy),
                new EntityClass(EntityClasses, EEntityNames.Мумия, EEntityType.Enemy),
                new EntityClass(EntityClasses, EEntityNames.Тролль, EEntityType.Enemy)
        ));

        Random rend = new Random(seed);

        for (EntityClass entity : EntityClasses) {
            int weaknessesCount = rend.nextInt(5 - 3) + 3;
            List<EntityClass> Opponents = EntityClasses.stream().filter(t -> t.getClassType() != entity.getClassType()).collect(Collectors.toList());
            for (int i = 0; i < weaknessesCount; i++)
                entity.setWeakness(Opponents.get(rend.nextInt(Opponents.size())).getClassName());
        }
    }

    public static void Init() {
        if (EntityClasses == null)
            Initialise(0);
    }

    public static EntityClass get(int index) {
        if (EntityClasses == null) Init();
        return EntityClasses.get(index);
    }

    public static EntityClass get(EEntityNames entityName) {
        if (EntityClasses == null) Init();
        return getItem(entityName);
    }

    public static EntityClass getByRandom(Random randomProvider, EEntityType entityType) {
        if (EntityClasses == null) Init();
        List<EntityClass> foundedCollection = EntityClasses.stream().filter(t -> t.getClassType() == entityType).collect(Collectors.toList());
        return foundedCollection.get(randomProvider.nextInt(foundedCollection.size()));
    }

    public static int size() {
        if (EntityClasses == null) Init();
        return EntityClasses.size();
    }
}
