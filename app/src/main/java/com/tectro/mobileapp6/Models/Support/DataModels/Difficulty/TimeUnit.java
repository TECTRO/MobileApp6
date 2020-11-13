package com.tectro.mobileapp6.Models.Support.DataModels.Difficulty;

public class TimeUnit {
    private final long timeStamp; //stores as nanos

    public TimeUnit(long millis) {
        this.timeStamp = toNanos(millis);
    }

    private long toNanos(long millis) {
        return millis * 1000 * 1000;
    }

    public long asNanos() {
        return timeStamp;
    }
    public long asMicros() {
        return timeStamp/1000;
    }
    public long asMillis() {
        return asMicros()/1000;
    }

    public long asSeconds() {
        return asMillis()/1000;
    }
}
