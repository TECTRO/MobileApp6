package com.tectro.mobileapp6.Models.Support.DataModels.Difficulty;

public class Difficulty {
    //region Accessors
    public long getTotalTime() {
        return TotalTime;
    }

    public byte getSpawnChance() {
        return SpawnChance;
    }

    public long getMinSpawnDelay() {
        return MinSpawnDelay;
    }

    public long getPlayerWaitingTime() {
        return PlayerWaitingTime;
    }

    public int getMaxHeroes() {
        return MaxHeroes;
    }

    public int getMinShields() {
        return MinShields;
    }


    //endregion

    private final long TotalTime;
    private final long MinSpawnDelay;
    private final long PlayerWaitingTime;
    private final byte SpawnChance;
    private final int MaxHeroes;
    private final int MinShields;

    public Difficulty(long totalTime, long minSpawnDelay, long playerWaitingTime, byte spawnChance, int maxHeroes, int minShields) {
        TotalTime = totalTime;
        MinSpawnDelay = minSpawnDelay;
        PlayerWaitingTime = playerWaitingTime;
        SpawnChance = spawnChance;
        MaxHeroes = maxHeroes;
        MinShields = minShields;
    }
}
