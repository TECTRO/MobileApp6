package com.tectro.mobileapp6;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tectro.mobileapp6.Models.GameModel.GameModel;
import com.tectro.mobileapp6.Models.GameModel.NotifyNames;
import com.tectro.mobileapp6.Models.Support.DataModels.GameClasses.ClassManager;
import com.tectro.mobileapp6.Models.Support.DataModels.GameClasses.EntityClass;
import com.tectro.mobileapp6.Models.Support.DataModels.GamePlayer;
import com.tectro.mobileapp6.Models.Support.Enums.EDifficulty;
import com.tectro.mobileapp6.Models.Support.Enums.EEntityNames;

import java.security.Key;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    GameModel model;
    TextView Hours;
    TextView Minutes;
    TextView Seconds;
    TextView Millis;

    ImageView Enemy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = GameModel.CreateInstance(this);
        model.getUpdateProvider().Subscribe(this::UpdateManager);

        Hours = findViewById(R.id.Hours);
        Minutes = findViewById(R.id.minutes);
        Seconds = findViewById(R.id.Seconds);
        Millis = findViewById(R.id.millis);
        Enemy = findViewById(R.id.enemy);

        ClassManager.Init();
    }

    public void updateClock(long elapsedTime) {
        long millis = elapsedTime>=0?elapsedTime / 1000 / 1000:0;
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
        Hours.setText(String.valueOf(hours));
        Minutes.setText(String.valueOf(minutes - (hours * 60)));
        Seconds.setText(String.valueOf(seconds - (minutes * 60)));
        Millis.setText(String.valueOf(millis - (seconds * 1000)));
    }

    Random rand = new Random();

    public void ShowEnemy(EntityClass enemy) {
        if (enemy != null)
            Enemy.setBackgroundColor(Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
        else
            Enemy.setBackgroundColor(Color.TRANSPARENT);
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

    public void UpdateManager(String key, Object value)
    {
        switch (key)
        {
            case NotifyNames.CollectionChanged:  NotifyCollection(); break;
            case NotifyNames.Enemy: ShowEnemy((EntityClass) value); break;
            case NotifyNames.PlayerChoiceWaitTime:
            case NotifyNames.PlayerWaitTime:
            case NotifyNames.TotalElapsedTime: updateClock((long) value);break;
            case NotifyNames.winningState: isWin((boolean)value);break;
        }
    }
}