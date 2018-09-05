package com.alexandervanderzalm.Model;

public interface IPitCollection<T> {

    public T Right(int index);
    public T Left(int index);
    public T Opposite(int index);

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
