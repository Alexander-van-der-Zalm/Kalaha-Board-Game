package com.alexandervanderzalm.game.Model.Pits;

public class PitData {
    public Integer player = 0;
    public Boolean isKalaha = false;
    public Integer stones = 0;

    public PitData(Integer player, Boolean isKalaha, Integer stones) {
        this.player = player;
        this.isKalaha = isKalaha;
        this.stones = stones;
    }

    public PitData() {
    }
}
