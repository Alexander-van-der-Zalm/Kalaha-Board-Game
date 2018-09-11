package com.alexandervanderzalm.game.Model.Pits;

public class KalahaPitData {
    public Integer player = 0;
    public Boolean isKalaha = false;
    public Integer stones = 0;

    public KalahaPitData(Integer player, Boolean isKalaha, Integer stones) {
        this.player = player;
        this.isKalaha = isKalaha;
        this.stones = stones;
    }

    public KalahaPitData() {
    }
}
