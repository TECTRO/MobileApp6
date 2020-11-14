package com.tectro.mobileapp6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tectro.mobileapp6.Adapters.ChooseDifficultyAdapter;
import com.tectro.mobileapp6.Models.GameModel.GameModel;
import com.tectro.mobileapp6.Models.GameModel.NotifyNames;
import com.tectro.mobileapp6.Models.Support.DataModels.GameClasses.ClassManager;
import com.tectro.mobileapp6.Models.Support.DataModels.GameClasses.EntityClass;
import com.tectro.mobileapp6.Models.Support.DataModels.GamePlayer;
import com.tectro.mobileapp6.Models.Support.Enums.EDifficulty;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DifficultyChoosingActivity extends AppCompatActivity {

    GameModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_choosing);

        model = GameModel.CreateInstance();

        RecyclerView recycler = findViewById(R.id.DifficultyChoosingButtons);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new ChooseDifficultyAdapter(this, this::setDifficulty));

        ClassManager.Init();
    }

    private void setDifficulty(int enumPos) {
        model.setGameDifficulty(EDifficulty.values()[enumPos]);
        model.getPlayer().getHeroes(List::clear);
        Intent intent = new Intent(this,CommandGatheringActivity.class);
        startActivity(intent);

    }
}