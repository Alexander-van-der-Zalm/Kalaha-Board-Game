package com.alexandervanderzalm.game.Model;

public class Pit implements IPit<Integer> {
    Integer stones = 0;

    @Override
    public void Add(Integer stones) {
        this.stones += stones;
    }

    @Override
    public int Amount() {
        return stones;
    }

    @Override
    public Integer GrabAll() {
        int all = stones;
        stones = 0;
        return all;
    }
}