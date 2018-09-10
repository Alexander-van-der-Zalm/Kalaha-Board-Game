package com.alexandervanderzalm.game.Model;

import java.util.ArrayList;
import java.util.List;

public class PitCollection<T extends IPit> implements IPitCollection<T> {

    public List<T> Pits;

    public PitCollection() {
        Pits = new ArrayList<>();
    }

    public PitCollection(List<T> pits) {
        this.Pits = pits;
    }

    @Override
    public T Right(int index) {
        return Pits.get(PitUtil.Right(Pits.size(),index));
    }

    @Override
    public T Right(T pit) {
        return Right(Pits.indexOf(pit));
    }

    @Override
    public T Left(int index) {
        return Pits.get(PitUtil.Left(Pits.size(),index));
    }

    @Override
    public T Left(T pit) {
        return Left(Pits.indexOf(pit));
    }

    @Override
    public T Opposite(int index) {
        return Pits.get(PitUtil.Opposite(Pits.size(),index));
    }

    @Override
    public T Opposite(T pit) {
        return Opposite(Pits.indexOf(pit));
    }

    @Override
    public T KalahaOfPlayer1() {
        return Pits.get(PitUtil.FirstKalaha());
    }

    @Override
    public T KalahaOfPlayer2() {
        return Pits.get(PitUtil.SecondKalaha(Pits.size()));
    }

    @Override
    public T KalahaOfPlayer(int currentPlayer) {
        return currentPlayer == 0 ? KalahaOfPlayer1() : KalahaOfPlayer2();
    }

    @Override
    public T Get(int index) {
        return Pits.get(index);
    }
}
