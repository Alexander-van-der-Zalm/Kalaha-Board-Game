package com.alexandervanderzalm.game.Model.Pits;

public interface IPitCollection<T extends IPit> {

    public T Right(int index);
    public T Right(T pit);
    public T Left(int index);
    public T Left(T pit);
    public T Opposite(int index);
    public T Opposite(T pit);
    public T KalahaOfPlayer1();
    public T KalahaOfPlayer2();
    public T KalahaOfPlayer(int currentPlayer);

    public T Get(int index);
}

// Becomes Pitcollection[x].Right()
// As opposed to
// Pitcollection.Right(x)
/*
public interface IPitNavigation<T> {
    public T Right();
    public T Left();
    public T Opposite();
}*/
