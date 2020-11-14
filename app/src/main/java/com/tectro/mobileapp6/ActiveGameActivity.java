package com.tectro.mobileapp6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tectro.mobileapp6.Adapters.TeamInGameAdapter;
import com.tectro.mobileapp6.Models.GameModel.GameModel;
import com.tectro.mobileapp6.Models.GameModel.NotifyNames;
import com.tectro.mobileapp6.Models.Support.DataModels.GameClasses.EntityClass;
import com.tectro.mobileapp6.Models.Support.Enums.EFightResult;

import java.util.concurrent.TimeUnit;

public class ActiveGameActivity extends AppCompatActivity {

    private GameModel model;

    private TextView millis;
    private TextView seconds;
    private TextView minutes;
    private LinearLayout AttackingEnemyPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_game);

        millis = findViewById(R.id.millis);
        seconds = findViewById(R.id.Seconds);
        minutes = findViewById(R.id.minutes);

        AttackingEnemyPanel = findViewById(R.id.AttackingEnemyPanel);
        AttackingEnemyPanel.setVisibility(View.INVISIBLE);

        model = GameModel.CreateInstance(this);

        RecyclerView recyclerView = findViewById(R.id.TeamInGameHolder);
        recyclerView.setAdapter(new TeamInGameAdapter(this, model.getPlayer()));

        model.getUpdateProvider().Subscribe(this::UpdateManager);
    }

    public void UpdateManager(String key, Object value) {
        switch (key) {
            case NotifyNames.CollectionChanged:
                NotifyCollection();
                break;
            case NotifyNames.Enemy:
                ShowEnemy((EntityClass) value);
                break;
            case NotifyNames.PlayerChoiceWaitTime:
            case NotifyNames.PlayerWaitTime:
            case NotifyNames.TotalElapsedTime:
                updateClock((long) value);
                break;
            case NotifyNames.winningState:
                isWin((boolean) value);
                break;
            case NotifyNames.FightResult:
                showFightResult((EFightResult) value);
        }
    }

    private void showFightResult(EFightResult value) {

    }

    private void isWin(boolean value) {

    }

    private void updateClock(long elapsedTime) {
        long millis = elapsedTime >= 0 ? elapsedTime / 1000 / 1000 : 0;
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
        this.minutes.setText(String.valueOf(minutes - (hours * 60)));
        this.seconds.setText(String.valueOf(seconds - (minutes * 60)));
        this.millis.setText(String.valueOf(millis - (seconds * 1000)));
    }

    private void ShowEnemy(EntityClass value) {
        if (value != null) {
            AttackingEnemyPanel.setVisibility(View.VISIBLE);
        } else AttackingEnemyPanel.setVisibility(View.INVISIBLE);
    }

    private void NotifyCollection() {

    }

/*  public void updateClock(long elapsedTime) {
        long millis = elapsedTime >= 0 ? elapsedTime / 1000 / 1000 : 0;
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
*//*        Hours.setText(String.valueOf(hours));
        Minutes.setText(String.valueOf(minutes - (hours * 60)));
        Seconds.setText(String.valueOf(seconds - (minutes * 60)));
        Millis.setText(String.valueOf(millis - (seconds * 1000)));*//*
    }*/

  /*  Random rand = new Random();

    public void ShowEnemy(EntityClass enemy) {
    *//*    if (enemy != null)
            Enemy.setBackgroundColor(Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
        else
            Enemy.setBackgroundColor(Color.TRANSPARENT);*//*
    }

    public void NotifyCollection() {
        Toast.makeText(this, "коллекция обновлена", Toast.LENGTH_SHORT).show();
    }

    public void isWin(boolean flag) {
        if (flag)
            Toast.makeText(this, "Вы победили", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Вы проиграли", Toast.LENGTH_SHORT).show();

    }

    public void startmap(View view) {
        model.getPlayer().AddHero();
        model.getPlayer().AddHero();
        model.getPlayer().AddHero();
        model.getPlayer().AddHero();
        model.getPlayer().AddHero();
        model.setGameDifficulty(EDifficulty.hell_point);
        model.startGame();
    }

    public void pushPlayer(View view) {
        GamePlayer player = model.getPlayer();

        if (player.isWaiting())
            player.stopWaiting();

        player.MakeChoice(0);

    }

    */
}