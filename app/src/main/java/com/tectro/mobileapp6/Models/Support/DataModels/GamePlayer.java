package com.tectro.mobileapp6.Models.Support.DataModels;

import com.tectro.mobileapp6.Models.GameModel.GameModel;
import com.tectro.mobileapp6.Models.Support.DataModels.Difficulty.Difficulty;
import com.tectro.mobileapp6.Models.Support.DataModels.Difficulty.DifficultyManager;
import com.tectro.mobileapp6.Models.Support.DataModels.GameClasses.ClassManager;
import com.tectro.mobileapp6.Models.Support.Enums.EEntityType;
import com.tectro.mobileapp6.Models.Support.Interfaces.ICollectionProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePlayer {

    GameModel parent;
    Random rand = new Random();

    public GamePlayer(GameModel parent) {
        Heroes = new ArrayList<>();
        Shields = 0;
        this.parent = parent;
    }

    //region Accessors
    public void getHeroes(ICollectionProvider<List<Hero>> provider) {
        provider.setCollection(Heroes);
    }

    public int getShields() {
        return Shields;
    }

    public void MakeChoice(int heroId) {
        if (heroId > 0 && heroId < Heroes.size())
            chosenSituation = new choice(Heroes.get(heroId), false);
    }

    public void MakeChoice(boolean shield) {
        if (shield && Shields > 0) {
            chosenSituation = new choice(null, true);
            Shields--;
        }
    }

    public choice getChosenSituation() {
        choice result = chosenSituation;
        chosenSituation = null;
        return result;
    }

    //endregion
    private ArrayList<Hero> Heroes;
    private int Shields;
    private choice chosenSituation;

    public void AddHero() {
        Difficulty difficulty = parent.getGameDifficulty();
        if (Heroes.size() < difficulty.getMaxHeroes())
            Heroes.add(new Hero(ClassManager.getByRandom(rand, EEntityType.Hero), true));
    }

    public void DelHero(int index) {
        if (index > 0 && index < Heroes.size())
            Heroes.remove(index);
    }

    public void CalculateShields() {
        Difficulty difficulty = parent.getGameDifficulty();
        Shields = difficulty.getMaxHeroes() + difficulty.getMinShields() - Heroes.size();
    }

    public static class choice {
        //region Accessors
        public Hero getChosenHero() {
            return chosenHero;
        }

        public boolean isShield() {
            return shield;
        }
        //endregion

        private final Hero chosenHero;
        private final boolean shield;

        public choice(Hero chosenHero, boolean shield) {
            this.chosenHero = chosenHero;
            this.shield = shield;
        }
    }
}
