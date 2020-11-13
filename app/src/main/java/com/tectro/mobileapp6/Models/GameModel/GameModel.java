package com.tectro.mobileapp6.Models.GameModel;

import android.content.Context;
import android.os.SystemClock;

import com.tectro.mobileapp6.Models.Support.DataModels.Difficulty.Difficulty;
import com.tectro.mobileapp6.Models.Support.DataModels.Difficulty.DifficultyManager;
import com.tectro.mobileapp6.Models.Support.DataModels.Difficulty.TimeUnit;
import com.tectro.mobileapp6.Models.Support.DataModels.GameClasses.ClassManager;
import com.tectro.mobileapp6.Models.Support.DataModels.GameClasses.EntityClass;
import com.tectro.mobileapp6.Models.Support.DataModels.GamePlayer;
import com.tectro.mobileapp6.Models.Support.DataModels.UpdateProvider.UpdateProvider;
import com.tectro.mobileapp6.Models.Support.Enums.EDifficulty;
import com.tectro.mobileapp6.Models.Support.Enums.EEntityType;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

public class GameModel {

    //region Singleton
    private static GameModel current;

    public static GameModel CreateInstance(Context context) {
        if (current == null) current = new GameModel(EDifficulty.normal);
        current.uProvider = new UpdateProvider(context);
        return current;
    }

    public GamePlayer getPlayer() {
        return Player;
    }
    //endregion

    //region Accessors
    public void setGameDifficulty(EDifficulty gameDifficulty) {
        GameDifficulty = DifficultyManager.getDifficulty(gameDifficulty);
    }

    public Difficulty getGameDifficulty() {
        return GameDifficulty;
    }

    public UpdateProvider getUpdateProvider() {
        return uProvider;
    }
    //endregion

    private UpdateProvider uProvider;

    private GamePlayer Player;
    private Difficulty GameDifficulty;

    private Thread ActiveGameThread;
    private Random rand;

    private GameModel(EDifficulty difficulty) {
        rand = new Random();
        GameDifficulty = DifficultyManager.getDifficulty(difficulty);
        Player = new GamePlayer(this);
        // Heroes = new ArrayList<>();
    }

    private void notifyWhenLooping(Function<Long, Boolean> continueCondition, Consumer<Long> registerFunction, Consumer<Long> notifyFunction) {
        long TotalElapsedTime = 0;
        while (continueCondition.apply(TotalElapsedTime)) {
            long loopTimeStamp = System.nanoTime();
            SystemClock.sleep(5);
            if (registerFunction != null) registerFunction.accept(TotalElapsedTime);
            TotalElapsedTime += System.nanoTime() - loopTimeStamp;
            notifyFunction.accept(TotalElapsedTime);
            //uProvider.Invoke("TotalElapsedTime", TotalElapsedTime);
        }
    }

    //region Helpers

    private Queue<TimeUnit> getEnemySpawnTimes() {
        long totalGameTime = GameDifficulty.getTotalTime().asMillis();
        long minSpawnDelay = GameDifficulty.getMinSpawnDelay().asMillis();

        Queue<TimeUnit> SpawnTimes = new ArrayDeque<>();
        for (long i = minSpawnDelay; i < totalGameTime; i += minSpawnDelay) {
            int chance = rand.nextInt(100) + 1;
            if (chance < GameDifficulty.getSpawnChance())
                SpawnTimes.offer(new TimeUnit(i));
        }
        return SpawnTimes;
    }

    private void HandleEnemySpawn(AtomicBoolean winningState) {
        EntityClass Enemy = ClassManager.getByRandom(rand, EEntityType.Enemy);

        uProvider.Invoke(EEntityType.Enemy.name(), Enemy);

        Player.UnlockChoice();
        notifyWhenLooping(
                elapsedTime -> elapsedTime < GameDifficulty.getPlayerWaitingTime().asNanos(), null,
                elapsedTime -> uProvider.Invoke("PlayerChoiceWaitTime", GameDifficulty.getPlayerWaitingTime().asNanos() - elapsedTime)
        );
       // SystemClock.sleep(GameDifficulty.getPlayerWaitingTime().asMillis());
        Player.LockChoice();
        GamePlayer.choice choice = Player.getChosenSituation();

        if (choice != null) {
            if (!choice.isShield()) {
                //todo end fight
                if (choice.getChosenHero().getHClass().compareTo(Enemy) == -1) {
                    Player.getHeroes(t -> t.remove(choice.getChosenHero()));
                    uProvider.Invoke("CollectionChanged", null);
                }
            }
        } else {
            Player.getHeroes(t ->
            {
                if (t.size() > 1)
                {
                    t.remove(0);
                }
                else
                    winningState.set(false);
            });
            uProvider.Invoke("CollectionChanged", null);
        }

        notifyWhenLooping(
                s -> Player.isWaiting(), null,
                elapsedTime -> uProvider.Invoke("PlayerWaitTime", elapsedTime)
        );

        uProvider.Invoke(EEntityType.Enemy.name(), null);
    }
    //endregion

    private AtomicBoolean isGameActive = new AtomicBoolean(false);

    public void startGame() {
        if (ActiveGameThread != null)
            if (ActiveGameThread.isAlive()) {
                isGameActive.set(false);
                try {
                    ActiveGameThread.join();
                } catch (Exception ignored) {
                }
            }

        ActiveGameThread = new Thread(() ->
        {
            AtomicBoolean winningState = new AtomicBoolean(true);
            Queue<TimeUnit> EnemySpawnTimes = getEnemySpawnTimes();

            AtomicBoolean canEnemyHandle = new AtomicBoolean(false);
            notifyWhenLooping(
                    ElapsedTime -> ElapsedTime < GameDifficulty.getTotalTime().asNanos() && winningState.get() && isGameActive.get(),
                    ElapsedTime ->
                    {
                        if (EnemySpawnTimes.peek() != null && ElapsedTime > EnemySpawnTimes.peek().asNanos()) {
                            EnemySpawnTimes.poll();
                            canEnemyHandle.set(true);
                        }
                    },
                    ElapsedTime ->
                    {
                        if(canEnemyHandle.get())
                        {
                            canEnemyHandle.set(false);
                            HandleEnemySpawn(winningState);
                        }
                        uProvider.Invoke("TotalElapsedTime", GameDifficulty.getTotalTime().asNanos() - ElapsedTime);
                    }
            );

            if (isGameActive.get())
                uProvider.Invoke("winningState", winningState.get());
        });

        isGameActive.set(true);
        ActiveGameThread.start();
    }
}
