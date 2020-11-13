package com.tectro.mobileapp6.Models.Support.DataModels.Difficulty;

import com.tectro.mobileapp6.Models.Support.Enums.EDifficulty;

//вводим время в миллисекундах!!!
public class DifficultyManager {
    public static Difficulty getDifficulty(EDifficulty difficultyType) {
        switch (difficultyType) {
            case very_easy:
                return new Difficulty(10 * 1000, 3 * 1000, 4 * 1000, (byte) 30, 8, 2);
            case easy:
                return new Difficulty(20 * 1000, 4 * 1000, 4 * 1000, (byte) 40, 7, 3);
            default://normal
                return new Difficulty(30 * 1000, 3 * 1000, 1 * 1000, (byte) 50, 6, 3);
            case hard:
                return new Difficulty(40 * 1000, 3 * 1000, 3 * 1000, (byte) 65, 5, 2);
            case hell_point:
                return new Difficulty(60 * 1000, 3 * 1000, 2 * 1000, (byte) 90, 4, 1);
        }

    }
}
