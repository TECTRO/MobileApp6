package com.tectro.mobileapp6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tectro.mobileapp6.Adapters.TeamGatheringAdapter;
import com.tectro.mobileapp6.Models.GameModel.GameModel;
import com.tectro.mobileapp6.Models.Support.DataModels.Hero;

import java.util.concurrent.atomic.AtomicInteger;

public class CommandGatheringActivity extends AppCompatActivity {

    private GameModel model;
    TeamGatheringAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_gathering);
        model = GameModel.CreateInstance(this);
        adapter = new TeamGatheringAdapter(this);
        model.getPlayer().getHeroes(adapter);
        ((RecyclerView)findViewById(R.id.CommandGatheringView)).setAdapter(adapter);
    }

    public void AddHero(View view) {
        model.getPlayer().AddHero();
        AtomicInteger lastItem = new AtomicInteger();
        model.getPlayer().getHeroes(t-> lastItem.set(t.size() - 1));
        adapter.notifyItemInserted(lastItem.get());
    }

    public void StartGame(View view) {
        AtomicInteger size = new AtomicInteger();
        model.getPlayer().getHeroes(t-> size.set(t.size()));
        if(size.get()>0)
        {
            model.getPlayer().getHeroes(t-> t.forEach(Hero::Lock));
            Intent intent = new Intent(this,ActiveGameActivity.class);
            startActivity(intent);
            model.startGame();
            finish();
        }
    }
}