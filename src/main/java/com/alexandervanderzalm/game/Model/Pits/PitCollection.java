package com.alexandervanderzalm.game.Model.Pits;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PitCollection<T extends IPit> implements IPitCollection<T> {

    public List<T> pList;

    public PitCollection() {
        pList = new ArrayList<>();
    }

    public PitCollection(List<T> pits) {
        this.pList = pits;
    }

    @Override
    public T Right(int index) {
        return pList.get(PitUtil.Right(pList.size(),index));
    }

    @Override
    public T Right(T pit) {
        return Right(pList.indexOf(pit));
    }

    @Override
    public T Left(int index) {
        return pList.get(PitUtil.Left(pList.size(),index));
    }

    @Override
    public T Left(T pit) {
        return Left(pList.indexOf(pit));
    }

    @Override
    public T Opposite(int index) {
        return pList.get(PitUtil.Opposite(pList.size(),index));
    }

    @Override
    public T Opposite(T pit) {
        return Opposite(pList.indexOf(pit));
    }

    @Override
    public T KalahaOfPlayer1() {
        return pList.get(PitUtil.FirstKalaha());
    }

    @Override
    public T KalahaOfPlayer2() {
        return pList.get(PitUtil.SecondKalaha(pList.size()));
    }

    @Override
    public T KalahaOfPlayer(int currentPlayer) {
        return currentPlayer == 0 ? KalahaOfPlayer1() : KalahaOfPlayer2();
    }

    @Override
    public Integer IndexOf(T pit) {
        return pList.indexOf(pit);
    }

    @Override
    public T Get(int index) {
        return pList.get(index);
    }

    @Override
    public Stream<T> Stream() {
        return pList.stream();
    }
}
