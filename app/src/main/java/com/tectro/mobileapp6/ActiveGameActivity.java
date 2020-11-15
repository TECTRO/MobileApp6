package com.tectro.mobileapp6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.VibrationAttributes;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tectro.mobileapp6.Adapters.TeamGatheringAdapter;
import com.tectro.mobileapp6.Adapters.TeamInGameAdapter;
import com.tectro.mobileapp6.Dialogs.HeroEndFightDialog;
import com.tectro.mobileapp6.Models.GameModel.GameModel;
import com.tectro.mobileapp6.Models.GameModel.NotifyNames;
import com.tectro.mobileapp6.Models.Support.DataModels.GameClasses.EntityClass;
import com.tectro.mobileapp6.Models.Support.DataModels.GamePlayer;
import com.tectro.mobileapp6.Models.Support.Enums.EFightResult;

import java.util.concurrent.TimeUnit;

public class ActiveGameActivity extends AppCompatActivity {

    private GameModel model;

    private TextView millis;
    private TextView seconds;
    private TextView minutes;
    private LinearLayout AttackingEnemyPanel;
    private ProgressBar progress;
    private TeamInGameAdapter TeamAdapter;
    private LinearLayout UseShield;
    private TextView UseShieldCount;
    private TextView ElapsedTimeCaption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_game);

        millis = findViewById(R.id.millis);
        seconds = findViewById(R.id.Seconds);
        minutes = findViewById(R.id.minutes);

        AttackingEnemyPanel = findViewById(R.id.AttackingEnemyPanel);
        AttackingEnemyPanel.setVisibility(View.INVISIBLE);

        ElapsedTimeCaption = findViewById(R.id.ElapsedTimeCaption);

        model = GameModel.CreateInstance(this);

        UseShield = findViewById(R.id.UseShield);
        UseShieldCount = findViewById(R.id.UseShieldCount);
        updateShieldButton(true);

        progress = findViewById(R.id.TotalProgress);
        progress.setMax((int) model.getGameDifficulty().getTotalTime().asMillis());

        GamePlayer player = model.getPlayer();
        TeamAdapter = new TeamInGameAdapter(this,player::MakeChoice);
        player.getHeroes(TeamAdapter);
        ((RecyclerView)findViewById(R.id.TeamInGameHolder)).setAdapter(TeamAdapter);

        model.getUpdateProvider().Subscribe(this::UpdateManager);


    }

    public void UpdateManager(String key, Object value) {
        switch (key) {
            case NotifyNames.CollectionChanged: NotifyCollection((int)value); break;
            case NotifyNames.Enemy: ShowEnemy((EntityClass) value); break;
            case NotifyNames.PlayerWaitTime:  break;
            case NotifyNames.PlayerChoiceWaitTime: updateClock((long) value); break;
            case NotifyNames.TotalElapsedTime: updateClock((long) value); updatePath((long) value); break;
            case NotifyNames.winningState: isWin((boolean) value); break;
            case NotifyNames.FightResult: showFightResult((EFightResult) value); break;
            case NotifyNames.ChoiceLockStatus: updateChoiceStatus((boolean)value); break;
        }
    }


    private void updateChoiceStatus(boolean isLock)
    {
        TeamAdapter.setChoiceAccessibility(isLock);
        if (!isLock)
            ElapsedTimeCaption.setText(R.string.Время_на_принятие_решение);
        updateShieldButton(isLock);
    }

    private void updateShieldButton(boolean isLock)
    {
        UseShieldCount.setText(String.valueOf(model.getPlayer().getShields()));
        UseShield.setVisibility(!isLock ? View.VISIBLE : View.GONE);
    }

    public void chooseShield(View view) {
        model.getPlayer().MakeChoice(true);
    }

    private void showFightResult(EFightResult value) {
        GamePlayer player = model.getPlayer();
        String message;
        switch (value)
        {
            case HeroWin:   message = "Ваш герой победи и может продолжить путь со своим отрядом";  break;
            case HeroLose:  message = "Ваш герой погиб в схватке, отрятд продолжает путь без него"; break;
            case UsedShield:message = "Вы защитились щитом и можете продолжать путешествие";        break;
            default: message = "НЕЗАПЛАНИРОВАННОЕ СОСТОЯНИЕ: "+ value.name(); break;
        }

        new HeroEndFightDialog(this,message, () ->
        {
            player.stopWaiting();
            ElapsedTimeCaption.setText(R.string.Оставшееся_время);
        }).show(getSupportFragmentManager(),null);
    }

    private void isWin(boolean flag) {
        if (flag)
            Toast.makeText(this, "Вашей комманде удалость пройти этот путь", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Ваша каооманда была уничтоженва силами противника", Toast.LENGTH_SHORT).show();
        finish();
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

    private void updatePath(long elapsedTime)
    {
        long millis = elapsedTime >= 0 ? elapsedTime / 1000 / 1000 : 0;
        progress.setProgress((int)(model.getGameDifficulty().getTotalTime().asMillis() - millis));
    }

    private void ShowEnemy(EntityClass value) {
        if (value != null) {
            AttackingEnemyPanel.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.HeroName)).setText(value.getClassName().name());

            String[] weaknesses = new String[value.getWeaknessCount()];
            for (int i = 0; i < value.getWeaknessCount(); i++)
                weaknesses[i] = value.getWeakness(i).name();

            ((TextView)findViewById(R.id.heroWeaknesses)).setText(String.join("\n", weaknesses));

        } else AttackingEnemyPanel.setVisibility(View.INVISIBLE);
    }

    private void NotifyCollection(int pos) {
        TeamAdapter.notifyItemRemoved(pos);
    }
}