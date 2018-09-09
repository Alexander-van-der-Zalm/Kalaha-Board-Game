package com.alexandervanderzalm.game.Model;

import java.util.ArrayList;
import java.util.List;

public class PitCollection<T extends IPit> implements IPitCollection<T> {

    public List<T> pits;

    public PitCollection() {
        pits = new ArrayList<>();
    }

    public PitCollection(List<T> pits) {
        this.pits = pits;
    }

    @Override
    public T Right(int index) {
        return pits.get((index + 1)%pits.size());
    }

    @Override
    public T Right(T pit) {
        return Right(pits.indexOf(pit));
    }

    @Override
    public T Left(int index) {
        return pits.get((index - 1)%pits.size());
    }

    @Override
    public T Left(T pit) {
        return Left(pits.indexOf(pit));
    }

    @Override
    public T Opposite(int index) {
        return pits.get((pits.size() - index)%pits.size());
    }

    @Override
    public T Opposite(T pit) {
        return Opposite(pits.indexOf(pit));
    }

    @Override
    public T Get(int index) {
        return pits.get(index);
    }
}
