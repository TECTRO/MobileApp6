package com.tectro.mobileapp6.Models.Support.DataModels.UpdateProvider;

import android.app.Activity;
import android.content.Context;

import com.tectro.mobileapp6.Models.Support.Interfaces.IUpdatable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class UpdateProvider implements IUpdatable {
    private ArrayList<BiConsumer<String, Object>> Subscribers;
    private Context context;


    public UpdateProvider(Context context) {
        this.context = context;
        Subscribers = new ArrayList<>();
    }

    private void sendToUI(Runnable func) {
        ((Activity) context).runOnUiThread(func);
    }

    @Override
    public void Subscribe(BiConsumer<String, Object> subscriber) {
        Subscribers.add(subscriber);
    }

    @Override
    public void Invoke(String key, Object value) {
        sendToUI(()->Subscribers.forEach(t->t.accept(key, value)));
    }
}
