package com.tectro.mobileapp6.Models.Support.DataModels.Difficulty;

public class Difficulty {
    //region Accessors
    public TimeUnit getTotalTime() {
        return TotalTime;
    }
    public TimeUnit getMinSpawnDelay() {
        return MinSpawnDelay;
    }
    public TimeUnit getPlayerWaitingTime() {
        return PlayerWaitingTime;
    }

    public byte getSpawnChance() {
        return SpawnChance;
    }
    public int getMaxHeroes() {
        return MaxHeroes;
    }
    public int getMinShields() {
        return MinShields;
    }
    //endregion

    private final TimeUnit TotalTime;
    private final TimeUnit MinSpawnDelay;
    private final TimeUnit PlayerWaitingTime;
    private final byte SpawnChance;
    private final int MaxHeroes;
    private final int MinShields;

    //храним в наносекундах
    public Difficulty(long totalTime, long minSpawnDelay, long playerWaitingTime, byte spawnChance, int maxHeroes, int minShields) {
        TotalTime = new TimeUnit(totalTime);
        MinSpawnDelay = new TimeUnit(minSpawnDelay);
        PlayerWaitingTime = new TimeUnit(playerWaitingTime);
        SpawnChance = spawnChance;
        MaxHeroes = maxHeroes;
        MinShields = minShields;
    }
}

