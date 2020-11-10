package com.tectro.mobileapp6.Models.GameModel;

import android.os.SystemClock;

import com.tectro.mobileapp6.Models.Support.DataModels.Difficulty.Difficulty;
import com.tectro.mobileapp6.Models.Support.DataModels.Difficulty.DifficultyManager;
import com.tectro.mobileapp6.Models.Support.DataModels.GameClasses.ClassManager;
import com.tectro.mobileapp6.Models.Support.DataModels.GameClasses.EntityClass;
import com.tectro.mobileapp6.Models.Support.DataModels.GamePlayer;
import com.tectro.mobileapp6.Models.Support.Enums.EDifficulty;
import com.tectro.mobileapp6.Models.Support.Enums.EEntityType;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GameModel {

    //region Singleton
    private static GameModel current;

    public GameModel CreateInstance() {
        if (current == null) current = new GameModel(EDifficulty.normal);
        return current;
    }
    //endregion

    //region Accessors
    //public void getHeroes(ICollectionProvider<List<Hero>> provider) {
    //    provider.setCollection(Heroes);
    //}

    public GamePlayer getPlayer() {
        return Player;
    }

    public void setGameDifficulty(EDifficulty gameDifficulty) {
        GameDifficulty = DifficultyManager.getDifficulty(gameDifficulty);
    }

    public Difficulty getGameDifficulty() {
        return GameDifficulty;
    }
    //endregion

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

    //private Hero PlayersChoice;

    public void startGame(BiConsumer<Long,Long> ElapsedTimeNotificator,
                          Consumer<EntityClass> EnemyNotificator,
                          Runnable CollectionNotificator
    ) {
        if (ActiveGameThread != null)
            if (ActiveGameThread.isAlive())
                ActiveGameThread.stop();

        ActiveGameThread = new Thread(() ->
        {
            long StartTime = System.nanoTime();
            long LastEnemyArrival = StartTime;
            long LapTime = StartTime;
            do {
                if (LastEnemyArrival - StartTime >= GameDifficulty.getMinSpawnDelay()) {
                    byte probability = (byte) (rand.nextInt(100) + 1);
                    if (probability <= GameDifficulty.getSpawnChance()) {
                        LastEnemyArrival = System.nanoTime();
                        EntityClass Enemy = ClassManager.getByRandom(rand, EEntityType.Enemy);
                        EnemyNotificator.accept(Enemy);
                        SystemClock.sleep(GameDifficulty.getPlayerWaitingTime());
                        StartTime+=GameDifficulty.getPlayerWaitingTime();
                        GamePlayer.choice choice = Player.getChosenSituation();
                        if (choice != null) {
                            if (!choice.isShield()) {
                                //todo end fight
                                if (choice.getChosenHero().getHClass().compareTo(Enemy) == -1){
                                    Player.getHeroes(t -> t.remove(choice.getChosenHero()));
                                    CollectionNotificator.run();
                                }
                            }
                        }else {
                            Player.getHeroes(t -> t.remove(0));
                            CollectionNotificator.run();
                        }
                        EnemyNotificator.accept(null);
                    }
                }

                //todo send notification
                LapTime = System.nanoTime();
                ElapsedTimeNotificator.accept(StartTime,LapTime);

            } while (LapTime - StartTime < GameDifficulty.getTotalTime());
        });
        ActiveGameThread.start();
    }
}
