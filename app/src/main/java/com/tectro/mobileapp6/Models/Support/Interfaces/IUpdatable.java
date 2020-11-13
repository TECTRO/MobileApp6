package com.tectro.mobileapp6.Models.Support.Interfaces;

import java.util.function.BiConsumer;

public interface IUpdatable {
    void Subscribe(BiConsumer<String,Object> subscriber);
    void Invoke(String key, Object value);
}
